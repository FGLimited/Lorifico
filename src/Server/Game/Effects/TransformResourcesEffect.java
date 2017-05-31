package Server.Game.Effects;

import Game.Effects.EffectType;
import Game.Positions.PositionType;
import Game.Usable.ResourceType;
import Game.UserObjects.PlayerState;
import Server.Game.Usable.UsableHelper;
import java.util.Map;

/**
 * Created by fiore on 21/05/2017.
 */
public class TransformResourcesEffect extends Effect {

    private final PositionType position = PositionType.ProductionAction;

    private final Map<ResourceType, Integer> requested;

    private final Map<ResourceType, Integer> toAdd;

    /**
     * Transform specified resources
     *
     * @param requested Requested resources
     * @param toAdd Transformed resources
     * @param activationValue Activation domestic value
     */
    public TransformResourcesEffect(Map<ResourceType, Integer> requested, Map<ResourceType, Integer> toAdd, int activationValue) {
        super(EffectType.Activable, activationValue);
        this.requested = requested;
        this.toAdd = toAdd;
    }

    @Override
    public boolean canApply(PlayerState currentMove) {
        return currentMove.getInUseDomestic().getValue() >= activationValue && currentMove.getCheckingPositionType() == position;
    }

    @Override
    public void apply(PlayerState currentMove) {

        final Map<ResourceType, Integer> currentResources = currentMove.getResources();

        UsableHelper.editResources(requested, currentResources, false);
        UsableHelper.editResources(toAdd, currentResources, true);

        currentMove.setResources(currentResources, true);
    }

}
