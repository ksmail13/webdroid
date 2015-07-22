package org.webdroid.util;

import java.io.PrintStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by micky on 2015. 7. 17..
 */
public class Log {
    public enum LogType{FILE_NAME, TIME}
    /**
     * choose log type
     */
    public static LogType LOG_TYPE = LogType.TIME;

    public static void logging(String message) {
        logging(message, System.out, null);
    }

    public static void errorLogging(String message, Throwable t) {
        logging(message, System.err, t);
    }

    public static void logging(String message, Throwable e) {
        logging(message, System.out, e);
    }

    public static void logging(String message, PrintStream target,  Throwable t) {

        if(LOG_TYPE == LogType.TIME) {
            // if you want time log then use this format
            DateFormat df = new SimpleDateFormat("MM / dd a hh:mm:ss");
            target.printf("[%s] %s", df.format(Calendar.getInstance().getTime()), message == null ? "" : message);
        }
        else if(LOG_TYPE == LogType.FILE_NAME){
            Thread th = Thread.currentThread();
            StackTraceElement[] ste = th.getStackTrace();
            StackTraceElement callMethod = ste[ste.length - 2];
            target.printf("[%s(%d)] %s", callMethod.getFileName(), callMethod.getLineNumber(), message);
        }
        if(t != null) {
            target.println(t.getLocalizedMessage());
            //target.print(t.getStackTrace()[0].toString());
            t.printStackTrace();
        }
        target.println();
    }

    public static void debugLogging(String message) {
        logging(message);
    }


}

