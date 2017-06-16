package Client.UI.CLI;

import Action.DisplayPopup;
import Client.UI.CLI.cliUtils.CliController;
import Client.UI.CLI.cliUtils.CliSout;
import Client.UI.CLI.gameComponents.GameTableCliController;
import Client.UI.*;

/**
 * Created by andrea on 10/05/2017.
 */
public class UserInterfaceImplemCLI implements UserInterface {
    private Login login;
    private Lobby lobby;
    private CliController cliController;
    private GameUI gameUI;
    private GameTable gameTable;


    /**
     * Creates main shell and asks data to connect to server
     */
    @Override
    public void init(String args[]) {
        ConnectionPage connectionPage = new ConnectionPage();
        cliController = new CliController(connectionPage, true);
    }

    /**
     * Changes current CLI 'page'
     *
     * @param cliPage          page controller we are switching to.
     * @param printHelpMessage
     */
    public void setCliPage(Object cliPage, boolean printHelpMessage) {
        cliController.setCliPage(cliPage, printHelpMessage);
    }

    @Override
    public void displayPopup(DisplayPopup.Level level, String title, String message) {
        CliSout.log(CliSout.LogLevel.values()[level.ordinal()], title +"\n\t"+message);
    }

    @Override
    public Login getLogin() {
        if (login == null) login = new LoginPage();
        return login;
    }

    @Override
    public Lobby getLobby() {
        if (lobby == null) lobby = new LobbyPage();
        return lobby;
    }

    @Override
    public GameUI getGameUI() {
        if (gameUI == null) gameUI = new GameUIPage();
        return gameUI;
    }

    @Override
    public GameTable getGameTable() {
        if (gameTable == null) gameTable = new GameTableCliController();
        return gameTable;
    }

}
