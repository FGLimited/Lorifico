package Server.Networking;

import Logging.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fiore on 10/05/2017.
 */
public class ConnectionHandler {

    private final List<LinkAcceptor> acceptors;

    public ConnectionHandler() {
        acceptors = new ArrayList<>();
    }

    /**
     * Add given connection acceptor to acceptors list
     *
     * @param newAcceptor Acceptor to be added
     */
    public void addAcceptor(LinkAcceptor newAcceptor) {
        acceptors.add(newAcceptor);
    }

    /**
     * Start all acceptors present in acceptors list
     */
    public void startAll() {
        Logger.log(Logger.LogLevel.Normal, "Starting all acceptor services.");
        acceptors.forEach(LinkAcceptor::listen);
    }

    /**
     * Stop all acceptors present in acceptors list
     */
    public void stopAll() {
        Logger.log(Logger.LogLevel.Normal, "Shutting down all acceptor services.");
        acceptors.forEach(LinkAcceptor::stop);
    }
}
