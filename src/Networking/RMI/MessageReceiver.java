package Networking.RMI;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by fiore on 10/05/2017.
 */
public interface MessageReceiver extends Remote {

    void messageReceived(final String message) throws RemoteException;
}
