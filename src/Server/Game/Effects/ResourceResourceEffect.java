package Server.Game.Effects;

import Game.Effects.Effect;
import Game.Effects.EffectType;
import Game.Usable.ResourceType;
import Game.UserObjects.PlayerState;

import java.util.Map;

/**
 * Created by fiore on 21/05/2017.
 */
public class ResourceResourceEffect implements Effect {

    private final EffectType type = EffectType.Immediate;

    private final ResourceType fromResource;

    private final int fromQuantity;

    private final ResourceType toResource;

    private final int toQuantity;

    public ResourceResourceEffect(ResourceType from, int fromQuantity, ResourceType to, int toQuantity) {
        fromResource = from;
        this.fromQuantity = fromQuantity;
        toResource = to;
        this.toQuantity = toQuantity;
    }

    @Override
    public EffectType getType() {
        return type;
    }

    @Override
    public boolean canApply(PlayerState currentMove) {
        return true;
    }

    @Override
    public void apply(PlayerState currentMove) {

        final Map<ResourceType, Integer> currentResources = currentMove.getResources();

        int toAdd = (currentResources.get(fromResource) / fromQuantity) * toQuantity;

        currentResources.replace(toResource, currentResources.get(toResource) + toAdd);

        currentMove.setResources(currentResources, true);

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
