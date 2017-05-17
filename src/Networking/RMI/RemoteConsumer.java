package Networking.RMI;

import java.rmi.RemoteException;

/**
 * Created by fiore on 10/05/2017.
 */
@FunctionalInterface
public interface RemoteConsumer<T> {

    void accept(T t) throws RemoteException;
}
