package Game.Positions;

import Game.Effects.Effect;
import Server.Game.Usable.Cost;

/**
 * Created by fiore on 10/05/2017.
 */
public enum PositionType {
    HarvestAction(Effect.class),
    ProductionAction(Effect.class),
    TerritoryTower(Cost.class),
    BuildingTower(Cost.class),
    PersonalityTower(Cost.class),
    ChallengeTower(Cost.class),
    Market(Cost.class),
    Council(Cost.class);

    public final Class tType;

    PositionType(Class tType) {
        this.tType = tType;
    }
}
