package Networking;

import Action.BaseAction;
import java.util.function.BiConsumer;

/**
 * Created by fiore on 10/05/2017.
 */
public interface CommLink {

    /**
     * Send given message on the comm link
     *
     * @param message Message to send
     */
    void sendMessage(BaseAction message);

    /**
     * Set a callback method to be called on new received message
     *
     * @param onMessageCallback Callback method reference
     */
    void setOnMessage(BiConsumer<CommLink, String> onMessageCallback);

    /**
     * Close connection on this link
     */
    void shutdown();
}
