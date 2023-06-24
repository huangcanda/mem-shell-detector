package org.wanghailu.detector.risk.check;

/**
 * @author cdhuang
 * @date 2023/6/21
 */
public class PreHandlerInterceptorCheck extends RiskMethodCheck{
    
    public PreHandlerInterceptorCheck(Class clazz) {
        super(clazz, "preHandle", null);
    }
}
