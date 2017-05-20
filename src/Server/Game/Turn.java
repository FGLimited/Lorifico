package Server.Game;

import Action.BaseAction;
import Game.Effects.Effect;
import Game.Usable.ResourceType;
import Game.UserObjects.GameUser;
import Game.UserObjects.PlayerState;
import Logging.Logger;
import Server.Game.UserObjects.GameTable;
import java.util.ArrayList;
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
        currentRound.forEach(user -> {

            // TODO: create move request and send it using this.move()

        });

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

        // TODO: send move request throw current user comm link

        synchronized (currentUser) {
            try {
                currentUser.wait(timeout);

            } catch (InterruptedException ie) {
                Logger.log(Logger.LogLevel.Warning, "User move was interrupted.\n" + ie.getMessage());
            }
        }

    }

    /**
     * Check each user
     */
    private void faithCheck(){

        if(number % 2 != 0)
            return;

        // turn 2 = 3 points || turn 4 = 4 points || turn 6 = 5 points
        int requestedFaith = number == 2 ? 3 : (number == 4 ? 4 : 5);

        order.forEach(user -> {

            if(user.getUserState().getResources().get(ResourceType.FaithPoint) >= requestedFaith) {

                // TODO: ask to user if he wants penalty or victory points and go back

                //move(user, /* reuqest */);

            }
            else {
                final PlayerState currentState = user.getUserState();

                Effect faithEffect = table.getFaithEffect();

                currentState.addEffect(faithEffect);

                if(faithEffect.canApply(currentState))
                    faithEffect.apply(currentState);

                user.updateUserState(currentState);
            }

        });

    }

}
