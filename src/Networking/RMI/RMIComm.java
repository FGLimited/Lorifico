package Networking.RMI;

import Logging.Logger;
import Networking.CommLink;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.function.BiConsumer;

/**
 * Created by fiore on 10/05/2017.
 */
public class RMIComm extends UnicastRemoteObject implements CommLink, MessageReceiver {

    private final transient RemoteConsumer<String> postMethod;

    private volatile transient BiConsumer<CommLink, String> onMessage;

    public RMIComm(RemoteConsumer<String> postMethod) throws RemoteException {
        this.postMethod = postMethod;
    }

    @Override
    public void setOnMessage(BiConsumer<CommLink, String> onMessageCallback) {
        onMessage = onMessageCallback;
    }

    @Override
    public void messageReceived(final String message) {

        if(onMessage != null)
            onMessage.accept(this, message);
    }

    @Override
    public void sendMessage(String message) {
        if(message == null || message.equals(""))
            return;

        try {
            postMethod.accept(message);
        } catch (RemoteException re) {
            Logger.log(Logger.LogLevel.Warning, "Can't send message through rmi mailbox.\n" + re.getMessage());
        }
    }

    @Override
    public void shutdown() {

    }
}
