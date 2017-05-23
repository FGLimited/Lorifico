package Server.Game.UserObjects;

import Game.UserObjects.DomesticColor;
import Game.UserObjects.FamilyColor;
import Networking.CommLink;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by fiore on 16/05/2017.
 */
public class GameUser implements Game.UserObjects.GameUser {

    private final FamilyColor familyColor;

    private final Map<DomesticColor, Domestic> domestics = new HashMap<>();

    private final transient CommLink userLink;

    private volatile transient Game.UserObjects.PlayerState currentState;

    private volatile boolean roundJump = false;

    /**
     * Initialize a new game user with player state
     *
     * @param userLink User comm link
     * @param familyColor Family color for this user
     */
    public GameUser(CommLink userLink, FamilyColor familyColor) {
        this.familyColor = familyColor;
        this.userLink = userLink;

        for (DomesticColor color : DomesticColor.values())
            domestics.put(color, new Domestic(familyColor, color, 0));

        currentState = new Server.Game.UserObjects.PlayerState(this);
    }

    @Override
    public FamilyColor getFamilyColor() {
        return familyColor;
    }

    @Override
    public CommLink getUserLink() {
        return userLink;
    }

    @Override
    public Map<DomesticColor, Domestic> getDomestics() {
        return new HashMap<>(domestics);
    }

    @Override
    public void setDomestics(Map<DomesticColor, Integer> newValues) {
        // Update domestic value
        newValues.forEach((color, value) -> domestics.get(color).setValue(value));

        // TODO: send domestic values update to user (send to this only)
    }

    @Override
    public boolean getRoundJump() {
        return roundJump;
    }

    @Override
    public void setRoundJump(boolean jumpRound) {
        roundJump = jumpRound;
    }

    @Override
    public Game.UserObjects.PlayerState getUserState() {
        return currentState.clone();
    }

    @Override
    public void updateUserState(Game.UserObjects.PlayerState newState) {
        currentState = newState;

        // TODO: send update to users (send to all)
    }
}
