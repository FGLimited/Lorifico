package Server.Game.Effects.Faith;

import Game.Cards.CardType;
import Server.Game.Effects.Effect;
import Game.Effects.EffectType;
import Game.UserObjects.PlayerState;

/**
 * Created by fiore on 20/05/2017.
 */
public class CardRemoveEffect extends Effect {

    private final CardType cardType;

    /**
     * Remove all cards of given type to avoid victory points calculation at the end of the game
     *
     * @param cardToRemove Card type to be removed
     */
    public CardRemoveEffect(CardType cardToRemove) {
        super(EffectType.Permanent, 0);
        cardType = cardToRemove;
    }

    @Override
    public boolean canApply(PlayerState currentMove) {
        return true;
    }

    @Override
    public void apply(PlayerState currentMove) {

        if(cardType == CardType.Challenge)
            currentMove.getEffects(EffectType.Final).clear();
        else
            currentMove.getCards(cardType).clear();

    }

}
