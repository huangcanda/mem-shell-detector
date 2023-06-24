package org.wanghailu.detector.risk.check;

import org.wanghailu.objectweb.asm.Type;

import java.lang.reflect.Method;

/**
 * @author cdhuang
 * @date 2023/6/21
 */
public class HandlerMethodCheck extends RiskMethodCheck {
    
    public HandlerMethodCheck(Class clazz, Method method) {
        super(clazz, method.getName(), Type.getMethodDescriptor(method));
    }
}
