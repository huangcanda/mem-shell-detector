package org.wanghailu.detector.risk.check;

/**
 * @author cdhuang
 * @date 2023/6/21
 */
public class RequestDestroyedListenerCheck extends RiskMethodCheck {
    
    public RequestDestroyedListenerCheck(Class clazz) {
        super(clazz, "requestDestroyed", "(Ljavax/servlet/ServletRequestEvent;)V");
    }
}
