package Server.Game.Effects.Faith;

import Game.Cards.CardType;
import Game.Effects.Effect;
import Game.Positions.PositionType;
import Game.Usable.ResourceType;
import Server.Game.Effects.PositionBonusEffect;
import java.util.*;

/**
 * Created by fiore on 17/05/2017.
 */
public class FaithDeck {

    private final Map<Integer, List<Effect>> faithEffects = new HashMap<>();

    /**
     * Initialize faith cards decks
     */
    public FaithDeck() {

        // TODO: load faith effects from json

        faithEffects.forEach((deckNumber, effects) -> Collections.shuffle(effects));

    }

    /**
     * Get faith effects for this game
     *
     * @return Three faith effects
     */
    public Map<Integer, Effect> getFaithEffect() {
        Map<Integer, Effect> gameFaithEffects = new HashMap<>();

        gameFaithEffects.put(1, null);
        gameFaithEffects.put(2, faithEffects.get(1).get(0));
        gameFaithEffects.put(3, null);
        gameFaithEffects.put(4, faithEffects.get(2).get(0));
        gameFaithEffects.put(5, null);
        gameFaithEffects.put(6, faithEffects.get(3).get(0));

        return gameFaithEffects;
    }

    public static void main(String[] args) {

        Effect one = new ResourcePenaltyEffect(Collections.singletonMap(ResourceType.MilitaryPoint, 1));
        Effect two = new ResourcePenaltyEffect(Collections.singletonMap(ResourceType.Gold, 1));
        Effect three = new ResourcePenaltyEffect(Collections.singletonMap(ResourceType.Slave, 1));

        Map<ResourceType, Integer> rockWood = new HashMap<>();
        rockWood.put(ResourceType.Rock, 1);
        rockWood.put(ResourceType.Wood, 1);
        Effect four = new ResourcePenaltyEffect(rockWood);

        Effect five = new PositionBonusEffect(PositionType.HarvestAction, -3);
        Effect six = new PositionBonusEffect(PositionType.ProductionAction, -3);
        Effect seven = new DomesticPenaltyEffect(1);

        List<Effect> firstDeck = Arrays.asList(one, two, three, four, five, six, seven);


        Effect eight = new PositionBonusEffect(PositionType.TerritoryTower, -4);
        Effect nine = new PositionBonusEffect(PositionType.BuildingTower, -4);
        Effect ten = new PositionBonusEffect(PositionType.PersonalityTower, -4);
        Effect eleven = new PositionBonusEffect(PositionType.ChallengeTower, -4);
        Effect twelve = new MarketDenyEffect();
        Effect thirteen = new SlaveValuePenaltyEffect(2);
        Effect fourteen = new JumpFirstRoundEffect();

        List<Effect> secondDeck = Arrays.asList(eight, nine, ten, eleven, twelve, thirteen, fourteen);


        Effect fifteen = new CardRemoveEffect(CardType.Personality);
        Effect sixteen = new CardRemoveEffect(CardType.Challenge);
        Effect seventeen = new CardRemoveEffect(CardType.Territory);
        Effect eighteen = new PointLoss(Collections.singletonMap(ResourceType.VictoryPoint, 5), 1);
        Effect nineteen = new PointLoss(Collections.singletonMap(ResourceType.MilitaryPoint, 1), 1);
        Effect twenty = new CardCostPenaltyEffect(
                Arrays.asList(ResourceType.Rock, ResourceType.Gold, ResourceType.Slave, ResourceType.Wood),
                1,
                CardType.Building);

        Map<ResourceType, Integer> oneOfAll = new HashMap<>();
        oneOfAll.put(ResourceType.Rock, 1);
        oneOfAll.put(ResourceType.Wood, 1);
        oneOfAll.put(ResourceType.Gold, 1);
        oneOfAll.put(ResourceType.Slave, 1);

        Effect twentyone = new PointLoss(oneOfAll, 1);

        List<Effect> thirdDeck = Arrays.asList(fifteen, sixteen, seventeen, eighteen, nineteen, twenty, twentyone);

        Map<Integer, List<Effect>> faithDeck = new HashMap<>();
        faithDeck.put(1, firstDeck);
        faithDeck.put(2, secondDeck);
        faithDeck.put(3, thirdDeck);

    }

}
