package org.wanghailu.detector.risk;

import org.wanghailu.detector.log.LogUtils;
import org.wanghailu.detector.risk.handler.*;
import org.wanghailu.detector.model.DetectorContext;

import java.util.ArrayList;
import java.util.List;

public class RiskClassCatcher {
    

    private DetectorContext context;

    List<BaseHandler> handlerList;
    
    public RiskClassCatcher(DetectorContext context) {
        this.context = context;
        init();
    }

    private void init() {
        handlerList = new ArrayList<>();
        handlerList.add(new RiskClassOnServerHandler(context,"风险类"));
        handlerList.add(new SpringMvcServerHandler(context,"SpringMVC容器"));
        handlerList.add(new TomcatServerHandler(context,"Tomcat容器"));
        handlerList.add(new JspOnServerHandler(context,"JSP"));
    }

    /**
     * 从tomcat容器或者spring-mvc上下文中获取风险类信息
     */
    public void filterRiskClassOnServer() {
        for (BaseHandler handler : handlerList) {
            if (handler.isEnabled()) {
                handler.doHandle();
            }else{
                LogUtils.debug("识别到项目没有使用"+handler.getInfo()+"!");
            }
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
