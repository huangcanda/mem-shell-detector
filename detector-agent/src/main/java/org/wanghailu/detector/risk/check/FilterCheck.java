package org.wanghailu.detector.risk.check;

/**
 * @author cdhuang
 * @date 2023/6/21
 */
public class FilterCheck extends RiskMethodCheck {
    
    
    public FilterCheck(Class clazz) {
        super(clazz, "doFilter", "(Ljavax/servlet/ServletRequest;Ljavax/servlet/ServletResponse;Ljavax/servlet/FilterChain;)V");
    }
}
