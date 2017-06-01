package Server.Game;

import Game.Cards.CardType;
import Game.Effects.Effect;
import Game.Usable.ResourceType;
import Game.UserObjects.GameUser;
import Logging.Logger;
import Networking.Gson.MySerializer;
import Server.Game.UserObjects.PlayerState;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * Created by fiore on 24/05/2017.
 */
public class GameHelper {

    private final Map<Integer, Integer> militaryTerritory = null;

    private final Map<CardType, Map<Integer, Integer>> victoryForCards = null;

    private final Map<Integer, Integer> faithWayBonus = null;

    private final Map<Integer, Integer> militaryWayBonus = null;

    private final List<Effect> councilFavors = null;

    private final PlayerState initialState = null;

    private static GameHelper gameHelper = null;

    public static GameHelper getInstance() {
        return gameHelper;
    }

    static {

        // Try loading cardHelper.json file from application resources
        BufferedReader jsonInputStream;

        try {
            jsonInputStream = Files.newBufferedReader(Paths.get("src/Server/Game/UserObjects/Serialize/gameHelper.json"));
        } catch (IOException ioe) {
            Logger.log(Logger.LogLevel.Error, "Can't load card helper.\n" + ioe.getMessage());
            throw new RuntimeException("Application file not found!");
        }

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Effect.class, new MySerializer<Effect>())
                .create();

        gameHelper = gson.fromJson(jsonInputStream, GameHelper.class);

    }

    /**
     * Get requested military points to own specified number of territory cards
     *
     * @param numberOfCards Number of territory cards
     * @return Requested military points number
     */
    public int requestedMilitary(int numberOfCards) {
        if(!militaryTerritory.containsKey(numberOfCards))
            return Collections.max(militaryTerritory.values());

        return militaryTerritory.get(numberOfCards);
    }

    /**
     * Get victory points to add at the end of the game for the specified number of cards of given type
     *
     * @param cardType Type of cards
     * @param numberOfCards Number of owned cards
     * @return Victory points to add
     */
    public int victoryForCards(CardType cardType, int numberOfCards) {

        if(victoryForCards.containsKey(cardType) && numberOfCards > 2) {

            Map<Integer, Integer> vicForCard = victoryForCards.get(cardType);

            if(vicForCard.containsKey(numberOfCards))
                return vicForCard.get(numberOfCards);

            return Collections.max(vicForCard.values());
        }

        return 0;
    }

    /**
     * Get victory points bonus for current position on faith way
     *
     * @param faithPoints Number of faith points
     * @return Victory points bonus
     */
    public int getFaithBonus(int faithPoints) {
        return faithWayBonus.get(faithPoints);
    }

    /**
     * Get victory points bonus for military way placement
     *
     * @param playerPosition Placement relative to other players on military way
     * @return Victory points bonus
     */
    public int getMilitaryBonus(int playerPosition) {
        if(militaryWayBonus.containsKey(playerPosition))
            return militaryWayBonus.get(playerPosition);

        return 0;
    }

    /**
     * Get list of available council favors
     *
     * @return List of effects
     */
    public List<Effect> getCouncilFavors() {
        return new ArrayList<>(councilFavors);
    }

    /**
     * Get initialize player state bound to given user
     *
     * @param boundUser User to bind to new player state
     * @return Player state initialized for game start
     */
    public Game.UserObjects.PlayerState getInitialPS(GameUser boundUser, int bonusGold) {
        final PlayerState newState = (PlayerState) initialState.clone();

        bonusGold = newState.getResources().get(ResourceType.Gold) + bonusGold;

        newState.setResources(Collections.singletonMap(ResourceType.Gold, bonusGold), false);

        try {
            Field gameUser = PlayerState.class.getDeclaredField("gameUser");
            gameUser.setAccessible(true);

            gameUser.set(newState, boundUser);

        } catch (NoSuchFieldException | IllegalAccessException e) {
            Logger.log(Logger.LogLevel.Error, "Unexpected field access error on PlayerState.\n" + e.getMessage());
        }

        return newState;
    }

}
