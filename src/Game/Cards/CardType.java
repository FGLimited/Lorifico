package Game.Cards;

import Game.Positions.PositionType;

/**
 * Created by fiore on 10/05/2017.
 */
public enum CardType {
    Territory(Game.Positions.PositionType.TerritoryTower),
    Building(Game.Positions.PositionType.BuildingTower),
    Personality(Game.Positions.PositionType.PersonalityTower),
    Challenge(Game.Positions.PositionType.ChallengeTower);

    public final PositionType PositionType;

    CardType(PositionType positionType) {
        PositionType = positionType;
    }
}
