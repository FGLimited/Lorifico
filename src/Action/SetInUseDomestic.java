package Action;


import Game.Positions.PositionType;
import Game.Usable.ResourceType;
import Game.UserObjects.Choosable;
import Model.User.User;
import Server.Game.Usable.Cost;
import Server.Game.UserObjects.Domestic;
import Server.Game.UserObjects.GameUser;
import Server.Game.UserObjects.PlayerState;

import java.util.*;

/**
 * Created by fiore on 23/05/2017.
 */
public class SetInUseDomestic implements BaseAction {

    private final Domestic selectedDomestic;
    private final List<PositionType> requestedPositions;
    private final Cost costBonus;
    private int slaves;

    /**
     * Choose which domestic to use for current round
     * and how many slaves to use to increment its value
     *
     * @param selectedDomestic Chosen domestic
     * @param slaves Slaves to use
     * @param requestedPositions Requested positions update (null for all positions)
     * @param costBonus Bonus to apply to costs returned in next update
     */
    public SetInUseDomestic(Domestic selectedDomestic, int slaves, List<PositionType> requestedPositions, Cost costBonus) {
        this.selectedDomestic = selectedDomestic;
        this.slaves = slaves;
        this.requestedPositions = requestedPositions;
        this.costBonus = costBonus;
    }

    public SetInUseDomestic(Domestic selectedDomestic, int slaves) {
        this(selectedDomestic, slaves, null, null);
    }

    /**
     * Used to set slaves if not set in constructor
     *
     * @param slaves slaves to add
     */
    public void setSlaves(int slaves) {
        this.slaves = slaves;
    }

    @Override
    public void doAction(User user) {

        // Get current game user
        GameUser gameUser = (GameUser) user.getGameUser();

        // Get current player state
        PlayerState currentState = gameUser.getUserState();

        // Create bound domestic or special neutral if necessary
        Domestic inUse;

        if(selectedDomestic.getType() != null)
            inUse = new Domestic(gameUser.getDomestics().get(selectedDomestic.getType()));
        else
            inUse = selectedDomestic;

        // Calculate slave increment value
        int increment = slaves / currentState.getSlavePerDomesticValue();

        // Update in use domestic value
        inUse.setValue(inUse.getValue() + increment);

        // Set domestic in current state
        currentState.setInUseDomestic(inUse);

        // Create resource update
        final HashMap<ResourceType, Integer> resourceUpdate = new HashMap<>();
        resourceUpdate.put(ResourceType.Slave, currentState.getResources().get(ResourceType.Slave) - slaves);

        // If cost bonus is present add bonus to player state
        if(costBonus != null) {
            costBonus.getResources().forEach((type, bonus) -> resourceUpdate.put(type, resourceUpdate.get(type) + bonus));
        }

        // Decrement slaves value
        currentState.setResources(resourceUpdate, false);

        // Update user state with new in use domestic
        gameUser.updateUserState(currentState);

        // Send requested positions
        Map<Integer, List<Choosable>> positions = user.getMatch().getTable()
                .getPositions(gameUser, requestedPositions);

        // If a cost bonus is specified apply it to all available costs
        if(costBonus != null){

            // Remove bonus resources from current player state
            costBonus.getResources().forEach((type, bonus) -> resourceUpdate.replace(type, resourceUpdate.get(type) - bonus));
            resourceUpdate.remove(ResourceType.Slave);
            currentState.setResources(resourceUpdate, false);
            gameUser.updateUserState(currentState);

            // Update all available costs removing bonus resources
            positions.forEach((number, list) -> {
                // If current list isn't a cost list go ahead
                if(list.isEmpty() || list.get(0).getClass() != Cost.class)
                    return;

                // Create new cost list
                List<Choosable> costs = new ArrayList<>();

                // Apply bonus to each cost and update new list
                list.forEach(cost -> costs.add(((Cost)cost).sum(costBonus, false)));

                // Update list in positions map
                list.clear();
                list.addAll(costs);
            });

        }

        // Send choosable for requested positions back to client
        user.getLink().sendMessage(new PositionChoosables(positions));

    }
}
