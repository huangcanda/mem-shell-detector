package org.wanghailu.detector.model;

public class ClassByteHolder {

    private static ThreadLocal<ClassByteHolder> threadContextThreadLocal = new ThreadLocal<ClassByteHolder>() {
        @Override
        protected ClassByteHolder initialValue() {
            return new ClassByteHolder();
        }
    };

    public static byte[] getDumpClass() {
        return threadContextThreadLocal.get().getDumpClassByte();
    }

    public static void setDumpClass(byte[] dumpClass) {
        threadContextThreadLocal.get().setDumpClassByte(dumpClass);
    }

    public static byte[] getDiskClass() {
        return threadContextThreadLocal.get().getDiskClassByte();
    }

    public static void setDiskClass(byte[] diskClass) {
        threadContextThreadLocal.get().setDiskClassByte(diskClass);
    }

    public static void clean() {
        threadContextThreadLocal.get().setDumpClassByte(null);
        threadContextThreadLocal.get().setDiskClassByte(null);
    }
    
    public static void finallyClean() {
        threadContextThreadLocal.remove();
    }
    
    
    private byte[] dumpClassByte;
    
    private byte[] diskClassByte;
    
    public byte[] getDumpClassByte() {
        return dumpClassByte;
    }
    
    public void setDumpClassByte(byte[] dumpClassByte) {
        this.dumpClassByte = dumpClassByte;
    }
    
    public byte[] getDiskClassByte() {
        return diskClassByte;
    }
    
    public void setDiskClassByte(byte[] diskClassByte) {
        this.diskClassByte = diskClassByte;
    }
}
