package org.wanghailu.detector.util;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.LiveBeansView;
import org.wanghailu.detector.log.LogUtils;

import java.util.Map;
import java.util.Set;

public class SpringContextUtil {

    private static ApplicationContext applicationContext;

    /**
     * 获得spring上下文 ApplicationContext
     *
     * @return
     */
    public static ApplicationContext getApplicationContext() {
        if (applicationContext == null) {
            try {
                Class shutdownHooks = Class.forName("java.lang.ApplicationShutdownHooks");
                Map hooks = (Map) ReflectUtils.getFieldValue(shutdownHooks, "hooks");
                for (Object value : hooks.values()) {
                    String className = value.getClass().getName();
                    if (className.startsWith("org.springframework.context.support.AbstractApplicationContext")) {
                        applicationContext = (ApplicationContext) ReflectUtils.getFieldValue(value, "this$0");
                        break;
                    }
                }
            } catch (ClassNotFoundException e) {
                LogUtils.error(e);
            }
            if (applicationContext == null) {
                Set<ConfigurableApplicationContext> applicationContextSet = (Set<ConfigurableApplicationContext>) ReflectUtils.getFieldValue(LiveBeansView.class, "applicationContexts");
                if (applicationContextSet != null && applicationContextSet.size() > 0) {
                    applicationContext = applicationContextSet.iterator().next();
                }
            }
        }
        return applicationContext;
    }

    /**
     * 通过name获取 Bean
     *
     * @param <T>  Bean类型
     * @param name Bean名称
     * @return Bean
     */
    @SuppressWarnings("unchecked")
    public static <T> T getBean(String name) {
        return (T) getApplicationContext().getBean(name);
    }

    /**
     * 通过class获取Bean
     *
     * @param <T>   Bean类型
     * @param clazz Bean类
     * @return Bean对象
     */
    public static <T> T getBean(Class<T> clazz) {
        return getApplicationContext().getBean(clazz);
    }
}
