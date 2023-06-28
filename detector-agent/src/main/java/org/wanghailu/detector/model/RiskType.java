package org.wanghailu.detector.model;

public enum RiskType {
    
    sinkClass(0),
    
    servlet(1),
    
    filter(2),
    
    listener(3),
    
    handlerMapping(4),
    
    controllerMethod(5),
    
    handlerInterceptor(6),

    jspServlet(7),;
    
    private int value;

    
    RiskType(int value) {
        this.value = value;
    }
    
    public int getValue() {
        return value;
    }

}
