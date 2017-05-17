package Server.Game.UserObjects;

import Game.Cards.Card;
import Game.Cards.CardType;
import Game.Effects.Effect;
import Game.Effects.EffectType;
import Game.Positions.PositionType;
import Game.Usable.ResourceType;
import Networking.CommLink;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by fiore on 16/05/2017.
 */
public class PlayerState implements Game.UserObjects.PlayerState {

    private final Map<ResourceType, Integer> resources = new HashMap<>();

    private final Map<ResourceType, Integer> resourcesPenalty;

    private final Map<EffectType, List<Effect>> effects = new HashMap<>();

    private final Map<CardType, List<Card>> cards = new HashMap<>();

    private final GameUser gameUser;

    private volatile Domestic inUseDomestic = null;

    private volatile PositionType checkingPosition = null;

    private volatile int slavePerDomestic = 1;

    /**
     * Initialize a new player state with specified comm link to send effects callbacks
     *
     * @param gameUser Bound user
     */
    public PlayerState(GameUser gameUser) {

        for (ResourceType type : ResourceType.values())
            resources.put(type, 0);

        resourcesPenalty = new HashMap<>(resources);

        for (EffectType type : EffectType.values())
            effects.put(type, new ArrayList<>());

        for (CardType type : CardType.values())
            cards.put(type, new ArrayList<>());

        this.gameUser = gameUser;
    }

    /**
     * Clone constructor
     *
     * @param toClone Instance to clone
     */
    private PlayerState(PlayerState toClone) {

        resources.putAll(toClone.resources);
        resourcesPenalty = toClone.resourcesPenalty;
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
    public PositionType getCheckingPositionType() {
        return checkingPosition;
    }

    @Override
    public void setCheckingPositionType(PositionType currentCheckingType) {
        checkingPosition = currentCheckingType;
    }

    @Override
    public Map<ResourceType, Integer> getResources() {
        return new HashMap<>(resources);
    }

    @Override
    public void setResources(Map<ResourceType, Integer> updatedResources, boolean added) {

        // Update each resource value (if resource has been added remove penalty)
        updatedResources.forEach((type, value) -> resources.replace(type, value - (added ? resourcesPenalty.get(type) : 0)));
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
    public int getCardsCount(CardType type) {
        return cards.get(type).size();
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
