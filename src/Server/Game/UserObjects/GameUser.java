package Server.Game.UserObjects;

import Action.ChooseFavor;
import Game.Usable.ResourceType;
import Game.UserObjects.DomesticColor;
import Game.UserObjects.FamilyColor;
import Networking.CommLink;
import Networking.Gson.GsonUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by fiore on 16/05/2017.
 */
public class GameUser implements Game.UserObjects.GameUser {

    private final FamilyColor familyColor;

    private final Map<DomesticColor, Domestic> domestics = new HashMap<>();

    private final Map<DomesticColor, Integer> penalty = new HashMap<>();

    private final CommLink userLink;

    private volatile transient Game.UserObjects.PlayerState currentState;

    private volatile boolean roundJump = false;

    private final AtomicInteger moves = new AtomicInteger(0);

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
        newValues.forEach((color, value) -> {
            if(penalty.containsKey(color))
                domestics.get(color).setValue(value - penalty.get(color));
            else
                domestics.get(color).setValue(value);
        });

        // TODO: send domestic values update to user (send to this only)
    }

    @Override
    public void setDomesticPenalty(DomesticColor color, int value) {
        penalty.put(color, value);
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
        return currentState.clone();
    }

    @Override
    public void updateUserState(Game.UserObjects.PlayerState newState) {
        currentState = newState;

        // Get favors added if any
        final int newFavors = currentState.getResources().get(ResourceType.Favor);

        // Ask user to choose gained favors
        if(newFavors > 0) {

            // Add new move to wait for
            setHasMoved(false);

            // Send council favors to client to let him choose
            getUserLink().sendMessage(new ChooseFavor(newFavors));

            // Reset favors to zero
            currentState.setResources(Collections.singletonMap(ResourceType.Favor, 0), false);
        }

        // TODO: send update to users (send to all)
    }

    @Override
    public void setHasMoved(boolean hasMoved) {

        if(!hasMoved)
            moves.incrementAndGet();
        else
            moves.decrementAndGet();

    }

    @Override
    public boolean getHasMoved() {
        return moves.compareAndSet(0,0);
    }
}
