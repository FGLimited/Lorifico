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

    private final Class<T> type;

    private final List<T> chosenTs;

    private final int positionNumber;

    /**
     * Move action for a cost/effect position
     *
     * @param positionNumber Position number
     * @param chosenTs Chosen cost / chosen effects to activate
     * @param tType Type of T (cost / effect)
     */
    public Move(int positionNumber, List<T> chosenTs, Class<T> tType) {
        this.positionNumber = positionNumber;
        this.chosenTs = chosenTs;
        type = tType;
    }

    @Override
    public void doAction(User user) {

        final GameUser gameUser = user.getGameUser();
        final GameTable table = user.getMatch().getTable();

        if(table == null){
            // TODO: sei un coglione
            return;
        }

        gameUser.setHasMoved(true);

        Position updatedPosition = table.occupy(user.getGameUser(), positionNumber, chosenTs, type);

        // TODO: send update to all players here if you want differential update
        // (else get global update from game table as you wish)

        if(!gameUser.getHasMoved()) {
            synchronized (gameUser){
                gameUser.notify();
            }
        }
    }
}
