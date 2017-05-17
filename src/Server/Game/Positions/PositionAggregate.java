package Server.Game.Positions;

import Game.Positions.Position;
import Server.Game.UserObjects.Domestic;
import Game.UserObjects.DomesticColor;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by fiore on 10/05/2017.
 *
 * Represents a set of aggregated positions (as a tower or harvest/production place)
 *
 */
public class PositionAggregate {

    private final List<Position> positions;

    /**
     * Create a new aggregate and set it in all given positions
     *
     * @param positions Positions to aggregate
     */
    public static void aggregate(List<? extends Position> positions) {

        new PositionAggregate(positions);

    }

    /**
     * Initialize thi aggregate with given positions
     *
     * @param positions Position to aggregate
     */
    private PositionAggregate(List<? extends Position> positions) {
        positions.forEach(position -> position.setAggregate(this));
        this.positions = new ArrayList<>(positions);
    }

    /**
     * Check if any position of the aggregate is occupied
     *
     * @return True if any position is occupied, false else
     */
    boolean isOccupied() {
        return positions.parallelStream().filter(position -> position.isOccupied() != null).count() != 0;
    }

    /**
     * Check if a domestic color and family for this aggregate (no value check is performed)
     *
     * @param inUse Current in use domestic
     * @return True if domestic can be placed, false else
     */
    boolean canOccupy(Domestic inUse) {

        // If domestic is neutral can always be placed
        // else check if a non neutral domestic of the same family is already present
        return inUse.getType() == DomesticColor.Neutral
                || positions.parallelStream()
                .filter(position -> {
                    Domestic current = position.isOccupied();

                    return current != null
                            && current.getType() != DomesticColor.Neutral
                            && current.getFamilyColor() == inUse.getFamilyColor();
                })
                .count() == 0;
    }
}
