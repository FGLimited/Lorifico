package Server.Game.Effects;

import Game.Cards.CardType;
import Game.Effects.Effect;
import Game.Effects.EffectType;
import Game.Positions.PositionType;
import Game.Usable.ResourceType;
import Game.UserObjects.PlayerState;
import java.util.Map;

/**
 * Created by fiore on 21/05/2017.
 */
public class CardResourceEffect implements Effect {

    private final EffectType type;

    private final PositionType position;

    private final CardType cardType;

    private final ResourceType resource;

    private final int quantity;

    private final int value;

    private volatile int cardNumber = 0;

    /**
     * Get specified quantity of a resource for each card of given type (Activable effect)
     *
     * @param cardType Type of card
     * @param resource Type of resource
     * @param quantity Quantity of resource for each card
     * @param activationValue Effect activation value
     * @param position Activation position
     */
    public CardResourceEffect(CardType cardType, ResourceType resource, int quantity, int activationValue, PositionType position) {
        type = EffectType.Activable;
        this.position = position;
        this.cardType = cardType;
        this.resource = resource;
        this.quantity = quantity;
        value = activationValue;
    }

    /**
     * Get specified quantity of a resource for each card of given type (Immediate effect)
     *
     * @param cardType Type of card
     * @param resource Type of resource
     * @param quantity Resource quantity for each card of given type
     */
    public CardResourceEffect(CardType cardType, ResourceType resource, int quantity) {
        type = EffectType.Immediate;
        this.position = null;
        this.cardType = cardType;
        this.resource = resource;
        this.quantity = quantity;
        value = 0;
    }

    @Override
    public EffectType getType() {
        return type;
    }

    @Override
    public boolean canApply(PlayerState currentMove) {
        return position == null || (currentMove.getInUseDomestic().getValue() >= value && currentMove.getCheckingPositionType() == position);
    }

    @Override
    public void apply(PlayerState currentMove) {

        final Map<ResourceType, Integer> currentResources = currentMove.getResources();

        int ownedCards = currentMove.getCards(cardType).size();

        currentResources.replace(resource, currentResources.get(resource) + (ownedCards * quantity));

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
