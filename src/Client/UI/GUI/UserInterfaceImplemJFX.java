package Client.UI.GUI;

import Client.UI.Dashboard;
import Client.UI.GameTable;
import Client.UI.Login;
import Client.UI.UserInterface;

/**
 * Created by Io on 09/05/2017.
 */
public class UserInterfaceImplemJFX implements UserInterface {
    @Override
    public void init() {
        System.out.println("Not implem.");
    }

    @Override
    public void displayPopup(String message) {

    }

    @Override
    public Login getLogin() {
        return null;
    }

    @Override
    public Dashboard getDashboard() {
        return null;
    }

    @Override
    public GameTable getGameTable() {
        return null;
    }
}
