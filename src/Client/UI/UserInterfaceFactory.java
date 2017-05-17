package Client.UI;

import Client.UI.CLI.UserInterfaceImplemCLI;
import Client.UI.GUI.UserInterfaceImplemJFX;

/**
 * Created by andrea on 10/05/2017.
 */

//This implements both Factory and Singleton pattern
public class UserInterfaceFactory {
    private static UserInterface userInterface = null;//Stored single ui object
    //Creates a new UserInterface obj and stores it
    private UserInterfaceFactory(UserInterfaceType userInterfaceType){
        if (userInterfaceType == UserInterfaceType.CLI){
            userInterface = new UserInterfaceImplemCLI();
        }else if (userInterfaceType == UserInterfaceType.JAVAFX){
            userInterface = new UserInterfaceImplemJFX();
        }
    }

    /**
     * Retrieves or creates the only instance of UserInterface
     * @param userInterfaceType
     * @return instance of UI
     */
    public static UserInterface getInstance(UserInterfaceType userInterfaceType){
        if (userInterface == null) new UserInterfaceFactory(userInterfaceType);
        return userInterface;
    }

    /**
     * Retrieves the only instance of UserInterface
     * @return instance of UI
     */
    public static UserInterface getInstance(){
        return userInterface;
    }

    public enum UserInterfaceType {
        CLI,
        JAVAFX
    }
}
