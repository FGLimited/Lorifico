package Networking.RMI;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by fiore on 10/05/2017.
 */
public interface MailBox extends Remote {

    /**
     * Post given message calling server receiver method
     *
     * @param message Message to send
     * @throws RemoteException If rmi is dumb
     */
    void clientPost(String message) throws RemoteException;

    /**
     * Post given message calling client receiver method
     *
     * @param message Message to send
     * @throws RemoteException If rmi is dumb
     */
    void serverPost(String message) throws RemoteException;

    /**
     * Set callback method for message received event on client
     *
     * @param clientLink Method to call on message received
     * @throws RemoteException If rmi is dumb as hell
     */
    void setClientLink(MessageReceiver clientLink) throws RemoteException;

    /**
     * Set callback method for message received event on server
     *
     * @param serverLink Method to call on message received
     * @throws RemoteException If rmi is dumb as hell
     */
    void setServerLink(MessageReceiver serverLink) throws RemoteException;
}
