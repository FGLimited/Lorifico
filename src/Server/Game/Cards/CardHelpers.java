package Server.Game.Cards;

import Game.Cards.CardType;
import Logging.Logger;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Map;

/**
 * Created by fiore on 24/05/2017.
 */
public class CardHelpers {


    private static final Map<Integer, Integer> militaryTerritory;

    private static final Map<CardType, Map<Integer, Integer>> victoryForCards;

    static {

        // Try loading cardHelper.json file from application resources
        InputStream jsonInputStream;

        try {
            jsonInputStream = Files.newInputStream(Paths.get("src/Server/Game/Cards/Serialize/cardHelpers.json"));
        } catch (IOException ioe) {
            Logger.log(Logger.LogLevel.Error, "Can't load card helper.\n" + ioe.getMessage());
            throw new RuntimeException("Application file not found!");
        }

        // Read json object from file
        JsonObject cardHelperJson = new JsonParser()
                .parse(new InputStreamReader(jsonInputStream))
                .getAsJsonObject();

        Gson gson = new Gson();
        Type milTerType = new TypeToken<Map<Integer, Integer>>(){}.getType();
        Type victorCardsType = new TypeToken<Map<CardType, Map<Integer, Integer>>>(){}.getType();

        // Load maps from json object
        militaryTerritory = gson.fromJson(cardHelperJson.getAsJsonObject("militaryTerritory"), milTerType);
        victoryForCards = gson.fromJson(cardHelperJson.getAsJsonObject("victoryForCards"), victorCardsType);
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

}
