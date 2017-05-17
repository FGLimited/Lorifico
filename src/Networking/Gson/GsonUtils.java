package Networking.Gson;

import Action.BaseAction;
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
        gson = new GsonBuilder().registerTypeAdapter(BaseAction.class, new MySerializer<BaseAction>())
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