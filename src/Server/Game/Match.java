package Server.Game;

import Game.Effects.Effect;
import Game.Usable.ResourceType;
import Game.UserObjects.DomesticColor;
import Game.UserObjects.FamilyColor;
import Logging.Logger;
import Model.User.User;
import Server.Game.Cards.SplitDeck;
import Server.Game.Effects.FaithDeck;
import Server.Game.UserObjects.GameTable;
import Game.UserObjects.GameUser;
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
        if(users.size() >= 2)

            matchExecutor.shutdownNow();

            matchExecutor.schedule(() -> {

                // If there are at least two users the game can start
                if(users.size() > 1)
                    initGame();

            }, startDelay, TimeUnit.MILLISECONDS);

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
     */
    private void initGame() {

        // Initialization

        // Initialize game table and cards deck
        table = new GameTable(users.size());

        cardsDeck = new SplitDeck();

        cardsDeck.shuffle();

        // Initialize all users and first round order
        List<GameUser> roundOrder = new ArrayList<>();
        FamilyColor[] colors = FamilyColor.values();

        for (int i = 0; i < users.size(); i++) {
            User current = users.get(i);

            current.setGameUser(new Server.Game.UserObjects.GameUser(current.getLink(), colors[i]));

            roundOrder.add(current.getGameUser());
            hasMoved.put(colors[i], false);
        }

        // Initialization end

        // Game execution

        int turnNumber = 1;
        int roundNumber = 1;

        Map<Integer, Effect> faithEffects = faithDeck.getFaithEffect();

        // Game consists of 6 turns
        for ( ; turnNumber <= 6; turnNumber++) {

            // Put new set of cards in all tower positions and update faith effect
            table.changeTurn(cardsDeck.getCardPerTurn(turnNumber), faithEffects.get(turnNumber));

            Turn current = new Turn(turnNumber, roundOrder, table);

            current.play();

            // Get new round order and free all positions
            roundOrder = table.changeRound(roundOrder);
        }

        // Convert all to victory points and determine game winner at the end
        endCheck();
    }

    /**
     * Round routine
     *
     * @param roundOrder Players order for this round
     */
    private void playRound(List<GameUser> roundOrder) {

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

            // Wait for a move until move timeout ends
            try {

                synchronized (hasMoved) {
                    hasMoved.wait(moveTimeout);
                }

            } catch (InterruptedException ie) {
                Logger.log(Logger.LogLevel.Error, "An error occurred while waiting for move.\n" +
                        "Match number: " + matchNumber + "\n" +
                        "Current user: " + current.getGameUser() + "\n" +
                        ie.getMessage());

                // TODO: spaccare tutto

            }

            // TODO:

        }

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
