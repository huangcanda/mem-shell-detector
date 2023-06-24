package org.wanghailu.detector.util;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * 异常工具类
 */
public class ExceptionUtils {

    /**
     * 绕过编译器检查抛出一个受检异常（把它假装成一个RuntimeException异常）
     *
     * @param t
     */
    public static void throwException(Throwable t) {
        ExceptionUtils.<RuntimeException>throwException0(t);
    }

    /**
     * 定义泛型异常，泛型最终被擦除而没有生成强转指令
     *
     * @param t
     * @param <E>
     * @throws E
     */
    private static <E extends Throwable> void throwException0(Throwable t) throws E {
        throw (E) t;
    }

    /**
     * 把异常对象打印成字符串
     *
     * @param exception
     * @return
     */
    public static String exceptionToString(Throwable exception) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw, true);
        exception.printStackTrace(pw);
        return sw.getBuffer().toString();
    }
}
