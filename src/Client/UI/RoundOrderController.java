package Client.UI;

import Game.UserObjects.FamilyColor;

import java.util.List;

/**
 * Created by andrea on 07/06/17.
 */
public interface RoundOrderController {

    /**
     * Sets order in which players are playing over turns
     *
     * @param familyColorOrderList
     */
    void setGameOrder(List<FamilyColor> familyColorOrderList);
}
