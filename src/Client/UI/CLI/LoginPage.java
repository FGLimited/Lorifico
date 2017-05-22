package Client.UI.CLI;

import Action.BaseAction;
import Action.LoginOrRegister;
import Client.CommunicationManager;
import Client.UI.Login;
import Client.UI.UserInterfaceFactory;
import com.budhash.cliche.*;

import java.io.IOException;

/**
 * Created by andrea on 11/05/17.
 */
public class LoginPage implements Login, ShellDependent {


    /**
     * This creates a new subshell and shows login page.
     */
    @Override
    public void showLoginPage() {
        Shell shell = ((UserInterfaceImplemCLI) UserInterfaceFactory.getInstance()).getShell();//Gets reference to main shell
        try {
            ShellFactory.createSubshell("Login", shell, "Effettua il login o registrati (?list per vedere i comandi)", this)
                    .commandLoop();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Command(description = "Login al gioco")
    public String login(
            @Param(name = "username", description = "Username")
                    String username,
            @Param(name = "password", description = "Password")
                    String password) {

        BaseAction baseAction = new LoginOrRegister(username, password, false);
        if (CommunicationManager.getInstance() == null) {
            return "Devi prima scegliere se usare RMI o SOCKET";
        }
        CommunicationManager.getInstance().sendMessage(baseAction);
        return "";
    }

    @Command(description = "Registrazione")
    public String register(
            @Param(name = "username", description = "Username")
                    String username,
            @Param(name = "password", description = "Password")
                    String password) {

        BaseAction baseAction = new LoginOrRegister(username, password, true);
        if (CommunicationManager.getInstance() == null) {
            return "Devi prima scegliere se usare RMI o SOCKET";
        }
        CommunicationManager.getInstance().sendMessage(baseAction);
        return "";
    }

    /**
     * Internally used by cliche to keep references
     *
     * @param theShell
     */
    @Override
    public void cliSetShell(Shell theShell) {
        ((UserInterfaceImplemCLI) UserInterfaceFactory.getInstance()).cliSetShell(theShell);
    }
}
