package Server.Game.Cards;

import Game.Cards.CardType;
import Game.Effects.Effect;
import Game.Usable.ResourceType;
import Game.UserObjects.PlayerState;
import Server.Game.GameHelper;
import Server.Game.Usable.Cost;
import Server.Game.Usable.UsableHelper;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by fiore on 10/05/2017.
 */
public class Card implements Game.Cards.Card {

    private final int number;

    private final String name;

    private final String description;

    private final CardType type;

    private final List<Cost> costs;

    private final List<Effect> effects;

    /**
     * Initialize a new card with given specifications
     *
     * @param type Card type
     * @param cardCosts Available costs for this card
     * @param cardEffects All cards effects (immediate and permanent)
     * @param name Card name
     * @param description Card description
     */
    public Card(CardType type, List<Cost> cardCosts, List<Effect> cardEffects, int number, String name, String description) {
        this.number = number;
        this.name = name;
        this.description = description;
        this.type = type;

        if(cardCosts != null)
            costs = cardCosts;
        else
            costs = Collections.singletonList(new Cost(null));

        // Update associated card numbers
        costs.forEach(cost -> cost.setCardNumber(number));

        if(cardEffects != null)
            effects = cardEffects;
        else
            effects = new ArrayList<>();

        // Update associated card numbers
        effects.forEach(effect -> effect.setCardNumber(number));
    }

    /**
     * Gson constructor
     */
    private Card() {
        number = 0;
        name = null;
        description = null;
        type = null;
        costs = null;
        effects = null;
    }

    @Override
    public CardType getType() {
        return type;
    }

    @Override
    public int getNumber() {
        return number;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public List<Effect> getEffects() {
        return effects;
    }

    @Override
    public List<Cost> getCosts() {
        return costs;
    }

    @Override
    public List<Cost> canBuy(PlayerState currentState) {

        // If card is a territory card check for military points
        if(type == CardType.Territory) {

            // Get required military points to add a new territory card
            Cost militaryPointCost = checkMilitaryPoints(currentState.getCards(type).size() + 1);

            // If user hasn't enough military points he can't buy this card
            if(!militaryPointCost.canBuy(currentState))
                return Collections.emptyList();
        }

        // Apply resource discount for this type of card if any
        final Map<ResourceType, Integer> currentResources = currentState.getResources();
        UsableHelper.editResources(currentState.getCostBonus(type), currentResources, true);
        currentState.setResources(currentResources, false);

        // Return all affordable costs
        return costs
                .parallelStream()
                .filter(cost -> cost.canBuy(currentState))
                .collect(Collectors.toList());
    }

    /**
     * Get military points requirements to get a new territory card
     *
     * @param cardsNumber Number of territory cards already owned by the user plus one
     * @return Military point cost correctly initialized
     */
    private Cost checkMilitaryPoints(int cardsNumber) {

        final int requiredPoints = GameHelper.getInstance().requestedMilitary(cardsNumber);

        // Create point cost with required military points
        // No point has to be removed, but they have to be there when buying the card

        // Return cost with requiring necessary points
        return new Cost(Collections.singletonMap(ResourceType.MilitaryPoint, 0), requiredPoints);
    }

    @Override
    public String toString() {
        return name;
    }
}
