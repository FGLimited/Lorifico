package Networking;

import Action.BaseAction;
import java.util.function.BiConsumer;

/**
 * Created by fiore on 01/06/2017.
 */
public class FakeLink implements CommLink {
    @Override
    public void sendMessage(BaseAction message) {
        System.out.println("Sending message from fake link");
    }

    @Override
    public void setOnMessage(BiConsumer<CommLink, String> onMessageCallback) {

    }

    @Override
    public void shutdown() {

    }
}
