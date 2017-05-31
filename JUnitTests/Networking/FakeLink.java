package Networking;

import Action.BaseAction;

import java.util.function.BiConsumer;

/**
 * Created by fiore on 18/05/2017.
 */
public class FakeLink implements CommLink {
    @Override
    public void sendMessage(BaseAction message) {

    }

    @Override
    public void setOnMessage(BiConsumer<CommLink, String> onMessageCallback) {

    }

    @Override
    public void shutdown() {

    }
}
