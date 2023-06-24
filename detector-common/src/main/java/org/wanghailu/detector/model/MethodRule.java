package org.wanghailu.detector.model;

import java.io.Serializable;

/**
 * @author cdhuang
 * @date 2023/6/12
 */
public class MethodRule implements Serializable {
    
    private static final long serialVersionUID = -6743567631108323096L;
    
    private int type;
    
    private String className;
    
    private String methodName;
    
    private String argumentDescriptor;
    
    private String source;
    
    private String target;
    
    private int score;
    
    public MethodRule() {
    }
    
    public MethodRule(int type, String className, String methodName, String argumentDescriptor, String source,
            String target, int score) {
        this.type = type;
        this.className = className;
        this.methodName = methodName;
        this.argumentDescriptor = argumentDescriptor;
        this.source = source;
        this.target = target;
        this.score = score;
    }
    
    public String getClassName() {
        return className;
    }
    
    public void setClassName(String className) {
        this.className = className;
    }
    
    public String getMethodName() {
        return methodName;
    }
    
    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }
    
    public int getType() {
        return type;
    }
    
    public void setType(int type) {
        this.type = type;
    }
    
    public String getArgumentDescriptor() {
        return argumentDescriptor;
    }
    
    public void setArgumentDescriptor(String argumentDescriptor) {
        this.argumentDescriptor = argumentDescriptor;
    }
    
    public String getSource() {
        return source;
    }
    
    public void setSource(String source) {
        this.source = source;
    }
    
    public String getTarget() {
        return target;
    }
    
    public void setTarget(String target) {
        this.target = target;
    }
    
    public int getScore() {
        return score;
    }
    
    public void setScore(int score) {
        this.score = score;
    }
}
