package org.wanghailu.detector.risk;

import org.wanghailu.detector.log.LogUtils;
import org.wanghailu.detector.risk.handler.RiskClassOnServerHandler;
import org.wanghailu.detector.risk.handler.SpringMvcServerHandler;
import org.wanghailu.detector.risk.handler.TomcatServerHandler;
import org.wanghailu.detector.model.DetectorContext;

public class RiskClassCatcher {
    

    private DetectorContext context;
    
    public RiskClassCatcher(DetectorContext context) {
        this.context = context;
    }
    
    /**
     * 从tomcat容器或者spring-mvc上下文中获取风险类信息
     */
    public void filterRiskClassOnServer() {
        RiskClassOnServerHandler riskClassOnServerHandler = new RiskClassOnServerHandler(context);
        riskClassOnServerHandler.doHandle();
        
        if (isTomcatWebServer()) {
            TomcatServerHandler tomcatServerHandler = new TomcatServerHandler(context);
            tomcatServerHandler.doHandle();
        }else{
            LogUtils.debug("识别不到Tomcat容器上下文!");
        }
        
        if (isSpringMvcWebServer()) {
            SpringMvcServerHandler springMvcServerHandler = new SpringMvcServerHandler(context);
            springMvcServerHandler.doHandle();
        }else{
            LogUtils.debug("识别不到SpringMVC容器上下文!");
        }
    }
    
    
    public boolean isTomcatWebServer() {
        return canLoadClass("org.apache.catalina.core.StandardContext");
    }
    
    public boolean isSpringMvcWebServer() {
        return canLoadClass("org.springframework.web.servlet.DispatcherServlet");
    }
    
    
    private boolean canLoadClass(String className) {
        return context.getLoadedClass(className) != null;
    }
}
