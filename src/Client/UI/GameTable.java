package Client.UI;

import Game.UserObjects.Choosable;
import Server.Game.UserObjects.Domestic;

import java.util.List;
import java.util.Map;

/**
 * Created by Io on 10/05/2017.
 */
public interface GameTable {

    /**
     * Called when a new turn is starting and all position must be clean.
     */
    void freeAllPositions();

    /**
     * Adds a domestic to specified position
     *
     * @param occupant       occupant domestic
     * @param positionNumber position to occupy
     */
    void addDomestic(Domestic occupant, int positionNumber);

    /**
     * Updates table position with costs
     *
     * @param choosablePerPos costs per position
     */
    void setCostsPerPosition(Map<Integer, List<Choosable>> choosablePerPos);

    /**
     * Disables market untill end of the game for this user
     */
    void marketDeny();
}
