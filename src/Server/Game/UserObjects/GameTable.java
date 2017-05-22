package Server.Game.UserObjects;

import Game.Cards.Card;
import Game.Cards.CardType;
import Game.Effects.Effect;
import Game.Positions.Position;
import Game.UserObjects.DomesticColor;
import Game.UserObjects.GameUser;
import Game.UserObjects.PlayerState;
import Logging.Logger;
import Networking.Gson.MySerializer;
import Server.Game.Positions.*;
import Server.Game.Usable.Cost;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * Created by fiore on 11/05/2017.
 */
public class GameTable {

    private final Map<CardType, List<TowerPosition>> towers = new HashMap<>();

    private final List<ActionPosition> actionPositions = new ArrayList<>();

    private final transient List<Position<Cost>> costPositions = new ArrayList<>();

    private final List<GameUser> nextTurnOrder = Collections.synchronizedList(new ArrayList<>());

    private final Map<DomesticColor, Integer> diceValue = new HashMap<>();

    private volatile Effect currentFaithEffect = null;

    /**
     * Initialize a new game table for the specified number of players
     *
     * @param playersNumber Players in game
     * @param jsonSetupFile Json initialization file path
     */
    public GameTable(int playersNumber, String jsonSetupFile) throws IOException {

        Path jsonSetupPath = Paths.get(jsonSetupFile);

        if(Files.notExists(jsonSetupPath))
            throw new FileNotFoundException("File doesn't exists!");

        initAll(Files.newInputStream(jsonSetupPath), playersNumber);
    }

    /**
     * Initialize standard game table object
     *
     * @param playersNumber Number of players
     */
    public GameTable(int playersNumber) throws IOException {

        try {
            initAll(Files.newInputStream(Paths.get("src/Server/Game/Positions/Serialize/table.json")),
                    playersNumber);

        } catch (IOException ioe) {
            Logger.log(Logger.LogLevel.Error, "Can't find standard table setup file.\n" + ioe.getMessage());
            throw ioe;
        }

    }

    /**
     * Get affordable costs by given user for all positions
     *
     * @param currentUser Current playing user
     * @return Map of position number and list of costs for that position
     */
    public Map<Integer, List<Cost>> getCosts(GameUser currentUser) {

        Map<Integer, List<Cost>> positionCosts = new HashMap<>();

        costPositions.forEach(position ->
                positionCosts.put(position.getNumber(), position.canOccupy(currentUser.getUserState())));

        return positionCosts;

    }

    /**
     * Get activable effects by given user for action positions
     *
     * @param currentUser Current playing user
     * @return Map of position number and list of activable effects for that position
     */
    public Map<Integer, List<Effect>> getEffects(GameUser currentUser) {

        Map<Integer, List<Effect>> positionEffects = new HashMap<>();

        actionPositions.forEach(position ->
                positionEffects.put(position.getNumber(), position.canOccupy(currentUser.getUserState())));

        return positionEffects;
    }

    /**
     * Get current faith effect
     *
     * @return Permanent faith effect
     */
    public Effect getFaithEffect() {
        return currentFaithEffect;
    }

    /**
     * Set given cards in corresponding towers, update faith effect if specified
     * and get dice values for next turn
     *
     * @param newCards New set of cards
     * @param newFaithEffect Faith effect (if null previous will remain)
     */
    public Map<DomesticColor, Integer> changeTurn(Map<CardType, List<Server.Game.Cards.Card>> newCards, Effect newFaithEffect) {

        // Update cards in tower positions
        newCards.forEach((type, list) ->
                towers.get(type).forEach(position ->
                        position.setCard(list.remove(0)))
        );

        // Update faith effect if necessary
        if(newFaithEffect != null)
            currentFaithEffect = newFaithEffect;

        // Return new dice values
        return getDiceValue();
    }

    /**
     * Throw all three dice and return results
     *
     * @return Dice throw results
     */
    private Map<DomesticColor, Integer> getDiceValue() {

        diceValue.clear();
        Random die = new Random(System.currentTimeMillis());

        diceValue.put(DomesticColor.Black, die.nextInt(6));
        diceValue.put(DomesticColor.Orange, die.nextInt(6));
        diceValue.put(DomesticColor.White, die.nextInt(6));
        diceValue.put(DomesticColor.Neutral, 0);

        return diceValue;
    }

    /**
     * Get new players order for next round and free all positions
     *
     * @param currentOrder Players order from current round
     * @return Players order for next round
     */
    public List<GameUser> changeRound(List<GameUser> currentOrder) {

        // Remove family in council from current order
        currentOrder.removeAll(nextTurnOrder);

        // Add left family at the end of list
        nextTurnOrder.addAll(currentOrder);

        // Create new order list
        List<GameUser> newOrder = new ArrayList<>(nextTurnOrder);

        // Free all positions
        actionPositions.forEach(Position::free);
        costPositions.forEach(Position::free);

        // Clear council order
        nextTurnOrder.clear();

        // Return new round order
        return newOrder;
    }

    /**
     * Occupy requested position with given user
     *
     * @param currentUser Current user
     * @param positionNumber Number of position to occupy
     * @param chosenTs Chosen effects/cost to activate/pay occupying specified position
     * @param type T type of selected Position&lt;T&gt;
     * @param <T> Effect or Cost
     */
    @SuppressWarnings("unchecked")
    public <T> void occupy(GameUser currentUser, int positionNumber, List<T> chosenTs, Class<T> type) {

        if(type == Effect.class)
            actionPositions.parallelStream()
                    .filter(pos -> pos.getNumber() == positionNumber)
                    .findFirst()
                    .ifPresent(pos -> {
                        PlayerState newState = pos.occupy(currentUser.getUserState(), (List<Effect>) chosenTs);

                        currentUser.updateUserState(newState);
                    });

        if(type == Cost.class)
            costPositions.parallelStream()
                    .filter(pos -> pos.getNumber() == positionNumber)
                    .findFirst()
                    .ifPresent(pos -> {
                        PlayerState newState = pos.occupy(currentUser.getUserState(), (List<Cost>) chosenTs);

                        currentUser.updateUserState(newState);
                    });

        // TODO: send update to all players here if you want differential update
        // (else get global update from game table as you wish)
    }

    /**
     * Load json table from given input stream and initialize current object
     *
     * @param jsonTableStream Json file stream
     * @param playersNumber Number of players
     */
    private void initAll(InputStream jsonTableStream, int playersNumber) {

        // Read table setup from json file
        JsonObject completeTable = new JsonParser()
                .parse(new InputStreamReader(jsonTableStream))
                .getAsJsonObject();

        // Initialize json deserializer
        final Gson gson = new GsonBuilder()
                .registerTypeAdapter(Position.class, new MySerializer<Position>())
                .registerTypeAdapter(Effect.class, new MySerializer<Effect>())
                .create();

        initTowers(gson, completeTable);

        initActions(gson, completeTable, playersNumber);

        initMarket(gson, completeTable, playersNumber);

        initCouncil(gson, completeTable);

    }

    /**
     * Load towers hash map from json table object
     *
     * @param deserializer Gson deserializer correctly initialized
     * @param tableJson Table json object
     */
    private void initTowers(Gson deserializer, JsonObject tableJson) {

        // Initialize all towers
        JsonObject towersJson = tableJson.getAsJsonObject("Towers");

        Type towersHashMapType = new TypeToken<Map<CardType, List<TowerPosition>>>(){}.getType();

        Map<CardType, List<TowerPosition>> loadedTowers = deserializer
                .fromJson(towersJson, towersHashMapType);

        // Aggregate positions in towers and add them to global cost positions list
        loadedTowers.forEach((type, list) -> {
            PositionAggregate.aggregate(list);

            costPositions.addAll(list);
        });

        towers.putAll(loadedTowers);
    }

    /**
     * Initialize harvest and production positions accordingly to players' number
     *
     * @param deserializer Gson initialized object
     * @param tableJson Table json object
     * @param playersNumber Number of players
     */
    private void initActions(Gson deserializer, JsonObject tableJson, int playersNumber) {

        Type actionPosListType = new TypeToken<List<ActionPosition>>(){}.getType();

        // Load harvest positions
        JsonArray harvestPosList = tableJson.getAsJsonArray("Harvest");

        List<ActionPosition> harvestPos = deserializer
                .fromJson(harvestPosList, actionPosListType);

        // If more than two players load all positions
        if(playersNumber > 2) {
            PositionAggregate.aggregate(harvestPos);

            actionPositions.addAll(harvestPos);
        }
        else // Else load first position only
            actionPositions.add(harvestPos.get(0));


        // Load production positions
        JsonArray productionPosList = tableJson.getAsJsonArray("Production");

        List<ActionPosition> productionPos = deserializer
                .fromJson(productionPosList, actionPosListType);

        // Same as above
        if(playersNumber > 2) {
            PositionAggregate.aggregate(productionPos);

            actionPositions.addAll(productionPos);
        }
        else
            actionPositions.add(productionPos.get(0));
    }

    /**
     * Initialize market positions accordingly to players' number
     *
     * @param deserializer Gson initialized object
     * @param tableJson Table json object
     * @param playersNumber Number of players
     */
    private void initMarket(Gson deserializer, JsonObject tableJson, int playersNumber) {

        // Load market positions
        JsonArray marketPosList = tableJson.getAsJsonArray("Market");

        List<MarketPosition> marketPos = deserializer
                .fromJson(marketPosList, new TypeToken<List<MarketPosition>>(){}.getType());

        // If less than four players remove last two market positions
        if(playersNumber < 4) {
            marketPos.remove(2);
            marketPos.remove(2);
        }

        PositionAggregate.aggregate(marketPos);

        costPositions.addAll(marketPos);

    }

    /**
     * Initialize council positions
     *
     * @param deserializer Gson initialized object
     * @param tableJson Table json object
     */
    private void initCouncil(Gson deserializer, JsonObject tableJson) {

        // Load council positions
        JsonArray councilPosList = tableJson.getAsJsonArray("Council");

        List<CouncilPosition> councilPos = deserializer
                .fromJson(councilPosList, new TypeToken<List<CouncilPosition>>(){}.getType());

        // Set next turn order list to be updated with sequence of displacement of domestic in council positions
        councilPos.forEach(position -> position.setOrderList(nextTurnOrder));

        PositionAggregate.aggregate(councilPos);

        costPositions.addAll(councilPos);

    }

}
