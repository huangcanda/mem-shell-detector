package org.wanghailu.detector.util;

import org.wanghailu.detector.log.LogUtils;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;

public class TruckUtil {


    public static byte[] getInputStreamData(InputStream in) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            streamCopy(in, outputStream);
            return outputStream.toByteArray();
        } catch (IOException e) {
            LogUtils.error(e);
        }
        return null;
    }

    public static void streamCopy(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int len;
        while ((len = in.read(buffer)) != -1) {
            out.write(buffer, 0, len);
        }
    }

    public static IOException streamClose(final Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (final IOException ioe) {
            return ioe;
        }
        return null;
    }

    public static void getDifferent(Object obj1, Object obj2, String objInfo, List<String> differentInfos,
                                    List<Class> compositeClassList) {
        if (obj1 == null && obj2 == null) {
            return;
        }
        if (obj1 == null || obj2 == null) {
            differentInfos.add(objInfo + "其中一个值为空，值1为" + obj1 + ",值2为" + obj2);
            return;
        }
        Class class1 = obj1.getClass();
        Class class2 = obj2.getClass();
        if (!class1.equals(class2)) {
            differentInfos.add(objInfo + "类型不同，值1为" + class1.getName() + ",值2为" + class2.getName());
            return;
        }
        if (class1.isArray()) {
            int length1 = Array.getLength(obj1);
            int length2 = Array.getLength(obj2);
            if (length1 != length2) {
                differentInfos.add(objInfo + "数组的长度不同，值1为" + length1 + ",值2为" + length2);
                return;
            }
            for (int i = 0; i < length1; i++) {
                getDifferent(Array.get(obj1, i), Array.get(obj2, i), objInfo + "中第" + (i + 1) + "个", differentInfos,
                        compositeClassList);
            }
        } else if (List.class.isAssignableFrom(class1)) {
            List list1 = (List) obj1;
            List list2 = (List) obj2;
            if (list1.size() != list2.size()) {
                differentInfos.add(objInfo + "list的长度不同，值1为" + list1.size() + ",值2为" + list2.size());
                return;
            }
            int size = list1.size();
            for (int i = 0; i < size; i++) {
                getDifferent(list1.get(i), list2.get(i), objInfo + "的第" + i + "个元素", differentInfos,
                        compositeClassList);
            }
        } else if (!compositeClassList.contains(class1)) {
            if (!obj1.equals(obj2)) {
                differentInfos.add(objInfo + "值不同，值1为" + obj1 + ",值2为" + obj2);
            }
            return;
        } else {
            String info = objInfo;
            Arrays.stream(obj1.getClass().getDeclaredFields()).filter(x -> !Modifier.isStatic(x.getModifiers()))
                    .forEach(field -> {
                        Object val1 = ReflectUtils.getFieldValue(obj1, field);
                        Object val2 = ReflectUtils.getFieldValue(obj2, field);
                        getDifferent(val1, val2, info + "的" + field.getName(), differentInfos, compositeClassList);
                    });
        }
    }
}
