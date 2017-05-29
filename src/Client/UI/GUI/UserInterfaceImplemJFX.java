package Client.UI.GUI;

import Action.DisplayPopup;
import Client.UI.*;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * Created by Io on 09/05/2017.
 */
public class UserInterfaceImplemJFX extends Application implements UserInterface {
    private Stage primaryStage;//Stage where GUI is shown
    private StackPane stackPane;//Every page is inside a stackPane,
    //every page controller should update this reference when loaded.

    private Login login;//Login page controller
    private Lobby lobby;//Lobby page controller


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
        ((UserInterfaceImplemJFX) UserInterfaceFactory.getInstance()).setPrimaryStage(primaryStage);///// N.B: JavaFX creates a NEW UserInterfaceImplemJFX obj, we have to reach the original one
        ((UserInterfaceImplemJFX) UserInterfaceFactory.getInstance()).changeScene("Scegli il server", "fxml/ConnectionPage.fxml", 300, 400, true, new ConnectionPageController());
    }

    @Override
    public void displayPopup(DisplayPopup.Level level, String title, String message) {

        if (!Platform.isFxApplicationThread()) {
            Platform.runLater(() -> displayPopup(level, title, message));
            return;
        }

        JFXButton button = new JFXButton("Perfetto");
        button.setStyle("-fx-background-color: limegreen");
        JFXDialogLayout content = new JFXDialogLayout();
        content.setHeading(new Label(title));
        content.setBody(new Label(message));
        content.setActions(button);
        JFXDialog dialog = new JFXDialog(getStackPane(), content, JFXDialog.DialogTransition.CENTER, false);
        dialog.show();
        button.setOnAction(action -> dialog.close());
    }


    @Override
    public Login getLogin() {
        if (login == null) login = new LoginPageController();
        return login;
    }

    @Override
    public ChooseAvatar getChooseAvatar() {
        return null;
    }

    @Override
    public Lobby getLobby() {
        if (lobby == null) lobby = new LobbyPageController();
        return lobby;
    }

    @Override
    public Dashboard getDashboard() {
        return null;
    }

    @Override
    public GameTable getGameTable() {
        return null;
    }

    /**
     * Changes scene
     *
     * @param title Title of windows
     * @param fxml  URI to FXML file
     * @param w     New page width
     * @param h     New page height
     */
    protected void changeScene(String title, String fxml, int w, int h, boolean resizable, Object controller) {
        if (!Platform.isFxApplicationThread()) {
            Platform.runLater(() -> changeScene(title, fxml, w, h, resizable, controller));
            return;
        }
        try {
            //Stage primaryStage = ((UserInterfaceImplemJFX) UserInterfaceFactory.getInstance()).getPrimaryStage();
            primaryStage.setTitle(title);
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxml));
            fxmlLoader.setController(controller);
            Parent root = fxmlLoader.load();
            primaryStage.setScene(new Scene(root, w, h));
            primaryStage.setResizable(resizable);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Stage getPrimaryStage() {
        return primaryStage;
    }

    private void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    private StackPane getStackPane() {
        return stackPane;
    }

    protected void setStackPane(StackPane stackPane) {
        this.stackPane = stackPane;
    }
}
