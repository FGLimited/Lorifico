package Networking.RMI;

import Action.BaseAction;
import Logging.Logger;
import Networking.CommLink;
import Networking.Gson.GsonUtils;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.BiConsumer;

/**
 * Created by fiore on 10/05/2017.
 */
public class RMIComm extends UnicastRemoteObject implements CommLink, MessageReceiver {

    private final transient RemoteConsumer<String> postMethod;

    private volatile transient BiConsumer<CommLink, String> onMessage;

    private final transient ExecutorService executor = Executors.newSingleThreadExecutor();

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
            executor.execute(() -> onMessage.accept(this, message));
    }

    @Override
    public void sendMessage(final BaseAction message) {
        if(message == null)
            return;

        try {
            postMethod.accept(GsonUtils.toGson(message));
        } catch (RemoteException re) {
            Logger.log(Logger.LogLevel.Warning, "Can't send message through rmi mailbox.\n" + re.getMessage());
        }
    }

    @Override
    public void shutdown() {

        executor.shutdownNow();
    }
}
