package org.wanghailu.detector.model;

import org.wanghailu.detector.log.LogUtils;

import java.lang.reflect.Method;

public class RiskMethodResult extends RiskClassResult {

    protected Method method;

    public RiskMethodResult(Class riskClass, RiskType riskType, Method method) {
        super(riskClass, riskType,true);
        this.method = method;
        LogUtils.debug("发现待检测风险方法"+riskClass.getName()+"."+method.getName()+",风险类型为"+riskType);
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    @Override
    public String toString() {
        return "MethodResult{" +
                "method=" + method +
                ", riskClass=" + riskClass +
                ", riskType=" + riskType +
                '}';
    }
}
