package org.wanghailu.detector.analyze.model;

import org.wanghailu.detector.analyze.AnalysisCodeContext;
import org.wanghailu.detector.analyze.AnalysisMethodInfo;
import org.wanghailu.detector.analyze.CodeMethodInvokeContext;
import org.wanghailu.detector.analyze.rule.MethodRuleOnContext;
import org.wanghailu.objectweb.asm.Type;

import java.util.List;
import java.util.Objects;


public class Trace {

    private Integer argIndex;

    private AnalysisMethodInfo analysisMethodInfo;

    private Type ldcType;

    private String ldcValue;

    private CodeMethodInvokeContext codeMethodInvokeContext;

    private MethodRuleOnContext methodRuleOnContext;

    private Object otherInfo;

    private List<AnalysisMethodInfo> analysisMethodInfoList = AnalysisCodeContext.getAnalysisCodeContext().invokeMethodList;

    public static Trace byArgIndex(Integer argIndex, AnalysisMethodInfo analysisMethodInfo) {
        return new Trace(argIndex,analysisMethodInfo);
    }

    public static Trace byLdcValue(Type ldcType, String ldcValue) {
        return new Trace(ldcType, ldcValue);
    }

    public static Trace byMethodInfo(CodeMethodInvokeContext codeMethodInvokeContext) {
        return new Trace(codeMethodInvokeContext);
    }

    public Trace(int argIndex, AnalysisMethodInfo analysisMethodInfo) {
        this.argIndex = argIndex;
        this.analysisMethodInfo = analysisMethodInfo;
    }

    public Trace(Type ldcType, String ldcValue) {
        this.ldcType = ldcType;
        this.ldcValue = ldcValue;
    }

    public Trace(CodeMethodInvokeContext codeMethodInvokeContext) {
        this.codeMethodInvokeContext = codeMethodInvokeContext;
    }

    public Integer getArgIndex() {
        return argIndex;
    }

    public void setArgIndex(Integer argIndex) {
        this.argIndex = argIndex;
    }

    public AnalysisMethodInfo getAnalysisMethodInfo() {
        return analysisMethodInfo;
    }

    public void setAnalysisMethodInfo(AnalysisMethodInfo analysisMethodInfo) {
        this.analysisMethodInfo = analysisMethodInfo;
    }

    public Type getLdcType() {
        return ldcType;
    }

    public void setLdcType(Type ldcType) {
        this.ldcType = ldcType;
    }

    public String getLdcValue() {
        return ldcValue;
    }

    public void setLdcValue(String ldcValue) {
        this.ldcValue = ldcValue;
    }

    public Object getOtherInfo() {
        return otherInfo;
    }

    public void setOtherInfo(Object otherInfo) {
        this.otherInfo = otherInfo;
    }

    public CodeMethodInvokeContext getCodeMethodInvokeContext() {
        return codeMethodInvokeContext;
    }

    public MethodRuleOnContext getMethodRuleOnContext() {
        return methodRuleOnContext;
    }

    public void setMethodRuleOnContext(MethodRuleOnContext methodRuleOnContext) {
        this.methodRuleOnContext = methodRuleOnContext;
    }

    public List<AnalysisMethodInfo> getAnalysisMethodInfoList() {
        return analysisMethodInfoList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Trace trace = (Trace) o;
        return Objects.equals(argIndex, trace.argIndex) &&
                Objects.equals(analysisMethodInfo, trace.analysisMethodInfo) &&
                Objects.equals(ldcType, trace.ldcType) &&
                Objects.equals(ldcValue, trace.ldcValue) &&
                Objects.equals(codeMethodInvokeContext, trace.codeMethodInvokeContext) &&
                Objects.equals(otherInfo, trace.otherInfo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(argIndex, analysisMethodInfo, ldcType, ldcValue, codeMethodInvokeContext, otherInfo);
    }
}
