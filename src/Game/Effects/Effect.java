package Game.Effects;

import Game.HasType;
import Game.UserObjects.Chosable;
import Game.UserObjects.PlayerState;

/**
 * Created by fiore on 10/05/2017.
 */
public interface Effect extends HasType<EffectType>, Chosable {

    /**
     * Check if this effect can be activated placing in use domestic in correct position
     *
     * @param currentMove Current player state
     * @return True if applicable, false else
     */
    boolean canApply(PlayerState currentMove);

    /**
     * Apply thi effect to player state (no check if performed on canApply)
     *
     * @param currentMove Current player state
     */
    void apply(PlayerState currentMove);

    /**
     * Get effect activation value if harvest or production effect
     *
     * @return Activation value
     */
    int getActivationValue();

    /**
     * Get associated card number
     *
     * @return Card number
     */
    int getCardNumber();

    /**
     * Set associated card number
     *
     * @param cardNumber Card number
     */
    void setCardNumber(int cardNumber);

}
