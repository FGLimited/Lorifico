package Logging;

import java.io.PrintStream;

/**
 * Created by fiore on 10/05/2017.
 */
public class Logger {
    private static LogLevel logLevel = LogLevel.Normal;

    private static PrintStream defaultOut = System.out;

    public static void log(LogLevel lvl, String message) {
        if (lvl.ordinal() < logLevel.ordinal()) return;

        message = lvl.colorCode + lvl.name() + LogLevel.colorReset + " - " + message;
        defaultOut.println(message);
    }

    public static void setLogLevel(LogLevel logLevel) {
        Logger.logLevel = logLevel;
    }

    public enum LogLevel {
        Normal ("\u001b[32m"),
        Warning ("\u001b[33m"),
        Error ("\u001b[31m"),
        None("");

        public final static String colorReset = "\u001b[0m";
        public final String colorCode;

        LogLevel(String ansiColorCode) {
            colorCode = ansiColorCode;
        }
    }
}
