package org.wanghailu.detector.log;

import org.wanghailu.detector.constant.CommonConstant;
import org.wanghailu.detector.model.MapArgs;
import org.wanghailu.detector.util.ExceptionUtils;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Base64;
import java.util.regex.Matcher;

public class LogUtils {

    private static ILogger logger;

    static {
        logger = new SystemOutLogger();
    }

    public static void start(MapArgs mapArgs) {
        boolean writeText = CommonConstant.writeTextLoggerType
                .equals(mapArgs.get(CommonConstant.loggerPropertyKey));
        if (writeText) {
            try {
                logger = new WriteTextLogger(mapArgs.get(CommonConstant.logFileNamePropertyKey));
            } catch (IOException e) {
                e.printStackTrace();
                logger = new SystemOutLogger();
            }
        } else {
            logger = new SystemOutLogger();
        }
        logger.start();
        logger.info(getLogPrefix("INFO")+"程序已初始化，当前日期："+ LocalDate.now());
    }

    /**
     * Logs an INFO level message.
     */
    public static void debug(String msg) {
        logger.info(getLogPrefix("DEBUG")+msg);
    }

    /**
     * Logs an INFO level message.
     */
    public static void debug(String msg, Throwable cause) {
        logger.info(getLogPrefix("DEBUG")+msg);
        logger.info(getLogPrefix("DEBUG")+ExceptionUtils.exceptionToString(cause));
    }

    /**
     * Logs an INFO level message.
     */
    public static void info(String msg) {
        logger.info(getLogPrefix("INFO")+msg);
    }

    /**
     * Logs an INFO level message.
     */
    public static void info(Throwable cause) {
        logger.info(getLogPrefix("INFO")+ExceptionUtils.exceptionToString(cause));
    }


    /**
     * Logs an INFO level message.
     */
    public static void info(String msg, Object... arguments) {
        logger.info(getLogPrefix("INFO")+format(msg, arguments));
    }

    /**
     * Logs an INFO level message.
     */
    public static void info(String msg, Throwable cause) {
        logger.info(getLogPrefix("INFO")+msg);
        logger.info(getLogPrefix("INFO")+ExceptionUtils.exceptionToString(cause));
    }

    /**
     * Logs an ERROR level message.
     */
    public static void error(String msg, Object... arguments) {
        logger.error(getLogPrefix("ERROR")+format(msg, arguments));
    }

    /**
     * Logs an ERROR level message.
     */
    public static void error(Throwable cause) {
        logger.error(getLogPrefix("ERROR")+ExceptionUtils.exceptionToString(cause));
    }

    /**
     * Logs an ERROR level message.
     */
    public static void error(String msg, Throwable cause) {
        logger.error(getLogPrefix("ERROR")+msg);
        logger.error(getLogPrefix("ERROR")+ExceptionUtils.exceptionToString(cause));
    }

    private static String getLogPrefix(String level){
        if(level.length()==4){
            return LocalTime.now().toString()+" ["+level+"]  ";
        }else{
            return LocalTime.now().toString()+" ["+level+"] ";
        }
    }
  private static String format(String from, Object... arguments) {
        if (from != null) {
            String computed = from;
            if (arguments != null && arguments.length != 0) {
                for (Object argument : arguments) {
                    computed = computed.replaceFirst("\\{\\}", Matcher.quoteReplacement(argument.toString()));
                }
            }
            return computed;
        }
        return null;
    }

    public static void dumpClass(String className,byte[] bytecode){
        bytecode = Base64.getEncoder().encode(bytecode);
        String str = new String(bytecode);
        logger.info(getLogPrefix("INFO")+"dumpClass bytecode,className:"+className+",base64:"+str);
    }

    public static void close() {
        logger.close();
        logger = null;
    }
}
