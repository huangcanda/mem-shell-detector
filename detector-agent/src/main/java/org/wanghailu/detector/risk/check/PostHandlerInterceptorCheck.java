package org.wanghailu.detector.risk.check;

/**
 * @author cdhuang
 * @date 2023/6/21
 */
public class PostHandlerInterceptorCheck extends RiskMethodCheck{
    
    public PostHandlerInterceptorCheck(Class clazz) {
        super(clazz, "postHandle", null);
    }
}
