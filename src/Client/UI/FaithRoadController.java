package Client.UI;

import Game.UserObjects.FamilyColor;

/**
 * Created by Io on 06/06/2017.
 */
public interface FaithRoadController {

    /**
     * Moves a pawn to given faith position.
     *
     * @param familyColor family color to move
     * @param position    position to go to
     */
    void moveToPosition(FamilyColor familyColor, int position);

    /**
     * Shows passed cards in UI
     *
     * @param first  faith card of first turn
     * @param second faith card of second turn
     * @param third  faith card of third turn
     */
    void showFaithCards(int first, int second, int third);
}
