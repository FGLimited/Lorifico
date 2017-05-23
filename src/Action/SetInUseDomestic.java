package Action;

import Game.UserObjects.DomesticColor;
import Game.UserObjects.PlayerState;
import Model.User.User;
import Server.Game.UserObjects.Domestic;
import Game.UserObjects.GameUser;

/**
 * Created by fiore on 23/05/2017.
 */
public class SetInUseDomestic implements BaseAction {

    private final DomesticColor color;

    private final int slaves;

    /**
     * Choose which domestic to use for current round
     * and how many slaves to use to increment its value
     *
     * @param color Chosen domestic color
     * @param slaves Slaves to use
     */
    public SetInUseDomestic(DomesticColor color, int slaves) {
        this.color = color;
        this.slaves = slaves;
    }

    @Override
    public void doAction(User user) {

        // Get current game user
        final GameUser gameUser = user.getGameUser();

        // Get current player state
        final PlayerState currentState = gameUser.getUserState();

        // Create bound domestic
        final Domestic inUse = new Domestic(gameUser.getDomestics().get(color));

        // Calculate slave increment value
        int increment = slaves / currentState.getSlavePerDomesticValue();

        // Update in use domestic value
        inUse.setValue(inUse.getValue() + increment);

        // Set domestic in current state
        currentState.setInUseDomestic(inUse);

        // Update user state with new in use domestic
        gameUser.updateUserState(currentState);

        // TODO: send positions with cost / effects

    }
}
