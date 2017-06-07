package Server.Game.Positions;

import Game.Positions.PositionType;
import Game.UserObjects.PlayerState;
import Server.Game.UserObjects.Domestic;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.Collections;
import java.util.List;

/**
 * Created by fiore on 31/05/2017.
 */
public abstract class Position<T> implements Game.Positions.Position<T> {

    private final PositionType type;

    private final int number;

    transient volatile PositionAggregate parent = null;

    volatile Domestic occupant = null;

    protected Position(PositionType type, int number) {
        this.type = type;
        this.number = number;
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
    public PlayerState occupy(PlayerState currentState, T chosenT) {
        return occupy(currentState, Collections.singletonList(chosenT));
    }

    @Override
    public PlayerState occupy(PlayerState currentState, List<T> chosenTs) {
        // Set occupant to in use domestic
        occupant = currentState.getInUseDomestic();

        occupant.setInPosition(true);

        return currentState;
    }

    @Nullable
    @Override
    public Domestic isOccupied() {
        return occupant;
    }

    @Override
    public void free() {

        if(occupant == null)
            return;

        occupant.setInPosition(false);
        occupant = null;
    }

    @Override
    public void setAggregate(PositionAggregate parent) {
        if(this.parent == null)
            this.parent = parent;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Position && number == ((Position)o).getNumber();
    }
}
