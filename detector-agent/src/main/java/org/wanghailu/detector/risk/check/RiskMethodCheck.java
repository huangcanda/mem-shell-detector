package org.wanghailu.detector.risk.check;

import org.wanghailu.detector.model.DetectorContext;

/**
 * @author cdhuang
 * @date 2023/6/21
 */
public abstract class RiskMethodCheck implements RiskCheckable {
    
    private Class clazz;
    
    private String methodName;
    
    private String methodDescriptor;
    
    public RiskMethodCheck(Class clazz, String methodName, String methodDescriptor) {
        this.clazz = clazz;
        this.methodName = methodName;
        this.methodDescriptor = methodDescriptor;
    }
    
    @Override
    public void check(DetectorContext detectorContext) {
        detectorContext.getAnalysisCodeManager().startDoAnalysis(clazz, methodName, methodDescriptor);
    }
    
    public Class getClazz() {
        return clazz;
    }
    
    public void setClazz(Class clazz) {
        this.clazz = clazz;
    }
    
    public String getMethodName() {
        return methodName;
    }
    
    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }
    
    public String getMethodDescriptor() {
        return methodDescriptor;
    }
    
    public void setMethodDescriptor(String methodDescriptor) {
        this.methodDescriptor = methodDescriptor;
    }
}
