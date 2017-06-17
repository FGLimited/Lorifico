package Server.Game.Positions;

import Game.Effects.Effect;
import Game.Effects.EffectType;
import Game.Positions.PositionType;
import Game.UserObjects.PlayerState;
import Server.Game.UserObjects.Domestic;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by fiore on 11/05/2017.
 */
public class ActionPosition extends Position<Effect> {

    private final int domesticPenalty;

    /**
     * Initialize an action position (Harvest or Production)
     *
     * @param positionType Harvest or Production
     * @param number Position number
     * @param domesticPenalty Penalty to apply to domestic value in this position
     */
    public ActionPosition(PositionType positionType, int number, int domesticPenalty) {
        super(positionType, number);
        this.domesticPenalty = domesticPenalty;
    }

    /**
     * Gson constructor
     */
    private ActionPosition() {
        domesticPenalty = 0;
    }

    @Override
    public List<Effect> canOccupy(PlayerState currentState) {

        // Apply position effects and permanent effects
        applyEffects(currentState);

        // If position is occupied or in use domestic has value zero can't occupy
        if(isOccupied() != null && currentState.getInUseDomestic().getValue() == 0)
            return Collections.emptyList();

        // Return list of activable effects
        return currentState.getEffects(EffectType.Activable).parallelStream()
                .filter(effect -> effect.canApply(currentState))
                .collect(Collectors.toList());
    }

    @Override
    public PlayerState occupy(PlayerState currentState, List<Effect> chosenTs) {
        super.occupy(currentState, chosenTs);

        // Apply position effects and permanent effects
        applyEffects(currentState);

        // Apply all chosen effects to current state
        chosenTs.forEach(effect -> effect.apply(currentState));

        // Return updated state
        return currentState;
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
        currentState.setCheckingPositionType(this.getType());

        // Apply all permanent effects
        currentState.getEffects(EffectType.Permanent)
                .forEach(effect -> {
                    if(effect.canApply(currentState))
                        effect.apply(currentState);
                });
    }

}
