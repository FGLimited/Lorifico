package Client.UI.GUI.resources.gameComponents;

import Client.Datawarehouse;
import Client.UI.PlayerStateObserver;
import Game.UserObjects.PlayerState;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.transform.Scale;

/**
 * Created by Io on 08/06/2017.
 */
public class MiliyaryVictoryBoxController implements PlayerStateObserver {
    private HBox node;//Node where we are attaching our UserAvatarClickableElements

    public MiliyaryVictoryBoxController(HBox militaryVictoryHBox) {
        this.node = militaryVictoryHBox;

        createImageWithStackedNumber();

        //Register as observers of PlayerState
        Datawarehouse.getInstance().registerPlayerStateObserver(this);
    }

    private void createImageWithStackedNumber() {
        String imageURL = "Client/UI/GUI/resources/images/misc/militaryShield.png";
        //Creates a new imageview containing user's avatar
        ImageView avatarImageView = (new ImageView(new Image(imageURL, true)));
        avatarImageView.getTransforms().add(new Scale(0.34, 0.34));

        //Add a label with user's username.
        Label label = new Label("");
        label.setFont(new Font(26));
        label.setTextFill(Color.WHITE);

        label.textProperty().addListener(((observable, oldValue, newValue) -> {
            if (Integer.valueOf(newValue) > 9)
                label.setPadding(new Insets(18, 0, 0, 18));//Move label under avatar
            else
                label.setPadding(new Insets(18, 0, 0, 25));//Move label under avatar
        }));

        label.setText("7");
        label.setText("27");

        Pane pane = new Pane();
        pane.getChildren().addAll(avatarImageView, label);
        pane.setPadding(new Insets(0, 20, 0, 20));
        node.getChildren().add(pane);
    }

    /**
     * This is the method called when Client receives
     *
     * @param playerState
     * @param username
     */
    @Override
    public void onPlayerStateUpdate(PlayerState playerState, String username) {

    }
}