package Action;

import Game.UserObjects.PlayerState;
import Model.User.User;

/**
 * Created by fiore on 01/06/2017.
 */
public class PlayerStateUpdate extends UserSpecific implements BaseAction {

    private final PlayerState updatedState;

    public PlayerStateUpdate(String username, PlayerState updatedState) {
        super(username);
        this.updatedState = updatedState;
    }

    @Override
    public void doAction(User user) {
        // TODO: update player state for specified username (check resources and cards)
    }
}
