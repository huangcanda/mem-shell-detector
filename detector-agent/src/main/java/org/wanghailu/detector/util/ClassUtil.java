package org.wanghailu.detector.util;

import org.wanghailu.detector.log.LogUtils;
import org.wanghailu.detector.model.ClassByteHolder;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.net.URL;
import java.security.ProtectionDomain;

public class ClassUtil {
    
    private static ClassFileTransformer transformer = new ClassFileTransformer() {
        
        @Override
        public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
                ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
            if (className == null || className.isEmpty()) {
                return null;
            }
            ClassByteHolder.setDumpClass(classfileBuffer);
            try {
                URL url = protectionDomain.getCodeSource().getLocation();
                String urlStr = url.toString();
                if (urlStr.startsWith("file:") && urlStr.endsWith(".jar")) {
                    urlStr = "jar:" + urlStr;
                }
                if (urlStr.endsWith(".jar")) {
                    urlStr = urlStr + "!/" + className + ".class";
                } else {
                    urlStr = urlStr + className + ".class";
                }
                
                URL realUrl = new URL(urlStr);
                try (InputStream inputStream = realUrl.openStream()) {
                    byte[] diskData = TruckUtil.getInputStreamData(inputStream);
                    ClassByteHolder.setDiskClass(diskData);
                }
            } catch (NullPointerException e2) {
            
            } catch (FileNotFoundException e3) {
            
            } catch (Exception e) {
                LogUtils.error(e);
            }
            return null;
        }
    };
    
    public static void onGetClassContext(Instrumentation inst, Runnable runnable) {
        try {
            inst.addTransformer(transformer, true);         //transform
            runnable.run();
        } finally {
            inst.removeTransformer(transformer);
        }
    }
    
    /* classfileBuffer - 类文件格式的输入字节缓冲区*/
    public static byte[] getClassBytesOnMemory(Instrumentation inst, Class clazz) {
        return ClassByteHolder.getDumpClass();
    }
    
    /* 读取磁盘字节 */
    public static byte[] getClassBytesOnDisk(Instrumentation inst, Class clazz) {
        return ClassByteHolder.getDiskClass();
    }
}
