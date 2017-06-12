package Client.UI.GUI.resources.gameComponents;

import Action.BaseAction;
import Action.SetInUseDomestic;
import Client.CommunicationManager;
import Client.Datawarehouse;
import Client.UI.GUI.UserInterfaceImplemJFX;
import Client.UI.UserInterfaceFactory;
import Game.Usable.ResourceType;
import Game.UserObjects.DomesticColor;
import Game.UserObjects.FamilyColor;
import Server.Game.UserObjects.Domestic;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXTextField;
import com.mysql.cj.core.util.StringUtils;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by andrea on 12/06/17.
 */
public class AddSlavedToDomesticDialog implements Initializable {
    private DomesticColor domesticColor;
    private Integer domesticValue;
    private JFXDialog jfxDialog;
    private JFXButton submitButton;


    @FXML
    private Label domesticValueLabel;

    @FXML
    private Label errorLabel;

    @FXML
    private JFXTextField slavesTextField;

    public AddSlavedToDomesticDialog(DomesticColor domesticColor, Integer domesticValue) {
        this.domesticColor = domesticColor;
        this.domesticValue = domesticValue;
        showDialog();
    }

    private void showDialog() {
        //Make sure we are on JavaFX thread:
        if (!Platform.isFxApplicationThread()) {
            Platform.runLater(() -> showDialog());
            return;
        }

        //Create new dialogContent
        JFXDialogLayout content = new JFXDialogLayout();

        //Create submit button
        submitButton = new JFXButton("Scegli Familiare");
        submitButton.setStyle("-fx-background-color: limegreen");

        //Try to load dialog content from .fxml
        Node dialogContent;
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Client/UI/GUI/fxml/DialogConent/AddSlavedToDomestic.fxml"));
            fxmlLoader.setController(this);
            dialogContent = fxmlLoader.load();
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        //Set contents to dialog
        content.setBody(dialogContent);
        content.setActions(submitButton);
        content.setHeading(new Label("Vuoi aggiungere dei servi?"));

        //Append dialog to root stackpane
        StackPane stackPane = ((UserInterfaceImplemJFX) UserInterfaceFactory.getInstance()).getRootStackPane();
        JFXDialog dialog = new JFXDialog(stackPane, content, JFXDialog.DialogTransition.CENTER, true);
        dialog.show();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        errorLabel.setText("");//Empty error label
        submitButton.setDisable(true);
        submitButton.setOnAction(action -> sendToServer(Integer.valueOf(slavesTextField.getText())));
        domesticValueLabel.setText(domesticValue.toString());

        final int maxSlaves;
        int maxSlaves1;
        try {
            maxSlaves1 = Datawarehouse.getInstance().getPlayerState(Datawarehouse.getInstance().getMyUsername())
                    .getResources().get(ResourceType.Slave);
        } catch (NullPointerException e) {
            maxSlaves1 = 0;
        }

        maxSlaves = maxSlaves1;
        slavesTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!StringUtils.isStrictlyNumeric(newValue)) {
                errorLabel.setText("Inserisci un numero");
                submitButton.setDisable(true);
            } else if (Integer.valueOf(newValue) > maxSlaves) {
                errorLabel.setText("Non hai questi schiavi");
                submitButton.setDisable(true);
            } else {
                errorLabel.setText("");
                submitButton.setDisable(false);
            }
        });
    }


    /**
     * Sends server selected
     *
     * @param slaves
     */
    private void sendToServer(int slaves) {
        FamilyColor myFamilyColor = Datawarehouse.getInstance().getFamilyColor(Datawarehouse.getInstance().getMyUsername());
        Domestic serverDomestic = new Domestic(myFamilyColor, domesticColor, domesticValue);

        BaseAction action = new SetInUseDomestic(serverDomestic, slaves);
        CommunicationManager.getInstance().sendMessage(action);

        jfxDialog.close();
    }
}
