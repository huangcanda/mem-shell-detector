package org.wanghailu.detector.analyze;

import org.wanghailu.detector.log.LogUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author cdhuang
 * @date 2023/6/19
 */
public class AnalysisCodeContext {
    
    private static ThreadLocal<AnalysisCodeContext> threadLocal = new ThreadLocal<AnalysisCodeContext>() {
        @Override
        protected AnalysisCodeContext initialValue() {
            return new AnalysisCodeContext();
        }
    };
    
    private Map<Class,byte[]> classByteMap = new HashMap<>();

    public List<AnalysisMethodInfo> invokeMethodList = new ArrayList<>();
    
    public static Map<Class,byte[]> getClassByteMap(){
        AnalysisCodeContext context = threadLocal.get();
        return context.classByteMap;
    }

    public static boolean isRecursionInvokeMethod(AnalysisMethodInfo analysisMethodInfo){
        List<AnalysisMethodInfo> analysisMethodInfoList = threadLocal.get().invokeMethodList;
        if(analysisMethodInfoList.size() > 30){
            LogUtils.info("当前调用链太长，可能存在递归调用，不继续深入解析代码。调用链如下");
            for (AnalysisMethodInfo methodInfo : analysisMethodInfoList) {
                LogUtils.info(methodInfo.toString());
            }
            return true;
        }
        return analysisMethodInfoList.contains(analysisMethodInfo);
    }

    public static List<AnalysisMethodInfo> getInvokeMethodList(){
        AnalysisCodeContext context = threadLocal.get();
        List<AnalysisMethodInfo> analysisMethodInfoList = new ArrayList<>(threadLocal.get().invokeMethodList);
        context.invokeMethodList = analysisMethodInfoList;
        return analysisMethodInfoList;
    }

    public static AnalysisCodeContext getAnalysisCodeContext(){
        return threadLocal.get();
    }
    
    public static void finallyClean(){
        threadLocal.remove();
    }
}
