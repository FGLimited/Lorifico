package Server.Game.Effects;

import Game.Effects.Effect;
import Game.Effects.EffectType;
import Game.Positions.PositionType;
import Game.Usable.ResourceType;
import Game.UserObjects.PlayerState;
import Server.Game.Usable.UsableHelper;
import java.util.Map;

/**
 * Created by fiore on 21/05/2017.
 */
public class TransformResourcesEffect implements Effect {

    private final EffectType type = EffectType.Activable;

    private final PositionType position = PositionType.ProductionAction;

    private final Map<ResourceType, Integer> requested;

    private final Map<ResourceType, Integer> toAdd;

    private final int value;

    private volatile int cardNumber = 0;

    /**
     * Transform specified resources
     *
     * @param requested Requested resources
     * @param toAdd Transformed resources
     * @param activationValue Activation domestic value
     */
    public TransformResourcesEffect(Map<ResourceType, Integer> requested, Map<ResourceType, Integer> toAdd, int activationValue) {
        this.requested = requested;
        this.toAdd = toAdd;
        value = activationValue;
    }

    @Override
    public EffectType getType() {
        return type;
    }

    @Override
    public boolean canApply(PlayerState currentMove) {
        return currentMove.getInUseDomestic().getValue() >= value && currentMove.getCheckingPositionType() == position;
    }

    @Override
    public void apply(PlayerState currentMove) {

        final Map<ResourceType, Integer> currentResources = currentMove.getResources();

        UsableHelper.editResources(requested, currentResources, false);
        UsableHelper.editResources(toAdd, currentResources, true);

        currentMove.setResources(currentResources, true);
    }

    @Override
    public int getActivationValue() {
        return value;
    }

    @Override
    public int getCardNumber() {
        return cardNumber;
    }

    @Override
    public void setCardNumber(int cardNumber) {
        this.cardNumber = cardNumber;
    }
}
