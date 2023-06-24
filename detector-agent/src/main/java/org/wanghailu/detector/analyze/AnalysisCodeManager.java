package org.wanghailu.detector.analyze;

import org.wanghailu.detector.analyze.model.StackItem;
import org.wanghailu.detector.analyze.model.Trace;
import org.wanghailu.detector.analyze.rule.MethodRuleOnContext;
import org.wanghailu.detector.log.LogUtils;
import org.wanghailu.detector.model.DetectorContext;
import org.wanghailu.detector.model.MethodRule;
import org.wanghailu.detector.util.ClassUtil;
import org.wanghailu.detector.util.ReflectUtils;
import org.wanghailu.objectweb.asm.ClassReader;
import org.wanghailu.objectweb.asm.Opcodes;
import org.wanghailu.objectweb.asm.Type;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @author cdhuang
 * @date 2023/6/19
 */
public class AnalysisCodeManager {
    
    public static int parsingOptions = ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES;
    
    private static ClassLoader extClassLoader;
    
    static {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        while (classLoader != null) {
            if ("sun.misc.Launcher$ExtClassLoader".equals(classLoader.getClass().getName())) {
                extClassLoader = classLoader;
                break;
            } else {
                classLoader = classLoader.getParent();
            }
        }
    }
    
    
    private DetectorContext detectorContext;
    
    public AnalysisCodeManager(DetectorContext detectorContext) {
        this.detectorContext = detectorContext;
    }

    /**
     * 初始方法，开始代码分析
     * @param realClass
     * @param methodName
     * @param methodDescriptor
     */
    public void startDoAnalysis(Class realClass, String methodName, String methodDescriptor) {
        try{
            List<StackItem> argList = new ArrayList<>();
            argList.add(new StackItem(Type.getObjectType(realClass.getName().replace(".","/"))));
            if(methodDescriptor!=null){
                for (Type argType : Type.getArgumentTypes(methodDescriptor)) {
                    argList.add(new StackItem(argType));
                }
            }
            realDoAnalysis(realClass,methodName,methodDescriptor,argList);
        }catch (Throwable e){
            LogUtils.error(realClass.getName()+"."+methodName+"检测代码出错！",e);
        }finally {
            AnalysisCodeContext.getInvokeMethodList().clear();
        }

    }

    /**
     * 分析代码
     * @param realClass
     * @param methodName
     * @param methodDescriptor
     * @param argList
     * @return
     */
    public AnalysisSinkClassVisitor realDoAnalysis(Class realClass, String methodName, String methodDescriptor,List<StackItem> argList) {
        Class declaringClass = getDeclaringClass(realClass, methodName, methodDescriptor);
        if(declaringClass==null){
            return null;
        }
        byte[] byteCodeOnMemory = getBytecode(declaringClass);
        if(byteCodeOnMemory==null){
            return null;
        }
        if (AnalysisCodeContext.isRecursionInvokeMethod(new AnalysisMethodInfo(declaringClass,methodName, methodDescriptor))) {
            return null;
        }
        ClassReader classReader = new ClassReader(byteCodeOnMemory);
        AnalysisSinkClassVisitor cv1 = new AnalysisSinkClassVisitor(detectorContext, declaringClass, methodName,
                methodDescriptor, argList);
        try {
            classReader.accept(cv1, parsingOptions);
        }catch (Throwable e){
            LogUtils.error("解析代码发生错误！调用链为");
            for (AnalysisMethodInfo analysisMethodInfo : AnalysisCodeContext.getInvokeMethodList()) {
                LogUtils.error(analysisMethodInfo.toString());
            }
            StackFrameAnalyzerAdapter stackFrameAnalyzerAdapter = cv1.getAnalyzerAdapterMap().get(methodName + "." + methodDescriptor);
            if(stackFrameAnalyzerAdapter!=null){
                AnalysisCodeContext.getInvokeMethodList().remove(stackFrameAnalyzerAdapter.analysisMethodInfo);
            }
            LogUtils.error(e);
        }
        return cv1;
    }

    /**
     * 在方法代码中继续分析代码
     * @param context
     * @return
     */
    public List<StackItem> analysisMethodOnCode(CodeMethodInvokeContext context) {
        List<StackItem> result = new ArrayList<>();

        MethodRuleOnContext methodRuleOnContext = getMethodRule(detectorContext.getCommonRuleMap(), context.getOwner(),
                context.getName(), context.getDescriptor());
        if (methodRuleOnContext != null) {
            return analysisMethodOnMethodRule(context,methodRuleOnContext);
        }

        String className = context.getOwner();
        Class clazz = detectorContext.getLoadedClass(className.replace("/","."));
        if (clazz==null) {
            return result;
        }
        Class declaringClass = getDeclaringClass(clazz, context.getName(), context.getDescriptor());
        if(declaringClass==null){
            return defaultHandleTraceInfo(context);
        }
        if (isJdkClass(clazz)||isJdkClass(declaringClass)) {
            return defaultHandleTraceInfo(context);
        }
        return doAnalysisCode(context);
    }



    /**
     * 默认处理，直接将信息汇总
     * @param context
     * @return
     */
    private List<StackItem> defaultHandleTraceInfo(CodeMethodInvokeContext context){
        List<StackItem> result = new ArrayList<>();
        Set<Trace> traces = new HashSet<>();
        for (StackItem argItem : context.getArgList()) {
            if(argItem!=null){
                traces.addAll(argItem.getTraces());
            }
        }
        result.add(new StackItem().addTraces(traces));
        return result;
    }

    /**
     * 根据规则分析代码传播
     * @param context
     * @param methodRuleOnContext
     * @return
     */
    private List<StackItem> analysisMethodOnMethodRule(CodeMethodInvokeContext context, MethodRuleOnContext methodRuleOnContext) {
        List<StackItem> argList = context.getArgList();
        int baseIndex = context.getOpcode() != Opcodes.INVOKESTATIC ? 0 : -1;
        List<Integer> targetIndexList = new ArrayList<>();
        MethodRule methodRule = methodRuleOnContext.getRule();
        String[] sources = methodRule.getSource().split("\\|");
        String[] targets = methodRule.getTarget().split("\\|");
        for (String source : sources) {
            if ("O".equals(source)) {
                targetIndexList.add(0);
            } else if (source.startsWith("P")) {
                for (String s : source.substring(1).split(",")) {
                    int i = Integer.parseInt(s);
                    targetIndexList.add(i + baseIndex);
                }
            }
        }
        Set<Trace> traces = new HashSet<>();
        for (Integer targetIndex : targetIndexList) {
            traces.addAll(argList.get(targetIndex).getTraces());
        }
        Trace trace = Trace.byMethodInfo(context);
        trace.setMethodRuleOnContext(methodRuleOnContext);
        boolean isContainR = false;
        for (String target : targets) {
            if ("R".equals(target)) {
                isContainR = true;
            }
            if ("O".equals(target)) {
                if ("<init>".equals(context.name)) {
                    isContainR = true;
                } else {
                    argList.get(0).addTraces(traces).addTrace(trace);
                }
            } else if (target.startsWith("P")) {
                for (String s : target.substring(1).split(",")) {
                    int i = Integer.parseInt(s);
                    argList.get(i + baseIndex).addTraces(traces).addTrace(trace);
                }
            }
        }
        List<StackItem> result = new ArrayList<>();
        if (isContainR) {
            result.add(new StackItem().addTraces(traces).addTrace(trace));
        }
        return result;
    }
    
    public List<StackItem> doAnalysisCode(CodeMethodInvokeContext context) {
        List<StackItem> result = new ArrayList<>();
        List<StackItem> argList = context.getArgList();
        String realType = context.getOwner();
        if (context.getOpcode() != Opcodes.INVOKESTATIC) {
            StackItem ownerItem = context.getArgList().get(0);
            if (ownerItem.type != null) {
                realType = ownerItem.type.getInternalName();
            }
        }
        String methodName = context.getName();
        String methodDescriptor = context.getDescriptor();
        Class realClass = detectorContext.getLoadedClass(realType.replace("/", "."));
        if(realClass==null){
            return result;
        }
        AnalysisSinkClassVisitor cv1 = realDoAnalysis(realClass, methodName, methodDescriptor,argList);
        if(cv1==null){
            return result;
        }
        StackFrameAnalyzerAdapter stackFrameAnalyzerAdapter = cv1.getAnalyzerAdapterMap().get(methodName + "." + methodDescriptor);
        if(stackFrameAnalyzerAdapter ==null){
            return defaultHandleTraceInfo(context);
        }
        Set<Trace> traces = stackFrameAnalyzerAdapter.getReturnTrace();
        Set<Trace> results = new HashSet<>();
        for (Trace trace : traces) {
            Integer argIndex = trace.getArgIndex();
            if (argIndex != null && trace.getAnalysisMethodInfo().equals(stackFrameAnalyzerAdapter.analysisMethodInfo)) {
                results.addAll(argList.get(argIndex).getTraces());
            } else {
                results.add(trace);
            }
        }
        result.add(new StackItem().addTraces(results));
        return result;
    }


    
    private byte[] getBytecode(Class clazz) {
        Map<Class, byte[]> classByteMap = AnalysisCodeContext.getClassByteMap();
        byte[] bytes = classByteMap.get(clazz);
        if (bytes != null) {
            return bytes;
        }
        try {
            detectorContext.getInst().retransformClasses(clazz);
        } catch (Throwable e) {
            LogUtils.error("Classes: " + clazz.getName() + " retransform error: " + e.getMessage());
            return null;
        }
        //cache 可能需要有淘汰机制
        byte[] byteCodeOnMemory = ClassUtil.getClassBytesOnMemory(detectorContext.getInst(), clazz);
        classByteMap.put(clazz, byteCodeOnMemory);
        return byteCodeOnMemory;
    }
    
    private Class getDeclaringClass(Class clazz, String methodName, String methodDescriptor) {
        if(methodName==null){
            return clazz;
        }
        Class[] parameterTypes = null;
        if(methodDescriptor!=null){
            parameterTypes = getParameterTypes(methodDescriptor);
        }
        try {
            if ("<init>".equals(methodName)) {
                return clazz;
            } else {
                Method method = ReflectUtils.getMethod(clazz,methodName, parameterTypes);
                return method.getDeclaringClass();
            }
        } catch (Exception e) {
            LogUtils.error(e);
            return null;
        }
    }

    private Class[] getParameterTypes(String methodDescriptor) {
        Type[] argumentTypes = Type.getArgumentTypes(methodDescriptor);
        Class[] parameterTypes = new Class[argumentTypes.length];
        int index = 0;
        for (Type argumentType : argumentTypes) {
            String className = argumentType.getClassName();
            try {
                boolean isArray = false;
                if(className.endsWith("[]")){
                    isArray = true;
                    className = className.substring(0,className.length()-2);
                }
                Class clazz;
                if ("int".equals(className)) {
                    clazz = Integer.TYPE;
                } else if ("boolean".equals(className)) {
                    clazz = Boolean.TYPE;
                } else if ("byte".equals(className)) {
                    clazz = Byte.TYPE;
                } else if ("char".equals(className)) {
                    clazz = Character.TYPE;
                } else if ("short".equals(className)) {
                    clazz = Short.TYPE;
                } else if ("double".equals(className)) {
                    clazz = Double.TYPE;
                } else if ("float".equals(className)) {
                    clazz = Float.TYPE;
                } else if ("long".equals(className)) {
                    clazz = Long.TYPE;
                }else{
                    clazz = Class.forName(className);
                }
                if(isArray){
                    Object array = Array.newInstance(clazz,0);
                    parameterTypes[index] = array.getClass();
                }else{
                    parameterTypes[index] = clazz;
                }
            } catch (Exception e) {
                LogUtils.error(e);
            }
            index++;
        }
        return parameterTypes;
    }

    /**
     * 校验是否存在Sink方法
     * @param context
     */
    public void checkSinkMethod(CodeMethodInvokeContext context) {
        MethodRuleOnContext methodRuleOnContext = getMethodRule(detectorContext.getSinkRuleMap(), context.getOwner(),
                context.getName(), context.getDescriptor());
        if (methodRuleOnContext != null) {
            LogUtils.info("--------------------");
            if (methodRuleOnContext.getRule().getArgumentDescriptor() != null) {
                if (context.getDescriptor().startsWith(methodRuleOnContext.getRule().getArgumentDescriptor())) {
                    LogUtils.info("发现Sink方法调用："+methodRuleOnContext);
                } else {
                    LogUtils.info("发现Sink方法调用："+methodRuleOnContext+",不同的方法描述："+context.getDescriptor());
                }
            } else {
                LogUtils.info("发现Sink方法调用："+methodRuleOnContext);
            }
            Trace sinkTrace = Trace.byMethodInfo(context);
            handleInvokeChain(sinkTrace,"Sink方法");
            handleTraceInfo(context.getArgList());
        } else if ("java/lang/reflect/Method".equalsIgnoreCase(context.getOwner()) && "invoke"
                .equals(context.getName())) {
            MethodRuleOnContext fullCheckMethodRule = checkSinkMethodOnReflect(context,false,null);
            checkSinkMethodOnReflect(context,true,fullCheckMethodRule);
        }
    }

    private MethodRuleOnContext checkSinkMethodOnReflect(CodeMethodInvokeContext context,boolean isFuzzy,MethodRuleOnContext fullCheckMethodRule){
        StackItem methodItem = context.getArgList().get(0);
        for (MethodRuleOnContext ruleOnContext : detectorContext.getSinkRuleList()) {
            boolean containMethodName = false;
            boolean containClassOwner = false;
            Trace classOwnerTrace = null;
            Trace methodNameTrace = null;
            for (Trace trace : methodItem.getTraces()) {
                if (StackFrameAnalyzerAdapter.STRING.equals(trace.getLdcType()) && ruleOnContext.getRule()
                        .getMethodName().equals(trace.getLdcValue())) {
                    containMethodName = true;
                    methodNameTrace = trace;
                    continue;
                }
                if (StackFrameAnalyzerAdapter.CLASS.equals(trace.getLdcType()) && ruleOnContext.getClassName()
                        .equals(trace.getLdcValue())) {
                    containClassOwner = true;
                    classOwnerTrace = trace;
                }
                if (trace.getCodeMethodInvokeContext()!=null && "java/lang/Object".equals(trace.getCodeMethodInvokeContext().getOwner()) && "getClass"
                        .equals(trace.getCodeMethodInvokeContext().getName())) {
                    if (ruleOnContext.getClassName().equals(trace.getOtherInfo())) {
                        containClassOwner = true;
                        classOwnerTrace = trace;
                    }
                }
            }
            Trace sinkTrace = Trace.byMethodInfo(context);
            if (containMethodName && containClassOwner) {
                LogUtils.info("--------------------");
                LogUtils.info("发现Sink方法的反射调用："+ruleOnContext);
                handleInvokeChain(sinkTrace,"Sink方法反射的");
                handleInvokeChain(classOwnerTrace,"反射的类型推断");
                handleInvokeChain(methodNameTrace,"反射的方法名推断");
                handleTraceInfo(context.getArgList());
                return ruleOnContext;
            }
            if(isFuzzy){
                if(containMethodName || containClassOwner){
                    if(fullCheckMethodRule!=null && fullCheckMethodRule.getRule().equals(ruleOnContext.getRule())){
                        continue;
                    }
                    if(containMethodName){
                        LogUtils.info("--------------------");
                        LogUtils.info("发现可能与该Sink方法同方法名的反射调用："+ruleOnContext);
                        handleInvokeChain(sinkTrace,"Sink方法反射的");
                        handleInvokeChain(methodNameTrace,"反射的方法名推断");
                    } else{
                        LogUtils.info("--------------------");
                        LogUtils.info("发现可能与该Sink方法同Class类型的反射调用："+ruleOnContext);
                        handleInvokeChain(sinkTrace,"Sink方法反射的");
                        handleInvokeChain(classOwnerTrace,"反射的类型推断");
                    }
                    handleTraceInfo(context.getArgList());
                    return ruleOnContext;
                }

            }
        }
        return null;
    }

    private void handleTraceInfo(List<StackItem> itemList){
        int index =0;
        for (StackItem item : itemList) {
            if(item!=null){
                for (Trace trace : item.getTraces()) {
                    handleTraceInfo(trace,index);
                }
            }
            index++;
        }
    }


    private void handleTraceInfo(Trace trace,int index){
        if(trace.getMethodRuleOnContext()==null){
            return;
        }
        MethodRule methodRule = trace.getMethodRuleOnContext().getRule();
        boolean sensitiveMethod = false;
        String info = null;
        if(methodRule.getType()==1){
            sensitiveMethod =true;
            info = "Source";
        }else if(methodRule.getType()==2){
            sensitiveMethod =true;
            info = "Base64处理";
        }else if(methodRule.getType()==4){
            sensitiveMethod =true;
            info = "IO或URL处理";
        }
        if(sensitiveMethod){
            LogUtils.info("第"+index+"个参数溯源发现可能的"+info+"调用:"+ trace.getMethodRuleOnContext());
            handleInvokeChain(trace,"");
        }
    }

    private void handleInvokeChain(Trace trace, String info){
        LogUtils.info("    "+info+"调用链如下:");
        for (AnalysisMethodInfo methodInfo : trace.getAnalysisMethodInfoList()) {
            LogUtils.info("    "+methodInfo.toString());
        }
    }
    
    private MethodRuleOnContext getMethodRule(Map<String, List<MethodRuleOnContext>> ruleMap, String methodOwner,
            String methodName, String methodDescriptor) {
        List<MethodRuleOnContext> rules = ruleMap.get(methodOwner + "." + methodName);
        if (rules != null) {
            for (MethodRuleOnContext rule : rules) {
                String ruleDescriptor = rule.getRule().getArgumentDescriptor();
                if (ruleDescriptor != null && ruleDescriptor.length() > 0) {
                    if (methodDescriptor.startsWith(ruleDescriptor)) {
                        return rule;
                    }
                } else {
                    return rule;
                }
            }
        }
        return null;
    }

    private boolean isJdkClass(Class clazz){
        String className = clazz.getName();
        return clazz.getClassLoader()==null||clazz.getClassLoader().equals(extClassLoader)
                ||className.startsWith("java.") || className.startsWith("javax.")
                || className.startsWith("sun.") || className.startsWith("com.sun.")
                || className.endsWith("Utils") || className.endsWith("Util");
    }
}
