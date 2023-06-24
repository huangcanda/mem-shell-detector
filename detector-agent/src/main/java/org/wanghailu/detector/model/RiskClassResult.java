package org.wanghailu.detector.model;

import org.wanghailu.detector.log.LogUtils;

import java.util.Objects;

public class RiskClassResult {

    protected Class riskClass;

    protected RiskType riskType;

    public RiskClassResult(Class riskClass, RiskType riskType) {
        this(riskClass,riskType,false);
    }
    
    public RiskClassResult(Class riskClass, RiskType riskType, boolean useInServer) {
        this.riskClass = riskClass;
        this.riskType = riskType;
        LogUtils.debug("发现待检测风险类"+riskClass.getName()+",风险类型为"+riskType);
    }
    
    public Class getRiskClass() {
        return riskClass;
    }

    public RiskType getRiskType() {
        return riskType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RiskClassResult that = (RiskClassResult) o;
        return Objects.equals(riskClass, that.riskClass) && riskType == that.riskType;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(riskClass, riskType);
    }
    
    @Override
    public String toString() {
        return "Result{" +
                "riskClass=" + riskClass +
                ", riskType=" + riskType +
                '}';
    }
}
