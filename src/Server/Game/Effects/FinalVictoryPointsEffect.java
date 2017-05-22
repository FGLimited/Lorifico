package Server.Game.Effects;

import Game.Effects.Effect;
import Game.Effects.EffectType;
import Game.Usable.ResourceType;
import Game.UserObjects.PlayerState;
import java.util.Map;

/**
 * Created by fiore on 22/05/2017.
 */
public class FinalVictoryPointsEffect implements Effect {

    private final EffectType type = EffectType.Final;

    private final int quantity;

    /**
     * Give specified victory points quantity at the end of the game
     *
     * @param quantity Victory points to add
     */
    public FinalVictoryPointsEffect(int quantity) {
        this.quantity = quantity;
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

        currentResources.replace(ResourceType.VictoryPoint, currentResources.get(ResourceType.VictoryPoint) + quantity);

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
