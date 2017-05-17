package Server.Networking;

/**
 * Created by fiore on 10/05/2017.
 */
public interface LinkAcceptor {

    /**
     * Listen for new client trying to connect
     */
    void listen();

    /**
     * Stop listening for new connections
     */
    void stop();
}
