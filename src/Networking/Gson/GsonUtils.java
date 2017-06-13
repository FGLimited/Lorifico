package Networking.Gson;

import Action.BaseAction;
import Game.Cards.Card;
import Game.UserObjects.Choosable;
import Game.UserObjects.GameUser;
import Game.UserObjects.PlayerState;
import Server.Game.Effects.Effect;
import Server.Game.Positions.Position;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * This class is used to serialize/deserialize payloads
 * <p>
 * Created by andrea on 06/05/17.
 */
public class GsonUtils {
    private static Gson gson = null;//gson obj

    //Creates gson builder
    private static void createGson() {
        gson = new GsonBuilder()
                .registerTypeAdapter(BaseAction.class, new MySerializer<BaseAction>())
                .registerTypeAdapter(Game.Effects.Effect.class, new MySerializer<Game.Effects.Effect>())
                .registerTypeAdapter(Effect.class, new MySerializer<Effect>())
                .registerTypeAdapter(Position.class, new MySerializer<Position>())
                .registerTypeAdapter(GameUser.class, new MySerializer<GameUser>())
                .registerTypeAdapter(PlayerState.class, new MySerializer<PlayerState>())
                .registerTypeAdapter(Choosable.class, new MySerializer<Choosable>())
                .registerTypeAdapter(Card.class, new MySerializer<Card>())
                .create();//Gson Builder to serialize communication
    }

    /**
     * Deserializes text to BaseAction
     *
     * @param string
     * @return deserialized obj
     */
    public static BaseAction fromGson(String string) {
        if (gson == null)
            createGson();

        return gson.fromJson(string, BaseAction.class);
    }

    /**
     * Serializes BaseAction to text
     *
     * @param baseAction
     * @return serialized obj
     */
    public static String toGson(BaseAction baseAction) {
        if (gson == null)
            createGson();

        return gson.toJson(baseAction, BaseAction.class);
    }
}