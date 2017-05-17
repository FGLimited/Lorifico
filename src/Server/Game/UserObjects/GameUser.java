package Server.Game.UserObjects;

import Game.UserObjects.*;
import Networking.CommLink;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by fiore on 16/05/2017.
 */
public class GameUser implements Game.UserObjects.GameUser {

    private final FamilyColor familyColor;

    private final Map<DomesticColor, Domestic> domestics = new HashMap<>();

    private final CommLink userLink;

    private volatile Game.UserObjects.PlayerState currentState;

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
        newValues.forEach((color, value) -> domestics.get(color).setValue(value));
    }

    @Override
    public void setRoundJump(boolean jumpRound) {
        roundJump = jumpRound;
    }

    @Override
    public boolean getRoundJump() {
        return roundJump;
    }

    @Override
    public Game.UserObjects.PlayerState getUserState() {
        return currentState;
    }

    @Override
    public void updateUserState(Game.UserObjects.PlayerState newState) {
        currentState = newState;
    }
}
