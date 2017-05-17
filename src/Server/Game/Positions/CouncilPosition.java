package Server.Game.Positions;

import Game.Effects.Effect;
import Game.Positions.Position;
import Game.Positions.PositionType;
import Game.Usable.ResourceType;
import Game.UserObjects.FamilyColor;
import Game.UserObjects.PlayerState;
import Server.Game.Effects.ImmediateEffect;
import Server.Game.Usable.Cost;
import Server.Game.UserObjects.Domestic;
import org.jetbrains.annotations.Nullable;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Created by fiore on 14/05/2017.
 */
public class CouncilPosition implements Position<Cost> {

    private final int number;

    private final PositionType type = PositionType.Market;

    private volatile transient PositionAggregate parent;

    private volatile transient List<FamilyColor> turnOrder;

    private final Effect immediatePositionEffect;

    private volatile Domestic occupant;

    /**
     * Initialize a new market position with given effect
     *
     * @param number Position's number
     */
    public CouncilPosition(int number) {
        this.number = number;

        HashMap<ResourceType, Integer> bonus = new HashMap<>();
        bonus.put(ResourceType.Gold, 1);
        bonus.put(ResourceType.Favor, 1);

        immediatePositionEffect = new ImmediateEffect(bonus);

    }

    /**
     * Set list to update with order of domestic placement to define next turn playing order
     *
     * @param nextTurnOrder Next turn playing order list
     */
    public void setOrderList(List<FamilyColor> nextTurnOrder) {
        if(turnOrder == null)
            turnOrder = nextTurnOrder;
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

        // Update list for next turn player's order
        turnOrder.add(occupant.getFamilyColor());

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
