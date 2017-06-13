package Client.UI.CLI;

import Action.DisplayPopup;
import Client.UI.*;
import com.budhash.cliche.Shell;
import com.budhash.cliche.ShellFactory;

import java.io.IOException;

/**
 * Created by andrea on 10/05/2017.
 */
public class UserInterfaceImplemCLI implements UserInterface {
    private Login login;
    private Shell shell;


    /**
     * Creates main shell and asks data to connect to server
     */
    @Override
    public void init(String args[]) {
        try {
            ShellFactory.createConsoleShell("Lorenzo", "Scrivi ?list per conoscere i comandi", new ConnectionPageController())
                    .commandLoop();
        } catch (IOException e) {
            System.err.println("Unable to start CLI Interface");
            e.printStackTrace();
        }
    }

    @Override
    public void displayPopup(DisplayPopup.Level level, String title, String message) {
        System.out.println(message);
    }

    @Override
    public Login getLogin() {
        if (login == null) login = new LoginPage();
        return login;
    }

    @Override
    public ChooseAvatar getChooseAvatar() {
        return null;
    }

    @Override
    public Lobby getLobby() {
        return null;
    }

    @Override
    public GameUI getGameUI() {
        return null;
    }

    @Override
    public GameTable getGameTable() {
        return null;
    }

    /**
     * Called by cliche which is poorly documented.
     *
     * @param theShell
     */
    public void cliSetShell(Shell theShell) {
        this.shell = theShell;
    }

    public Shell getShell() {
        return shell;
    }
}
