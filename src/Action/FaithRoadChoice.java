package Action;

import Game.UserObjects.GameUser;
import Model.User.User;

/**
 * Created by fiore on 06/06/2017.
 */
public class FaithRoadChoice implements BaseAction {

    private final boolean churchSupport;

    public FaithRoadChoice(boolean churchSupport) {
        this.churchSupport = churchSupport;
    }

    @Override
    public void doAction(User user) {

        // Get current game user
        final GameUser currentUser = user.getGameUser();

        // Set user choice received from client
        currentUser.setChurchSupport(churchSupport);

        // Set for move end
        currentUser.setHasMoved(true);

        // Notify turn to go ahead
        synchronized (currentUser) {
            currentUser.notify();
        }

    }
}
