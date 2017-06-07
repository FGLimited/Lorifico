package Game.UserObjects;

import Networking.CommLink;
import Server.Game.UserObjects.Domestic;
import java.util.Map;

/**
 * Created by fiore on 08/05/2017.
 */
public interface GameUser {

    /**
     * Get user's family color
     *
     * @return Family color
     */
    FamilyColor getFamilyColor();

    /**
     * Get user comm link
     *
     * @return Comm link
     */
    CommLink getUserLink();

    /**
     * Get all user's domestics
     *
     * @return Domestic's map
     */
    Map<DomesticColor, Domestic> getDomestics();

    /**
     * Get current user state (state is not passed by reference)
     *
     * @return Current user state object
     */
    PlayerState getUserState();

}
