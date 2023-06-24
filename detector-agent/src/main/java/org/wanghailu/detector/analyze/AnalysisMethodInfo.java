package org.wanghailu.detector.analyze;


import org.wanghailu.detector.analyze.model.StackItem;

import java.util.List;
import java.util.Objects;

public class AnalysisMethodInfo {

    protected int access;

    protected Class clazz;

    protected String methodName;

    protected String methodDescriptor;

    protected List<StackItem> itemList;

    public AnalysisMethodInfo(Class clazz, String methodName, String methodDescriptor) {
        this.clazz = clazz;
        this.methodName = methodName;
        this.methodDescriptor = methodDescriptor;
    }

    public AnalysisMethodInfo(int access, Class clazz, String methodName, String methodDescriptor) {
        this.access = access;
        this.clazz = clazz;
        this.methodName = methodName;
        this.methodDescriptor = methodDescriptor;
    }
    
    public Class getClazz() {
        return clazz;
    }
    
    public String getMethodName() {
        return methodName;
    }
    
    public String getMethodDescriptor() {
        return methodDescriptor;
    }

    public List<StackItem> getItemList() {
        return itemList;
    }

    public void setItemList(List<StackItem> itemList) {
        this.itemList = itemList;
    }

    public int getAccess() {
        return access;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AnalysisMethodInfo that = (AnalysisMethodInfo) o;
        return Objects.equals(clazz, that.clazz) &&
                Objects.equals(methodName, that.methodName) &&
                Objects.equals(methodDescriptor, that.methodDescriptor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(clazz, methodName, methodDescriptor);
    }

    @Override
    public String toString() {
        return clazz.getName() + "." + methodName + '('
                + methodDescriptor + ')';
    }
}
