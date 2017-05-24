package Server.Game;

import Game.Cards.CardType;
import Game.Effects.Effect;
import Game.UserObjects.GameUser;
import Logging.Logger;
import Networking.Gson.MySerializer;
import Server.Game.UserObjects.PlayerState;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Map;

/**
 * Created by fiore on 24/05/2017.
 */
public class GameHelper {

    private static final Map<Integer, Integer> militaryTerritory;

    private static final Map<CardType, Map<Integer, Integer>> victoryForCards;

    private static final Map<Integer, Integer> faithWayBonus;

    private static final Map<Integer, Integer> militaryWayBonus;

    private static final PlayerState initialState;

    static {

        // Try loading cardHelper.json file from application resources
        InputStream jsonInputStream;

        try {
            jsonInputStream = Files.newInputStream(Paths.get("src/Server/Game/UserObjects/Serialize/gameHelper.json"));
        } catch (IOException ioe) {
            Logger.log(Logger.LogLevel.Error, "Can't load card helper.\n" + ioe.getMessage());
            throw new RuntimeException("Application file not found!");
        }

        // Read json object from file
        JsonObject cardHelperJson = new JsonParser()
                .parse(new InputStreamReader(jsonInputStream))
                .getAsJsonObject();

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Effect.class, new MySerializer<Effect>())
                .create();
        Type intintMapType = new TypeToken<Map<Integer, Integer>>(){}.getType();
        Type victorCardsType = new TypeToken<Map<CardType, Map<Integer, Integer>>>(){}.getType();

        // Load maps from json object
        militaryTerritory = gson.fromJson(cardHelperJson.getAsJsonObject("militaryTerritory"), intintMapType);
        victoryForCards = gson.fromJson(cardHelperJson.getAsJsonObject("victoryForCards"), victorCardsType);
        faithWayBonus = gson.fromJson(cardHelperJson.getAsJsonObject("faithWayBonus"), intintMapType);
        militaryWayBonus = gson.fromJson(cardHelperJson.getAsJsonObject("militaryWayBonus"), intintMapType);
        initialState = gson.fromJson(cardHelperJson.getAsJsonObject("initialState"), PlayerState.class);
    }

    /**
     * Get requested military points to own specified number of territory cards
     *
     * @param numberOfCards Number of territory cards
     * @return Requested military points number
     */
    public static int requestedMilitary(int numberOfCards) {
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
    public static int victoryForCards(CardType cardType, int numberOfCards) {

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
    public static int getFaithBonus(int faithPoints) {
        return faithWayBonus.get(faithPoints);
    }

    /**
     * Get victory points bonus for military way placement
     *
     * @param playerPosition Placement relative to other players on military way
     * @return Victory points bonus
     */
    public static int getMilitaryBonus(int playerPosition) {
        if(militaryWayBonus.containsKey(playerPosition))
            return militaryWayBonus.get(playerPosition);

        return 0;
    }

    /**
     * Get initialize player state bound to given user
     *
     * @param boundUser User to bind to new player state
     * @return Player state initialized for game start
     */
    public static Game.UserObjects.PlayerState getInitialPS(GameUser boundUser) {
        final PlayerState newState = (PlayerState) initialState.clone();

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
