package Server.Game;

import Action.FaithCardsUpdate;
import Action.SendMatchAttendees;
import Action.TowersUpdate;
import Game.Cards.CardType;
import Game.Effects.Effect;
import Game.Effects.EffectType;
import Game.Usable.ResourceType;
import Game.UserObjects.DomesticColor;
import Game.UserObjects.FamilyColor;
import Game.UserObjects.GameUser;
import Game.UserObjects.PlayerState;
import Logging.Logger;
import Model.User.User;
import Server.Game.Cards.SplitDeck;
import Server.Game.Effects.Faith.FaithDeck;
import Server.Game.UserObjects.GameTable;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by fiore on 10/05/2017.
 */
public class Match extends UserHandler {

    private static AtomicInteger matchCounter = new AtomicInteger(0);

    private final int matchNumber = matchCounter.getAndIncrement();

    private volatile ScheduledExecutorService matchExecutor = Executors.newSingleThreadScheduledExecutor();

    private volatile boolean isStarted = false;

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
        return isStarted;
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
     *
     * @param leftUser User who left the game
     */
    public void abort(User leftUser) {
        matchExecutor.shutdownNow();

        // TODO: notify all users of game end because of a user left
    }

    public synchronized void addUser(User newUser) {
        // Add new user to users list
        users.add(newUser);
        newUser.setMatch(this);

        //Send all match users a list container other attendees
        sendAll(new SendMatchAttendees(users));

        // When the second users is added start countdown for match start
        if(users.size() >= 2) {

            // If another thread is already waiting shut it down
            if(!matchExecutor.isShutdown()) {
                matchExecutor.shutdownNow();

                // Create new executor because old one can't be used after shutdown
                matchExecutor = Executors.newSingleThreadScheduledExecutor();
            }

            // Schedule new match start
            matchExecutor.schedule(() -> {

                // If there are at least two users the game can start
                if (users.size() > 1)
                    initGame();

            }, startDelay, TimeUnit.MILLISECONDS);
        }

        // When maximum player
        if(users.size() == 4) {
            isStarted = true;

            // Stop countdown
            matchExecutor.shutdownNow();

            // Start game immediately
            matchExecutor.execute(this::initGame);
        }

    }

    /**
     * Get current game table if is initialized
     *
     * @return Initialized game table or null
     */
    public GameTable getTable() {
        return table;
    }

    /**
     * Initialize game table, cards deck and game users for all users
     *
     * @return List for first round of game
     */
    private List<GameUser> initObjects() throws IOException {

        // Initialize game table and cards deck
        table = GameTable.load(users.size());

        cardsDeck = new SplitDeck();
        cardsDeck.shuffle();

        faithDeck = new FaithDeck();
        faithDeck.shuffle();

        // Initialize all users and first round order
        List<GameUser> firstRoundOrder = new ArrayList<>();
        FamilyColor[] colors = FamilyColor.values();

        // Create game user for each user
        for (int i = 0; i < users.size(); i++) {
            // Get user
            User current = users.get(i);

            // Create new game user for current user
            final GameUser newGameUser = new Server.Game.UserObjects.GameUser(current, colors[i]);

            // Set new game user (this will send update to all clients)
            current.setGameUser(newGameUser);

            // Get initial player state bound to current game user
            final PlayerState initialState = GameHelper.getInstance().getInitialPS(newGameUser, i);

            // Update current state (this will send update to all clients)
            newGameUser.updateUserState(initialState);

            // Add game user to first round order
            firstRoundOrder.add(current.getGameUser());
        }

        return firstRoundOrder;
    }

    /**
     * Initialize game objects for match start and takes care of game execution
     */
    private void initGame() {

        isStarted = true;

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

        // Get faith cards for this game
        final Map<Integer, Effect> faithEffects = faithDeck.getFaithEffect();

        // Send faith cards to all clients
        sendAll(new FaithCardsUpdate(faithEffects));

        // Game consists of 6 turns
        for ( ; turnNumber <= 6; turnNumber++) {

            // Update cards in tower positions for next turn
            final TowersUpdate cardUpdate = table.changeTurn(cardsDeck.getCardPerTurn(turnNumber), faithEffects.get(turnNumber));

            // Send card update to all clients
            sendAll(cardUpdate);

            // Throw dice for this turn
            final Map<DomesticColor, Integer> diceValues = table.getDiceValue();

            // Update domestic values for current turn
            roundOrder.forEach(user -> user.setDomestics(diceValues));

            // Initialize new turn object to perform all rounds
            Turn current = new Turn(turnNumber, roundOrder, table, moveTimeout);

            // Play all rounds for current turn and get order list for next turn
            roundOrder = current.playAllRounds();
        }

        // Convert all to victory points and determine game winner at the end
        endCheck(roundOrder);
    }

    /**
     * Perform all final calculations for victory points
     */
    private void endCheck(List<GameUser> users) {

        // Sort users for military points
        users.sort(Comparator.comparingInt(a -> a.getUserState().getResources().get(ResourceType.MilitaryPoint)));

        final Map<GameUser, Integer> military = new HashMap<>();
        military.put(users.get(0), 1);

        for (int i = 1; i < users.size(); i++) {

            final GameUser current = users.get(i);
            final GameUser previous = users.get(i - 1);

            if(current.getUserState().getResources().get(ResourceType.MilitaryPoint) < previous.getUserState().getResources().get(ResourceType.MilitaryPoint))
                military.put(current, military.get(previous) + 1);

            military.put(current, military.get(previous));
        }

        // Convert all to victory points for each user
        users.forEach(user -> convertToVictory(user, military.get(user)));

        // Order by victory points
        users.sort(Comparator.comparingInt(user -> user.getUserState().getResources().get(ResourceType.VictoryPoint)));

        // TODO: send classification
    }

    /**
     * Convert every left resource or military/faith point to victory points
     *
     * @param user User to compute
     * @param militaryWayPosition Position relative to other users on military track
     */
    private void convertToVictory(GameUser user, int militaryWayPosition) {

        // Get current player state
        final PlayerState currentState = user.getUserState();

        int victoryPoints = 0;

        // Check cards number
        for (CardType type : CardType.values())
            victoryPoints += GameHelper.getInstance().victoryForCards(type, currentState.getCards(type).size());

        // Add military way bonus
        victoryPoints += GameHelper.getInstance().getMilitaryBonus(militaryWayPosition);

        final Map<ResourceType, Integer> finalResources = currentState.getResources();

        // Add faith way bonus
        victoryPoints += GameHelper.getInstance().getFaithBonus(finalResources.get(ResourceType.FaithPoint));

        // Calculate total resources left and add victory points bonus
        int totalResourcesLeft = finalResources.get(ResourceType.Wood) + finalResources.get(ResourceType.Rock)
                + finalResources.get(ResourceType.Gold) + finalResources.get(ResourceType.Slave);

        victoryPoints += totalResourcesLeft / 5;

        // Update victory points
        finalResources.replace(ResourceType.VictoryPoint, finalResources.get(ResourceType.VictoryPoint) + victoryPoints);
        currentState.setResources(finalResources, true);

        // Apply all final effects
        currentState.getEffects(EffectType.Final).forEach(finalEffect -> {
            if(finalEffect.canApply(user.getUserState()))
                finalEffect.apply(currentState);
        });

        // Update user state
        user.updateUserState(currentState);
    }
}
