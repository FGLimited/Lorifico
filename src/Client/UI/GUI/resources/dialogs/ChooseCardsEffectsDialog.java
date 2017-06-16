package Client.UI.GUI.resources.dialogs;

import Action.BaseAction;
import Action.Move;
import Client.CommunicationManager;
import Client.UI.GUI.UserInterfaceImplemJFX;
import Client.UI.GUI.resources.gameComponents.GameCard;
import Client.UI.UserInterfaceFactory;
import Game.Effects.Effect;
import Game.UserObjects.Choosable;
import Logging.Logger;
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
import java.util.*;

/**
 * Created by andrea on 15/06/17.
 */
public class ChooseCardsEffectsDialog {
    private List<Choosable> choosenEffectsList = new ArrayList();
    private int position;//Position that user is trying to occupy

    public ChooseCardsEffectsDialog(int position, List<Choosable> choosableEffectsList) {
        this.position = position;

        //Creates a new Map containing cardNumber in key, activable effects in a List in value
        Map<Integer, List<Choosable>> cardChoosableMap = new HashMap<>();
        choosableEffectsList.forEach(choosable -> {
            if (choosable instanceof Effect) {
                Effect effect = (Effect) choosable;
                cardChoosableMap.putIfAbsent(effect.getCardNumber(), new ArrayList<Choosable>());
                cardChoosableMap.get(effect.getCardNumber()).add(effect);
            }
        });

        Iterator<Map.Entry<Integer, List<Choosable>>> iterator = cardChoosableMap.entrySet().iterator();

        if (iterator.hasNext()) {
            askWhichEffectActivate(iterator);
        } else {
            Logger.log(Logger.LogLevel.Error, "Nothing to show in ChooseCardsEffectsDialog");
        }
    }

    /**
     * Asks user which effects activate per card
     *
     * @param iterator iterator of map containing cards (keys) and effects (values)
     */
    private void askWhichEffectActivate(Iterator<Map.Entry<Integer, List<Choosable>>> iterator) {
        //Make sure we are on JavaFX thread:
        if (!Platform.isFxApplicationThread()) {
            Platform.runLater(() -> askWhichEffectActivate(iterator));
            return;
        }

        //Retrieve data we have to show in dialog
        Map.Entry<Integer, List<Choosable>> entry = iterator.next();

        //Key 0 are defaults (and always activated effect), so let's activate them and skip
        if (entry.getKey() == 0) {
            entry.getValue().forEach(choosable -> choosenEffectsList.add(choosable));
            if (iterator.hasNext()) {
                entry = iterator.next();
            } else {
                sendDataToServer();
                return;
            }
        }

        //Create new dialog layout obj
        JFXDialogLayout content = new JFXDialogLayout();

        //Create new dialog in root stackpane
        StackPane stackPane = ((UserInterfaceImplemJFX) UserInterfaceFactory.getInstance()).getRootStackPane();
        JFXDialog jfxDialog = new JFXDialog(stackPane, content, JFXDialog.DialogTransition.CENTER, true);

        //Load dialog content from .fxml and pushes data to it via controller
        Node dialogContent;
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Client/UI/GUI/fxml/DialogConent/ChooseCardsEffects.fxml"));
            fxmlLoader.setController(new AskWhichEffectActivateController(entry.getKey(), entry.getValue(), jfxDialog));
            dialogContent = fxmlLoader.load();
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        //Set loaded content to dialog
        content.setBody(dialogContent);
        content.setHeading(new Label("Scegli che effetti giocare"));
        jfxDialog.show();

        //Set what to do when dialog is closed
        if (iterator.hasNext()) {
            jfxDialog.setOnDialogClosed(event -> askWhichEffectActivate(iterator));//Open another dialog with next card.
        } else {
            jfxDialog.setOnDialogClosed(event -> sendDataToServer());
        }
    }

    /**
     * Asks server to occupy position with selected effects.
     */
    private void sendDataToServer() {
        BaseAction action = new Move(position, choosenEffectsList);
        CommunicationManager.getInstance().sendMessage(action);
    }


    private class AskWhichEffectActivateController implements Initializable {
        private int cardNumber;
        private List<Choosable> activableCardEffects;
        private JFXDialog jfxDialog;

        @FXML
        private ImageView cardImageView;

        @FXML
        private VBox buttonsVBox;

        public AskWhichEffectActivateController(Integer cardNumber, List<Choosable> activableCardEffects, JFXDialog jfxDialog) {
            this.cardNumber = cardNumber;
            this.activableCardEffects = activableCardEffects;
            this.jfxDialog = jfxDialog;
        }

        @Override
        public void initialize(URL location, ResourceBundle resources) {
            cardImageView.setImage(GameCard.getCard(cardNumber).getImage());

            activableCardEffects.forEach(choosable -> {
                if (choosable instanceof Effect) {//This should (hopefully) always be true
                    Effect effect = (Effect) choosable;

                    JFXButton jfxButton = new JFXButton(effect.getDescription());
                    jfxButton.setStyle("-fx-background-color: limegreen");
                    jfxButton.setPadding(new Insets(0, 0, 20, 0));

                    //When an effect is selected (by clicking on his button), dialog is closed and effect added to choosen list.
                    jfxButton.setOnAction(event -> {
                        choosenEffectsList.add(effect);
                        jfxDialog.close();
                    });

                    buttonsVBox.getChildren().add(jfxButton);
                }
            });

            //Add button to chose 'no effect from this card'
            JFXButton jfxButton = new JFXButton("Nessuna azione di questa carta");
            jfxButton.setStyle("-fx-background-color: red");
            jfxButton.setOnAction(event -> jfxDialog.close());
            buttonsVBox.getChildren().add(jfxButton);
        }
    }
}
