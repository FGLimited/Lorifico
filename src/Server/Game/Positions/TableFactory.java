package Server.Game.Positions;

import Game.Cards.CardType;
import Game.Effects.Effect;
import Game.Positions.PositionType;
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

    public static void main(String[] args) throws IOException {

        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(Position.class, new MySerializer<Position>())
                .registerTypeAdapter(Effect.class, new MySerializer<Effect>())
                .create();

        JsonObject completeTable = serializeActions(gson, serializeTowers(gson, new JsonObject()));

        String tableJson = gson.toJson(completeTable);

        PrintWriter pw = new PrintWriter("src/Server/Game/Positions/Serialize/table.json", "UTF-8");

        pw.println(tableJson);

        pw.close();
    }

    private static JsonObject serializeActions(Gson gson, JsonObject jsonObj) {

        final Map<Integer, Server.Game.Positions.Position> positions = new HashMap<>();

        // Harvest positions
        positions.put(20, new ActionPosition(PositionType.HarvestAction, 20, 0));
        positions.put(21, new ActionPosition(PositionType.HarvestAction, 21, 3));
        positions.put(22, new ActionPosition(PositionType.HarvestAction, 22, 3));
        positions.put(23, new ActionPosition(PositionType.HarvestAction, 23, 3));

        // Production positions
        positions.put(30, new ActionPosition(PositionType.ProductionAction, 30, 0));
        positions.put(31, new ActionPosition(PositionType.ProductionAction, 31, 3));
        positions.put(32, new ActionPosition(PositionType.ProductionAction, 32, 3));
        positions.put(33, new ActionPosition(PositionType.ProductionAction, 33, 3));

        // Market positions
        positions.put(40, new MarketPosition(40, new ImmediateEffect(Collections.singletonMap(ResourceType.Gold, 5))));
        positions.put(41, new MarketPosition(41, new ImmediateEffect(Collections.singletonMap(ResourceType.Slave, 5))));
        positions.put(42, new MarketPosition(42, new ImmediateEffect(new HashMap<ResourceType, Integer>() {
            {
                put(ResourceType.Gold, 2);
                put(ResourceType.MilitaryPoint, 3);
            }
        })));
        positions.put(43, new MarketPosition(43, new ImmediateEffect(Collections.singletonMap(ResourceType.Favor, 2))));

        for(int i = 0; i < 4; i++) {
            positions.put(50 + i, new CouncilPosition(50 + i));
        }

        Type posListType = new TypeToken<Map<Integer, Server.Game.Positions.Position>>(){}.getType();

        JsonObject completeJson = jsonObj != null ? jsonObj : new JsonObject();
        completeJson.add("positions", gson.toJsonTree(positions, posListType));

        return completeJson;
    }

    private static JsonObject serializeTowers(Gson gson, JsonObject jsonObj) {

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

        Map<CardType, List<TowerPosition>> towers = new HashMap<>();
        towers.put(CardType.Territory, territoryTower);
        towers.put(CardType.Building, buildingTower);
        towers.put(CardType.Personality, personalityTower);
        towers.put(CardType.Challenge, challangeTower);

        Type towersType = new TypeToken<Map<CardType, List<TowerPosition>>>(){}.getType();

        JsonObject completeJson = jsonObj != null ? jsonObj : new JsonObject();

        completeJson.add("towers", gson.toJsonTree(towers, towersType));

        return completeJson;
    }
}
