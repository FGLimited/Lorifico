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
import java.util.function.Consumer;

/**
 * Created by andrea on 12/06/17.
 */
public class AddSlaveToDomesticDialog implements Initializable {
    private DomesticColor domesticColor;
    private Integer domesticValue;
    private JFXDialog jfxDialog;
    private JFXButton submitButton;
    private DomesticBoxController domesticBoxController;

    //This stores special SetInUseDomestic we have to use if this is a Special Domestic.
    private SetInUseDomestic setInUseDomesticAction;


    @FXML
    private Label domesticValueLabel;

    @FXML
    private Label errorLabel;

    @FXML
    private JFXTextField slavesTextField;

    /**
     * Used to add slaves to REGULAR domestic
     *
     * @param domesticColor
     * @param domesticValue
     * @param domesticBoxController box to disable after we complete action
     */
    public AddSlaveToDomesticDialog(DomesticColor domesticColor, Integer domesticValue, DomesticBoxController domesticBoxController) {
        this.domesticColor = domesticColor;
        this.domesticValue = domesticValue;
        this.domesticBoxController = domesticBoxController;
        showDialog(this::sendRegularDomesticToServer);
    }

    /**
     * Used to add slaves to a SPECIAL domestic
     *
     * @param domestic               special domestic
     * @param setInUseDomesticAction action we have to send back to server
     */
    public AddSlaveToDomesticDialog(Domestic domestic, SetInUseDomestic setInUseDomesticAction) {
        this.domesticColor = domestic.getType();
        this.domesticValue = domestic.getValue();
        this.setInUseDomesticAction = setInUseDomesticAction;
        showDialog(this::sendBonusDomesticToServer);
    }

    /**
     * Show dialog with specified callback when green button is pressed
     *
     * @param callback callback
     */
    private void showDialog(Consumer<Integer> callback) {
        //Make sure we are on JavaFX thread:
        if (!Platform.isFxApplicationThread()) {
            Platform.runLater(() -> showDialog(callback));
            return;
        }

        //Create new dialogContent
        JFXDialogLayout content = new JFXDialogLayout();

        //Create submit button
        submitButton = new JFXButton("Scegli Familiare");
        submitButton.setStyle("-fx-background-color: limegreen");
        submitButton.setOnAction(action -> callback.accept(Integer.valueOf(slavesTextField.getText())));

        //Try to load dialog content from .fxml
        Node dialogContent;
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Client/UI/GUI/fxml/DialogConent/AddSlaveToDomestic.fxml"));
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
        jfxDialog = new JFXDialog(stackPane, content, JFXDialog.DialogTransition.CENTER, true);
        jfxDialog.show();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        errorLabel.setText("");//Empty error label
        submitButton.setDisable(true);
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
                errorLabel.setText("Hai solo " + maxSlaves + " schiavi");
                submitButton.setDisable(true);
            } else if (Integer.valueOf(newValue) + domesticValue == 0) {
                errorLabel.setText("Deve valere almeno 1");
                submitButton.setDisable(true);
            } else {
                errorLabel.setText("");
                submitButton.setDisable(false);
            }
        });
    }


    /**
     * Sends server selected slaves
     *
     * @param slaves
     */
    private void sendRegularDomesticToServer(int slaves) {
        FamilyColor myFamilyColor = Datawarehouse.getInstance().getFamilyColor(Datawarehouse.getInstance().getMyUsername());
        Domestic serverDomestic = new Domestic(myFamilyColor, domesticColor, domesticValue);

        BaseAction action = new SetInUseDomestic(serverDomestic, slaves);
        CommunicationManager.getInstance().sendMessage(action);

        jfxDialog.close();
        domesticBoxController.disableDomestic(domesticColor);//Disable played domestic
        domesticBoxController.disableDomesticBox();//Disable domestic box (we cannot play another domestic now)
    }

    /**
     * Sends server selected slaves for special domestic
     *
     * @param slaves
     */
    private void sendBonusDomesticToServer(int slaves) {
        setInUseDomesticAction.setSlaves(slaves);
        CommunicationManager.getInstance().sendMessage(setInUseDomesticAction);

        jfxDialog.close();
    }
}
