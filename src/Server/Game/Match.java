package Server.Game;

import Action.BaseAction;
import Action.SendMatchAttendees;
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
import Networking.Gson.GsonUtils;
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
public class Match implements UserHandler {

    private static AtomicInteger matchCounter = new AtomicInteger(0);

    private final int matchNumber = matchCounter.getAndIncrement();

    private final List<User> users = Collections.synchronizedList(new ArrayList<>());

    private volatile ScheduledExecutorService matchExecutor = Executors.newSingleThreadScheduledExecutor();

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

        //Send all match users a list container other attendees
        getAllUsers().forEach(user -> {
            BaseAction baseAction = new SendMatchAttendees(getAllUsers());
            user.getLink().sendMessage(GsonUtils.toGson(baseAction));
        });

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
        table = new GameTable(users.size());

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
            GameUser newGameUser = new Server.Game.UserObjects.GameUser(current.getLink(), colors[i]);

            // Get initial player state bound to current game user
            final PlayerState initialState = GameHelper.getInitialPS(newGameUser);

            // Add gold bonus as needed
            initialState.setResources(Collections.singletonMap(ResourceType.Gold, initialState.getResources().get(ResourceType.Gold) + i), false);

            // Update current state
            newGameUser.updateUserState(initialState);

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
            victoryPoints += GameHelper.victoryForCards(type, currentState.getCards(type).size());

        // Add military way bonus
        victoryPoints += GameHelper.getMilitaryBonus(militaryWayPosition);

        final Map<ResourceType, Integer> finalResources = currentState.getResources();

        // Add faith way bonus
        victoryPoints += GameHelper.getFaithBonus(finalResources.get(ResourceType.FaithPoint));

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
