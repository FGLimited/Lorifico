package Server.Game.Effects.Faith;

import Game.Effects.Effect;
import Game.Effects.EffectType;
import Game.Usable.ResourceType;
import Game.UserObjects.PlayerState;
import Server.Game.Usable.UsableHelper;
import java.util.Map;

/**
 * Created by fiore on 19/05/2017.
 */
public class ResourcePenaltyEffect implements Effect {

    private final EffectType type = EffectType.Permanent;

    private final Map<ResourceType, Integer> resources;

    private volatile boolean isApplied = false;

    public ResourcePenaltyEffect(Map<ResourceType, Integer> penalties) {
        resources = UsableHelper.cloneMap(penalties);
    }


    @Override
    public EffectType getType() {
        return type;
    }

    @Override
    public boolean canApply(PlayerState currentMove) {
        return !isApplied;
    }

    @Override
    public void apply(PlayerState currentMove) {

        // If already applied return
        if(isApplied)
            return;

        // Add penalties to user state
        resources.forEach(currentMove::setPenalty);

        // Set effect applied to avoid multiple execution
        isApplied = true;

    }

    @Override
    public int getActivationValue() {
        return 0;
    }

    @Override
    public int getCardNumber() {
        return 0;
    }

    @Override
    public void setCardNumber(int cardNumber) {

    }
}
