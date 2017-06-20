package Server.Game.Cards;

import Game.Cards.*;
import Game.Effects.Effect;
import Game.Positions.PositionType;
import Game.Usable.ResourceType;
import Networking.Gson.MySerializer;
import Server.Game.Effects.*;
import Server.Game.Usable.Cost;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * Created by fiore on 16/05/2017.
 */
public class SplitDeck {

    private final Map<Integer, Map<CardType, List<Card>>> cardPerTurn;

    /**
     * Initialize a new deck of cards
     *
     * @param fileName Json file to load deck from
     * @throws IOException If standard deck json file isn't found
     */
    public SplitDeck(final String fileName) throws IOException {

        InputStream splitDeckJson = Files
                .newInputStream(Paths.get(fileName));

        Type splitDeckType = new TypeToken<Map<Integer, Map<CardType, List<Card>>>>(){}.getType();

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Effect.class, new MySerializer<Effect>())
                .create();

        cardPerTurn = gson.fromJson(new InputStreamReader(splitDeckJson), splitDeckType);
    }

    public SplitDeck() throws IOException {
        this("src/Server/Game/Cards/Serialize/splitDeck.json");
    }

    /**
     * Shuffle cards
     */
    public void shuffle() {
        cardPerTurn.forEach((turnNumber, cards) ->
                cards.forEach((type, list) ->
                        Collections.shuffle(list)));
    }

    /**
     * Get cards sets for specified turn
     *
     * @param turnNumber Turn number
     * @return Set of cards for each tower
     */
    public Map<CardType, List<Card>> getCardPerTurn(Integer turnNumber) {

        Map<CardType, List<Card>> currentTurn = new HashMap<>();

        // Get requested cards from global map
        cardPerTurn.get((turnNumber - 1) % 2 + 1).forEach((type, list) -> {

            List<Card> tower = new ArrayList<>();

            for (int i = turnNumber % 2 == 0 ? 4 : 0; i < (turnNumber % 2 == 0 ? 8 : 4); i++) {
                tower.add(list.get(i));
            }

            currentTurn.put(type, tower);
        });

        return currentTurn;
    }

    /*
    public static void main(String[] args) throws IOException {

        Map<Integer, Map<CardType, List<Card>>> splitDeck = new HashMap<>();
        splitDeck.put(1, new HashMap<>());
        splitDeck.put(2, new HashMap<>());
        splitDeck.put(3, new HashMap<>());

        territoryCards().forEach((age, cards) -> splitDeck.get(age).put(CardType.Territory, cards));
        buildingCards().forEach((age, cards) -> splitDeck.get(age).put(CardType.Building, cards));
        personalityCards().forEach((age, cards) -> splitDeck.get(age).put(CardType.Personality, cards));
        challengeCards().forEach((age, cards) -> splitDeck.get(age).put(CardType.Challenge, cards));

        Type splitDeckType = new TypeToken<Map<Integer, Map<CardType, List<Card>>>>(){}.getType();

        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(Effect.class, new MySerializer<Effect>())
                .create();

        String splitDeckJson = gson.toJson(splitDeck, splitDeckType);

        PrintWriter pw = new PrintWriter("src/Server/Game/Cards/Serialize/splitDeck.json", "UTF-8");

        pw.println(splitDeckJson);

        pw.close();

    }

    private static Map<Integer, List<Card>> challengeCards() {

        Effect oneone = new ImmediateEffect(Collections.singletonMap(ResourceType.MilitaryPoint, 5));
        Effect onetwo = new FinalVictoryPointsEffect(4);
        Cost onecost = new Cost(Collections.singletonMap(ResourceType.Gold, 4));
        Card one = new Card(CardType.Challenge, Collections.singletonList(onecost), Arrays.asList(oneone, onetwo), 73, "Ingaggiare Reclute", "");

        Effect twoone = new ImmediateEffect(Collections.singletonMap(ResourceType.FaithPoint, 1));
        Effect twotwo = new FinalVictoryPointsEffect(5);
        Cost twocost = new Cost(new HashMap<ResourceType, Integer>() {
            {
                put(ResourceType.Gold, 1);
                put(ResourceType.Wood, 1);
                put(ResourceType.Rock, 1);
            }
        });
        Card two = new Card(CardType.Challenge, Collections.singletonList(twocost), Arrays.asList(twoone, twotwo), 74, "Riparare la Chiesa", "");

        Effect threeone = new ImmediateEffect(new HashMap<ResourceType, Integer>(){
            {
                put(ResourceType.MilitaryPoint, 2);
                put(ResourceType.Favor, 1);
            }
        });
        Effect threetwo = new FinalVictoryPointsEffect(3);
        Cost threecost = new Cost(Collections.singletonMap(ResourceType.Rock, 3));
        Card three = new Card(CardType.Challenge, Collections.singletonList(threecost), Arrays.asList(threeone, threetwo), 75, "Costruire le Mura", "");

        Effect fourone = new ImmediateEffect(Collections.singletonMap(ResourceType.Favor, 2));
        Effect fourtwo = new FinalVictoryPointsEffect(4);
        Cost fourcost = new Cost(new HashMap<ResourceType, Integer>() {
            {
                put(ResourceType.Wood, 2);
                put(ResourceType.Rock, 2);
            }
        });
        Card four = new Card(CardType.Challenge, Collections.singletonList(fourcost), Arrays.asList(fourone, fourtwo), 76, "Innalzare una Statua", "");

        Effect fiveone = new ImmediateEffect(Collections.singletonMap(ResourceType.Gold, 3));
        Effect fivetwo = new FinalVictoryPointsEffect(5);
        Cost fivecost = new Cost(Collections.singletonMap(ResourceType.MilitaryPoint, 2), 3);
        Card five = new Card(CardType.Challenge, Collections.singletonList(fivecost), Arrays.asList(fiveone, fivetwo), 77, "Campagna Militare", "");

        Effect sixone = new ImmediateEffect(Collections.singletonMap(ResourceType.Slave, 4));
        Effect sixtwo = new FinalVictoryPointsEffect(4);
        Cost sixcost = new Cost(Collections.singletonMap(ResourceType.Wood, 3));
        Card six = new Card(CardType.Challenge, Collections.singletonList(sixcost), Arrays.asList(sixone, sixtwo), 78, "Ospitare i Mendiacanti", "");

        Effect sevenone = new ImmediateEffect(Collections.singletonMap(ResourceType.FaithPoint, 2));
        Effect seventwo = new FinalVictoryPointsEffect(5);
        Cost sevencost = new Cost(Collections.singletonMap(ResourceType.MilitaryPoint, 3), 5);
        Card seven = new Card(CardType.Challenge, Collections.singletonList(sevencost), Arrays.asList(sevenone, seventwo), 79, "Combattere le Eresie", "");

        Effect eightone = new ImmediateEffect(Collections.singletonMap(ResourceType.FaithPoint, 3));
        Effect eighttwo = new FinalVictoryPointsEffect(1);
        Cost eightcostone = new Cost(Collections.singletonMap(ResourceType.MilitaryPoint, 2), 4);
        Cost eightcosttwo = new Cost(new HashMap<ResourceType, Integer>(){
            {
                put(ResourceType.Wood, 1);
                put(ResourceType.Gold, 2);
                put(ResourceType.Rock, 1);
            }
        });
        Card eight = new Card(CardType.Challenge, Arrays.asList(eightcostone, eightcosttwo), Arrays.asList(eightone, eighttwo), 80, "Sostegno al Vescovo", "");

        Effect nineone = new ImmediateEffect(Collections.singletonMap(ResourceType.MilitaryPoint, 6));
        Effect ninetwo = new FinalVictoryPointsEffect(5);
        Cost ninecost = new Cost(Collections.singletonMap(ResourceType.Gold, 6));
        Card nine = new Card(CardType.Challenge, Collections.singletonList(ninecost), Arrays.asList(nineone, ninetwo), 81, "Ingaggiare Soldati", "");

        Effect tenone = new ImmediateEffect(Collections.singletonMap(ResourceType.FaithPoint, 2));
        Effect tentwo = new FinalVictoryPointsEffect(6);
        Cost tencost = new Cost(new HashMap<ResourceType, Integer>(){
            {
                put(ResourceType.Gold, 2);
                put(ResourceType.Wood, 2);
                put(ResourceType.Rock, 2);
            }
        });
        Card ten = new Card(CardType.Challenge, Collections.singletonList(tencost), Arrays.asList(tenone, tentwo), 82, "Riparare l'Abbazia", "");

        Effect elevenone = new ImmediateEffect(new HashMap<ResourceType, Integer>(){
            {
                put(ResourceType.MilitaryPoint, 3);
                put(ResourceType.Favor, 1);
            }
        });
        Effect eleventwo = new FinalVictoryPointsEffect(2);
        Cost elevencost = new Cost(Collections.singletonMap(ResourceType.Rock, 4));
        Card eleven = new Card(CardType.Challenge, Collections.singletonList(elevencost), Arrays.asList(elevenone, eleventwo), 83, "Costruire i Bastioni", "");

        Effect twelveone = new ImmediateEffect(new HashMap<ResourceType, Integer>(){
            {
                put(ResourceType.Gold, 5);
                put(ResourceType.Favor, 1);
            }
        });
        Effect twelvetwo = new FinalVictoryPointsEffect(3);
        Cost twelvecost = new Cost(Collections.singletonMap(ResourceType.MilitaryPoint, 3), 6);
        Card twelve = new Card(CardType.Challenge, Collections.singletonList(twelvecost), Arrays.asList(twelveone, twelvetwo), 84, "Supporto al Re", "");

        Effect thirteenone = new BonusDomesticEffect(Collections.singletonList(PositionType.HarvestAction), 4, null);
        Effect thirteentwo = new FinalVictoryPointsEffect(5);
        Cost thirteencost = new Cost(new HashMap<ResourceType, Integer>(){
            {
                put(ResourceType.Slave, 2);
                put(ResourceType.Gold, 3);
            }
        });
        Card thirteen = new Card(CardType.Challenge, Collections.singletonList(thirteencost), Arrays.asList(thirteenone, thirteentwo), 85, "Scavare Canalizzazioni", "");

        Effect fourteenone = new ImmediateEffect(Collections.singletonMap(ResourceType.Slave, 5));
        Effect fourteentwo = new FinalVictoryPointsEffect(4);
        Cost fourteencost = new Cost(Collections.singletonMap(ResourceType.Wood, 4));
        Card fourteen = new Card(CardType.Challenge, Collections.singletonList(fourteencost), Arrays.asList(fourteenone, fourteentwo), 86, "Accogliere gli Stranieri", "");

        Effect fifteenone = new ImmediateEffect(new HashMap<ResourceType, Integer>(){
            {
                put(ResourceType.Gold, 5);
                put(ResourceType.FaithPoint, 1);
            }
        });
        Effect fifteentwo = new FinalVictoryPointsEffect(5);
        Cost fifteencost = new Cost(Collections.singletonMap(ResourceType.MilitaryPoint, 4), 5);
        Card fifteen = new Card(CardType.Challenge, Collections.singletonList(fifteencost), Arrays.asList(fifteenone, fifteentwo), 87, "Crociata", "");

        Effect sixteenone = new ImmediateEffect(Collections.singletonMap(ResourceType.FaithPoint, 3));
        Effect sixteentwo = new FinalVictoryPointsEffect(4);
        Cost sixteenxostone = new Cost(Collections.singletonMap(ResourceType.MilitaryPoint, 4), 7);
        Cost sixteencosttwo = new Cost(new HashMap<ResourceType, Integer>(){
            {
                put(ResourceType.Wood, 2);
                put(ResourceType.Gold, 3);
                put(ResourceType.Rock, 2);
            }
        });
        Card sixteen = new Card(CardType.Challenge, Arrays.asList(sixteenxostone, sixteencosttwo), Arrays.asList(sixteenone, sixteentwo), 88, "Sostegno al Cardinale", "");

        Effect seventeenone = new ImmediateEffect(Collections.singletonMap(ResourceType.MilitaryPoint, 7));
        Effect seventeentwo = new FinalVictoryPointsEffect(6);
        Cost seventeencost = new Cost(Collections.singletonMap(ResourceType.Gold, 8));
        Card seventeen = new Card(CardType.Challenge, Collections.singletonList(seventeencost), Arrays.asList(seventeenone, seventeentwo), 89, "Ingaggiare Mercenari", "");

        Effect eighteenone = new ImmediateEffect(Collections.singletonMap(ResourceType.FaithPoint, 1));
        Effect eighteentwo = new BonusDomesticEffect(Arrays
                .asList(PositionType.TerritoryTower, PositionType.BuildingTower, PositionType.PersonalityTower, PositionType.ChallengeTower)
                , 7, null);
        Effect eighteenthree = new FinalVictoryPointsEffect(5);
        Cost eighteencost = new Cost(new HashMap<ResourceType, Integer>(){
            {
                put(ResourceType.Gold, 3);
                put(ResourceType.Wood, 3);
                put(ResourceType.Rock, 3);
            }
        });
        Card eighteen = new Card(CardType.Challenge, Collections.singletonList(eighteencost), Arrays.asList(eighteenone, eighteentwo, eighteenthree), 90, "Riparare la Cattedrale", "");

        Effect nineteenone = new ImmediateEffect(new HashMap<ResourceType, Integer>(){
            {
                put(ResourceType.MilitaryPoint, 4);
                put(ResourceType.Favor, 1);
            }
        });
        Effect nineteentwo = new FinalVictoryPointsEffect(4);
        Cost nineteencost = new Cost(Collections.singletonMap(ResourceType.Rock, 6));
        Card nineteen = new Card(CardType.Challenge, Collections.singletonList(nineteencost), Arrays.asList(nineteenone, nineteentwo), 91, "Costruire le Torri", "");

        Effect twentyonee = new ImmediateEffect(Collections.singletonMap(ResourceType.FaithPoint, 3));
        Effect twentytwoe = new FinalVictoryPointsEffect(3);
        Cost twentycost = new Cost(Collections.singletonMap(ResourceType.Wood, 6));
        Card twenty = new Card(CardType.Challenge, Collections.singletonList(twentycost), Arrays.asList(twentyonee, twentytwoe), 92, "Commissionare Arte Sacra", "");

        Effect twentyoneone = new ImmediateEffect(new HashMap<ResourceType, Integer>(){
            {
                put(ResourceType.Wood, 3);
                put(ResourceType.Rock, 3);
                put(ResourceType.Gold, 3);
            }
        });
        Effect twentyonetwo = new FinalVictoryPointsEffect(7);
        Cost twentyonecost = new Cost(Collections.singletonMap(ResourceType.MilitaryPoint, 6), 12);
        Card twentyone = new Card(CardType.Challenge, Collections.singletonList(twentyonecost), Arrays.asList(twentyoneone, twentyonetwo), 93, "Conquista Militare", "");

        Effect twentytwoone = new BonusDomesticEffect(Collections.singletonList(PositionType.ProductionAction), 3, null);
        Effect twentytwotwo = new FinalVictoryPointsEffect(5);
        Cost twentytwocost = new Cost(new HashMap<ResourceType, Integer>(){
            {
                put(ResourceType.Slave, 3);
                put(ResourceType.Gold, 4);
            }
        });
        Card twentytwo = new Card(CardType.Challenge, Collections.singletonList(twentytwocost), Arrays.asList(twentytwoone, twentytwotwo), 94, "Migliorare le Strade", "");

        Effect twentythreeone = new ImmediateEffect(Collections.singletonMap(ResourceType.FaithPoint, 4));
        Effect twentythreetwo = new FinalVictoryPointsEffect(8);
        Cost twentythreecost = new Cost(Collections.singletonMap(ResourceType.MilitaryPoint, 8), 15);
        Card twentythree = new Card(CardType.Challenge, Collections.singletonList(twentythreecost), Arrays.asList(twentythreeone, twentythreetwo), 95, "Guerra Santa", "");

        Effect twentyfourone = new ImmediateEffect(Collections.singletonMap(ResourceType.FaithPoint, 2));
        Effect twentyfourtwo = new FinalVictoryPointsEffect(10);
        Cost twentyfourcostone = new Cost(new HashMap<ResourceType, Integer>(){
            {
                put(ResourceType.Wood, 3);
                put(ResourceType.Gold, 4);
                put(ResourceType.Rock, 3);
            }
        });
        Cost twentyfourcosttwo = new Cost(Collections.singletonMap(ResourceType.MilitaryPoint, 5), 10);
        Card twentyfour = new Card(CardType.Challenge, Arrays.asList(twentyfourcostone, twentyfourcosttwo), Arrays.asList(twentyfourone, twentyfourtwo), 96, "Sostegno al Papa", "");

        return new HashMap<Integer, List<Card>>() {
            {
                put(1, Arrays.asList(one, two, three, four, five, six, seven, eight));
                put(2, Arrays.asList(nine, ten, eleven, twelve, thirteen, fourteen, fifteen, sixteen));
                put(3, Arrays.asList(seventeen, eighteen, nineteen, twenty, twentyone, twentytwo, twentythree, twentyfour));
            }
        };
    }

    private static Map<Integer, List<Card>> personalityCards() {

        Effect oneone = new ImmediateEffect(Collections.singletonMap(ResourceType.MilitaryPoint, 3));
        Effect onetwo = new PositionBonusEffect(PositionType.TerritoryTower, 2);
        Cost onecost = new Cost(Collections.singletonMap(ResourceType.Gold, 2));
        Card one = new Card(CardType.Personality, Collections.singletonList(onecost), Arrays.asList(oneone, onetwo), 49, "Condottiero", "");

        Effect twoone = new PositionBonusEffect(PositionType.BuildingTower, 2);
        Effect twotwo = new CostBonusEffect(CardType.Building, new HashMap<ResourceType, Integer>() {
            {
                put(ResourceType.Wood, 1);
                put(ResourceType.Rock, 1);
            }
        });
        Cost twocost = new Cost(Collections.singletonMap(ResourceType.Gold, 4));
        Card two = new Card(CardType.Personality, Collections.singletonList(twocost), Arrays.asList(twoone, twotwo), 50, "Costruttore", "");

        Effect threeone = new PositionBonusEffect(PositionType.PersonalityTower, 2);
        Effect threetwo = new CostBonusEffect(CardType.Personality, Collections.singletonMap(ResourceType.Gold, 1));
        Cost threecost = new Cost(Collections.singletonMap(ResourceType.Gold, 4));
        Card three = new Card(CardType.Personality, Collections.singletonList(threecost), Arrays.asList(threeone, threetwo), 51, "Dama", "");

        Effect fourone = new ImmediateEffect(Collections.singletonMap(ResourceType.Favor, 1));
        Effect fourtwo = new PositionBonusEffect(PositionType.ChallengeTower, 2);
        Cost fourcost = new Cost(Collections.singletonMap(ResourceType.Gold, 2));
        Card four = new Card(CardType.Personality, Collections.singletonList(fourcost), Arrays.asList(fourone, fourtwo), 52, "Cavaliere", "");

        Effect fiveone = new PositionBonusEffect(PositionType.HarvestAction, 2);
        Cost fivecost = new Cost(Collections.singletonMap(ResourceType.Gold, 3));
        Card five = new Card(CardType.Personality, Collections.singletonList(fivecost), Collections.singletonList(fiveone), 53, "Contadino", "");

        Effect sixone = new PositionBonusEffect(PositionType.ProductionAction, 2);
        Cost sixcost = new Cost(Collections.singletonMap(ResourceType.Gold, 3));
        Card six = new Card(CardType.Personality, Collections.singletonList(sixcost), Collections.singletonList(sixone), 54, "Artigiano", "");

        Effect sevenone = new ImmediateEffect(Collections.singletonMap(ResourceType.FaithPoint, 4));
        // Deny bonus of 3rd, 4th tower positions not implemented
        Cost sevencost = new Cost(Collections.singletonMap(ResourceType.Gold, 2));
        Card seven = new Card(CardType.Personality, Collections.singletonList(sevencost), Collections.singletonList(sevenone), 55, "Predicatore", "");

        Effect eightone = new ImmediateEffect(Collections.singletonMap(ResourceType.FaithPoint, 1));
        Effect eighttwo = new BonusDomesticEffect(Arrays
                .asList(PositionType.TerritoryTower, PositionType.BuildingTower, PositionType.PersonalityTower, PositionType.ChallengeTower)
                , 4, null);
        Cost eightcost = new Cost(Collections.singletonMap(ResourceType.Gold, 3));
        Card eight = new Card(CardType.Personality, Collections.singletonList(eightcost), Arrays.asList(eightone, eighttwo), 56, "Badessa", "");

        Effect nineone = new BonusDomesticEffect(Collections.singletonList(PositionType.TerritoryTower), 6, null);
        Effect ninetwo = new ImmediateEffect(Collections.singletonMap(ResourceType.MilitaryPoint, 2));
        Cost ninecost = new Cost(Collections.singletonMap(ResourceType.Gold, 4));
        Card nine = new Card(CardType.Personality, Collections.singletonList(ninecost), Arrays.asList(nineone, ninetwo), 57, "Capitano", "");

        Effect tenone = new BonusDomesticEffect(Collections.singletonList(PositionType.BuildingTower), 6, new HashMap<ResourceType, Integer>() {
            {
                put(ResourceType.Wood, 1);
                put(ResourceType.Rock, 1);
            }
        });
        Cost tencost = new Cost(Collections.singletonMap(ResourceType.Gold, 4));
        Card ten = new Card(CardType.Personality, Collections.singletonList(tencost), Collections.singletonList(tenone), 58, "Architetto", "");

        Effect elevenone = new BonusDomesticEffect(Collections.singletonList(PositionType.PersonalityTower), 6, Collections.singletonMap(ResourceType.Gold, 2));
        Cost elevencost = new Cost(Collections.singletonMap(ResourceType.Gold, 3));
        Card eleven = new Card(CardType.Personality, Collections.singletonList(elevencost), Collections.singletonList(elevenone), 59, "Mecenate", "");

        Effect twelveone = new BonusDomesticEffect(Collections.singletonList(PositionType.ChallengeTower), 6, null);
        Effect twelvetwo = new ImmediateEffect(Collections.singletonMap(ResourceType.Favor, 1));
        Cost twelvecost = new Cost(Collections.singletonMap(ResourceType.Gold, 4));
        Card twelve = new Card(CardType.Personality, Collections.singletonList(twelvecost), Arrays.asList(twelveone, twelvetwo), 60, "Eroe", "");

        Effect thirteenone = new PositionBonusEffect(PositionType.HarvestAction, 3);
        Cost thirteencost = new Cost(Collections.singletonMap(ResourceType.Gold, 4));
        Card thirteen = new Card(CardType.Personality, Collections.singletonList(thirteencost), Collections.singletonList(thirteenone), 61, "Fattore", "");

        Effect fourteenone = new PositionBonusEffect(PositionType.ProductionAction, 3);
        Cost fourteencost = new Cost(Collections.singletonMap(ResourceType.Gold, 4));
        Card fourteen = new Card(CardType.Personality, Collections.singletonList(fourteencost), Collections.singletonList(fourteenone), 62, "Studioso", "");

        Effect fifteenone = new ImmediateEffect(Collections.singletonMap(ResourceType.FaithPoint, 3));
        Cost fifteencost = new Cost(Collections.singletonMap(ResourceType.Gold, 5));
        Card fifteen = new Card(CardType.Personality, Collections.singletonList(fifteencost), Collections.singletonList(fifteenone), 63, "Messo Papale", "");

        Effect sixteenone = new ImmediateEffect(Collections.singletonMap(ResourceType.Favor, 3));
        Cost sixteencost = new Cost(Collections.singletonMap(ResourceType.Gold, 5));
        Card sixteen = new Card(CardType.Personality, Collections.singletonList(sixteencost), Collections.singletonList(sixteenone), 64, "Messo Reale", "");

        Effect seventeenone = new CardResourceEffect(CardType.Territory, ResourceType.VictoryPoint, 2);
        Cost seventeencost = new Cost(Collections.singletonMap(ResourceType.Gold, 6));
        Card seventeen = new Card(CardType.Personality, Collections.singletonList(seventeencost), Collections.singletonList(seventeenone), 65, "Nobile", "");

        Effect eighteenone = new CardResourceEffect(CardType.Building, ResourceType.VictoryPoint, 2);
        Cost eighteencost = new Cost(Collections.singletonMap(ResourceType.Gold, 6));
        Card eighteen = new Card(CardType.Personality, Collections.singletonList(eighteencost), Collections.singletonList(eighteenone), 66, "Governatore", "");

        Effect nineteenone = new CardResourceEffect(CardType.Personality, ResourceType.VictoryPoint, 2);
        Cost nineteencost = new Cost(Collections.singletonMap(ResourceType.Gold, 7));
        Card nineteen = new Card(CardType.Personality, Collections.singletonList(nineteencost), Collections.singletonList(nineteenone), 67, "Cortigiana", "");

        Effect twentyonee = new CardResourceEffect(CardType.Challenge, ResourceType.VictoryPoint, 2);
        Cost twentycost = new Cost(Collections.singletonMap(ResourceType.Gold, 6));
        Card twenty = new Card(CardType.Personality, Collections.singletonList(twentycost), Collections.singletonList(twentyonee), 68, "Araldo", "");

        Effect twentyoneone = new ImmediateEffect(Collections.singletonMap(ResourceType.FaithPoint, 2));
        Effect twentyonetwo = new BonusDomesticEffect(Collections.singletonList(PositionType.HarvestAction), 4, null);
        Cost twentyonecost = new Cost(Collections.singletonMap(ResourceType.Gold, 4));
        Card twentyone = new Card(CardType.Personality, Collections.singletonList(twentyonecost), Arrays.asList(twentyoneone, twentyonetwo), 69, "Cardinale", "");

        Effect twentytwoone = new ImmediateEffect(Collections.singletonMap(ResourceType.FaithPoint, 1));
        Effect twentytwotwo = new BonusDomesticEffect(Collections.singletonList(PositionType.ProductionAction), 4, null);
        Cost twentytwocost = new Cost(Collections.singletonMap(ResourceType.Gold, 5));
        Card twentytwo = new Card(CardType.Personality, Collections.singletonList(twentytwocost), Arrays.asList(twentytwoone, twentytwotwo), 70, "Vescovo", "");

        Effect twentythreeone = new ResourceResourceEffect(ResourceType.MilitaryPoint, 2, ResourceType.VictoryPoint, 1);
        Cost twentythreecost = new Cost(Collections.singletonMap(ResourceType.Gold, 5));
        Card twentythree = new Card(CardType.Personality, Collections.singletonList(twentythreecost), Collections.singletonList(twentythreeone), 71, "Generale", "");

        Effect twentyfourone = new BonusDomesticEffect(Arrays
                .asList(PositionType.TerritoryTower, PositionType.BuildingTower, PositionType.PersonalityTower, PositionType.ChallengeTower)
                , 7, null);
        Effect twentyfourtwo = new ImmediateEffect(Collections.singletonMap(ResourceType.Favor, 1));
        Cost twentyfourcost = new Cost(Collections.singletonMap(ResourceType.Gold, 6));
        Card twentyfour = new Card(CardType.Personality, Collections.singletonList(twentyfourcost), Arrays.asList(twentyfourone, twentyfourtwo), 72, "Ambasciatore", "");

        return new HashMap<Integer, List<Card>>() {
            {
                put(1, Arrays.asList(one, two, three, four, five, six, seven, eight));
                put(2, Arrays.asList(nine, ten, eleven, twelve, thirteen, fourteen, fifteen, sixteen));
                put(3, Arrays.asList(seventeen, eighteen, nineteen, twenty, twentyone, twentytwo, twentythree, twentyfour));
            }
        };
    }

    private static Map<Integer, List<Card>> buildingCards() {

        Effect oneone = new ImmediateEffect(Collections.singletonMap(ResourceType.VictoryPoint, 5));
        Effect onetwo = new CardResourceEffect(CardType.Building, ResourceType.Gold, 1, 5, PositionType.ProductionAction);
        Cost onecost = new Cost(new HashMap<ResourceType, Integer>() {
            {
                put(ResourceType.Wood, 1);
                put(ResourceType.Rock, 3);
            }
        });
        Card one = new Card(CardType.Building, Collections.singletonList(onecost), Arrays.asList(oneone, onetwo), 25, "Zecca", "");

        Effect twoone = new ImmediateEffect(Collections.singletonMap(ResourceType.VictoryPoint, 5));
        Effect twotwo = new CardResourceEffect(CardType.Territory, ResourceType.Gold, 1, 5, PositionType.ProductionAction);
        Cost twocost = new Cost(new HashMap<ResourceType, Integer>() {
            {
                put(ResourceType.Wood, 3);
                put(ResourceType.Rock, 1);
            }
        });
        Card two = new Card(CardType.Building, Collections.singletonList(twocost), Arrays.asList(twoone, twotwo), 26, "Esattoria", "");

        Effect threeone = new ImmediateEffect(Collections.singletonMap(ResourceType.VictoryPoint, 6));
        Effect threetwo = new CardResourceEffect(CardType.Challenge, ResourceType.VictoryPoint, 1, 6, PositionType.ProductionAction);
        Cost threecost = new Cost(new HashMap<ResourceType, Integer>() {
            {
                put(ResourceType.Gold, 2);
                put(ResourceType.Rock, 2);
            }
        });
        Card three = new Card(CardType.Building, Collections.singletonList(threecost), Arrays.asList(threeone, threetwo), 27, "Arco di Trionfo", "");

        Effect fourone = new ImmediateEffect(Collections.singletonMap(ResourceType.VictoryPoint, 6));
        Effect fourtwo = new CardResourceEffect(CardType.Personality, ResourceType.VictoryPoint, 1, 6, PositionType.ProductionAction);
        Cost fourcost = new Cost(new HashMap<ResourceType, Integer>() {
            {
                put(ResourceType.Gold, 2);
                put(ResourceType.Wood, 2);
            }
        });
        Card four = new Card(CardType.Building, Collections.singletonList(fourcost), Arrays.asList(fourone, fourtwo), 28, "Teatro", "");

        Effect fiveone = new ImmediateEffect(Collections.singletonMap(ResourceType.VictoryPoint, 3));
        Effect fivetwo = new TransformResourcesEffect(Collections.singletonMap(ResourceType.Wood, 1), Collections.singletonMap(ResourceType.Gold, 3), 4);
        Effect fivethree = new TransformResourcesEffect(Collections.singletonMap(ResourceType.Wood, 2), Collections.singletonMap(ResourceType.Gold, 5), 4);
        Cost fivecost = new Cost(new HashMap<ResourceType, Integer>() {
            {
                put(ResourceType.Gold, 1);
                put(ResourceType.Wood, 2);
            }
        });
        Card five = new Card(CardType.Building, Collections.singletonList(fivecost), Arrays.asList(fiveone, fivetwo, fivethree), 29, "Falegnameria", "");

        Effect sixone = new ImmediateEffect(Collections.singletonMap(ResourceType.VictoryPoint, 2));
        Effect sixtwo = new TransformResourcesEffect(Collections.singletonMap(ResourceType.Rock, 1), Collections.singletonMap(ResourceType.Gold, 3), 3);
        Effect sixthree = new TransformResourcesEffect(Collections.singletonMap(ResourceType.Rock, 2), Collections.singletonMap(ResourceType.Gold, 5), 3);
        Cost sixcost = new Cost(new HashMap<ResourceType, Integer>() {
            {
                put(ResourceType.Gold, 1);
                put(ResourceType.Rock, 2);
            }
        });
        Card six = new Card(CardType.Building, Collections.singletonList(sixcost), Arrays.asList(sixone, sixtwo, sixthree), 30, "Tagliapietre", "");

        Effect sevenone = new ImmediateEffect(Collections.singletonMap(ResourceType.FaithPoint, 1));
        Effect seventwo = new TransformResourcesEffect(Collections.singletonMap(ResourceType.Gold, 1), Collections.singletonMap(ResourceType.FaithPoint, 1), 2);
        Cost sevencost = new Cost(Collections.singletonMap(ResourceType.Wood, 2));
        Card seven = new Card(CardType.Building, Collections.singletonList(sevencost), Arrays.asList(sevenone, seventwo), 31, "Cappella", "");

        Effect eightone = new ImmediateEffect(Collections.singletonMap(ResourceType.VictoryPoint, 1));
        Effect eighttwo = new TransformResourcesEffect(Collections.singletonMap(ResourceType.Gold, 1), Collections.singletonMap(ResourceType.Favor, 1), 1);
        Cost eightcost = new Cost(Collections.singletonMap(ResourceType.Rock, 2));
        Card eight = new Card(CardType.Building, Collections.singletonList(eightcost), Arrays.asList(eightone, eighttwo), 32, "Residenza", "");

        Effect nineone = new ImmediateEffect(Collections.singletonMap(ResourceType.VictoryPoint, 3));
        Effect ninetwo = new TransformResourcesEffect(Collections.singletonMap(ResourceType.Gold, 3), new HashMap<ResourceType, Integer>() {
            {
                put(ResourceType.Wood, 2);
                put(ResourceType.Rock, 2);
            }
        }, 3);
        Cost ninecost = new Cost(new HashMap<ResourceType, Integer>() {
            {
                put(ResourceType.Wood, 2);
                put(ResourceType.Rock, 1);
            }
        });
        Card nine = new Card(CardType.Building, Collections.singletonList(ninecost), Arrays.asList(nineone, ninetwo), 33, "Mercato", "");

        Effect tenone = new ImmediateEffect(Collections.singletonMap(ResourceType.VictoryPoint, 4));
        Effect tentwo = new TransformResourcesEffect(Collections.singletonMap(ResourceType.Gold, 1), Collections.singletonMap(ResourceType.VictoryPoint, 3), 3);
        Effect tenthree = new TransformResourcesEffect(Collections.singletonMap(ResourceType.Gold, 2), Collections.singletonMap(ResourceType.VictoryPoint, 5), 3);
        Cost tencost = new Cost(Collections.singletonMap(ResourceType.Wood, 3));
        Card ten = new Card(CardType.Building, Collections.singletonList(tencost), Arrays.asList(tenone, tentwo, tenthree), 34, "Tesoreria", "");

        Effect elevenone = new ImmediateEffect(Collections.singletonMap(ResourceType.VictoryPoint, 5));
        Effect eleventwo = new TransformResourcesEffect(Collections.singletonMap(ResourceType.Wood, 1), Collections.singletonMap(ResourceType.VictoryPoint, 3), 4);
        Effect eleventhree = new TransformResourcesEffect(Collections.singletonMap(ResourceType.Wood, 3), Collections.singletonMap(ResourceType.VictoryPoint, 7), 4);
        Cost elevencost = new Cost(Collections.singletonMap(ResourceType.Wood, 4));
        Card eleven = new Card(CardType.Building, Collections.singletonList(elevencost), Arrays.asList(elevenone, eleventwo, eleventhree), 35, "Gilda dei Pittori", "");

        Effect twelveone = new ImmediateEffect(Collections.singletonMap(ResourceType.VictoryPoint, 6));
        Effect twelvetwo = new TransformResourcesEffect(Collections.singletonMap(ResourceType.Rock, 1), Collections.singletonMap(ResourceType.VictoryPoint, 3), 5);
        Effect twelvethree = new TransformResourcesEffect(Collections.singletonMap(ResourceType.Rock, 3), Collections.singletonMap(ResourceType.VictoryPoint, 7), 5);
        Cost twelvecost = new Cost(Collections.singletonMap(ResourceType.Rock, 4));
        Card twelve = new Card(CardType.Building, Collections.singletonList(twelvecost), Arrays.asList(twelveone, twelvetwo, twelvethree), 36, "Gilda degli Scultori", "");

        Effect thirteenone = new ImmediateEffect(Collections.singletonMap(ResourceType.VictoryPoint, 4));
        Effect thirteentwo = new TransformResourcesEffect(new HashMap<ResourceType, Integer>() {
            {
                put(ResourceType.Slave, 1);
                put(ResourceType.Wood, 1);
                put(ResourceType.Rock, 1);
            }
        }, Collections.singletonMap(ResourceType.VictoryPoint, 6), 4);
        Cost thirteencost = new Cost(new HashMap<ResourceType, Integer>() {
            {
                put(ResourceType.Wood, 1);
                put(ResourceType.Rock, 2);
            }
        });
        Card thirteen = new Card(CardType.Building, Collections.singletonList(thirteencost), Arrays.asList(thirteenone, thirteentwo), 37, "Gilda dei Costruttori", "");

        Effect fourteenone = new ImmediateEffect(new HashMap<ResourceType, Integer>(){
            {
                put(ResourceType.VictoryPoint, 2);
                put(ResourceType.FaithPoint, 1);
            }
        });
        Effect fourteentwo = new TransformResourcesEffect(Collections.singletonMap(ResourceType.FaithPoint, 1), new HashMap<ResourceType, Integer>(){
            {
                put(ResourceType.Gold, 2);
                put(ResourceType.VictoryPoint, 2);
            }
        }, 2);
        Cost fourteencost = new Cost(Collections.singletonMap(ResourceType.Rock, 3));
        Card fourteen = new Card(CardType.Building, Collections.singletonList(fourteencost), Arrays.asList(fourteenone, fourteentwo), 38, "Battistero", "");

        Effect fifteenone = new ImmediateEffect(Collections.singletonMap(ResourceType.VictoryPoint, 3));
        Effect fifteentwo = new TransformResourcesEffect(Collections.singletonMap(ResourceType.Slave, 1), Collections.singletonMap(ResourceType.MilitaryPoint, 3), 1);
        Cost fifteencost = new Cost(new HashMap<ResourceType, Integer>() {
            {
                put(ResourceType.Wood, 1);
                put(ResourceType.Rock, 1);
            }
        });
        Card fifteen = new Card(CardType.Building, Collections.singletonList(fifteencost), Arrays.asList(fifteenone, fifteentwo), 39, "Caserma", "");

        Effect sixteenone = new ImmediateEffect(Collections.singletonMap(ResourceType.VictoryPoint, 8));
        Effect sixteentwo = new AddResourcesEffect(new HashMap<ResourceType, Integer>() {
            {
                put(ResourceType.MilitaryPoint, 2);
                put(ResourceType.VictoryPoint, 2);
            }
        }, 6, PositionType.ProductionAction);
        Cost sixteencost = new Cost(new HashMap<ResourceType, Integer>() {
            {
                put(ResourceType.Gold, 2);
                put(ResourceType.Wood, 2);
                put(ResourceType.Rock, 2);
            }
        });
        Card sixteen = new Card(CardType.Building, Collections.singletonList(sixteencost), Arrays.asList(sixteenone, sixteentwo), 40, "Fortezza", "");

        Effect seventeenone = new ImmediateEffect(Collections.singletonMap(ResourceType.VictoryPoint, 7));
        Effect seventeentwo = new AddResourcesEffect(Collections.singletonMap(ResourceType.Gold, 5), 2, PositionType.ProductionAction);
        Cost seventeencost = new Cost(new HashMap<ResourceType, Integer>() {
            {
                put(ResourceType.Gold, 3);
                put(ResourceType.Wood, 1);
                put(ResourceType.Rock, 3);
            }
        });
        Card seventeen = new Card(CardType.Building, Collections.singletonList(seventeencost), Arrays.asList(seventeenone, seventeentwo), 41, "Banca", "");

        Effect eighteenone = new ImmediateEffect(Collections.singletonMap(ResourceType.VictoryPoint, 8));
        Effect eighteentwo = new TransformResourcesEffect(Collections.singletonMap(ResourceType.Gold, 4), new HashMap<ResourceType, Integer>(){
            {
                put(ResourceType.Wood, 3);
                put(ResourceType.Rock, 3);
            }
        }, 4);
        Cost eighteencost = new Cost(new HashMap<ResourceType, Integer>(){
            {
                put(ResourceType.Gold, 4);
                put(ResourceType.Wood, 3);
            }
        });
        Card eighteen = new Card(CardType.Building, Collections.singletonList(eighteencost), Arrays.asList(eighteenone, eighteentwo), 42, "Fiera", "");

        Effect nineteenone = new ImmediateEffect(Collections.singletonMap(ResourceType.VictoryPoint, 10));
        Effect nineteentwo = new AddResourcesEffect(Collections.singletonMap(ResourceType.VictoryPoint, 3), 1, PositionType.ProductionAction);
        Cost nineteencost = new Cost(new HashMap<ResourceType, Integer>(){
            {
                put(ResourceType.Slave, 2);
                put(ResourceType.Wood, 4);
                put(ResourceType.Rock, 2);
            }
        });
        Card nineteen = new Card(CardType.Building, Collections.singletonList(nineteencost), Arrays.asList(nineteenone, nineteentwo), 43, "Giardino", "");

        Effect twentyonee = new ImmediateEffect(Collections.singletonMap(ResourceType.VictoryPoint, 9));
        Effect twentytwoe = new AddResourcesEffect(new HashMap<ResourceType, Integer>(){
            {
                put(ResourceType.VictoryPoint, 2);
                put(ResourceType.Favor, 1);
            }
        }, 5, PositionType.ProductionAction);
        Cost twentycost = new Cost(new HashMap<ResourceType, Integer>(){
            {
                put(ResourceType.Gold, 2);
                put(ResourceType.Wood, 2);
                put(ResourceType.Rock, 4);
            }
        });
        Card twenty = new Card(CardType.Building, Collections.singletonList(twentycost), Arrays.asList(twentyonee, twentytwoe), 44, "Catelletto", "");

        Effect twentyoneone = new ImmediateEffect(Collections.singletonMap(ResourceType.VictoryPoint, 9));
        Effect twentyonetwo = new TransformResourcesEffect(Collections.singletonMap(ResourceType.Gold, 1), new HashMap<ResourceType, Integer>(){
            {
                put(ResourceType.Slave, 2);
                put(ResourceType.VictoryPoint, 4);
            }
        }, 6);
        Cost twentyonecost = new Cost(new HashMap<ResourceType, Integer>() {
            {
                put(ResourceType.Gold, 3);
                put(ResourceType.Wood, 3);
                put(ResourceType.Rock, 1);
            }
        });
        Card twentyone = new Card(CardType.Building, Collections.singletonList(twentyonecost), Arrays.asList(twentyoneone, twentyonetwo), 45, "Palazzo", "");

        Effect twentytwoone = new ImmediateEffect(new HashMap<ResourceType, Integer>() {
            {
                put(ResourceType.VictoryPoint, 5);
                put(ResourceType.FaithPoint, 1);
            }
        });
        Effect twentytwotwo = new TransformResourcesEffect(Collections.singletonMap(ResourceType.Wood, 1), Collections.singletonMap(ResourceType.FaithPoint, 2), 1);
        Effect twentytwothree = new TransformResourcesEffect(Collections.singletonMap(ResourceType.Rock, 1), Collections.singletonMap(ResourceType.FaithPoint, 2), 1);
        Cost twentytwocost = new Cost(new HashMap<ResourceType, Integer>(){
            {
                put(ResourceType.Wood, 1);
                put(ResourceType.Rock, 4);
            }
        });
        Card twentytwo = new Card(CardType.Building, Collections.singletonList(twentytwocost), Arrays.asList(twentytwoone, twentytwotwo), 46, "Basilica", "");

        Effect twentythreeone = new ImmediateEffect(Collections.singletonMap(ResourceType.VictoryPoint, 7));
        Effect twentythreetwo = new TransformResourcesEffect(Collections.singletonMap(ResourceType.Slave, 1), new HashMap<ResourceType, Integer>(){
            {
                put(ResourceType.MilitaryPoint, 3);
                put(ResourceType.VictoryPoint, 1);
            }
        }, 3);
        Cost twentythreecost = new Cost(new HashMap<ResourceType, Integer>(){
            {
                put(ResourceType.Slave, 1);
                put(ResourceType.Wood, 2);
                put(ResourceType.Rock, 2);
            }
        });
        Card twentythree = new Card(CardType.Building, Collections.singletonList(twentythreecost), Arrays.asList(twentythreeone, twentythreetwo), 47, "Accademia Militare", "");

        Effect twentyfourone = new ImmediateEffect(new HashMap<ResourceType, Integer>(){
            {
                put(ResourceType.VictoryPoint, 7);
                put(ResourceType.FaithPoint, 3);
            }
        });
        Effect twentyfourtwo = new AddResourcesEffect(Collections.singletonMap(ResourceType.VictoryPoint, 1), 2, PositionType.ProductionAction);
        Cost twentyfourcost = new Cost(new HashMap<ResourceType, Integer>(){
            {
                put(ResourceType.Wood, 4);
                put(ResourceType.Rock, 4);
            }
        });
        Card twentyfour = new Card(CardType.Building, Collections.singletonList(twentyfourcost), Arrays.asList(twentyfourone, twentyfourtwo), 48, "Cattedrale", "");


        return new HashMap<Integer, List<Card>>() {
            {
                put(1, Arrays.asList(one, two, three, four, five, six, seven, eight));
                put(2, Arrays.asList(nine, ten, eleven, twelve, thirteen, fourteen, fifteen, sixteen));
                put(3, Arrays.asList(seventeen, eighteen, nineteen, twenty, twentyone, twentytwo, twentythree, twentyfour));
            }
        };
    }

    private static Map<Integer, List<Card>> territoryCards() {

        Effect oneone = new AddResourcesEffect(Collections.singletonMap(ResourceType.Gold, 1), 1, PositionType.HarvestAction);
        Card one = new Card(CardType.Territory, Collections.singletonList(new Cost(null)), Collections.singletonList(oneone), 1, "Avamposto Commerciale", "");

        Effect twoone = new ImmediateEffect(Collections.singletonMap(ResourceType.Wood, 1));
        Effect twotwo = new AddResourcesEffect(Collections.singletonMap(ResourceType.Wood, 1), 2, PositionType.HarvestAction);
        Card two = new Card(CardType.Territory, Collections.singletonList(new Cost(null)), Arrays.asList(twoone, twotwo), 2, "Bosco", "");

        Map<ResourceType, Integer> threeRes = new HashMap<>();
        threeRes.put(ResourceType.Gold, 1);
        threeRes.put(ResourceType.Slave, 1);
        Effect threeone = new AddResourcesEffect(threeRes, 3, PositionType.HarvestAction);
        Card three = new Card(CardType.Territory, Collections.singletonList(new Cost(null)), Collections.singletonList(threeone), 3, "Borgo", "");

        Effect fourone = new ImmediateEffect(Collections.singletonMap(ResourceType.Rock, 2));
        Effect fourtwo = new AddResourcesEffect(Collections.singletonMap(ResourceType.Rock, 2), 4, PositionType.HarvestAction);
        Card four = new Card(CardType.Territory, Collections.singletonList(new Cost(null)), Arrays.asList(fourone, fourtwo), 4, "Cava di Ghiaia", "");

        Effect fiveone = new ImmediateEffect(Collections.singletonMap(ResourceType.Wood, 1));
        Effect fivetwo = new AddResourcesEffect(Collections.singletonMap(ResourceType.Wood, 3), 5, PositionType.HarvestAction);
        Card five = new Card(CardType.Territory, Collections.singletonList(new Cost(null)), Arrays.asList(fiveone, fivetwo), 5, "Foresta", "");

        Effect sixone = new ImmediateEffect(new HashMap<ResourceType, Integer>() {
            {
                put(ResourceType.Slave, 1);
                put(ResourceType.MilitaryPoint, 2);
            }
        });
        Effect sixtwo = new AddResourcesEffect(new HashMap<ResourceType, Integer>() {
            {
                put(ResourceType.FaithPoint, 1);
                put(ResourceType.Rock, 1);
            }
        }, 6, PositionType.HarvestAction);
        Card six = new Card(CardType.Territory, Collections.singletonList(new Cost(null)), Arrays.asList(sixone, sixtwo), 6, "Monastero", "");

        Effect sevenone = new AddResourcesEffect(new HashMap<ResourceType, Integer>() {
            {
                put(ResourceType.MilitaryPoint, 2);
                put(ResourceType.Rock, 1);
            }
        }, 5, PositionType.HarvestAction);
        Card seven = new Card(CardType.Territory, Collections.singletonList(new Cost(null)), Collections.singletonList(sevenone), 7, "Rocca", "");

        Effect eightone = new ImmediateEffect(Collections.singletonMap(ResourceType.Gold, 3));
        Effect eighttwo = new AddResourcesEffect(Collections.singletonMap(ResourceType.Favor, 1), 6, PositionType.HarvestAction);
        Card eight = new Card(CardType.Territory, Collections.singletonList(new Cost(null)), Arrays.asList(eightone, eighttwo), 8, "Città", "");

        Effect nineone = new ImmediateEffect(Collections.singletonMap(ResourceType.Gold, 1));
        Effect ninetwo = new AddResourcesEffect(Collections.singletonMap(ResourceType.Gold, 2), 1, PositionType.HarvestAction);
        Card nine = new Card(CardType.Territory, Collections.singletonList(new Cost(null)), Arrays.asList(nineone, ninetwo), 9, "Miniera d'oro", "");

        Effect tenone = new ImmediateEffect(Collections.singletonMap(ResourceType.Slave, 1));
        Effect tentwo = new AddResourcesEffect(new HashMap<ResourceType, Integer>() {
            {
                put(ResourceType.MilitaryPoint, 1);
                put(ResourceType.Wood, 2);
            }
        }, 3, PositionType.HarvestAction);
        Card ten = new Card(CardType.Territory, Collections.singletonList(new Cost(null)), Arrays.asList(tenone, tentwo), 10, "Villaggio Montano", "");

        Effect elevenone = new ImmediateEffect(new HashMap<ResourceType, Integer>() {
            {
                put(ResourceType.Slave, 2);
                put(ResourceType.Rock, 1);
            }
        });
        Effect eleventwo = new AddResourcesEffect(new HashMap<ResourceType, Integer>() {
            {
                put(ResourceType.Slave, 1);
                put(ResourceType.Rock, 2);
            }
        }, 4, PositionType.HarvestAction);
        Card eleven = new Card(CardType.Territory, Collections.singletonList(new Cost(null)), Arrays.asList(elevenone, eleventwo), 11, "Villaggio Minerario", "");

        Effect twelveone = new ImmediateEffect(Collections.singletonMap(ResourceType.Wood, 1));
        Effect twelvetwo = new AddResourcesEffect(Collections.singletonMap(ResourceType.Rock, 3), 3, PositionType.HarvestAction);
        Card twelve = new Card(CardType.Territory, Collections.singletonList(new Cost(null)), Arrays.asList(twelveone, twelvetwo), 12, "Cava di Pietra", "");

        Effect thirteenone = new ImmediateEffect(new HashMap<ResourceType, Integer>() {
            {
                put(ResourceType.Slave, 2);
                put(ResourceType.Wood, 1);
            }
        });
        Effect thirteentwo = new AddResourcesEffect(new HashMap<ResourceType, Integer>() {
            {
                put(ResourceType.Gold, 1);
                put(ResourceType.Wood, 2);
            }
        }, 4, PositionType.HarvestAction);
        Card thirteen = new Card(CardType.Territory, Collections.singletonList(new Cost(null)), Arrays.asList(thirteenone, thirteentwo), 13, "Possedimento", "");

        Effect fourteenone = new ImmediateEffect(Collections.singletonMap(ResourceType.FaithPoint, 1));
        Effect fourteentwo = new AddResourcesEffect(Collections.singletonMap(ResourceType.FaithPoint, 1), 2, PositionType.HarvestAction);
        Card fourteen = new Card(CardType.Territory, Collections.singletonList(new Cost(null)), Arrays.asList(fourteenone, fourteentwo), 14, "Eremo", "");

        Effect fifteenone = new AddResourcesEffect(new HashMap<ResourceType, Integer>() {
            {
                put(ResourceType.MilitaryPoint, 2);
                put(ResourceType.Slave, 2);
            }
        }, 5, PositionType.HarvestAction);
        Card fifteen = new Card(CardType.Territory, Collections.singletonList(new Cost(null)), Collections.singletonList(fifteenone), 15, "Maniero", "");

        Effect sixteenone = new ImmediateEffect(Collections.singletonMap(ResourceType.Gold, 4));
        Effect sixteentwo = new AddResourcesEffect(new HashMap<ResourceType, Integer>() {
            {
                put(ResourceType.Gold, 1);
                put(ResourceType.Rock, 1);
                put(ResourceType.Wood, 2);
            }
        }, 6, PositionType.HarvestAction);
        Card sixteen = new Card(CardType.Territory, Collections.singletonList(new Cost(null)), Arrays.asList(sixteenone, sixteentwo), 16, "Ducato", "");

        Effect seventeenone = new ImmediateEffect(new HashMap<ResourceType, Integer>() {
            {
                put(ResourceType.Gold, 1);
                put(ResourceType.Slave, 1);
            }
        });
        Effect seventeentwo = new AddResourcesEffect(Collections.singletonMap(ResourceType.Gold, 3), 1, PositionType.HarvestAction);
        Card seventeen = new Card(CardType.Territory, Collections.singletonList(new Cost(null)), Arrays.asList(seventeenone, seventeentwo), 17, "Città Mercantile", "");

        Effect eighteenone = new ImmediateEffect(new HashMap<ResourceType, Integer>() {
            {
                put(ResourceType.VictoryPoint, 1);
                put(ResourceType.Wood, 1);
            }
        });
        Effect eighteentwo = new AddResourcesEffect(new HashMap<ResourceType, Integer>(){
            {
                put(ResourceType.VictoryPoint, 2);
                put(ResourceType.Wood, 2);
            }
        }, 3, PositionType.HarvestAction);
        Card eighteen = new Card(CardType.Territory, Collections.singletonList(new Cost(null)), Arrays.asList(eighteenone, eighteentwo), 18, "Tenuta", "");

        Effect nineteenone = new ImmediateEffect(Collections.singletonMap(ResourceType.MilitaryPoint, 2));
        Effect nineteentwo = new AddResourcesEffect(new HashMap<ResourceType, Integer>() {
            {
                put(ResourceType.VictoryPoint, 4);
                put(ResourceType.Wood, 1);
            }
        }, 5, PositionType.HarvestAction);
        Card nineteen = new Card(CardType.Territory, Collections.singletonList(new Cost(null)), Arrays.asList(nineteenone, nineteentwo), 19, "Colonia", "");

        Effect twentyonee = new ImmediateEffect(Collections.singletonMap(ResourceType.VictoryPoint, 3));
        Effect twentytwoe = new AddResourcesEffect(new HashMap<ResourceType, Integer>() {
            {
                put(ResourceType.VictoryPoint, 1);
                put(ResourceType.Rock, 2);
            }
        }, 2, PositionType.HarvestAction);
        Card twenty = new Card(CardType.Territory, Collections.singletonList(new Cost(null)), Arrays.asList(twentyonee, twentytwoe), 20, "Cava di Marmo", "");

        Effect twentyoneone = new ImmediateEffect(new HashMap<ResourceType, Integer>() {
            {
                put(ResourceType.Favor, 1);
                put(ResourceType.Rock, 1);
            }
        });
        Effect twentyonetwo = new AddResourcesEffect(new HashMap<ResourceType, Integer>() {
            {
                put(ResourceType.VictoryPoint, 4);
                put(ResourceType.Rock, 1);
            }
        }, 6, PositionType.HarvestAction);
        Card twentyone = new Card(CardType.Territory, Collections.singletonList(new Cost(null)), Arrays.asList(twentyoneone, twentyonetwo), 21, "Provincia", "");

        Effect twentytwoone = new ImmediateEffect(Collections.singletonMap(ResourceType.FaithPoint, 1));
        Effect twentytwotwo = new AddResourcesEffect(new HashMap<ResourceType, Integer>() {
            {
                put(ResourceType.Gold, 1);
                put(ResourceType.FaithPoint, 1);
            }
        }, 1, PositionType.HarvestAction);
        Card twentytwo = new Card(CardType.Territory, Collections.singletonList(new Cost(null)), Arrays.asList(twentytwoone, twentytwotwo), 22, "Santuario", "");

        Effect twentythreeone = new ImmediateEffect(new HashMap<ResourceType, Integer>() {
            {
                put(ResourceType.VictoryPoint, 2);
                put(ResourceType.Gold, 2);
            }
        });
        Effect twentythreetwo = new AddResourcesEffect(new HashMap<ResourceType, Integer>() {
            {
                put(ResourceType.MilitaryPoint, 3);
                put(ResourceType.Slave, 1);
            }
        }, 4, PositionType.HarvestAction);
        Card twentythree = new Card(CardType.Territory, Collections.singletonList(new Cost(null)), Arrays.asList(twentythreeone, twentythreetwo), 23, "Castello", "");

        Effect twentyfourone = new ImmediateEffect(new HashMap<ResourceType, Integer>() {
            {
                put(ResourceType.MilitaryPoint, 2);
                put(ResourceType.Slave, 1);
            }
        });
        Effect twentyfourtwo = new AddResourcesEffect(new HashMap<ResourceType, Integer>(){
            {
                put(ResourceType.MilitaryPoint, 1);
                put(ResourceType.Slave, 2);
            }
        }, 2, PositionType.HarvestAction);
        Card twentyfour = new Card(CardType.Territory, Collections.singletonList(new Cost(null)), Arrays.asList(twentyfourone, twentyfourtwo), 24, "Città Fortificata", "");

        return new HashMap<Integer, List<Card>>() {
            {
                put(1, Arrays.asList(one, two, three, four, five, six, seven, eight));
                put(2, Arrays.asList(nine, ten, eleven, twelve, thirteen, fourteen, fifteen, sixteen));
                put(3, Arrays.asList(seventeen, eighteen, nineteen, twenty, twentyone, twentytwo, twentythree, twentyfour));
            }
        };
    }
    */

}
