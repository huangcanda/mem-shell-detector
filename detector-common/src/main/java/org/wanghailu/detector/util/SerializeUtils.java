package org.wanghailu.detector.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Base64;

/**
 * @author cdhuang
 * @date 2023/6/12
 */
public class SerializeUtils {
    
    
    public static <T> T deserializeFromString(String str) {
        if(str==null||str.trim().length()==0){
            return null;
        }
        try {
            byte[] data = Base64.getDecoder().decode(str);
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
            ObjectInputStream in = new ObjectInputStream(byteArrayInputStream);
            return (T) in.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public static String serializeToString(Object object) {
        if(object==null){
            return "";
        }
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream outputStream = new ObjectOutputStream(byteArrayOutputStream);
            outputStream.writeObject(object);
            byte[] data = byteArrayOutputStream.toByteArray();
            data = Base64.getEncoder().encode(data);
            return new String(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
