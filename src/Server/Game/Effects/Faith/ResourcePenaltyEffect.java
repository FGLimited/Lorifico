package Server.Game.Effects.Faith;

import Server.Game.Effects.Effect;
import Game.Effects.EffectType;
import Game.Usable.ResourceType;
import Game.UserObjects.PlayerState;
import Server.Game.Usable.UsableHelper;
import java.util.Map;

/**
 * Created by fiore on 19/05/2017.
 */
public class ResourcePenaltyEffect extends Effect {

    private final Map<ResourceType, Integer> resources;

    private volatile boolean isApplied = false;

    /**
     * Apply a permanent penalty to specified resources
     * Each time a resource is added the specified value is removed
     *
     * @param penalties Penalty for resource
     */
    public ResourcePenaltyEffect(Map<ResourceType, Integer> penalties) {
        super(EffectType.Permanent, 0);
        resources = UsableHelper.cloneMap(penalties);
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

}
