package Client.UI.GUI.resources.dialogs;

import Action.BaseAction;
import Action.FaithRoadChoice;
import Client.CommunicationManager;
import Client.UI.GUI.UserInterfaceImplemJFX;
import Client.UI.UserInterfaceFactory;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;

/**
 * Created by andrea on 16/06/17.
 */
public class FaithRoadChoiceDialog {

    /**
     * Creates a new dialog asking user if he wants to support
     */
    public FaithRoadChoiceDialog() {
        show();
    }

    private void show() {
        //Make sure we are on JavaFX thread:
        if (!Platform.isFxApplicationThread()) {
            Platform.runLater(() -> show());
            return;
        }

        //Create new dialogContent
        JFXDialogLayout content = new JFXDialogLayout();

        //Append dialog to root stackpane
        StackPane stackPane = ((UserInterfaceImplemJFX) UserInterfaceFactory.getInstance()).getRootStackPane();
        JFXDialog dialog = new JFXDialog(stackPane, content, JFXDialog.DialogTransition.CENTER, true);

        //Create dialog content
        Label label = new Label("Vuoi supportare la Chiesa?");
        label.setFont(Font.font(20));

        //Creates YES button
        JFXButton yesButton = new JFXButton("Supporta");
        yesButton.setStyle("-fx-background-color: lime");
        yesButton.setPadding(new Insets(0, 20, 0, 20));
        yesButton.setOnAction(event ->
        {
            sendToServer(true);
            dialog.setOnDialogClosed(event1 -> {
            });
            dialog.close();
        });

        //Create NO button
        JFXButton noButton = new JFXButton("Non questa volta");
        noButton.setStyle("-fx-background-color: red");
        noButton.setPadding(new Insets(0, 20, 0, 20));
        noButton.setOnAction(event ->
        {
            sendToServer(false);
            dialog.setOnDialogClosed(event1 -> {
            });
            dialog.close();
        });

        //Set contents to dialog
        content.setBody(label);
        content.setActions(noButton, yesButton);
        content.setHeading(new Label("E' il momento di decidere..."));


        dialog.show();
        dialog.setOnDialogClosed(event -> show());//If user dismisses dialog... reopen it :P

    }

    private void sendToServer(boolean isSupporting) {
        BaseAction baseAction = new FaithRoadChoice(isSupporting);
        CommunicationManager.getInstance().sendMessage(baseAction);
    }
}
