package Client.UI.GUI.resources.gameComponents;

import Client.Datawarehouse;
import Client.UI.PlayerStateObserver;
import Game.Usable.ResourceType;
import Game.UserObjects.PlayerState;
import Logging.Logger;
import javafx.scene.control.Label;

/**
 * Created by Io on 08/06/2017.
 */
public class MiliyaryVictoryBoxController implements PlayerStateObserver {
    private Label militaryLabel, victoryLabel;
    private String username;//User we are showing (this can be anyone of playing users!!)

    public MiliyaryVictoryBoxController(Label militaryLabel, Label victoryLabel) {
        this.militaryLabel = militaryLabel;
        this.victoryLabel = victoryLabel;

        //Register as observers of PlayerState
        Datawarehouse.getInstance().registerPlayerStateObserver(this);
    }

    /**
     * Used to show passed user's military and victory points
     *
     * @param username
     */
    public void showPointsOf(String username) {
        this.username = username;//Update reference to the one we are showing.

        //Update points on GUI
        PlayerState playerState = Datawarehouse.getInstance().getPlayerState(username);
        if (playerState == null) {
            Logger.log(Logger.LogLevel.Error, "MilitaryVictoryBox is trying to show a null Playerstate");
            return;
        }
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
        if (username.equals(this.username)) {
            //If we received userState we are displaying we can update GUI
            victoryLabel.setText(playerState.getResources().get(ResourceType.VictoryPoint).toString());
            militaryLabel.setText(playerState.getResources().get(ResourceType.MilitaryPoint).toString());
            Logger.log(Logger.LogLevel.Normal, "MilitaryVictoryBox received PlayerState");
        }
    }
}