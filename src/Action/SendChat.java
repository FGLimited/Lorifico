package Action;

import Model.User.User;
import Server.Game.Match;

/**
 * Created by fiore on 13/06/2017.
 */
public class SendChat implements BaseAction {

    private final String username;

    private final String message;

    public SendChat(String username, String message) {
        this.username = username;
        this.message = message;
    }

    @Override
    public void doAction(User user) {

        // Get current match
        Match current = user.getMatch();

        // Send message back to all if player is in a match
        if(current != null)
            current.sendAll(new ChatMessage(username, message));

    }
}
