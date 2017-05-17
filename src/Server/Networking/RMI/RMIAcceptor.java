package Server.Networking.RMI;

import Logging.Logger;
import Server.Networking.LinkAcceptor;
import Server.Networking.LinkHandler;
import Networking.RMI.MailBox;
import Networking.RMI.RMIComm;
import Networking.RMI.RMICommFactory;
import org.jetbrains.annotations.Nullable;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 * Created by fiore on 10/05/2017.
 */
public class RMIAcceptor extends UnicastRemoteObject implements LinkAcceptor, RMICommFactory {

    private final LinkHandler handler;

    private boolean isListening = false;

    /**
     * Initialize new rmi connections acceptor with a link handler to receive new comm link
     *
     * @param linkHandler New link handler
     * @throws RemoteException If rmi can't load a byte without throwing something
     */
    public RMIAcceptor(LinkHandler linkHandler) throws RemoteException {
        handler = linkHandler;

        loadRMIRegistry();
    }

    /**
     * Create rmi registry and load this acceptor on it to be accessed from remote clients
     *
     * @throws RemoteException If rmi can't even initialize itself
     */
    private void loadRMIRegistry() throws RemoteException {
        Registry rmiRegistry = LocateRegistry.createRegistry(1099);

        try {
            rmiRegistry.bind("RMICommFactory", this);
        } catch (AlreadyBoundException abe) {
            Logger.log(Logger.LogLevel.Error, "Object has already been loaded, but will be overwritten.\n" + abe.getMessage());

            rmiRegistry.rebind("RMICommFactory", this);
        }
    }

    @Override
    public void listen() {
        Logger.log(Logger.LogLevel.Normal, "RMI acceptor started.");

        isListening = true;
    }

    @Override
    public void stop() {
        Logger.log(Logger.LogLevel.Normal, "RMI acceptor shutdown.");

        isListening = false;
    }

    @Override
    public @Nullable MailBox connect() throws RemoteException {
        if(!isListening)
            return null;

        // Initialize a new shared mailbox
        final MailBox newBuffer = new RMIMailBox();

        // Creates commLink for the server side
        final RMIComm serverLink = new RMIComm(newBuffer::serverPost);
        newBuffer.setServerLink(serverLink);

        // Submit new link to the server comm handler
        handler.addClientComm(serverLink);

        // Return shared mailbox to the client
        return newBuffer;
    }
}
