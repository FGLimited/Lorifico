package Server.Game.Positions;

import Game.Effects.Effect;
import Game.Effects.EffectType;
import Game.Positions.Position;
import Game.Positions.PositionType;
import Game.UserObjects.PlayerState;
import Server.Game.UserObjects.Domestic;
import org.jetbrains.annotations.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by fiore on 11/05/2017.
 */
public class ActionPosition implements Position<Effect> {

    private final int number;

    private final PositionType type = PositionType.Action;

    private final EffectType effectType;

    private final int domesticPenalty;

    private volatile PositionAggregate parent;

    private volatile Domestic occupant;

    public ActionPosition(EffectType effectType, int number, int domesticPenalty) {
        this.effectType = effectType;
        this.number = number;
        this.domesticPenalty = domesticPenalty;
    }

    @Override
    public int compareTo(Position other) {
        return number - other.getNumber();
    }

    @Override
    public void setAggregate(PositionAggregate parent) {
        if(this.parent == null)
            this.parent = parent;
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
    public List<Effect> canOccupy(PlayerState currentState) {

        // Apply position effects and permanent effects
        applyEffects(currentState);

        // If position is occupied or in use domestic has value zero can't occupy
        if(occupant != null && currentState.getInUseDomestic().getValue() == 0)
            return Collections.emptyList();

        // Return list of activable effects
        return currentState.getEffects(effectType).parallelStream()
                .filter(effect -> effect.canApply(currentState))
                .collect(Collectors.toList());
    }

    /**
     * Apply position penalty and player permanent effects to current state
     *
     * @param currentState Current player state
     */
    private void applyEffects(PlayerState currentState) {

        // Get in use domestic
        Domestic inUse = currentState.getInUseDomestic();

        // Update domestic value applying penalty
        inUse.setValue(inUse.getValue() - domesticPenalty);

        // Set current position type
        currentState.setCheckingPositionType(type);

        // Apply all permanent effects
        currentState.getEffects(EffectType.Permanent)
                .forEach(effect -> effect.apply(currentState));
    }

    @Override
    public PlayerState occupy(PlayerState currentState, Effect chosenT) {

        // Apply position effects and permanent effects
        applyEffects(currentState);

        // Set in use domestic as occupant
        occupant = currentState.getInUseDomestic();

        // Apply selected effect to current state
        chosenT.apply(currentState);

        // Return updated state
        return currentState;
    }

    @Override
    public PlayerState occupy(PlayerState currentState, List<Effect> chosenTs) {

        // Apply position effects and permanent effects
        applyEffects(currentState);

        // Set in use domestic as occupant
        occupant = currentState.getInUseDomestic();

        // Apply all chosen effects to current state
        chosenTs.forEach(effect -> effect.apply(currentState));

        // Return updated state
        return currentState;
    }

    @Override
    public @Nullable Domestic isOccupied() {
        return occupant;
    }

    @Override
    public void free() {
        occupant = null;
    }
}
