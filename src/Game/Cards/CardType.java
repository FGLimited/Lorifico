package Game.Cards;

import Game.Positions.PositionType;

/**
 * Created by fiore on 10/05/2017.
 */
public enum CardType {
    Territory("Territorio", Game.Positions.PositionType.TerritoryTower),
    Building("Edificio", Game.Positions.PositionType.BuildingTower),
    Personality("Personaggio", Game.Positions.PositionType.PersonalityTower),
    Challenge("Impresa", Game.Positions.PositionType.ChallengeTower);

    public final PositionType PositionType;

    private final String typeName;

    CardType(String name, PositionType positionType) {
        PositionType = positionType;
        typeName = name;
    }

    public String getName() {
        return typeName;
    }
}
