package Networking.RMI;

import org.jetbrains.annotations.Nullable;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by fiore on 10/05/2017.
 */
public interface RMICommFactory extends Remote {

    /**
     * Initialize a new mailbox on the server to be bound on the new client
     *
     * @return Mailbox already bound on server side
     * @throws RemoteException If rmi can't handle more than three lines of code
     */
    @Nullable MailBox connect() throws RemoteException;
}
