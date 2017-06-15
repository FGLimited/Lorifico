package Server.Game.Positions;

import Game.Effects.Effect;
import Game.Positions.PositionType;
import Game.UserObjects.PlayerState;
import Server.Game.Effects.ImmediateEffect;
import Server.Game.Usable.Cost;
import java.util.Collections;
import java.util.List;

/**
 * Created by fiore on 14/05/2017.
 */
public class MarketPosition extends Position<Cost> {

    private final Effect immediatePositionEffect;

    /**
     * Initialize a new market position with given effect
     *
     * @param number Position's number
     * @param immediateEffect Effect activated when position is occupied
     */
    public MarketPosition(int number, ImmediateEffect immediateEffect) {
        super(PositionType.Market, number);
        immediatePositionEffect = immediateEffect;
    }

    /**
     * Gson constructor
     */
    private MarketPosition() {
        immediatePositionEffect = null;
    }

    @Override
    public List<Cost> canOccupy(PlayerState currentState) {

        // If position is occupied
        // or domestic has zero value
        // or aggregate bounds won't let this domestic in
        // return empty list
        if(isOccupied() != null
                || currentState.getInUseDomestic().getValue() < 1
                || !parent.canOccupy(currentState.getInUseDomestic()))
            return Collections.emptyList();

        // If can occupy return empty cost
        return Collections.singletonList(new Cost(null, null));
    }

    @Override
    public PlayerState occupy(PlayerState currentState, List<Cost> chosenTs) {
        super.occupy(currentState, chosenTs);

        // Apply immediate effect
        immediatePositionEffect.apply(currentState);

        // Return updated state
        return currentState;
    }

}
