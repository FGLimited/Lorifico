package Client.UI.GUI.resources.gameComponents;

import Client.Datawarehouse;
import Client.UI.PlayerStateObserver;
import Game.Usable.ResourceType;
import Game.UserObjects.PlayerState;
import Logging.Logger;
import javafx.application.Platform;
import javafx.scene.control.Label;

/**
 * Created by Io on 08/06/2017.
 */
public class MiliyaryVictoryBoxController implements PlayerStateObserver {
    private Label militaryLabel, victoryLabel, showingUserLabel;
    private String usernameShowed;//User we are showing (this can be anyone of playing users!!)

    public MiliyaryVictoryBoxController(Label showingUserLabel, Label militaryLabel, Label victoryLabel) {
        this.militaryLabel = militaryLabel;
        this.victoryLabel = victoryLabel;
        this.showingUserLabel = showingUserLabel;//Label containing whose data is displayed (if different from me)

        //Register as observers of PlayerState
        Datawarehouse.getInstance().registerPlayerStateObserver(this);
    }

    /**
     * Used to show passed user's military and victory points
     *
     * @param username
     */
    public void showPointsOf(String username) {
        this.usernameShowed = username;//Update reference to the one we are showing.

        //Update points on GUI
        PlayerState playerState = Datawarehouse.getInstance().getPlayerState(username);
        if (playerState == null) {
            Logger.log(Logger.LogLevel.Error, "MilitaryVictoryBox is trying to show a null Playerstate");
            return;
        }

        //If user showed is not me, write it on UI
        if (!username.equals(Datawarehouse.getInstance().getMyUsername())) {
            showingUserLabel.setText(username);
        } else {
            showingUserLabel.setText("");
        }

        //Update labels
        victoryLabel.setText(playerState.getResources().get(ResourceType.VictoryPoint).toString());
        militaryLabel.setText(playerState.getResources().get(ResourceType.MilitaryPoint).toString());
    }

    /**
     * This is the method called when Client receives
     *
     * @param playerState
     * @param username
     */
    @Override
    public void onPlayerStateUpdate(PlayerState playerState, String username) {
        if (!Platform.isFxApplicationThread()) {
            Platform.runLater(() -> onPlayerStateUpdate(playerState, username));
            return;
        }

        if (usernameShowed == null) {
            usernameShowed = Datawarehouse.getInstance().getMyUsername();
            showingUserLabel.setText("");
        }

        if (username.equals(this.usernameShowed)) {
            //If we received userState we are displaying we can update GUI
            victoryLabel.setText(playerState.getResources().get(ResourceType.VictoryPoint).toString());
            militaryLabel.setText(playerState.getResources().get(ResourceType.MilitaryPoint).toString());
            Logger.log(Logger.LogLevel.Normal, "MilitaryVictoryBox received PlayerState");
        }
    }
}