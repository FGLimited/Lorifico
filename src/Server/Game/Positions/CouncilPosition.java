package Server.Game.Positions;

import Game.Effects.Effect;
import Game.Positions.PositionType;
import Game.Usable.ResourceType;
import Game.UserObjects.PlayerState;
import Server.Game.Effects.ImmediateEffect;
import Server.Game.Usable.Cost;
import Server.Game.UserObjects.GameUser;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Created by fiore on 14/05/2017.
 */
public class CouncilPosition extends Position<Cost> {

    private transient volatile List<GameUser> turnOrder;

    private final Effect immediatePositionEffect;

    /**
     * Initialize a new market position with given effect
     *
     * @param number Position's number
     */
    public CouncilPosition(int number) {
        super(PositionType.Council, number);

        immediatePositionEffect = new ImmediateEffect(new HashMap<ResourceType, Integer>() {
            {
                put(ResourceType.Gold, 1);
                put(ResourceType.Favor, 1);
            }
        });
    }

    /**
     * Gson constructor
     */
    private CouncilPosition() {
        immediatePositionEffect = null;
    }

    /**
     * Set list to update with order of domestic placement to define next turn playing order
     *
     * @param nextTurnOrder Next turn playing order list
     */
    public void setOrderList(List<GameUser> nextTurnOrder) {
        if(turnOrder == null)
            turnOrder = nextTurnOrder;
    }

    @Override
    public List<Cost> canOccupy(PlayerState currentState) {

        // If position is occupied
        // or domestic has zero value
        // return empty list
        if(isOccupied() != null
                || currentState.getInUseDomestic().getValue() < 1)
            return Collections.emptyList();

        // If can occupy return empty cost
        return Collections.singletonList(new Cost(null));
    }

    @Override
    public PlayerState occupy(PlayerState currentState, List<Cost> chosenTs) {
        super.occupy(currentState, chosenTs);

        // Apply immediate effect
        immediatePositionEffect.apply(currentState);

        final GameUser currentUser = (GameUser) currentState.getGameUser();

        // Update list for next turn player's order
        if(!turnOrder.stream().anyMatch(user -> user.getFamilyColor() == currentUser.getFamilyColor()))
            turnOrder.add(currentUser);

        // Return updated state
        return currentState;
    }

}
