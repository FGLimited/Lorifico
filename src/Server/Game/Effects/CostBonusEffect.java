package Server.Game.Effects;

import Game.Cards.CardType;
import Game.Effects.EffectType;
import Game.Usable.ResourceType;
import Game.UserObjects.PlayerState;
import java.util.Map;

/**
 * Created by fiore on 21/05/2017.
 */
public class CostBonusEffect extends Effect {

    private final CardType cardType;

    private final Map<ResourceType, Integer> bonus;

    private volatile boolean isApplied = false;

    /**
     * Get resource discount on specified type of card
     *
     * @param cardType Card type
     * @param bonus Resources bonuses
     */
    public CostBonusEffect(CardType cardType, Map<ResourceType, Integer> bonus) {
        super(EffectType.Permanent, 0);
        this.cardType = cardType;
        this.bonus = bonus;
    }

    /**
     * Gson constructor
     */
    private CostBonusEffect() {
        cardType = null;
        bonus = null;
    }

    @Override
    public boolean canApply(PlayerState currentMove) {
        return !isApplied;
    }

    @Override
    public void apply(PlayerState currentMove) {

        if (isApplied)
            return;

        bonus.forEach((resource, quantity) -> currentMove.setCostBonus(cardType, resource, quantity));

        isApplied = true;
    }

}
