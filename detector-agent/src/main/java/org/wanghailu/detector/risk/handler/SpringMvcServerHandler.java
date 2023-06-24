package org.wanghailu.detector.risk.handler;

import org.springframework.context.ApplicationContext;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.handler.AbstractHandlerMapping;
import org.springframework.web.servlet.handler.AbstractHandlerMethodMapping;
import org.springframework.web.servlet.handler.WebRequestHandlerInterceptorAdapter;
import org.wanghailu.detector.log.LogUtils;
import org.wanghailu.detector.model.DetectorContext;
import org.wanghailu.detector.model.Pair;
import org.wanghailu.detector.model.RiskClassResult;
import org.wanghailu.detector.model.RiskMethodResult;
import org.wanghailu.detector.model.RiskType;
import org.wanghailu.detector.risk.check.AfterHandlerInterceptorCheck;
import org.wanghailu.detector.risk.check.HandlerMappingCheck;
import org.wanghailu.detector.risk.check.HandlerMethodCheck;
import org.wanghailu.detector.risk.check.PostHandlerInterceptorCheck;
import org.wanghailu.detector.risk.check.PreHandlerInterceptorCheck;
import org.wanghailu.detector.util.ReflectUtils;
import org.wanghailu.detector.util.SpringContextUtil;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SpringMvcServerHandler {
    
    private DetectorContext detectorContext;
    
    public SpringMvcServerHandler(DetectorContext detectorContext) {
        this.detectorContext = detectorContext;
    }
    
    public DispatcherServlet getDispatcherServlet() {
        ApplicationContext context = SpringContextUtil.getApplicationContext();
        if (context == null) {
            return null;
        }
        return context.getBean(DispatcherServlet.class);
    }
    
    public void doHandle() {
        DispatcherServlet dispatcherServlet = getDispatcherServlet();
        if (dispatcherServlet == null) {
            return;
        }
        Set<Class> handlerMappingClass = new HashSet<>();
        Set<Class> handlerInterceptorClass = new HashSet<>();
        Set<Pair<Class, Method>> handlerMethodSet = new HashSet<>();
        List<HandlerMapping> handlerMappings = (List<HandlerMapping>) ReflectUtils
                .getFieldValue(dispatcherServlet, "handlerMappings");
        if (handlerMappings == null) {
            return;
        }
        for (HandlerMapping handlerMapping : handlerMappings) {
            handlerMappingClass.add(handlerMapping.getClass());
            if (handlerMapping instanceof AbstractHandlerMapping) {
                AbstractHandlerMapping abstractHandlerMapping = (AbstractHandlerMapping) handlerMapping;
                List<HandlerInterceptor> adaptedInterceptors = (List<HandlerInterceptor>) ReflectUtils
                        .getFieldValue(abstractHandlerMapping, "adaptedInterceptors");
                if (adaptedInterceptors != null) {
                    for (HandlerInterceptor adaptedInterceptor : adaptedInterceptors) {
                        handlerInterceptorClass.add(adaptedInterceptor.getClass());
                        if (adaptedInterceptor instanceof WebRequestHandlerInterceptorAdapter) {
                            Object requestInterceptor = ReflectUtils
                                    .getFieldValue(adaptedInterceptor, "requestInterceptor");
                            handlerInterceptorClass.add(requestInterceptor.getClass());
                        }
                    }
                } else {
                    LogUtils.error("abstractHandlerMapping FieldValue adaptedInterceptors is null");
                }
                
            }
            if (handlerMapping instanceof AbstractHandlerMethodMapping) {
                Object mappingRegistry = ReflectUtils.getFieldValue(handlerMapping, "mappingRegistry");
                Map<Object, HandlerMethod> mappingLookup = (Map) ReflectUtils
                        .getFieldValue(mappingRegistry, "mappingLookup");
                if (mappingLookup == null) {
                    try {
                        Method method = handlerMapping.getClass().getMethod("getHandlerMethods");
                        mappingLookup = (Map<Object, HandlerMethod>) method.invoke(handlerMapping);
                    } catch (Exception e) {
                    
                    }
                }
                if (mappingLookup != null) {
                    for (HandlerMethod handlerMethod : mappingLookup.values()) {
                        handlerMethodSet.add(new Pair<>(handlerMethod.getBeanType(), handlerMethod.getMethod()));
                    }
                } else {
                    LogUtils.error("mappingRegistry FieldValue mappingLookup is null");
                }
            }
            
        }
        for (Class mappingClass : handlerMappingClass) {
            detectorContext.addRiskCheckAble(new HandlerMappingCheck(mappingClass));
            detectorContext
                    .addRiskClassResult(new RiskClassResult(mappingClass, RiskType.handlerMapping, true));
        }
        for (Class interceptorClass : handlerInterceptorClass) {
            detectorContext.addRiskCheckAble(new PreHandlerInterceptorCheck(interceptorClass));
            detectorContext.addRiskCheckAble(new PostHandlerInterceptorCheck(interceptorClass));
            detectorContext.addRiskCheckAble(new AfterHandlerInterceptorCheck(interceptorClass));
            detectorContext.addRiskClassResult(
                    new RiskClassResult(interceptorClass, RiskType.handlerInterceptor, true));
        }
        for (Pair<Class, Method> classMethodPair : handlerMethodSet) {
            detectorContext
                    .addRiskCheckAble(new HandlerMethodCheck(classMethodPair.getFirst(), classMethodPair.getSecond()));
            detectorContext.addRiskClassResult(
                    new RiskMethodResult(classMethodPair.getFirst(), RiskType.controllerMethod,
                            classMethodPair.getSecond()));
        }
    }
}
