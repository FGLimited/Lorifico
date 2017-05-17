package Server.Game;

import Game.Effects.Effect;
import Game.Usable.ResourceType;
import Game.UserObjects.GameUser;
import Game.UserObjects.PlayerState;
import Server.Game.UserObjects.GameTable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by fiore on 17/05/2017.
 */
public class Turn {

    private final int number;

    private final List<GameUser> order;

    private final List<GameUser> lastRound = new ArrayList<>();

    private final GameTable table;

    public Turn(int turnNumber, List<GameUser> userOrder, GameTable table) {
        number = turnNumber;
        order = userOrder;
        this.table = table;

        order.forEach(user -> {
            if(user.getRoundJump())
                lastRound.add(user);
        });
    }

    public void play() {

        // First round with only allowed players
        List<GameUser> firstRound = new ArrayList<>(order);
        firstRound.removeAll(lastRound);

        firstRound.forEach(this::move);

        // Other three round with all players
        for (int roundNumber = 2; roundNumber <= 4; roundNumber++) {
            order.forEach(this::move);
        }

        // Last extra round for remaining players
        lastRound.forEach(this::move);

        // Faith way check if necessary
        faithCheck();
    }

    private void move(GameUser currentUser) {

    }

    private void faithCheck(){

        if(number % 2 != 0)
            return;

        // turn 2 = 3 points || turn 4 = 4 points || turn 6 = 5 points
        int requetedFaith = number == 2 ? 3 : (number == 4 ? 4 : 5);

        order.forEach(user -> {

            if(user.getUserState().getResources().get(ResourceType.FaithPoint) >= requetedFaith) {

                // TODO: ask what to do

            }
            else {
                final PlayerState currentState = user.getUserState();

                Effect faithEffect = table.getFaithEffect();

                currentState.addEffect(faithEffect);

                faithEffect.apply(currentState);

                user.updateUserState(currentState);
            }

        });

    }

}
