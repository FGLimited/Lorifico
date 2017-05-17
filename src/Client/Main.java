package Client;

import Client.UI.UserInterface;
import Client.UI.UserInterfaceFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by andrea on 10/05/2017.
 */
public class Main {
    public static void main(String[] args) {
        BufferedReader sysIn = new BufferedReader(new InputStreamReader(System.in));
        String cmdIn = "";

        System.out.println("Type the name of your preferred UI.");

        //Ask user about the UI
        do {
            System.out.println("You can choose between " + UserInterfaceFactory.UserInterfaceType.CLI.toString() + " or " + UserInterfaceFactory.UserInterfaceType.JAVAFX);

            try {
                cmdIn = sysIn.readLine();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
        while (!(cmdIn.toUpperCase().equals(UserInterfaceFactory.UserInterfaceType.CLI.toString()) || cmdIn.toUpperCase().equals(UserInterfaceFactory.UserInterfaceType.JAVAFX.toString())));

        //NB: We cannot close the BufferedReader, CLI will use it!

        //Get the desired UI and run it.

        //Create a new user interface, we don't need to save it because Factory will from now on return always the same obj
        UserInterface userInterface = UserInterfaceFactory.getInstance(UserInterfaceFactory.UserInterfaceType.valueOf(cmdIn.toUpperCase()));
        userInterface.init();
    }
}