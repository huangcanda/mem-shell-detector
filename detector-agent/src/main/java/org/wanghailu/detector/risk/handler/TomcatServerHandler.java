package org.wanghailu.detector.risk.handler;

import org.apache.catalina.Container;
import org.apache.catalina.Wrapper;
import org.apache.catalina.core.StandardContext;
import org.apache.tomcat.util.descriptor.web.FilterDef;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.WebApplicationContext;
import org.wanghailu.detector.log.LogUtils;
import org.wanghailu.detector.model.DetectorContext;
import org.wanghailu.detector.model.RiskClassResult;
import org.wanghailu.detector.model.RiskType;
import org.wanghailu.detector.risk.check.FilterCheck;
import org.wanghailu.detector.risk.check.RequestDestroyedListenerCheck;
import org.wanghailu.detector.risk.check.RequestInitializedListenerCheck;
import org.wanghailu.detector.risk.check.ServletCheck;
import org.wanghailu.detector.util.ReflectUtils;
import org.wanghailu.detector.util.SpringContextUtil;

import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletRequestListener;
import java.util.HashSet;
import java.util.Set;

public class TomcatServerHandler extends BaseHandler{

    public TomcatServerHandler(DetectorContext context,String info) {
        super(context,info);
    }

    @Override
    public boolean isEnabled() {
        return detectorContext.getLoadedClass("org.apache.catalina.core.StandardContext") != null;
    }
    
    public StandardContext getContext() {
        ApplicationContext context = SpringContextUtil.getApplicationContext();
        if (context == null || context instanceof WebApplicationContext == false) {
            return null;
        }
        WebApplicationContext webApplicationContext = (WebApplicationContext) context;
        ServletContext servletContext = webApplicationContext.getServletContext();
        Object applicationContext = ReflectUtils.getFieldValue(servletContext, "context");
        return (StandardContext) ReflectUtils.getFieldValue(applicationContext, "context");
    }

    @Override
    public void doHandle() {
        StandardContext context = getContext();
        if (context == null) {
            LogUtils.error("applicationContext FieldValue context is null");
            return;
        }
        Set<Class> filterClass = new HashSet<>();
        for (FilterDef filterDef : context.findFilterDefs()) {
            filterClass.add(filterDef.getFilter().getClass());
        }
        for (Class aClass : filterClass) {
            detectorContext.addRiskCheckAble(new FilterCheck(aClass));
            detectorContext.addRiskClassResult(new RiskClassResult(aClass, RiskType.filter, true));
        }
        
        Set<Class> servletClass = new HashSet<>();
        Container[] servletContainer = context.findChildren();
        for (Container container : servletContainer) {
            if (container instanceof Wrapper) {
                Servlet servlet = ((Wrapper) container).getServlet();
                servletClass.add(servlet.getClass());
            }
        }
        for (Class aClass : servletClass) {
            detectorContext.addRiskCheckAble(new ServletCheck(aClass));
            detectorContext.addRiskClassResult(new RiskClassResult(aClass, RiskType.servlet, true));
        }
        
        Set<Class> listenerClass = new HashSet<>();
        for (Object applicationEventListener : context.getApplicationEventListeners()) {
            if (applicationEventListener instanceof ServletRequestListener) {
                listenerClass.add(applicationEventListener.getClass());
            }
        }
        
        for (Class aClass : listenerClass) {
            detectorContext.addRiskCheckAble(new RequestInitializedListenerCheck(aClass));
            detectorContext.addRiskCheckAble(new RequestDestroyedListenerCheck(aClass));
            detectorContext.addRiskClassResult(new RiskClassResult(aClass, RiskType.listener, true));
        }
    }
}
