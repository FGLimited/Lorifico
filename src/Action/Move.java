package Action;

import Game.Positions.Position;
import Game.UserObjects.GameUser;
import Model.User.User;
import Server.Game.UserObjects.GameTable;
import java.util.List;

/**
 * Created by fiore on 23/05/2017.
 */
public class Move<T> implements BaseAction {

    private final List<T> chosenTs;

    private final int positionNumber;

    /**
     * Move action for a cost/effect position
     *
     * @param positionNumber Position number
     * @param chosenTs Chosen cost / chosen effects to activate
     */
    public Move(int positionNumber, List<T> chosenTs) {
        this.positionNumber = positionNumber;
        this.chosenTs = chosenTs;
    }

    @Override
    public void doAction(User user) {

        // Get current user and player state
        final GameUser gameUser = user.getGameUser();
        final GameTable table = user.getMatch().getTable();

        // If table is null, game hasn't started yet
        if(table == null){
            // TODO: sei un coglione
            return;
        }

        // Set move completed
        gameUser.setHasMoved(true);

        // Occupy selected position (this will activate all card/position effects)
        Position updatedPosition = table.occupy(user.getGameUser(), positionNumber, chosenTs);

        // TODO: send update to all players here if you want differential update
        // (else get global update from game table as you wish)

        // If this was last move notify on gameuser object
        if(gameUser.getHasMoved()) {
            synchronized (gameUser){
                gameUser.notify();
            }
        }
    }
}
