package Action;

import Game.Positions.Position;
import Game.UserObjects.Choosable;
import Server.Game.UserObjects.GameUser;
import Model.User.User;
import Server.Game.UserObjects.GameTable;
import java.util.List;

/**
 * Created by fiore on 23/05/2017.
 */
public class Move implements BaseAction {

    private final List<Choosable> chosenTs;

    private final int positionNumber;

    /**
     * Move action for a cost/effect position
     *
     * @param positionNumber Position number
     * @param chosenTs Chosen cost / chosen effects to activate
     */
    public Move(int positionNumber, List<Choosable> chosenTs) {
        this.positionNumber = positionNumber;
        this.chosenTs = chosenTs;
    }

    @Override
    public void doAction(User user) {

        // Get current user and player state
        final GameUser gameUser = (GameUser) user.getGameUser();
        final GameTable table = user.getMatch().getTable();

        // If table is null, game hasn't started yet
        if(table == null){
            // TODO: scantot
            return;
        }

        // Set move completed
        gameUser.setHasMoved(true);

        // Occupy selected position (this will activate all card/position effects)
        Position updatedPosition = table.occupy(gameUser, positionNumber, chosenTs);

        // Create position update message
        BaseAction updateMessage = new UpdatePosition(updatedPosition.getNumber(), updatedPosition.isOccupied());

        // Send update to all users in this match
        user.getMatch().sendAll(updateMessage);

        // If this was last move notify on gameUser object
        if(gameUser.getHasMoved()) {
            synchronized (gameUser){
                gameUser.notify();
            }
        }
    }
}
