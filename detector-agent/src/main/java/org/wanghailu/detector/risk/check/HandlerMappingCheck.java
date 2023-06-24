package org.wanghailu.detector.risk.check;

/**
 * @author cdhuang
 * @date 2023/6/21
 */
public class HandlerMappingCheck extends RiskMethodCheck{
    
    public HandlerMappingCheck(Class clazz) {
        super(clazz, "getHandler", "(Ljavax/servlet/http/HttpServletRequest;)Lorg/springframework/web/servlet/HandlerExecutionChain;");
    }
}
