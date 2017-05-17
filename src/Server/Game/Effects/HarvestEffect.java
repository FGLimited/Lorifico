package Server.Game.Effects;

import Game.Effects.Effect;
import Game.Effects.EffectType;
import Game.Usable.ResourceType;
import Server.Game.Usable.UsableHelper;
import Game.UserObjects.PlayerState;
import java.util.Map;

/**
 * Created by fiore on 10/05/2017.
 */
public class HarvestEffect implements Effect {

    private final EffectType type = EffectType.Harvest;

    private final Map<ResourceType, Integer> resources;

    private final int activationValue;

    private volatile int cardNumber;

    /**
     * Initialize new harvesting effect
     *
     * @param resources Resources to add
     * @param activationValue Necessary domestic value for activation
     */
    public HarvestEffect(Map<ResourceType, Integer> resources, int activationValue) {
        this.resources = resources;
        this.activationValue = activationValue;
    }

    @Override
    public EffectType getType() {
        return type;
    }

    @Override
    public boolean canApply(PlayerState currentMove) {
        return currentMove.getInUseDomestic().getValue() >= activationValue;
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

    @Override
    public int getActivationValue() {
        return activationValue;
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
