package Server.Game.Positions;

import Game.Effects.Effect;
import Game.Positions.Position;
import Game.Positions.PositionType;
import Game.UserObjects.PlayerState;
import Server.Game.Effects.ImmediateEffect;
import Server.Game.Usable.Cost;
import Server.Game.UserObjects.Domestic;
import org.jetbrains.annotations.Nullable;
import java.util.Collections;
import java.util.List;

/**
 * Created by fiore on 14/05/2017.
 */
public class MarketPosition implements Position<Cost> {

    private final int number;

    private final PositionType type = PositionType.Market;

    private transient volatile PositionAggregate parent;

    private final Effect immediatePositionEffect;

    private volatile Domestic occupant;

    /**
     * Initialize a new market position with given effect
     *
     * @param number Position's number
     * @param immediateEffect Effect activated when position is occupied
     */
    public MarketPosition(int number, ImmediateEffect immediateEffect) {
        this.number = number;
        immediatePositionEffect = immediateEffect;
    }

    @Override
    public PositionType getType() {
        return type;
    }

    @Override
    public int getNumber() {
        return number;
    }

    @Override
    public List<Cost> canOccupy(PlayerState currentState) {

        // If position is occupied
        // or domestic has zero value
        // or aggregate bounds won't let this domestic in
        // return empty list
        if(occupant != null
                || currentState.getInUseDomestic().getValue() > 0
                || !parent.canOccupy(currentState.getInUseDomestic()))
            return Collections.emptyList();

        // If can occupy return empty cost
        return Collections.singletonList(new Cost(null, null));
    }

    @Override
    public PlayerState occupy(PlayerState currentState, Cost chosenT) {

        // Apply immediate effect
        immediatePositionEffect.apply(currentState);

        // Set occupant to in use domestic
        occupant = currentState.getInUseDomestic();

        occupant.setInPosition(true);

        // Return updated state
        return currentState;
    }

    @Override
    public PlayerState occupy(PlayerState currentState, List<Cost> chosenTs) {
        return occupy(currentState, chosenTs.get(0));
    }

    @Nullable
    @Override
    public Domestic isOccupied() {
        return occupant;
    }

    @Override
    public void free() {

        occupant.setInPosition(false);
        occupant = null;
    }

    @Override
    public void setAggregate(PositionAggregate parent) {
        if(this.parent == null)
            this.parent = parent;
    }

    @Override
    public int compareTo(Position other) {
        return number - other.getNumber();
    }
}
