package Client.UI.CLI.cliUtils;

import Server.Game.Usable.Cost;

import java.util.stream.Collectors;

/**
 * Created by andrea on 16/06/17.
 */
public class TextUtils {
    public static String pad(String s, int lenght) {
        if (s.length() >= lenght) return s;
        int difference = lenght - s.length();
        String out = s;
        for (int i = 0; i < difference; i++) {
            out = out + " ";
        }
        return out;
    }

    public static void printCost(Cost cost) {
        String resourcesCost = "";
        if (cost.getRequestedPoints() > 0) {
            resourcesCost = cost.getRequestedPoints() + " punti militari, ";
        }

        if (cost.getResources().size() == 0) {
            resourcesCost = "Nessun costo";
        } else {
            resourcesCost = resourcesCost + cost.getResources().entrySet().stream().map(resourceTypeIntegerEntry -> resourceTypeIntegerEntry.getKey().toCostString(resourceTypeIntegerEntry.getValue())).collect(Collectors.joining(", "));
        }
        System.out.println("\t" + resourcesCost);
    }
}
