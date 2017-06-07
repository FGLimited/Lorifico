package Client.UI;

import Game.UserObjects.PlayerState;

/**
 * Created by andrea on 07/06/17.
 */
public interface PlayerStateObserver {

    /**
     * Callback
     *
     * @param playerState
     * @param username
     */
    void onPlayerStateUpdate(PlayerState playerState, String username);
}
