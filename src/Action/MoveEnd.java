package Action;

import Model.User.User;

/**
 * Created by fiore on 06/06/2017.
 */
public class MoveEnd implements BaseAction {

    private final boolean isTimeout;

    public MoveEnd(boolean isTimeout) {
        this.isTimeout = isTimeout;
    }

    @Override
    public void doAction(User user) {
        // TODO: if isTimeout notify user that move time is out and no other move can be performed

        // TODO: disable all, now is move time for another user
    }
}
