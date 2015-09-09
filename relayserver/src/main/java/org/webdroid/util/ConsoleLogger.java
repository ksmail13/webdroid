package org.webdroid.util;

/**
 * Created by micky on 2015. 8. 1..
 */
public class ConsoleLogger {

    private final String TAG;

    public static ConsoleLogger createLogger(Class clazz) {
        return new ConsoleLogger(clazz.getSimpleName());
    }

    private ConsoleLogger(String tag) {
        TAG = tag;
    }

    private String logMessage(Object msg) {
        return String.format("[%s] %s", TAG, msg);
    }

    public void info(Object msg) {
        Log.logging(logMessage(msg));
    }

    public void info(Object msg, Throwable t) {
        Log.logging(logMessage(msg), t);
    }

    public void debug(Object msg) {
        Log.debugLogging(logMessage(msg));
    }

    public void debug(Object msg, Throwable t) {
        Log.debugLogging(logMessage(msg), t);
    }

    public void error(Object msg) {
        Log.errorLogging(logMessage(msg));
    }

    public void error(Object msg, Throwable t) {
        Log.errorLogging(logMessage(msg), t);
    }
}
