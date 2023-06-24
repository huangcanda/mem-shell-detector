package org.wanghailu.detector.analyze;

import org.wanghailu.detector.analyze.model.StackItem;
import org.wanghailu.detector.model.DetectorContext;
import org.wanghailu.objectweb.asm.ClassVisitor;
import org.wanghailu.objectweb.asm.MethodVisitor;
import org.wanghailu.objectweb.asm.Opcodes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnalysisSinkClassVisitor extends ClassVisitor {
    
    public DetectorContext detectorContext;
    
    protected Class clazz;
    
    private String methodName;
    
    private String methodDescriptor;
    
    private Map<String, StackFrameAnalyzerAdapter> analyzerAdapterMap = new HashMap<>();

    private List<StackItem> itemList;

    public AnalysisSinkClassVisitor(DetectorContext detectorContext, Class clazz, String methodName,
                                    String methodDescriptor, List<StackItem> itemList) {
        super(Opcodes.ASM9);
        this.detectorContext = detectorContext;
        this.clazz = clazz;
        this.methodName = methodName;
        this.methodDescriptor = methodDescriptor;
        this.itemList = itemList;
    }
    
    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature,
            String[] exceptions) {
        MethodVisitor methodVisitor = super.visitMethod(access, name, descriptor, signature, exceptions);
        boolean isAnalysis = true;
        if(methodName!=null){
            if (!methodName.equals(name)){
                isAnalysis = false;
            }else if(methodDescriptor!=null && !methodDescriptor.equals(descriptor)){
                isAnalysis = false;
            }
        }
        if (isAnalysis) {
            AnalysisMethodInfo analysisMethodInfo = new AnalysisMethodInfo(access,clazz, name, descriptor);
            analysisMethodInfo.setItemList(itemList);
            methodVisitor = new StackFrameAnalyzerAdapter(analysisMethodInfo, methodVisitor, detectorContext);
            analyzerAdapterMap.put(name+"."+descriptor, (StackFrameAnalyzerAdapter) methodVisitor);
        }
        return methodVisitor;
    }
    
    public Map<String, StackFrameAnalyzerAdapter> getAnalyzerAdapterMap() {
        return analyzerAdapterMap;
    }
}
