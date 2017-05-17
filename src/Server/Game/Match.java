package Server.Game;

import Game.UserObjects.DomesticColor;
import Game.UserObjects.FamilyColor;
import Logging.Logger;
import Model.User.User;
import Server.Game.Cards.SplitDeck;
import Server.Game.Effects.FaithDeck;
import Server.Game.UserObjects.GameTable;
import Server.Game.UserObjects.GameUser;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by fiore on 10/05/2017.
 */
public class Match implements UserHandler {

    private static AtomicInteger matchCounter = new AtomicInteger(0);

    private final int matchNumber = matchCounter.getAndIncrement();

    private final List<User> users = Collections.synchronizedList(new ArrayList<>());

    private final ScheduledExecutorService matchExecutor = Executors.newSingleThreadScheduledExecutor();

    private final ExecutorService playerWaiter = Executors.newSingleThreadExecutor();

    private final long startDelay;

    private final long moveTimeout;

    private final Map<FamilyColor, Boolean> hasMoved = Collections.synchronizedMap(new HashMap<>());

    private volatile GameTable table;

    private volatile SplitDeck cardsDeck;

    private volatile FaithDeck faithDeck;

    public Match(long startDelay, long moveTimeout) {
        this.startDelay = startDelay;
        this.moveTimeout = moveTimeout;
    }

    /**
     * Get match number
     *
     * @return Match number
     */
    public int getNumber() {
        return matchNumber;
    }

    /**
     * Get match status
     *
     * @return True if match is running, false if players are still waiting
     */
    public boolean isStarted() {
        return false;
    }

    /**
     * Save current match and players' status and return json string
     *
     * @return Json string containing match and players' information
     */
    public String saveStatus() {
        return null;
    }

    /**
     * Abort match without saving current status
     */
    public void abort() {

    }

    public void addUser(User newUser) {
        // Add new user to users list
        users.add(newUser);

        // TODO: send user list to all users

        // When the second users is added start countdown for match start
        if(users.size() == 2)
            matchExecutor.schedule(() -> {

                // If there are at least two users the game can start
                if(users.size() > 1)
                    initGame();

            }, startDelay, TimeUnit.SECONDS);

        // When maximum player
        if(users.size() == 4) {

            // Stop countdown
            matchExecutor.shutdownNow();

            // Start game immediately
            matchExecutor.execute(this::initGame);
        }

    }

    /**
     * Notify player move
     *
     * @param color Player's family color
     */
    public void setMoved(FamilyColor color) {
        hasMoved.replace(color, true);
    }

    /**
     * Check if player with given family color can move in current round
     *
     * @param color Player's family color
     * @return True if can move, false else
     */
    public boolean canMove(FamilyColor color) {
        return !hasMoved.get(color);
    }

    @Override
    public List<User> getAllUsers() {
        return users;
    }

    /**
     * Initialize game objects for match start and takes care of game execution
     *
     * @return Player's order list
     */
    private void initGame() {

        // Initialization

        // Initialize game table and cards deck
        table = new GameTable(users.size());

        cardsDeck = new SplitDeck();

        cardsDeck.shuffle();

        // Initialize all users and first round order
        List<FamilyColor> roundOrder = new ArrayList<>();
        FamilyColor[] colors = FamilyColor.values();

        for (int i = 0; i < users.size(); i++) {
            User current = users.get(i);

            current.setGameUser(new GameUser(current.getLink(), colors[i]));

            roundOrder.add(colors[i]);
            hasMoved.put(colors[i], false);
        }

        // Initialization end

        // Game execution

        int turnNumber = 1;
        int roundNumber = 1;

        // Game consists of 6 turns
        for ( ; turnNumber <= 6; turnNumber++) {

            // Put new set of cards in all tower positions and update faith effect
            table.changeTurn(cardsDeck.getCardPerTurn(turnNumber), faithDeck.getFaithEffect(turnNumber));

            // Each turn has 4 round (one for each domestic)
            for ( ; roundNumber <= 4; roundNumber++) {

                // Get new round order and free all positions
                roundOrder = table.changeRound(roundOrder);

                // Take care of round routine
                playRound(roundOrder);
            }

            // 2, 4 and 6 turn executes faith way check
            faithCheck(turnNumber);
        }

        // Convert all to victory points and determine game winner at the end
        endCheck();
    }

    /**
     * Round routine
     *
     * @param roundOrder Players order for this round
     */
    private void playRound(List<FamilyColor> roundOrder) {

        // Throw dice and update domestic value for all players
        final Map<DomesticColor, Integer> newValues = table.getDiceValue();
        users.forEach(user -> user.getGameUser().setDomestics(newValues));

        // Reset move for all players
        hasMoved.replaceAll((family, hasMoved) -> false);

        // TODO: send dice value update to all users

        // Call every user in correct order to perform his move
        for (FamilyColor color : roundOrder) {

            // Get current player
            final User current = users.parallelStream()
                    .filter(user -> user.getGameUser().getFamilyColor() == color)
                    .findFirst()
                    .get();

            // Ask current user to make a move
            Future<Boolean> move = playerWaiter.submit(() -> waitMove(current));

            boolean hasMoved = false;

            // Wait for a move until move timeout ends
            try {

                hasMoved = move.get(moveTimeout, TimeUnit.SECONDS);
            } catch (InterruptedException | ExecutionException ee) {
                Logger.log(Logger.LogLevel.Error, "An error occurred while waiting for move.\n" +
                        "Match number: " + matchNumber + "\n" +
                        "Current user: " + current.getGameUser() + "\n" +
                        ee.getMessage());

                // TODO: spaccare tutto

            } catch (TimeoutException te) {
                // If user hasn't moved send error and go ahead
                // TODO: send timeout error to current user


                playerWaiter.shutdownNow();
                hasMoved = false;
            }

            // TODO:

        }

    }

    private void faithCheck(int turnNumber){

        if(turnNumber % 2 != 0)
            return;

        // turn 2 = 3 points || turn 4 = 4 points || turn 6 = 5 points
        int requetedFaith = turnNumber == 2 ? 3 : (turnNumber == 4 ? 4 : 5);

        users.forEach(user -> {



        });

    }

    private void endCheck() {

    }

    /**
     * Wait for a move from given user
     *
     * @param currentUser User to wait for
     * @return True if player has moved, false else
     */
    private boolean waitMove(User currentUser) {

        synchronized (hasMoved) {
            try {
                hasMoved.wait();
            } catch (InterruptedException ie) {

            }
        }

        return hasMoved.get(currentUser.getGameUser().getFamilyColor());
    }

}
