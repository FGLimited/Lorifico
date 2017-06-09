package Client.UI.GUI.resources.gameComponents;

import Client.Datawarehouse;
import Client.UI.PlayerStateObserver;
import Game.UserObjects.PlayerState;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

/**
 * Created by Io on 08/06/2017.
 */
public class MiliyaryVictoryBoxController extends Pane implements PlayerStateObserver {
    private HBox node;//Node where we are attaching our UserAvatarClickableElements

    public MiliyaryVictoryBoxController(HBox militaryVictoryHBox) {
        this.node = militaryVictoryHBox;

        //Register as observers of PlayerState
        Datawarehouse.getInstance().registerPlayerStateObserver(this);
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