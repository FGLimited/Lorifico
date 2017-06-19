package Client.UI;

import Game.UserObjects.FamilyColor;

/**
 * Created by Io on 06/06/2017.
 */
public interface FaithRoadController {
    /**
     * Shows passed cards in UI
     *
     * @param first  faith card of first turn
     * @param second faith card of second turn
     * @param third  faith card of third turn
     */
    void showFaithCards(int first, int second, int third);

    /**
     * Shows a faith cube on 1st 2nd or 3rd card
     *
     * @param familyColor color of cube
     * @param cardOrdinal may be 1,2,3
     */
    void showFaithCube(FamilyColor familyColor, int cardOrdinal);
}
