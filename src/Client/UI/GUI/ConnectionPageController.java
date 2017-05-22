package Client.UI.GUI;

import Action.DisplayPopup;
import Client.UI.UserInterfaceFactory;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Io on 22/05/2017.
 */
public class ConnectionPageController implements Initializable {

    @FXML
    private StackPane root;

    @FXML
    private JFXTextField ipField;

    @FXML
    private JFXTextField portField;

    @FXML
    private ChoiceBox<?> choiceBox;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ((UserInterfaceImplemJFX) UserInterfaceFactory.getInstance()).setStackPane(root);//Updates reference to root stack pane in UserInterface, this way popus will be displayed in this page.
    }

    @FXML
    void connect(ActionEvent event) {
        UserInterfaceFactory.getInstance().displayPopup(DisplayPopup.Level.Normal, "Ciaone", "hello world");
    }

    @FXML
    void loginButtonPressed(ActionEvent event) {

    }

}
