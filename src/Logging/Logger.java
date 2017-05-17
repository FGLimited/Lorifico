package Logging;

import java.io.PrintStream;

/**
 * Created by fiore on 10/05/2017.
 */
public class Logger {

    private static PrintStream defaultOut = System.out;

    public static void log(LogLevel lvl, String message) {

        message = lvl.colorCode + lvl.name() + LogLevel.colorReset + " - " + message;
        defaultOut.println(message);

    }

    public enum LogLevel {
        Normal ("\u001b[32m"),
        Warning ("\u001b[33m"),
        Error ("\u001b[31m");

        public final String colorCode;

        public final static String colorReset = "\u001b[0m";

        LogLevel(String ansiColorCode) {
            colorCode = ansiColorCode;
        }
    }
}
