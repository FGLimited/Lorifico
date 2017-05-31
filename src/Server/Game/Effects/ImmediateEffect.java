package Server.Game.Effects;

import Game.Effects.EffectType;
import Game.Usable.ResourceType;
import Server.Game.Usable.UsableHelper;
import Game.UserObjects.PlayerState;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by fiore on 11/05/2017.
 */
public class ImmediateEffect extends Effect {

    private final Map<ResourceType, Integer> resources;

    /**
     * Initialize an immediate effect with given resources to add
     *
     * @param resources Resource to add on effect activation
     */
    public ImmediateEffect(Map<ResourceType, Integer> resources) {
        super(EffectType.Immediate, 0);
        this.resources = resources == null ? new HashMap<>() : resources;
    }

    @Override
    public boolean canApply(PlayerState currentMove) {
        return true;
    }

    @Override
    public void apply(PlayerState currentMove) {

        // Get current resources
        final Map<ResourceType, Integer> currentResources = currentMove.getResources();

        // Add resources from effect
        UsableHelper.editResources(resources, currentResources, true);

        // Update resources on current state
        currentMove.setResources(currentResources, true);
    }

}
