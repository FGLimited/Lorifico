package Client.UI.GUI;

import Action.DisplayPopup;
import Client.CommunicationManager;
import Client.Networking.CommFactory;
import Client.UI.UserInterfaceFactory;
import com.jfoenix.controls.JFXTextField;
import com.mysql.cj.core.util.StringUtils;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.StackPane;

import java.io.IOException;
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
    private ChoiceBox<CommFactory.LinkType> choiceBox;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ((UserInterfaceImplemJFX) UserInterfaceFactory.getInstance()).setStackPane(root);//Updates reference to root stack pane in UserInterface, this way popus will be displayed in this page.
        populateChoiceBox();
    }

    /**
     * Called when button is pressed
     *
     * @param event
     */
    @FXML
    void connect(ActionEvent event) {
        if (isFormValid()) {
            try {
                CommunicationManager.getInstance(choiceBox.getValue(), ipField.getText(), Integer.valueOf(portField.getText()));

                //If everything was ok we can ask user to login (following line is not throwing exceptions)
                UserInterfaceFactory.getInstance().getLogin().showPage();
            } catch (IOException | NumberFormatException e) {
                UserInterfaceFactory.getInstance().displayPopup(DisplayPopup.Level.Error, "Qualcosa non funziona", e.getMessage());
            }
        }
    }

    /**
     * Populates checkbox with link's types
     */
    private void populateChoiceBox() {
        choiceBox.setItems(FXCollections.observableArrayList(CommFactory.LinkType.RMI, CommFactory.LinkType.SOCKET));
        choiceBox.setTooltip(new Tooltip("Scegli il metodo di connessione verso il server"));
    }

    /**
     * Validates input
     */
    private boolean isFormValid() {
        if (choiceBox.getValue() == null) {
            UserInterfaceFactory.getInstance().displayPopup(DisplayPopup.Level.Normal, "Attenzione", "Scegli il tipo di connessione da utilizzare");
            return false;
        }
        if (ipField.getText().isEmpty() || !StringUtils.isStrictlyNumeric(portField.getText())) {
            UserInterfaceFactory.getInstance().displayPopup(DisplayPopup.Level.Normal, "Attenzione", "Scegli un server valido");
            return false;
        }
        return true;
    }

}
