package Client.UI.GUI.resources.gameComponents;

import Client.Datawarehouse;
import Client.UI.GUI.UserInterfaceImplemJFX;
import Client.UI.UserInterfaceFactory;
import Game.Cards.CardType;
import Logging.Logger;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by andrea on 13/06/17.
 */
public class PunchboardController implements Initializable {
    private String username;//Username we are showing
    private JFXDialog jfxDialog;//Dialog itself
    private Integer[] positions = {0, 0, 0, 0};//counter of cards showed per type (key is enum. order)

    @FXML
    private AnchorPane anchorPane;//Node containing punchboard imageview etc..

    public PunchboardController(String username) {
        this.username = username;//Set username
        createPunchboardDialog();
    }

    private void createPunchboardDialog() {
        //Make sure we are on JavaFX thread:
        if (!Platform.isFxApplicationThread()) {
            Platform.runLater(() -> createPunchboardDialog());
            return;
        }

        //Create new dialogContent
        JFXDialogLayout content = new JFXDialogLayout();

        //Try to load dialog content from .fxml
        Node dialogContent;
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Client/UI/GUI/fxml/DialogConent/Punchboard.fxml"));
            fxmlLoader.setController(this);
            dialogContent = fxmlLoader.load();
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        //Set contents to dialog
        content.setBody(dialogContent);
        content.setHeading(new Label("Plancia"));

        //Append dialog to root stackpane
        StackPane stackPane = ((UserInterfaceImplemJFX) UserInterfaceFactory.getInstance()).getRootStackPane();
        jfxDialog = new JFXDialog(stackPane, content, JFXDialog.DialogTransition.CENTER, true);
        jfxDialog.show();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Logger.log(Logger.LogLevel.Normal, "Showing " + username + "'s punchboard");
        Datawarehouse.getInstance().getPlayerState(username).getCards(CardType.Building).forEach(card -> createAndAddCardImageView(card.getNumber()));
        Datawarehouse.getInstance().getPlayerState(username).getCards(CardType.Territory).forEach(card -> createAndAddCardImageView(card.getNumber()));
        Datawarehouse.getInstance().getPlayerState(username).getCards(CardType.Personality).forEach(card -> createAndAddCardImageView(card.getNumber()));
        Datawarehouse.getInstance().getPlayerState(username).getCards(CardType.Challenge).forEach(card -> createAndAddCardImageView(card.getNumber()));
    }

    /**
     * Creates a new ImageView containing card and attaches it to main anchorPane
     *
     * @param cardNumber card's number
     * @return
     */
    private ImageView createAndAddCardImageView(int cardNumber) {
        GameCard gameCard = GameCard.getCard(cardNumber);
        ImageView imageView = new ImageView(gameCard.getImage());
        imageView.setFitWidth(92);
        imageView.setFitHeight(147);

        //Sets (Yellow,Purple) and (Green, Blue) cards Y
        if (gameCard.getCardType().equals(CardType.Building) || gameCard.getCardType().equals(CardType.Challenge)) {
            imageView.setLayoutY(14);
        } else {
            imageView.setLayoutY(205);
        }

        int position = positions[gameCard.getCardType().ordinal()];//Position of card to be placed equals to counter.

        //Sets (Yellow,Green) and (Purple, Blue) cards X
        if (gameCard.getCardType().equals(CardType.Building) || gameCard.getCardType().equals(CardType.Territory)) {
            if (position < 3) {
                imageView.setLayoutX(65 + 105.6 * position);
            } else {
                imageView.setLayoutX(65 + 108 * position);
            }
        } else {
            imageView.setLayoutX(720 + 40 * position);
        }

        positions[gameCard.getCardType().ordinal()]++;//We placed a card, increment counter
        anchorPane.getChildren().add(imageView);//Add to punchboard
        imageView.setOnMouseClicked(event -> showCardInDialog(cardNumber));//Set callback
        return imageView;
    }

    public void showCardInDialog(int cardNumber) {
        //Make sure we are on JavaFX thread:
        if (!Platform.isFxApplicationThread()) {
            Platform.runLater(() -> createPunchboardDialog());
            return;
        }

        //Create new dialogContent
        JFXDialogLayout content = new JFXDialogLayout();

        //Set contents to dialog
        content.setBody(new ImageView(GameCard.getCard(cardNumber).getImage()));
        content.setHeading(new Label("Visualizza carta"));

        //Append dialog to root stackpane
        StackPane stackPane = ((UserInterfaceImplemJFX) UserInterfaceFactory.getInstance()).getRootStackPane();
        JFXDialog jfxDialog = new JFXDialog(stackPane, content, JFXDialog.DialogTransition.CENTER, true);
        jfxDialog.show();
    }
}
