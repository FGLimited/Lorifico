package Client.UI.CLI;

import Action.BaseAction;
import Action.LoginOrRegister;
import Client.CommunicationManager;
import Client.UI.UserInterfaceFactory;
import com.budhash.cliche.Command;
import com.budhash.cliche.Param;

/**
 * Created by andrea on 16/06/17.
 */
public class LoginPage implements Client.UI.Login {
    @Override
    public void showPage() {
        ((UserInterfaceImplemCLI) (UserInterfaceFactory.getInstance())).setCliPage(this, true);
    }

    @Command(description = "Login con utente esistente")
    public void login(
            @Param(name = "username", description = "Username")
                    String username,
            @Param(name = "password", description = "Password")
                    String password) {

        BaseAction baseAction = new LoginOrRegister(username, password, false);
        CommunicationManager.getInstance().sendMessage(baseAction);
    }

    @Command(description = "Registrazione nuovo utente")
    public void register(
            @Param(name = "username", description = "Username")
                    String username,
            @Param(name = "password", description = "Password")
                    String password) {

        BaseAction baseAction = new LoginOrRegister(username, password, true);
        CommunicationManager.getInstance().sendMessage(baseAction);
    }
}
