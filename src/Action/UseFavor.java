package Action;

import Game.Effects.Effect;
import Game.UserObjects.GameUser;
import Game.UserObjects.PlayerState;
import Model.User.User;
import java.util.List;

/**
 * Created by fiore on 25/05/2017.
 */
public class UseFavor implements BaseAction {

    private final List<Effect> chosenFavors;

    /**
     * Return chosen favor to server to be applied
     *
     * @param chosenFavors Chosen favors
     */
    public UseFavor(List<Effect> chosenFavors) {
        this.chosenFavors = chosenFavors;
    }

    @Override
    public void doAction(User user) {

        // Get current user and player state
        final GameUser currentUser = user.getGameUser();
        final PlayerState currentState = currentUser.getUserState();

        // Apply chosen council favors to player state
        chosenFavors.forEach(favor -> favor.apply(currentState));

        // Update player state on current user
        currentUser.updateUserState(currentState);

        // Set move completed
        currentUser.setHasMoved(true);

        // Notify if this is last move
        if(currentUser.getHasMoved()) {
            synchronized (currentUser){
                currentUser.notify();
            }
        }

    }
}
