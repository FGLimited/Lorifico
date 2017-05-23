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
     * Set new domestic values
     *
     * @param newValues New values map
     */
    void setDomestics(Map<DomesticColor, Integer> newValues);

    /**
     * Add permanent penalty value to specified domestic
     *
     * @param color Domestic color
     * @param penalty Penalty value
     */
    void setDomesticPenalty(DomesticColor color, int penalty);

    void setRoundJump(boolean jumpRound);

    boolean getRoundJump();

    /**
     * Get current user state (state is not passed by reference)
     *
     * @return Current user state object
     */
    PlayerState getUserState();

    /**
     * Update user state
     *
     * @param newState New state to update user state
     */
    void updateUserState(PlayerState newState);

    /**
     * Set true when player has performed his move
     *
     * @param hasMoved HasMoved value
     */
    void setHasMoved(boolean hasMoved);

    /**
     * Check if player has moved or not
     *
     * @return True if has moved, false else
     */
    boolean getHasMoved();

}
