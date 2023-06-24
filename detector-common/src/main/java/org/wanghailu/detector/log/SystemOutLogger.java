package org.wanghailu.detector.log;

public class SystemOutLogger implements ILogger {

    @Override
    public void info(String msg) {
        System.out.println(msg);
    }

    @Override
    public void error(String msg) {
        System.err.println(msg);
    }

}
