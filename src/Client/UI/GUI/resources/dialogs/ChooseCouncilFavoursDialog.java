package Client.UI.GUI.resources.dialogs;

import Action.BaseAction;
import Action.UseFavor;
import Client.CommunicationManager;
import Client.UI.GUI.UserInterfaceImplemJFX;
import Client.UI.UserInterfaceFactory;
import Game.Effects.Effect;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.util.*;

/**
 * Created by andrea on 15/06/17.
 */
public class ChooseCouncilFavoursDialog implements Initializable {
    private int howManyFavours;
    private JFXButton submitButton;
    private List<Effect> choosableCouncilFavors;
    private List<Effect> chosenCouncilFavors = new ArrayList<>();

    @FXML
    private Label favoursNumberLabel;

    @FXML
    private ImageView favour0;

    @FXML
    private ImageView favour1;

    @FXML
    private ImageView favour2;

    @FXML
    private ImageView favour3;

    @FXML
    private ImageView favour4;


    /**
     * Creates a dialog asking user to select different council favours
     *
     * @param choosableCouncilFavors list of choosable favours
     * @param howManyFavoursToChoose how many different favours user has to choose.
     */
    public ChooseCouncilFavoursDialog(List<Effect> choosableCouncilFavors, int howManyFavoursToChoose) {
        this.howManyFavours = howManyFavoursToChoose;
        this.choosableCouncilFavors = choosableCouncilFavors;
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

        //Try to load dialog content from .fxml
        Node dialogContent;
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Client/UI/GUI/fxml/DialogConent/ChooseCouncilFavoursDialog.fxml"));
            fxmlLoader.setController(this);
            dialogContent = fxmlLoader.load();
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        //Create submit button (starts disabled)
        submitButton = new JFXButton("Okay");
        submitButton.setStyle("-fx-background-color: lime");
        submitButton.setDisable(true);
        submitButton.setOnAction(event -> {
            dialog.setOnDialogClosed(event1 -> {
            });//Remove callback
            sendToServer();
            dialog.close();
        });

        //Set contents to dialog
        content.setBody(dialogContent);
        content.setActions(submitButton);
        content.setHeading(new Label("Scegli i favori del consiglio"));


        dialog.show();
        dialog.setOnDialogClosed(event -> show());//If user dismisses dialog... reopen it :P

    }

    private void sendToServer() {
        BaseAction baseAction = new UseFavor(chosenCouncilFavors);
        CommunicationManager.getInstance().sendMessage(baseAction);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        favoursNumberLabel.setText(howManyFavours + "");//Update how many favours user has to click

        //Map each ImageView to respective favour (we assume that favour0 in fxml is the same in key=0 in List etc)
        Map<ImageView, Effect> imageViewEffectMap = new HashMap<ImageView, Effect>() {{
            put(favour0, choosableCouncilFavors.get(0));
            put(favour1, choosableCouncilFavors.get(1));
            put(favour2, choosableCouncilFavors.get(2));
            put(favour3, choosableCouncilFavors.get(3));
            put(favour4, choosableCouncilFavors.get(4));
        }};

        //set action for each imageview
        imageViewEffectMap.forEach((imageView, effect) -> {
            imageView.setOnMouseClicked(event -> {
                if (chosenCouncilFavors.contains(effect)) {
                    //If effect was already selected, remove selection (remove from List) and remove highlight from imageView.
                    chosenCouncilFavors.remove(effect);
                    imageView.setEffect(null);
                    submitButton.setDisable(true);//if we removed a card, for sure submit has to be disabled
                } else {
                    //If effect wasn't selected, select it (adding to List) and highlight imageView.
                    if (chosenCouncilFavors.size() >= howManyFavours) return;//We reached maximum.
                    chosenCouncilFavors.add(effect);
                    ColorAdjust colorAdjust = new ColorAdjust();
                    colorAdjust.setContrast(0.1);
                    colorAdjust.setHue(-0.05);
                    colorAdjust.setBrightness(0.7);
                    colorAdjust.setSaturation(0.7);
                    imageView.setEffect(colorAdjust);

                    if (chosenCouncilFavors.size() == howManyFavours) submitButton.setDisable(false);
                }
            });
        });
    }
}
