package Server.Networking.RMI;

import Networking.RMI.MailBox;
import Networking.RMI.MessageReceiver;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * Created by fiore on 10/05/2017.
 */
public class RMIMailBox extends UnicastRemoteObject implements MailBox, Serializable {

    /**
     * Client remote callback to call when server send a message
     */
    private volatile MessageReceiver clientLink;

    /**
     * Server remote callback to call when client send a message
     */
    private volatile MessageReceiver serverLink;

    public RMIMailBox() throws RemoteException {
        super();
    }

    @Override
    public void setClientLink(MessageReceiver clientLink) throws RemoteException {
        this.clientLink = clientLink;
    }

    @Override
    public void setServerLink(MessageReceiver serverLink) throws RemoteException {
        this.serverLink = serverLink;
    }

    @Override
    public void serverPost(final String message) throws RemoteException {
        if(clientLink != null)
            clientLink.messageReceived(message);
    }

    @Override
    public void clientPost(final String message) throws RemoteException {
        if(serverLink != null)
            serverLink.messageReceived(message);
    }
}
