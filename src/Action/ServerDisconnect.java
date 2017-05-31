package Action;

import Model.User.User;
import Model.UserManager;
import Server.Game.Match;

/**
 * Sent from client to server to disconnect the user completely
 */
public class ServerDisconnect implements BaseAction {
    @Override
    public void doAction(User user) {

        // Abort current match if initialized
        final Match currentMatch = user.getMatch();
        if(currentMatch != null)
            currentMatch.abort(user);

        UserManager.getInstance().disconnectUser(user.getUsername());
    }
}
