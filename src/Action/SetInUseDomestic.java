package Action;

import Game.Usable.ResourceType;
import Game.UserObjects.DomesticColor;
import Game.UserObjects.PlayerState;
import Model.User.User;
import Server.Game.UserObjects.Domestic;
import Game.UserObjects.GameUser;
import java.util.Collections;

/**
 * Created by fiore on 23/05/2017.
 */
public class SetInUseDomestic implements BaseAction {

    private final Domestic selectedDomestic;

    private final int slaves;

    /**
     * Choose which domestic to use for current round
     * and how many slaves to use to increment its value
     *
     * @param selectedDomestic Chosen domestic
     * @param slaves Slaves to use
     */
    public SetInUseDomestic(Domestic selectedDomestic, int slaves) {
        this.selectedDomestic = selectedDomestic;
        this.slaves = slaves;
    }

    @Override
    public void doAction(User user) {

        // Get current game user
        final GameUser gameUser = user.getGameUser();

        // Get current player state
        final PlayerState currentState = gameUser.getUserState();

        // Create bound domestic or special neutral if necessary
        final Domestic inUse;

        if(selectedDomestic.getType() != null)
            inUse = new Domestic(gameUser.getDomestics().get(selectedDomestic.getType()));
        else
            inUse = new Domestic(gameUser.getFamilyColor(), DomesticColor.Neutral, selectedDomestic.getValue());

        // Calculate slave increment value
        int increment = slaves / currentState.getSlavePerDomesticValue();

        // Update in use domestic value
        inUse.setValue(inUse.getValue() + increment);

        // Set domestic in current state
        currentState.setInUseDomestic(inUse);

        // Decrement slaves value
        currentState.setResources(Collections.singletonMap(ResourceType.Slave, currentState.getResources().get(ResourceType.Slave) - slaves), false);

        // Update user state with new in use domestic
        gameUser.updateUserState(currentState);

        // Send all positions only if normal domestic is selected
        // else they have already been sent from BonusDomesticEffect
        if(selectedDomestic.getType() != null) {
            // TODO: send positions with cost / effects
        }

    }
}
