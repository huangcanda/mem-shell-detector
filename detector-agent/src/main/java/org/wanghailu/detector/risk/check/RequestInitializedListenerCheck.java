package org.wanghailu.detector.risk.check;

/**
 * @author cdhuang
 * @date 2023/6/21
 */
public class RequestInitializedListenerCheck extends RiskMethodCheck {
    
    public RequestInitializedListenerCheck(Class clazz) {
        super(clazz, "requestInitialized", "(Ljavax/servlet/ServletRequestEvent;)V");
    }
}
