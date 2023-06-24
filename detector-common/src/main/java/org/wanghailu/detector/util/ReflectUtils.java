package org.wanghailu.detector.util;


import org.wanghailu.detector.log.LogUtils;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 反射工具类(反射相关的杂七杂八的往这里扔)
 */
public class ReflectUtils {

    
    public static <T> T newInstance(Class<T> clazz) {
        try {
            return clazz.newInstance();
        } catch (Exception e) {
            ExceptionUtils.throwException(e);
            return null;
        }
    }

    /**
     * 利用反射设置指定对象的指定属性为指定的值
     *
     * @param obj        目标对象
     * @param fieldName  目标属性
     * @param fieldValue 目标值
     */
    public static void setFieldValue(Object obj, String fieldName, Object fieldValue) {
        if (obj instanceof Map) {
            ((Map) obj).put(fieldName, fieldValue);
            return;
        }
        Field field = ReflectUtils.getField(obj, fieldName);
        if (field != null) {
            try {
                field.setAccessible(true);
                field.set(obj, fieldValue);
            } catch (IllegalArgumentException e) {
                LogUtils.error(e);
            } catch (IllegalAccessException e) {
                LogUtils.error(e);
            }
        }
    }

    /**
     * 利用反射获取指定对象的指定属性
     *
     * @param obj       目标对象
     * @param fieldName 目标属性
     * @return 目标属性的值
     */
    public static Object getFieldValue(Object obj, String fieldName) {
        if (obj instanceof Map) {
            return ((Map) obj).get(fieldName);
        }
        Object result = null;
        Field field = ReflectUtils.getField(obj, fieldName);
        if (field != null) {
            field.setAccessible(true);
            try {
                result = field.get(obj instanceof Class ? null : obj);
            } catch (IllegalArgumentException e) {
                LogUtils.error(e);
            } catch (IllegalAccessException e) {
                LogUtils.error(e);
            }
        }
        return result;
    }

    public static Object getFieldValue(Object obj, Field field) {
        Object result = null;
        field.setAccessible(true);
        try {
            result = field.get(obj instanceof Class ? null : obj);
        } catch (IllegalArgumentException e) {
            LogUtils.error(e);
        } catch (IllegalAccessException e) {
            LogUtils.error(e);
        }
        return result;
    }

    /**
     * 利用反射获取指定对象里面的指定属性
     *
     * @param obj       目标对象或对象的class
     * @param fieldName 目标属性
     * @return 目标字段
     */
    public static Field getField(Object obj, String fieldName) {
        Class c;
        if (obj instanceof Class) {
            c = (Class) obj;
        } else {
            c = obj.getClass();
        }
        Field field = null;
        for (Class<?> clazz = c; clazz != Object.class; clazz = clazz.getSuperclass()) {
            try {
                field = clazz.getDeclaredField(fieldName);
                break;
            } catch (NoSuchFieldException e) {
                //这里不用做处理，子类没有该字段可能对应的父类有，都没有就返回null。
            }
        }
        return field;
    }

    /**
     * 获得类的相关field,method信息
     *
     * @param clazz
     * @return
     */
    public static PropertyDescriptor[] getBeanPropertyDescriptors(Class clazz) {
        BeanInfo info = null;
        try {
            info = Introspector.getBeanInfo(clazz, Object.class);
        } catch (IntrospectionException e) {
            LogUtils.error(e);
        }
        List<Field> fields = getAllField(clazz);
        Map<String, Integer> fieldOrderMap = new HashMap<>();
        int order = 0;
        for (Field field : fields) {
            String fieldName = field.getName();
            if (fieldOrderMap.get(fieldName) == null) {
                fieldOrderMap.put(fieldName, order);
                order++;
            }
        }
        PropertyDescriptor[] array = info.getPropertyDescriptors();
        Arrays.sort(array, (o1, o2) -> {
            Integer order1 = 1000;
            if (o1.getName() != null && fieldOrderMap.get(o1.getName()) != null) {
                order1 = fieldOrderMap.get(o1.getName());
            }
            Integer order2 = 1000;
            if (o2.getName() != null && fieldOrderMap.get(o2.getName()) != null) {
                order2 = fieldOrderMap.get(o2.getName());
            }
            return order1.compareTo(order2);
        });
        return array;
    }

    /**
     * 获得clazz的所有field
     *
     * @param clazz
     * @return
     */
    public static List<Field> getAllField(Class clazz) {
        List<Field> result = new ArrayList<>();
        for (; clazz != Object.class; clazz = clazz.getSuperclass()) {
            for (Field declaredField : clazz.getDeclaredFields()) {
                result.add(declaredField);
            }
        }
        return result;
    }

    public static Method findDeclaredMethod(final Class type, final String methodName, final Class[] parameterTypes)
            throws NoSuchMethodException {

        Class cl = type;
        while (cl != null) {
            try {
                return cl.getDeclaredMethod(methodName, parameterTypes);
            } catch (NoSuchMethodException e) {
                cl = cl.getSuperclass();
            }
        }
        throw new NoSuchMethodException(methodName);
    }

    public static Object invokeMethod(Method method, Object target, Object... args) {
        try {
            return method.invoke(target, args);
        } catch (Exception ex) {
            ExceptionUtils.throwException(ex);
            return null;
        }
    }

    public static Method getMethod(Object obj, String methodName, Class<?>... paramTypes) {
        Class clazz;
        if (obj instanceof Class) {
            clazz = (Class) obj;
        } else {
            clazz = obj.getClass();
        }
        Class<?> searchType = clazz;
        while (searchType != null) {
            Method[] methods = (searchType.isInterface() ? searchType.getMethods() : getDeclaredMethods(searchType));
            for (Method method : methods) {
                if (methodName.equals(method.getName()) && (paramTypes == null || Arrays
                        .equals(paramTypes, method.getParameterTypes()))) {
                    return method;
                }
            }
            searchType = searchType.getSuperclass();
        }
        return null;
    }

    private static Method[] getDeclaredMethods(Class<?> clazz) {
        try {
            Method[] declaredMethods = clazz.getDeclaredMethods();
            List<Method> defaultMethods = findConcreteMethodsOnInterfaces(clazz);
            if (defaultMethods != null) {
                Method[] result = new Method[declaredMethods.length + defaultMethods.size()];
                System.arraycopy(declaredMethods, 0, result, 0, declaredMethods.length);
                int index = declaredMethods.length;
                for (Method defaultMethod : defaultMethods) {
                    result[index] = defaultMethod;
                    index++;
                }
                return result;
            }
            return declaredMethods;
        } catch (Throwable ex) {
            throw new IllegalStateException(
                    "Failed to introspect Class [" + clazz.getName() + "] from ClassLoader [" + clazz.getClassLoader()
                            + "]", ex);
        }
    }

    private static List<Method> findConcreteMethodsOnInterfaces(Class<?> clazz) {
        List<Method> result = null;
        for (Class<?> ifc : clazz.getInterfaces()) {
            for (Method ifcMethod : ifc.getMethods()) {
                if (!Modifier.isAbstract(ifcMethod.getModifiers())) {
                    if (result == null) {
                        result = new ArrayList<>();
                    }
                    result.add(ifcMethod);
                }
            }
        }
        return result;
    }
}
