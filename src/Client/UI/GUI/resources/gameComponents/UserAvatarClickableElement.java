package Client.UI.GUI.resources.gameComponents;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.transform.Scale;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Io on 08/06/2017.
 */
public class UserAvatarClickableElement extends Pane {
    private static Map<String, UserAvatarClickableElement> stringUserAvatarClickableElementMap = new HashMap();

    /**
     * Creates a new HBox containing user avatars and his username
     *
     * @param username
     */
    private UserAvatarClickableElement(String username) {
        String avatarURL = "Client/UI/GUI/resources/images/avatars/avatar00000.png";

        //Creates a new imageview containing user's avatar
        ImageView avatarImageView = (new ImageView(new Image(avatarURL, true)));
        avatarImageView.getTransforms().add(new Scale(0.08, 0.08));

        //Add a label with user's username.
        Label label = new Label(username);
        label.setFont(new Font(15));
        label.setPadding(new Insets(36, 0, 0, 0));//Move label under avatar

        minWidthProperty().bind(label.widthProperty().add(20));
        label.translateXProperty().bind(widthProperty().subtract(label.widthProperty()).divide(2));
        avatarImageView.translateXProperty().bind(widthProperty().divide(2).subtract(20));

        //Attach to VBox
        getChildren().addAll(avatarImageView, label);

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
}
