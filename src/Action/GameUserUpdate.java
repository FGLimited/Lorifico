package Action;

import Game.UserObjects.GameUser;
import Model.User.User;

/**
 * Created by fiore on 01/06/2017.
 */
public class GameUserUpdate extends UserSpecific implements BaseAction {

    private final GameUser updatedUser;

    public GameUserUpdate(String username, GameUser updatedUser) {
        super(username);
        this.updatedUser = updatedUser;
    }

    @Override
    public void doAction(User user) {
        // TODO: update game user of user with specified username
    }
}