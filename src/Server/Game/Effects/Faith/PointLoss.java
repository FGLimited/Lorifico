package Server.Game.Effects.Faith;

import Game.Effects.Effect;
import Game.Effects.EffectType;
import Game.Usable.ResourceType;
import Game.UserObjects.PlayerState;
import java.util.Map;

/**
 * Created by fiore on 20/05/2017.
 */
public class PointLoss implements Effect {

    private final EffectType type = EffectType.Permanent;

    private final Map<ResourceType, Integer> requestedResources;

    private final int removedPoints;

    private volatile boolean isApplied = false;

    /**
     * Remove 'removedValue' victory points for each quantity of of specified resources in player state
     *
     * @param requestedResources Map of resources and necessary quantity to love 'removedValue' victory points
     * @param removedValue Victory point loss for each resource present
     */
    public PointLoss(Map<ResourceType, Integer> requestedResources, int removedValue) {
        this.requestedResources = requestedResources;
        removedPoints = removedValue;
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

        if(isApplied)
            return;

        // Get current resources
        final Map<ResourceType, Integer> currentResources = currentMove.getResources();

        // Calculate how many victory points to remove
        int pointsToRemove = 0;

        for (Map.Entry<ResourceType, Integer> resource: requestedResources.entrySet())
            pointsToRemove += (currentResources.get(resource.getKey()) / resource.getValue()) * pointsToRemove;


        // Remove victory points
        currentResources.replace(ResourceType.VictoryPoint, currentResources.get(ResourceType.VictoryPoint) - pointsToRemove);

        // Set resources in current state
        currentMove.setResources(currentResources, false);

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
