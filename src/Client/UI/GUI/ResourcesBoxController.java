package Client.UI.GUI;

import Client.Datawarehouse;
import Client.UI.PlayerStateObserver;
import Game.Usable.ResourceType;
import Game.UserObjects.PlayerState;
import Logging.Logger;
import javafx.application.Platform;
import javafx.scene.control.Label;

/**
 * Created by andrea on 11/06/17.
 */
public class ResourcesBoxController implements PlayerStateObserver {
    private Label rockLabel, woodLabel, slavesLabel, goldLabel;

    /**
     * Creates a new controller which takes care of keeping resources labels up to date
     *
     * @param rockLabel
     * @param woodLabel
     * @param slavesLabel
     * @param goldLabel
     */
    public ResourcesBoxController(Label rockLabel, Label woodLabel, Label slavesLabel, Label goldLabel) {
        this.rockLabel = rockLabel;
        this.goldLabel = goldLabel;
        this.woodLabel = woodLabel;
        this.slavesLabel = slavesLabel;

        Datawarehouse.getInstance().registerPlayerStateObserver(this);
    }

    /**
     * Called from Datawarehouse when a new playerstare is available
     *
     * @param playerState
     * @param username
     */
    @Override
    public void onPlayerStateUpdate(PlayerState playerState, String username) {
        //Make sure we are on JavaFX thread:
        if (!Platform.isFxApplicationThread()) {
            Platform.runLater(() -> onPlayerStateUpdate(playerState, username));
            return;
        }

        //if it's my PlayerState we can update GUI
        if (username.equals(Datawarehouse.getInstance().getMyUsername())) {
            rockLabel.setText(playerState.getResources().get(ResourceType.Rock).toString());
            goldLabel.setText(playerState.getResources().get(ResourceType.Gold).toString());
            woodLabel.setText(playerState.getResources().get(ResourceType.Wood).toString());
            slavesLabel.setText(playerState.getResources().get(ResourceType.Slave).toString());
            Logger.log(Logger.LogLevel.Normal, "ResourcesBoxController updated shown resources on GUI");
        }
    }
}
