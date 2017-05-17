package Game.UserObjects;

import Game.Cards.Card;
import Game.Cards.CardType;
import Game.Effects.Effect;
import Game.Effects.EffectType;
import Game.Positions.PositionType;
import Game.Usable.ResourceType;
import Networking.CommLink;
import Server.Game.UserObjects.Domestic;
import java.util.List;
import java.util.Map;

/**
 * Created by fiore on 08/05/2017.
 */
public interface PlayerState extends Cloneable {

    /**
     * Get domestic chosen from the player
     *
     * @return Currently in use domestic
     */
    Domestic getInUseDomestic();

    /**
     * Get current position type being checked for occupation availability
     *
     * @return Checking position type
     */
    PositionType getCheckingPositionType();

    /**
     * Set position type to be checked for occupation availability
     *
     * @param currentCheckingType Current position type to be checked
     */
    void setCheckingPositionType(PositionType currentCheckingType);

    /**
     * Get all currently available resources
     *
     * @return Copy of list of resources
     */
    Map<ResourceType, Integer> getResources();

    /**
     * Update current resources
     *
     * @param updatedResources Updated list of resources
     * @param added True if resources have been added (resource penalty is applied), false else (no penalty applied)
     */
    void setResources(Map<ResourceType, Integer> updatedResources, boolean added);

    /**
     * Apply a penalty of specified quantity on every resource update
     *
     * @param type Type of resource to penalize
     * @param quantity Resource quantity to be removed
     */
    void setPenalty(ResourceType type, int quantity);

    /**
     * Get number of slaves requested to increment domestic value by one
     *
     * @return Number of requested slaves
     */
    int getSlavePerDomesticValue();

    /**
     * Set how many slaves to be used to increment domestic value by one
     *
     * @param slaveNumber Slaves necessary to increment domestic value
     */
    void setSlavePerDomesticValue(int slaveNumber);

    /**
     * Get all effects of requested type
     *
     * @param type Type of effects
     * @return List of all user's effect of requested type
     */
    List<Effect> getEffects(EffectType type);

    /**
     * Add an effect to effects list
     *
     * @param newEffect New effect to add
     */
    void addEffect(Effect newEffect);

    /**
     * Add new card to user's card (card effects will be added to effects list and immediate effect will be applied)
     *
     * @param newCard New card to add
     */
    void addCard(Card newCard);

    /**
     * Get number of owned cards of specified type
     *
     * @param type Card's type
     * @return Number of cards owned by the user
     */
    int getCardsCount(CardType type);

    /**
     * Get user's comm object
     *
     * @return User's comm link
     */
    GameUser getGameUser();

    /**
     * Clone this instance
     *
     * @return Cloned instance
     */
    PlayerState clone();

}
