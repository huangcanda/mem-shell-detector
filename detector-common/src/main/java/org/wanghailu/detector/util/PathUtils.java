package org.wanghailu.detector.util;

import org.wanghailu.detector.log.LogUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * referer https://github.com/alibaba/arthas/blob/master/common/src/main/java/com/taobao/arthas/common/FileUtils.java
 **/

public class PathUtils {
    public static String getCurrentJarPath() throws Exception {
        return new File(PathUtils.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getPath();
    }

    public static String getCurrentDirectory() {
        try {
            return new File(getCurrentJarPath()).getParent();
        } catch (Exception e) {
            return new File(".").getAbsolutePath();
        }
    }

    public static byte[] readResources(String src) throws IOException {
        InputStream is = PathUtils.class.getResourceAsStream(src);
        if(is == null){
            return null;
        }

        ByteArrayOutputStream bytestream = new ByteArrayOutputStream();
        int ch;
        try {
            while ((ch = is.read()) != -1) {
                bytestream.write(ch);
            }
        } catch (IOException e) {
            LogUtils.error(e);
        }
        byte[] program = bytestream.toByteArray();

        try {
            bytestream.close();
        } catch (IOException e) {
            LogUtils.error(e);
        }

        is.close();

        return program;
    }

    public static void copyResources(byte[] program, File dst) throws IOException {

        if(program == null){
            return ;
        }
        java.io.FileOutputStream fo = new java.io.FileOutputStream(dst);
        fo.write(program);
        fo.close();
    }

    public static boolean createDirectory(String absolute_path) {
        File dir = new File(absolute_path);
        return createDirectory(dir);
    }

    public static boolean createDirectory(File dir) {
        try {
            dir.mkdirs();
        } catch (Throwable t) {
            return false;
        }
        return true;
    }

    public static File getTempDirectory() {
        return new File(System.getProperty("java.io.tmpdir"));
    }


}
