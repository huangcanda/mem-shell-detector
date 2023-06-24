package org.wanghailu.detector.log;


public interface ILogger {

    /**
     * Logs an INFO level message.
     */
    void info(String msg);

    /**
     * Logs an ERROR level message.
     */
    void error(String msg);

    default void start() {

    }

    default void close() {

    }
}
