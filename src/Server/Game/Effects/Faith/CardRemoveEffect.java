package Server.Game.Effects.Faith;

import Game.Cards.CardType;
import Game.Effects.Effect;
import Game.Effects.EffectType;
import Game.UserObjects.PlayerState;

/**
 * Created by fiore on 20/05/2017.
 */
public class CardRemoveEffect implements Effect {

    private final EffectType type = EffectType.Permanent;

    private final CardType cardType;

    /**
     * Remove all cards of given type to avoid victory points calculation at the end of the game
     *
     * @param cardToRemove Card type to be removed
     */
    public CardRemoveEffect(CardType cardToRemove) {
        cardType = cardToRemove;
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

        currentMove.getCards(cardType).clear();

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
