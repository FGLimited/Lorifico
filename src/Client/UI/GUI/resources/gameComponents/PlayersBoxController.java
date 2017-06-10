package Client.UI.GUI.resources.gameComponents;

import Client.Datawarehouse;
import Client.UI.PlayerStateObserver;
import Game.UserObjects.PlayerState;
import Logging.Logger;
import javafx.application.Platform;
import javafx.scene.layout.HBox;

/**
 * Created by Io on 08/06/2017.
 */
public class PlayersBoxController implements PlayerStateObserver {
    private HBox node;//Node where we are attaching our UserAvatarClickableElements

    public PlayersBoxController(HBox playersHBox, MiliyaryVictoryBoxController miliyaryVictoryBoxController) {
        this.node = playersHBox;
        Datawarehouse.getInstance().registerPlayerStateObserver(this);
    }

    /**
     * Method called when new PlayerState is available, we 'll update current users playing.
     *
     * @param playerState
     * @param username
     */
    @Override
    public void onPlayerStateUpdate(PlayerState playerState, String username) {
        //Be sure we are on javafx thread
        if (!Platform.isFxApplicationThread()) {
            Platform.runLater(() -> onPlayerStateUpdate(playerState, username));
            return;
        }

        Logger.log(Logger.LogLevel.Normal, "PlayersBoxController just received a PlayerState");

        //if we have a link to node, check if user's are shown on GUI
        if (node != null) {
            //If user related to this PlayerState is not shown on GUI, let's attach it.
            if (!node.getChildren().contains(UserAvatarClickableElement.getInstance(username))) {
                node.getChildren().add(UserAvatarClickableElement.getInstance(username));
            }
        } else Logger.log(Logger.LogLevel.Error, "PlayersBoxController, node is null");
    }
}