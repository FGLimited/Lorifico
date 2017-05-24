package Server.Game.UserObjects;

import Game.Cards.Card;
import Game.Cards.CardType;
import Game.Effects.Effect;
import Game.Effects.EffectType;
import Game.Positions.PositionType;
import Game.Usable.ResourceType;
import Server.Game.Usable.UsableHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by fiore on 16/05/2017.
 */
public class PlayerState implements Game.UserObjects.PlayerState {

    private final Map<ResourceType, Integer> resources;

    private final Map<ResourceType, Integer> resourcesPenalty;

    private final Map<CardType, Map<ResourceType, Integer>> resourceBonus = new HashMap<>();

    private final Map<EffectType, List<Effect>> effects = new HashMap<>();

    private final Map<CardType, List<Card>> cards = new HashMap<>();

    private final transient GameUser gameUser;

    private volatile Domestic inUseDomestic = null;

    private volatile PositionType checkingPosition = null;

    private volatile int slavePerDomestic = 1;

    /**
     * Initialize a new player state with specified comm link to send effects callbacks
     *
     * @param gameUser Bound user
     */
    public PlayerState(GameUser gameUser) {

        resources = new HashMap<>();

        for (ResourceType type : ResourceType.values())
            resources.put(type, 0);

        resourcesPenalty = UsableHelper.cloneMap(resources);

        for (EffectType type : EffectType.values())
            effects.put(type, new ArrayList<>());

        for (CardType type : CardType.values()) {
            cards.put(type, new ArrayList<>());
            resourceBonus.put(type, UsableHelper.cloneMap(resources));
        }

        this.gameUser = gameUser;
    }

    /**
     * Clone constructor
     *
     * @param toClone Instance to clone
     */
    private PlayerState(PlayerState toClone) {

        resources = UsableHelper.cloneMap(toClone.resources);
        resourcesPenalty = UsableHelper.cloneMap(toClone.resourcesPenalty);
        toClone.resourceBonus.forEach((key, resources) ->
                resourceBonus.put(key, UsableHelper.cloneMap(resources)));

        effects.putAll(toClone.effects);
        cards.putAll(toClone.cards);
        gameUser = toClone.gameUser;
        inUseDomestic = toClone.inUseDomestic;
        checkingPosition = toClone.checkingPosition;
        slavePerDomestic = toClone.slavePerDomestic;

    }

    @Override
    public Domestic getInUseDomestic() {
        return inUseDomestic;
    }

    @Override
    public void setInUseDomestic(Domestic inUse) {
        inUseDomestic = inUse;
    }

    @Override
    public PositionType getCheckingPositionType() {
        return checkingPosition;
    }

    @Override
    public void setCheckingPositionType(PositionType currentCheckingType) {
        checkingPosition = currentCheckingType;
    }

    @Override
    public Map<ResourceType, Integer> getResources() {
        return UsableHelper.cloneMap(resources);
    }

    @Override
    public void setResources(Map<ResourceType, Integer> updatedResources, boolean applyPenalty) {

        // Update each resource value (if resource has been added remove penalty)
        updatedResources.forEach((type, value) -> {
            if (resources.get(type) < value + (applyPenalty ? resourcesPenalty.get(type) : 0))
                resources.replace(type, value - (applyPenalty ? resourcesPenalty.get(type) : 0));

            if(resources.get(type) > value)
                resources.replace(type, Integer.valueOf(value));
        });
    }

    @Override
    public void setPenalty(ResourceType type, int quantity) {
        resourcesPenalty.replace(type, quantity);
    }

    @Override
    public int getSlavePerDomesticValue() {
        return slavePerDomestic;
    }

    @Override
    public void setSlavePerDomesticValue(int slaveNumber) {
        slavePerDomestic = slaveNumber;
    }

    @Override
    public List<Effect> getEffects(EffectType type) {
        return effects.get(type);
    }

    @Override
    public void addEffect(Effect newEffect) {
        effects.get(newEffect.getType()).add(newEffect);
    }

    @Override
    public void addCard(Card newCard) {

        // Add card to correct card list
        cards.get(newCard.getType()).add(newCard);

        // Add card effects to relative effects list
        newCard.getEffects().forEach(effect -> effects.get(effect.getType()).add(effect));

        // Apply immediate effects and remove them from list
        effects.get(EffectType.Immediate).forEach(effect -> effect.apply(this));
        effects.get(EffectType.Immediate).clear();
    }

    @Override
    public List<Card> getCards(CardType type) {
        return cards.get(type);
    }

    @Override
    public void setCostBonus(CardType type, ResourceType resourceType, int quantity) {
        resourceBonus.get(type).replace(resourceType, quantity);
    }

    @Override
    public Map<ResourceType, Integer> getCostBonus(CardType type) {
        return resourceBonus.get(type);
    }

    @Override
    public GameUser getGameUser() {
        return gameUser;
    }

    @Override
    public Game.UserObjects.PlayerState clone() {
        return new PlayerState(this);
    }
}
