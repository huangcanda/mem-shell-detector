package org.wanghailu.detector.analyze.rule;

import org.wanghailu.detector.model.MethodRule;

/**
 * @author cdhuang
 * @date 2023/6/14
 */
public class MethodRuleOnContext {
    
    private MethodRule rule;
    
    private Class clazz;
    
    private String clazzType;
    
    public MethodRuleOnContext(MethodRule rule, Class clazz) {
        this.rule = rule;
        this.clazz = clazz;
        this.clazzType = clazz.getName().replace(".", "/");
    }
    
    public MethodRule getRule() {
        return rule;
    }
    
    public void setRule(MethodRule rule) {
        this.rule = rule;
    }
    
    public Class getClazz() {
        return clazz;
    }
    
    public void setClazz(Class clazz) {
        this.clazz = clazz;
    }
    
    public String getClassName() {
        return clazz.getName();
    }
    
    public String getClazzType() {
        return clazzType;
    }
    
    public void setClazzType(String clazzType) {
        this.clazzType = clazzType;
    }
    
    @Override
    public String toString() {
        return clazzType + "." + rule.getMethodName() + (rule.getArgumentDescriptor() == null ? ""
                : rule.getArgumentDescriptor());
    }
}
