package Server.Game.Positions;

import Game.Cards.Card;
import Game.Cards.CardType;
import Game.Effects.EffectType;
import Game.UserObjects.PlayerState;
import Server.Game.Usable.Cost;
import Game.Effects.Effect;
import Game.Usable.ResourceType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by fiore on 10/05/2017.
 */
public class TowerPosition extends Position<Cost> {

    private final Effect immediatePositionEffect;

    private final int positionValue;

    private final CardType cardType;

    private volatile Card currentCard;

    /**
     * Initialize a new tower position
     *
     * @param immediateEffect Immediate effect activated by domestic in this position
     * @param value Necessary value of the domestic for this position
     * @param cardType Type of card which will be in this position
     * @param number Position id number
     */
    public TowerPosition(Effect immediateEffect, int value, CardType cardType, int number) {
        super(cardType.PositionType, number);
        immediatePositionEffect = immediateEffect;
        positionValue = value;
        this.cardType = cardType;
    }

    /**
     * Gson constructor
     */
    private TowerPosition() {
        immediatePositionEffect = null;
        positionValue = 0;
        cardType = null;
    }

    /**
     * Get position card type
     *
     * @return Position card type
     */
    public Card getCard() {
        return currentCard;
    }

    /**
     * Put given card in this position
     *
     * @param cardToBind Card to put
     * @throws IllegalArgumentException If card type is different from position card type
     */
    public void setCard(Card cardToBind) throws IllegalArgumentException {

        if(cardToBind.getType() != cardType)
            throw new IllegalArgumentException("Wrong card type! Can't put " + currentCard.getType().name() + " card in " + cardType.name() + " position.");

        currentCard = cardToBind;
    }

    @Override
    public List<Cost> canOccupy(PlayerState currentState) {

        // Apply user permanent effects to his current state
        applyEffects(currentState);

        // If position is occupied no one else can be there
        // or if domestic value is too low
        // or if non neutral domestic of same family is already present in the tower
        if(isOccupied() != null
                || currentState.getInUseDomestic().getValue() < positionValue
                || !parent.canOccupy(currentState.getInUseDomestic()))
            return Collections.emptyList();

        final Cost occupiedCost;

        // If tower is already occupied add 3 gold units cost
        if(parent.isOccupied()) {

            // Initialize cost with 3 gold units request
            occupiedCost = new Cost(Collections.singletonMap(ResourceType.Gold, 3));

            // Check if user can afford this
            if(!occupiedCost.canBuy(currentState))
                return Collections.emptyList();

            // If is affordable apply it to current state
            occupiedCost.apply(currentState);
        }
        else
            occupiedCost = null;

        // Check if card in current position is affordable
        List<Cost> affordableCosts = currentCard.canBuy(currentState);

        // If card isn't affordable return false or occupation cost isn't present return card cost only
        if(affordableCosts.isEmpty() || occupiedCost == null)
            return affordableCosts;

        // Else create new cost list
        List<Cost> totalCosts = new ArrayList<>();

        // Add occupation cost to card cost and populate totalCosts list
        affordableCosts.forEach(cardCost -> totalCosts.add(cardCost.sum(occupiedCost, true)));

        // Return total costs' list
        return totalCosts;
    }

    /**
     * Apply player permanent effects to current state
     *
     * @param currentState Current player state
     */
    private void applyEffects(PlayerState currentState) {

        // Set current position type
        currentState.setCheckingPositionType(this.getType());

        // Apply all permanent effects
        currentState.getEffects(EffectType.Permanent)
                .forEach(effect -> effect.apply(currentState));
    }

    @Override
    public PlayerState occupy(PlayerState currentState, List<Cost> chosenCosts) {
        super.occupy(currentState, chosenCosts);

        // Apply user permanent effects to his current state
        applyEffects(currentState);

        // Apply position and card cost chosen
        chosenCosts.get(0).apply(currentState);

        // Apply position immediate effect
        if(immediatePositionEffect != null)
            immediatePositionEffect.apply(currentState);

        // Add card to user state
        currentState.addCard(currentCard);

        // Remove card from position
        currentCard = null;

        // Return updated player state
        return currentState;
    }

}
