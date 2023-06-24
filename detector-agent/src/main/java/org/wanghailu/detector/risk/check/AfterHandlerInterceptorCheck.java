package org.wanghailu.detector.risk.check;

/**
 * @author cdhuang
 * @date 2023/6/21
 */
public class AfterHandlerInterceptorCheck extends RiskMethodCheck{
    
    public AfterHandlerInterceptorCheck(Class clazz) {
        super(clazz, "afterCompletion", null);
    }
}
