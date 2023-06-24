package org.wanghailu.detector.risk.check;

/**
 * @author cdhuang
 * @date 2023/6/21
 */
public class ServletCheck extends RiskMethodCheck{
    
    public ServletCheck(Class clazz) {
        super(clazz, "service","(Ljavax/servlet/ServletRequest;Ljavax/servlet/ServletResponse;)V");
    }
}
