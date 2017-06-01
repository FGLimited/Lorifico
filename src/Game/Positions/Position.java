package Game.Positions;

import Game.UserObjects.PlayerState;
import Server.Game.Positions.PositionAggregate;
import Server.Game.UserObjects.Domestic;
import Game.HasType;
import org.jetbrains.annotations.Nullable;
import java.util.List;

/**
 * Created by fiore on 10/05/2017.
 */
public interface Position<T> extends HasType<PositionType> {

    /**
     * Get position number
     *
     * @return Position number
     */
    int getNumber();

    /**
     * Check if a user in his current state can occupy this position
     *
     * @param currentState Current player state
     * @return List of affordable costs/activable effects for given user on this position; empty list if can't occupy position
     */
    List<T> canOccupy(PlayerState currentState);

    /**
     * Place active domestic in this position activating all costs and effects relative to it
     * (No check about canOccupy is performed, if canOccupy returned false wrong result will come)
     *
     * @param currentState Current user state to modify
     * @param chosenT If T is a Cost is the chosen cost to buy this position, if T is an Effect is the effect to activate
     * @return Updated user state
     */
    PlayerState occupy(PlayerState currentState, T chosenT);

    /**
     * Place active domestic in this position activating all costs and effects relative to it
     * (No check about canOccupy is performed, if canOccupy returned false wrong result will come)
     *
     * @param currentState Current user state to modify
     * @param chosenTs List of effects to activate occupying this position (if Cost list is given only the first is used)
     * @return Updated user state
     */
    PlayerState occupy(PlayerState currentState, List<T> chosenTs);

    /**
     * Get currently occupying
     *
     * @return Domestic color if occupied, null if free
     */
    @Nullable Domestic isOccupied();

    /**
     * Remove domestic from position if present
     */
    void free();

    /**
     * Set aggregate containing this position
     *
     * @param parent Parent container
     */
    void setAggregate(PositionAggregate parent);

    /**
     * Check position number only
     *
     * @param o Position to be compared
     * @return True if positions have same number, false else
     */
    @Override
    boolean equals(Object o);

}
