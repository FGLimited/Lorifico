package Client.Networking;

import Logging.Logger;
import Networking.CommLink;
import Networking.RMI.MailBox;
import Networking.RMI.RMIComm;
import Networking.RMI.RMICommFactory;
import Networking.Socket.SocketComm;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.net.Socket;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Created by fiore on 10/05/2017.
 */
public class CommFactory {

    /**
     * Try to connect to the specified server using requested connection type
     *
     * @param hostname Server hostname
     * @param port Server port number
     * @param type Type of connection to use
     * @return Initialized comm link using requested connection type if connection is successful
     */
    public @Nullable CommLink getLink(String hostname, int port, LinkType type) {
        switch (type){
            case RMI:
                return getRMILink(hostname, port);
            case SOCKET:
                return getSocketLink(hostname, port);
            default:
                return null;
        }
    }

    /**
     * Try to connect to the server using a socket
     *
     * @param hostname Server hostname
     * @param port Server port
     * @return Initialized comm link if connection is successful
     */
    private @Nullable CommLink getSocketLink(String hostname, int port) {
        Socket socket;

        // Try to connect to the server with socket
        try {
            socket = new Socket(hostname, port);
        } catch (IOException ioe) {
            Logger.log(Logger.LogLevel.Error, "Can't connect to the server.\n" + ioe.getMessage());
            return null;
        }

        // Initialize and return new socket link as comm link
        return new SocketComm(socket);
    }

    /**
     * Try to connect to the server using rmi communication
     *
     * @param hostname Server hostname
     * @param port Server port
     * @return Initialized comm link if connection is successful
     */
    private @Nullable CommLink getRMILink(String hostname, int port) {
        RMICommFactory rmiCommFactory;

        try {
            // Load rmi server registry to get the comm factory object
            Registry rmiRegistry = LocateRegistry.getRegistry(hostname, port);

            // Look for rmi comm factory in server registry
            rmiCommFactory = (RMICommFactory) rmiRegistry.lookup("RMICommFactory");

        } catch (RemoteException re) {
            Logger.log(Logger.LogLevel.Error, "Can't locate rmi registry.\n" + re.getMessage());
            return null;
        }
        catch (NotBoundException nbe) {
            Logger.log(Logger.LogLevel.Error, "Can't locate rmi comm factory in rmi registry.\n" + nbe.getMessage());
            return null;
        }

        if(rmiCommFactory == null)
            return null;

        final MailBox newMailBox;

        try {
            // Get shared mailbox from server
            newMailBox = rmiCommFactory.connect();
        } catch (RemoteException re) {
            Logger.log(Logger.LogLevel.Error, "Can't connect to the server through rmi comm factory.\n" + re.getMessage());
            return null;
        }

        if(newMailBox == null)
            return null;

        // Try to bind client rmi link to server mailbox
        try {
            final RMIComm link = new RMIComm(newMailBox::clientPost);

            // Set callback method for message received events
            newMailBox.setClientLink(link);

            // Return initialized rmi comm link as comm link if connection is online
            return link;
        } catch (RemoteException re) {
            Logger.log(Logger.LogLevel.Error, "Can't initialize RMIComm object.\n" + re.getMessage());

            return null;
        }
    }

    public enum LinkType {
        RMI,
        SOCKET
    }
}
