package Server.Game;

import Game.Effects.Effect;
import Game.Usable.ResourceType;
import Game.UserObjects.DomesticColor;
import Game.UserObjects.FamilyColor;
import Logging.Logger;
import Model.User.User;
import Server.Game.Cards.SplitDeck;
import Server.Game.Effects.Faith.FaithDeck;
import Server.Game.UserObjects.GameTable;
import Game.UserObjects.GameUser;
import java.io.IOException;
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

    private final long startDelay;

    private final long moveTimeout;

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
        matchExecutor.shutdownNow();
    }

    public void addUser(User newUser) {
        // Add new user to users list
        users.add(newUser);
        newUser.setMatch(this);

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

    @Override
    public List<User> getAllUsers() {
        return users;
    }

    /**
     * Initialize game table, cards deck and game users for all users
     *
     * @return List for first round of game
     */
    private List<GameUser> initObjects() throws IOException {

        // Initialize game table and cards deck
        table = new GameTable(users.size());

        cardsDeck = new SplitDeck();
        cardsDeck.shuffle();

        faithDeck = new FaithDeck();
        faithDeck.shuffle();

        // Initialize all users and first round order
        List<GameUser> firstRoundOrder = new ArrayList<>();
        FamilyColor[] colors = FamilyColor.values();

        // Initial resources for all users
        Map<ResourceType, Integer> initialResources = new HashMap<>();
        initialResources.put(ResourceType.Wood, 2);
        initialResources.put(ResourceType.Rock, 2);
        initialResources.put(ResourceType.Slave, 3);

        // Create game user for each user
        for (int i = 0; i < users.size(); i++) {
            // Get user
            User current = users.get(i);

            // Create new game user for current user
            GameUser newGameUser = new Server.Game.UserObjects.GameUser(current.getLink(), colors[i]);

            // Add initial resources
            newGameUser.getUserState().setResources(initialResources, false);

            // Set new game user
            current.setGameUser(newGameUser);

            // Add game user to first round order
            firstRoundOrder.add(current.getGameUser());
        }

        return firstRoundOrder;
    }

    /**
     * Initialize game objects for match start and takes care of game execution
     */
    private void initGame() {

        // Initialize all users and first round order
        List<GameUser> roundOrder;

        try {

            roundOrder = initObjects();

        } catch (IOException ioe) {
            Logger.log(Logger.LogLevel.Error, "IO error during match initialization.\n" +
                    "Match number: " + matchNumber + "\n" +
                    ioe.getMessage());

            // TODO: notify all users and exit

            return;
        }

        // Game execution

        int turnNumber = 1;

        Map<Integer, Effect> faithEffects = faithDeck.getFaithEffect();

        // TODO: send faith effects to all users

        // Game consists of 6 turns
        for ( ; turnNumber <= 6; turnNumber++) {

            // Put new set of cards in all tower positions, update faith effect
            // and get new dice values for current turn
            Map<DomesticColor, Integer> diceValues = table
                    .changeTurn(cardsDeck.getCardPerTurn(turnNumber), faithEffects.get(turnNumber));

            // TODO: send all positions update to users

            // Update domestic values for current turn
            roundOrder.forEach(user -> user.setDomestics(diceValues));

            // Initialize new turn object to perform all rounds
            Turn current = new Turn(turnNumber, roundOrder, table, moveTimeout);

            // Play all rounds for current turn and get order list for next turn
            roundOrder = current.playAllRounds();
        }

        // Convert all to victory points and determine game winner at the end
        endCheck();
    }

    /**
     * Perform all final calculations for victory points
     */
    private void endCheck() {

    }
}
