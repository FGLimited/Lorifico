package Server.Game.Effects.Faith;

import Game.Cards.CardType;
import Game.Effects.Effect;
import Game.Effects.EffectType;
import Game.Usable.ResourceType;
import Game.UserObjects.PlayerState;
import Server.Game.Usable.Cost;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by fiore on 20/05/2017.
 */
public class CardCostPenaltyEffect implements Effect {

    private final EffectType type = EffectType.Permanent;

    private final CardType cardType;

    private final List<ResourceType> resourcesType;

    private final int removedPoints;

    public CardCostPenaltyEffect(List<ResourceType> resources, int pointsToRemove, CardType cardType) {
        this.cardType = cardType;
        resourcesType = resources;
        removedPoints = pointsToRemove;
    }

    private volatile boolean isApplied = false;

    @Override
    public EffectType getType() {
        return type;
    }

    @Override
    public boolean canApply(PlayerState currentMove) {
        return !isApplied;
    }

    @Override
    public void apply(PlayerState currentMove) {

        // Get all costs from given card type
        final List<Cost> cardCosts = new ArrayList<>();
        currentMove.getCards(cardType).forEach(card -> cardCosts.addAll(card.getCosts()));

        final AtomicInteger pointsToRemove = new AtomicInteger(0);

        // Check each cost to find requested resources and add resource value to removed points counter
        cardCosts.forEach(cost -> {

            // Current cost resources
            final Map<ResourceType, Integer> current = cost.getResources();

            // If requested resource is present increment pointsToRemove
            resourcesType.forEach(resource -> {
                if(current.containsKey(resource))
                    pointsToRemove.set(current.get(resource) * removedPoints);
            });

        });

        // Get current user resources
        final Map<ResourceType, Integer> currentResources = currentMove.getResources();

        // Remove victory points
        currentResources.replace(ResourceType.VictoryPoint, currentResources.get(ResourceType.VictoryPoint) - pointsToRemove.get());

        // Update user resources
        currentMove.setResources(currentResources, false);

        isApplied = true;

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
