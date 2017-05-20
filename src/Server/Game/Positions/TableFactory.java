package Server.Game.Positions;

import Game.Cards.CardType;
import Game.Effects.Effect;
import Game.Effects.EffectType;
import Game.Positions.Position;
import Game.Usable.ResourceType;
import Networking.Gson.MySerializer;
import Server.Game.Effects.ImmediateEffect;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.util.*;

/**
 * Created by fiore on 14/05/2017.
 */
public class TableFactory {

    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(Position.class, new MySerializer<Position>())
            .registerTypeAdapter(Effect.class, new MySerializer<Effect>())
            .create();

    public static void main(String[] args) throws IOException {

        JsonObject completeTable = serializeActions(serializeTowers(new JsonObject()));

        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        String tableJson = gson.toJson(completeTable);

        PrintWriter pw = new PrintWriter("table.json", "UTF-8");

        pw.println(tableJson);

        pw.close();
    }

    private static JsonObject serializeActions(JsonObject jsonObj) {

        // Harvest positions
        ActionPosition twenty = new ActionPosition(EffectType.Harvest, 20, 0);
        ActionPosition twentyone = new ActionPosition(EffectType.Harvest, 21, 3);
        ActionPosition twentytwo = new ActionPosition(EffectType.Harvest, 22, 3);
        ActionPosition twentythree = new ActionPosition(EffectType.Harvest, 23, 3);

        List<ActionPosition> harvestAggregate = new ArrayList<>(Arrays.asList(twenty, twentyone, twentytwo, twentythree));

        // Production positions
        ActionPosition thirty = new ActionPosition(EffectType.Production, 30, 0);
        ActionPosition thirtyone = new ActionPosition(EffectType.Production, 31, 3);
        ActionPosition thirtytwo = new ActionPosition(EffectType.Production, 32, 3);
        ActionPosition thirtythree = new ActionPosition(EffectType.Production, 33, 3);

        List<ActionPosition> productionAggregate = new ArrayList<>(Arrays.asList(thirty, thirtyone, thirtytwo, thirtythree));

        // Market positions
        MarketPosition forty = new MarketPosition(40, new ImmediateEffect(Collections.singletonMap(ResourceType.Gold, 5)));
        MarketPosition fortyone = new MarketPosition(41, new ImmediateEffect(Collections.singletonMap(ResourceType.Slave, 5)));

        HashMap<ResourceType, Integer> marketBonus = new HashMap<>();
        marketBonus.put(ResourceType.Gold, 2);
        marketBonus.put(ResourceType.Favor, 1);

        MarketPosition fortytwo = new MarketPosition(42, new ImmediateEffect(marketBonus));
        MarketPosition fortythree = new MarketPosition(43, new ImmediateEffect(Collections.singletonMap(ResourceType.Favor, 2)));

        List<MarketPosition> marketAggregate = new ArrayList<>(Arrays.asList(forty, fortyone, fortytwo, fortythree));

        // Council positions
        List<CouncilPosition> councilAggregate = new ArrayList<>();

        for(int i = 0; i < 4; i++) {
            councilAggregate.add(new CouncilPosition(50 + i));
        }

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Position.class, new MySerializer<Position>())
                .registerTypeAdapter(Effect.class, new MySerializer<Effect>())
                .create();

        Type actionPosListType = new TypeToken<List<ActionPosition>>(){}.getType();
        Type marketPosListType = new TypeToken<List<MarketPosition>>(){}.getType();
        Type councilPosListType = new TypeToken<List<CouncilPosition>>(){}.getType();

        String harvest = gson.toJson(harvestAggregate, actionPosListType);
        JsonArray harvestJson = new JsonParser().parse(harvest).getAsJsonArray();

        String production = gson.toJson(productionAggregate, actionPosListType);
        JsonArray productionJson = new JsonParser().parse(production).getAsJsonArray();

        String market = gson.toJson(marketAggregate, marketPosListType);
        JsonArray marketJson = new JsonParser().parse(market).getAsJsonArray();

        String council = gson.toJson(councilAggregate, councilPosListType);
        JsonArray councilJson = new JsonParser().parse(council).getAsJsonArray();

        JsonObject completeJson = jsonObj != null ? jsonObj : new JsonObject();
        completeJson.add("Harvest", harvestJson);
        completeJson.add("Production", productionJson);
        completeJson.add("Market", marketJson);
        completeJson.add("Council", councilJson);

        return completeJson;
    }

    private static JsonObject serializeTowers(JsonObject jsonObj) {

        // Territory tower
        TowerPosition one = new TowerPosition(
                null,
                1,
                CardType.Territory,
                1);
        TowerPosition two = new TowerPosition(
                null,
                3,
                CardType.Territory,
                2);
        TowerPosition three = new TowerPosition(
                new ImmediateEffect(Collections.singletonMap(ResourceType.Wood, 1)),
                5,
                CardType.Territory,
                3);
        TowerPosition four = new TowerPosition(
                new ImmediateEffect(Collections.singletonMap(ResourceType.Wood, 2)),
                7,
                CardType.Territory,
                4);

        List<TowerPosition> territoryTower = new ArrayList<>(Arrays.asList(one, two, three, four));

        // Building tower
        TowerPosition five = new TowerPosition(
                null,
                1,
                CardType.Building,
                5);
        TowerPosition six = new TowerPosition(
                null,
                3,
                CardType.Building,
                6);
        TowerPosition seven = new TowerPosition(
                new ImmediateEffect(Collections.singletonMap(ResourceType.Rock, 1)),
                5,
                CardType.Building,
                7);
        TowerPosition eight = new TowerPosition(
                new ImmediateEffect(Collections.singletonMap(ResourceType.Rock, 2)),
                7,
                CardType.Building,
                8);

        List<TowerPosition> buildingTower = new ArrayList<>(Arrays.asList(five, six, seven, eight));

        //Personality tower
        TowerPosition nine = new TowerPosition(
                null,
                1,
                CardType.Personality,
                9);
        TowerPosition ten = new TowerPosition(
                null,
                3,
                CardType.Personality,
                10);
        TowerPosition eleven = new TowerPosition(
                new ImmediateEffect(Collections.singletonMap(ResourceType.MilitaryPoint, 1)),
                5,
                CardType.Personality,
                11);
        TowerPosition twelve = new TowerPosition(
                new ImmediateEffect(Collections.singletonMap(ResourceType.MilitaryPoint, 2)),
                7,
                CardType.Personality,
                12);

        List<TowerPosition> personalityTower = new ArrayList<>(Arrays.asList(nine, ten, eleven, twelve));

        // Challenge tower
        TowerPosition thirteen = new TowerPosition(
                null,
                1,
                CardType.Challenge,
                13);
        TowerPosition fourteen = new TowerPosition(
                null,
                3,
                CardType.Challenge,
                14);
        TowerPosition fifteen = new TowerPosition(
                new ImmediateEffect(Collections.singletonMap(ResourceType.Gold, 1)),
                5,
                CardType.Challenge,
                15);
        TowerPosition sixteen = new TowerPosition(
                new ImmediateEffect(Collections.singletonMap(ResourceType.Gold, 2)),
                7,
                CardType.Challenge,
                16);

        List<TowerPosition> challangeTower = new ArrayList<>(Arrays.asList(thirteen, fourteen, fifteen, sixteen));

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Position.class, new MySerializer<Position>())
                .registerTypeAdapter(Effect.class, new MySerializer<Effect>())
                .create();

        Type posListType = new TypeToken<List<TowerPosition>>(){}.getType();

        String territory = gson.toJson(territoryTower, posListType);
        JsonArray territoryJson = new JsonParser().parse(territory).getAsJsonArray();

        String building = gson.toJson(buildingTower, posListType);
        JsonArray buildingJson = new JsonParser().parse(building).getAsJsonArray();

        String personality = gson.toJson(personalityTower, posListType);
        JsonArray personalityJson = new JsonParser().parse(personality).getAsJsonArray();

        String challange = gson.toJson(challangeTower, posListType);
        JsonArray challengeJson = new JsonParser().parse(challange).getAsJsonArray();

        JsonObject completeJson = jsonObj != null ? jsonObj : new JsonObject();

        JsonObject hashTowers = new JsonObject();
        hashTowers.add("Territory", territoryJson);
        hashTowers.add("Building", buildingJson);
        hashTowers.add("Personality", personalityJson);
        hashTowers.add("Challenge", challengeJson);

        completeJson.add("Towers", hashTowers);

        return completeJson;
    }
}
