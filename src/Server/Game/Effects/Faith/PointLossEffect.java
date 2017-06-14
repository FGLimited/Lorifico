package Server.Game.Effects.Faith;

import Server.Game.Effects.Effect;
import Game.Effects.EffectType;
import Game.Usable.ResourceType;
import Game.UserObjects.PlayerState;
import java.util.Map;

/**
 * Created by fiore on 20/05/2017.
 */
public class PointLossEffect extends Effect {

    private final Map<ResourceType, Integer> requestedResources;

    private final int removedPoints;

    private volatile boolean isApplied = false;

    /**
     * Remove 'removedValue' victory points for each quantity of specified resources in player state
     *
     * @param requestedResources Map of resources and necessary quantity to love 'removedValue' victory points
     * @param removedValue Victory point loss for each resource present
     */
    public PointLossEffect(Map<ResourceType, Integer> requestedResources, int removedValue) {
        super(EffectType.Permanent, 0);
        this.requestedResources = requestedResources;
        removedPoints = removedValue;
    }

    @Override
    public boolean canApply(PlayerState currentMove) {
        return !isApplied;
    }

    @Override
    public void apply(PlayerState currentMove) {

        if(isApplied)
            return;

        // Get current resources
        Map<ResourceType, Integer> currentResources = currentMove.getResources();

        // Calculate how many victory points to remove
        int pointsToRemove = 0;

        for (Map.Entry<ResourceType, Integer> resource: requestedResources.entrySet())
            pointsToRemove += (currentResources.get(resource.getKey()) / resource.getValue()) * removedPoints;


        // Remove victory points
        currentResources.replace(ResourceType.VictoryPoint, currentResources.get(ResourceType.VictoryPoint) - pointsToRemove);

        // Set resources in current state
        currentMove.setResources(currentResources, false);

        isApplied = true;
    }

}
