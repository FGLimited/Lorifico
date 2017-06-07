package Action;

import Model.User.User;
import Server.Game.Match;

/**
 * Created by fiore on 07/06/2017.
 */
public class StartMatch implements BaseAction {
    @Override
    public void doAction(User user) {
        final Match currentMatch = user.getMatch();

        // Start match relative to calling user
        if(currentMatch != null)
            currentMatch.start();
    }
}
