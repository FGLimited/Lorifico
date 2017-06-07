package Server.Game.UserObjects;

import Action.ChooseFavor;
import Action.DiceDomesticUpdate;
import Action.PlayerStateUpdate;
import Game.Usable.ResourceType;
import Game.UserObjects.DomesticColor;
import Game.UserObjects.FamilyColor;
import Model.User.User;
import Networking.CommLink;
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

    private final transient Map<DomesticColor, Integer> penalty = new HashMap<>();

    private final transient User user;

    private volatile transient PlayerState currentState;

    private volatile transient boolean roundJump = false;

    private volatile transient boolean churchSupport = false;

    private final transient AtomicInteger moves = new AtomicInteger(0);

    /**
     * Initialize a new game user with player state
     *
     * @param user User object
     * @param familyColor Family color for this user
     */
    public GameUser(User user, FamilyColor familyColor) {
        this.familyColor = familyColor;
        this.user = user;

        for (DomesticColor color : DomesticColor.values())
            domestics.put(color, new Domestic(familyColor, color, 0));
    }

    /**
     * Gson constructor
     */
    private GameUser() {
        familyColor = null;
        user = null;
    }

    @Override
    public FamilyColor getFamilyColor() {
        return familyColor;
    }

    @Override
    public CommLink getUserLink() {
        return user.getLink();
    }

    @Override
    public Map<DomesticColor, Domestic> getDomestics() {
        return new HashMap<>(domestics);
    }

    /**
     * Set new domestic values
     *
     * @param newValues New values map
     */
    public void setDomestics(Map<DomesticColor, Integer> newValues) {
        // Update domestic value
        newValues.forEach((color, value) -> {
            if(penalty.containsKey(color))
                domestics.get(color).setValue(value - penalty.get(color));
            else
                domestics.get(color).setValue(value);
        });

        // Assure neutral domestic to have value of zero
        domestics.get(DomesticColor.Neutral).setValue(0);

        // Send update to all users
        if(user.getMatch() != null)
            user.getMatch().sendAll(new DiceDomesticUpdate(user.getUsername(), newValues, domestics));
    }

    /**
     * Add permanent penalty value to specified domestic
     *
     * @param color Domestic color
     * @param value Penalty value
     */
    public void setDomesticPenalty(DomesticColor color, int value) {
        penalty.put(color, value);
    }

    /**
     * Set true if player has to jump first round of each turn
     *
     * @param jumpRound True to jump first round of turn
     */
    public void setRoundJump(boolean jumpRound) {
        roundJump = jumpRound;
    }

    /**
     * Get round jump flag for this user
     *
     * @return Round jump flag
     */
    public boolean getRoundJump() {
        return roundJump;
    }

    @Override
    public PlayerState getUserState() {
        return (PlayerState) currentState.clone();
    }

    /**
     * Update user state
     *
     * @param newState New state to update user state
     */
    public void updateUserState(PlayerState newState) {
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

        // Send update to all users
        if(user.getMatch() != null)
            user.getMatch().sendAll(new PlayerStateUpdate(user.getUsername(), currentState));
    }

    /**
     * Set true when player has performed his move
     *
     * @param hasMoved HasMoved value
     */
    public void setHasMoved(boolean hasMoved) {
        if(!hasMoved)
            moves.incrementAndGet();
        else
            moves.decrementAndGet();
    }

    /**
     * Check if player has moved or not
     *
     * @return True if has moved, false else
     */
    public boolean getHasMoved() {
        return moves.compareAndSet(0,0);
    }

    /**
     * Get church support intentions of this user
     *
     * @return True if wants to support the church, false else
     */
    public boolean getChurchSupport() {
        return churchSupport;
    }

    /**
     * Set church support intentions
     *
     * @param supportChurch True to support church, false else
     */
    public void setChurchSupport(boolean supportChurch) {
        churchSupport = supportChurch;
    }
}
