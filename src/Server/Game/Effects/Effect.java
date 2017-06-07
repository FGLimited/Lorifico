package Server.Game.Effects;

import Game.Effects.EffectType;

/**
 * Created by fiore on 31/05/2017.
 */
public abstract class Effect implements Game.Effects.Effect {

    private final EffectType type;

    private volatile int cardNumber;

    final int activationValue;

    protected Effect(EffectType type, int activationValue) {
        this.type = type;
        this.activationValue = activationValue;
    }

    protected Effect() {
        type = null;
        activationValue = 0;
    }

    @Override
    public EffectType getType() {
        return type;
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
