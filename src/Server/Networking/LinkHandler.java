package Server.Networking;

import Networking.CommLink;

/**
 * Created by fiore on 10/05/2017.
 *
 * Receiver for newly connected clients not yet bound to a user
 */
public interface LinkHandler {

    /**
     * Add new comm link from just connected client
     *
     * @param newLink New comm link
     */
    void addClientComm(CommLink newLink);
}
