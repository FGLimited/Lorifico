package Client.UI.GUI;

import Client.UI.Dashboard;
import Client.UI.GameTable;
import Client.UI.Login;
import Client.UI.UserInterface;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Created by Io on 09/05/2017.
 */
public class UserInterfaceImplemJFX extends Application implements UserInterface {
    private Stage primaryStage;

    /**
     * Used to init the stage
     * @param args
     */
    @Override
    public void init(String args[]) {
        launch(args);
    }

    /**
     * Starts GUI and asks user to provide ip/port/method in order to connect to the server.
     * @param primaryStage
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
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

    public Stage getPrimaryStage() {
        return primaryStage;
    }
}
