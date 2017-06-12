package Client.UI.GUI.resources.gameComponents;

import Client.UI.GUI.UserInterfaceImplemJFX;
import Client.UI.UserInterfaceFactory;
import Game.UserObjects.Choosable;
import Server.Game.Usable.Cost;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 * Created by andrea on 12/06/17.
 */
public class ChooseCardCostDialog implements Initializable {
    private Map<Integer, List<Choosable>> choosablePerPos;
    private Integer positionNumber, cardNumber;

    @FXML
    private ImageView cardImageView;

    @FXML
    private VBox buttonsVBox;

    public ChooseCardCostDialog(Map<Integer, List<Choosable>> choosablePerPos, int positionNumber, int cardNumber) {
        this.choosablePerPos = choosablePerPos;
        this.positionNumber = positionNumber;
        this.cardNumber = cardNumber;
        showDialog();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //Place buy buttons
        List<Choosable> choosableList = choosablePerPos.get(positionNumber);

        //Set card's image
        cardImageView.setImage(GameCard.getCard(cardNumber).getImage());

        choosableList.forEach(choosable -> {
            if (choosable instanceof Cost) {
                Cost cost = (Cost) choosable;//Cast object to his original type... they are all COSTS!

                String stringOfCosts = "";

                if (cost.getRequestedPoints() > 0) {
                    stringOfCosts = cost.getRequestedPoints() + " Punti Militare";
                }
                stringOfCosts = stringOfCosts + " " + cost.getResources().entrySet().stream().
                        map(mapLine -> mapLine.getKey().toCostString(mapLine.getValue())).collect(Collectors.joining(" "));

                //Creates new button labeled with just created 'stringOfCosts'
                JFXButton jfxButton = new JFXButton(stringOfCosts);
                jfxButton.setStyle("-fx-background-color: limegreen");
                jfxButton.setPrefWidth(258);
                jfxButton.setPrefHeight(27);
                jfxButton.setPadding(new Insets(0, 0, 20, 0));

                //Appends button to scene
                buttonsVBox.getChildren().add(jfxButton);
            }
        });

    }

    private void showDialog() {
        //Make sure we are on JavaFX thread:
        if (!Platform.isFxApplicationThread()) {
            Platform.runLater(() -> showDialog());
            return;
        }

        //Create new dialogContent
        JFXDialogLayout content = new JFXDialogLayout();

        //Try to load dialog content from .fxml
        Node dialogContent;
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Client/UI/GUI/fxml/DialogConent/ChooseCardCost.fxml"));
            fxmlLoader.setController(this);
            dialogContent = fxmlLoader.load();
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        //Set contents to dialog
        content.setBody(dialogContent);
        content.setHeading(new Label("Posiziona il tuo familiare"));

        //Append dialog to root stackpane
        StackPane stackPane = ((UserInterfaceImplemJFX) UserInterfaceFactory.getInstance()).getRootStackPane();
        JFXDialog dialog = new JFXDialog(stackPane, content, JFXDialog.DialogTransition.CENTER, true);
        dialog.show();
    }
}
