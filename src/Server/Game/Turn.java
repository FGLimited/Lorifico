package Server.Game;

import Action.*;
import Game.Effects.Effect;
import Game.Usable.ResourceType;
import Game.UserObjects.GameUser;
import Game.UserObjects.PlayerState;
import Logging.Logger;
import Networking.CommLink;
import Server.Game.UserObjects.GameTable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by fiore on 17/05/2017.
 */
public class Turn {

    private final int number;

    private volatile int roundNumber = 1;

    private volatile List<GameUser> order;

    private volatile List<GameUser> lastRound = new ArrayList<>();

    private final GameTable table;

    private final long timeout;

    /**
     * Initialize a new turn with given first round order
     *
     * @param turnNumber Turn number
     * @param userOrder First round users order
     * @param table Game table for current match
     * @param timeout Move timeout in milliseconds
     */
    public Turn(int turnNumber, List<GameUser> userOrder, GameTable table, long timeout) {
        number = turnNumber;
        order = userOrder;
        this.table = table;
        this.timeout = timeout;

        order.forEach(user -> {
            if(user.getRoundJump())
                lastRound.add(user);
        });
    }

    /**
     * Perform all round recursively
     *
     * @return Players order list for next round
     */
    public List<GameUser> playAllRounds() {

        boolean isLast = false;

        // Get current round order
        List<GameUser> currentRound;

        if(roundNumber == 5) {
            currentRound = new ArrayList<>(lastRound);
            lastRound.clear();
        }
        else {
            currentRound = new ArrayList<>(order);

            // In first round check for penalized users
            if(roundNumber == 1) {
                currentRound.removeAll(lastRound);
            }
        }

        // Ask for move to each user
        currentRound.forEach(user -> move(user, new MoveRequest()));

        // If is last round check for left user, else finalize
        if(roundNumber >= 4 && lastRound.isEmpty())
                isLast = true;
        else // Else update round order and go ahead
            order = table.changeRound(currentRound);

        // If is last round perform faith way points check
        if(isLast) {
            faithCheck();
            return table.changeRound(order);
        }

        // Increment round number
        roundNumber++;

        return playAllRounds();
    }

    /**
     * Waits for a notification of move performed on current user object
     *
     * @param currentUser Current game user to wait for
     * @param moveRequest Move request to send to current user
     */
    private void move(final GameUser currentUser, BaseAction moveRequest) {

        final CommLink userLink = currentUser.getUserLink();

        currentUser.setHasMoved(false);

        // Ask user to perform a move
        userLink.sendMessage(moveRequest);

        // Wait for user move until timeout
        synchronized (currentUser) {
            try {
                currentUser.wait(timeout);

            } catch (InterruptedException ie) {
                Logger.log(Logger.LogLevel.Warning, "User move was interrupted.\n" + ie.getMessage());
            }
        }

        // If user hasn't moved after timeout send timeout message and go ahead
        userLink.sendMessage(new MoveEnd(!currentUser.getHasMoved()));
    }

    /**
     * Check each user
     */
    private void faithCheck(){

        if(number % 2 != 0)
            return;

        // turn 2 = 3 points || turn 4 = 4 points || turn 6 = 5 points
        int requestedFaith = number == 2 ? 3 : (number == 4 ? 4 : 5);

        order.parallelStream().forEach(user -> {

            // Get current player state
            final PlayerState currentState = user.getUserState();

            // Get current faith points number
            final int faithPoints = currentState.getResources().get(ResourceType.FaithPoint);

            if(faithPoints >= requestedFaith) {

                // ask to user if he wants penalty or victory points and go back
                move(user, new FaithRoadRequest());
            }


            if(user.getChurchSupport()) {
                // Get victory points for current faith road position
                final int victoryPoints = GameHelper.getInstance().getFaithBonus(faithPoints);

                // Update current player state
                currentState.setResources(
                        Collections.singletonMap(ResourceType.VictoryPoint,
                                currentState.getResources().get(ResourceType.VictoryPoint) + victoryPoints),
                        true);
            }
            else {

                // Get faith penalty for current turn
                final Effect faithEffect = table.getFaithEffect();

                // Add penalty to user list
                currentState.addEffect(faithEffect);

                // Apply penalty if possible
                if(faithEffect.canApply(currentState))
                    faithEffect.apply(currentState);

                // Notify client to put penalty cube on current faith card
                user.getUserLink().sendMessage(new FaithPenaltyApplied(number / 2));
            }

            // Update player state with new changes
            user.updateUserState(currentState);
        });

    }

}
