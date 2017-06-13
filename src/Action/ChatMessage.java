package Action;

import Model.User.User;

/**
 * Created by fiore on 13/06/2017.
 */
public class ChatMessage implements BaseAction {

    private final String username;

    private final String message;

    public ChatMessage(String username, String message) {
        this.username = username;
        this.message = message;
    }

    @Override
    public void doAction(User user) {
        // TODO: show message in chat log
    }
}
