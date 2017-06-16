package Client.UI.CLI.cliUtils;

import java.io.PrintStream;

/**
 * Created by fiore on 10/05/2017.
 */
public class CliSout {

    private static PrintStream defaultOut = System.out;

    public static void log(LogLevel lvl, String message) {

        message = lvl.colorCode + lvl.name() + LogLevel.colorReset + " - " + message;
        defaultOut.println(message);

    }

    public enum LogLevel {
        Informazione("\u001b[32m"),
        Avvertimento("\u001b[33m"),
        Errore("\u001b[31m");

        public final static String colorReset = "\u001b[0m";
        public final String colorCode;

        LogLevel(String ansiColorCode) {
            colorCode = ansiColorCode;
        }
    }
}
