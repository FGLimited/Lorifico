package Client.UI.GUI.resources.gameComponents;

import Client.Datawarehouse;
import Client.UI.TurnObserver;
import Logging.Logger;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.transform.Scale;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Io on 08/06/2017.
 */
public class UserAvatarClickableElement extends Pane implements TurnObserver {
    private static Map<String, UserAvatarClickableElement> stringUserAvatarClickableElementMap = new HashMap();
    private String username;//Username of this User
    private Label usernameLabel;//Label where username is shown

    /**
     * Creates a new HBox containing user avatars and his username
     *
     * @param username
     */
    private UserAvatarClickableElement(String username) {
        this.username = username;
        String avatarURL = "Client/UI/GUI/resources/images/avatars/avatar00000.png";

        //Creates a new imageview containing user's avatar
        ImageView avatarImageView = (new ImageView(new Image(avatarURL, true)));
        avatarImageView.getTransforms().add(new Scale(0.08, 0.08));

        //Add a usernameLabel with user's username.
        usernameLabel = new Label(username);
        usernameLabel.setFont(new Font(15));
        usernameLabel.setPadding(new Insets(36, 0, 0, 0));//Move usernameLabel under avatar

        minWidthProperty().bind(usernameLabel.widthProperty().add(20));
        usernameLabel.translateXProperty().bind(widthProperty().subtract(usernameLabel.widthProperty()).divide(2));
        avatarImageView.translateXProperty().bind(widthProperty().divide(2).subtract(20));

        //Attach to VBox
        getChildren().addAll(avatarImageView, usernameLabel);

        Datawarehouse.getInstance().registerTurnObserver(this);//Register as turn observer

        //If it's already my turn, change text's color:
        if (Datawarehouse.getInstance().getWhoseTurn().equals(username)) usernameLabel.setTextFill(Color.RED);
    }

    /**
     * Singleton getter
     *
     * @param username user we are interested in
     * @return
     */
    public static UserAvatarClickableElement getInstance(String username) {
        //If user's elements doesn't exist we have to create it.
        if (!stringUserAvatarClickableElementMap.containsKey(username))
            stringUserAvatarClickableElementMap.put(username, new UserAvatarClickableElement(username));
        return stringUserAvatarClickableElementMap.get(username);
    }

    /**
     * On hover we'll show this user's data in MilitaryVictoryBox controller.
     *
     * @param miliyaryVictoryBoxController
     */
    public void setHoverCallBack(MiliyaryVictoryBoxController miliyaryVictoryBoxController) {
        //When we hover on a UserAvatarClickable let's show his data on right side.
        this.setOnMouseEntered((event) -> {
            miliyaryVictoryBoxController.showPointsOf(username);
        });

        //When mouse exits lets go back and show playing user's data.
        this.setOnMouseExited((event) -> {
            miliyaryVictoryBoxController.showPointsOf(Datawarehouse.getInstance().getMyUsername());
        });
    }

    /**
     * Method called when turn changes, if it's this user's turn we'll highlight it, otherwise remove effect
     *
     * @param username user playing current turn.
     */
    @Override
    public void onTurnChange(String username) {
        if (!Platform.isFxApplicationThread()) {
            Platform.runLater(() -> onTurnChange(username));
            return;
        }
        Logger.log(Logger.LogLevel.Normal, "UserAvatarClickableElement: updating playing user");

        //if it's my turn color label's text in red
        if (username.equals(this.username)) {
            usernameLabel.setTextFill(Color.RED);
        } else {
            usernameLabel.setTextFill(Color.BLACK);
        }
    }
}
