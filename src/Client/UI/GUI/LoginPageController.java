package Client.UI.GUI;

import Action.BaseAction;
import Action.DisplayPopup;
import Action.LoginOrRegister;
import Client.CommunicationManager;
import Client.UI.UserInterfaceFactory;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by andrea on 22/05/17.
 */
public class LoginPageController implements Client.UI.Login, Initializable {

    @FXML
    private StackPane root;

    @FXML
    private JFXTextField usernameField;

    @FXML
    private JFXPasswordField passwordField;

    @FXML
    private CheckBox checkBox;

    @FXML
    private JFXButton submitButton;

    /**
     * This method changes current shown page to LoginPage's
     */
    @Override
    public void showLoginPage() {
        ((UserInterfaceImplemJFX) (UserInterfaceFactory.getInstance())).changeScene("Login", "fxml/LoginPage.fxml", 415, 415, true);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ((UserInterfaceImplemJFX) UserInterfaceFactory.getInstance()).setStackPane(root);//Updates reference to root stack pane in UserInterface, this way popus will be displayed in this page.
        checkBox.selectedProperty().addListener((observable, oldValue, newValue) ->
                submitButton.setText((newValue ? "Registrati" : "Login")));
    }

    @FXML
    void onSubmitButton(ActionEvent event) {
        if (isFormValid()) {
            BaseAction baseAction = new LoginOrRegister(usernameField.getText(), passwordField.getText(), checkBox.isSelected());
            CommunicationManager.getInstance().sendMessage(baseAction);
        }
    }

    private boolean isFormValid() {
        if (usernameField.getLength() == 0 || passwordField.getLength() == 0) {
            UserInterfaceFactory.getInstance().displayPopup(DisplayPopup.Level.Warning, "Attenzione", "Compila username e password");
            return false;
        }
        return true;
    }
}