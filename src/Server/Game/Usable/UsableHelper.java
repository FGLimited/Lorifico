package Server.Game.Usable;

import java.util.Map;

/**
 * Created by fiore on 11/05/2017.
 */
public class UsableHelper {

    /**
     * Edit given state resources using toEdit resources list
     *
     * @param src Map of resources to add/remove
     * @param dest Map to update
     * @param addRemove True to add resources, false to remove them
     * @param <T> Key type
     */
    public static <T> void editResources(Map<T, Integer> src, Map<T, Integer> dest, boolean addRemove) {

        for(Map.Entry<T, Integer> resource : src.entrySet())
            dest.merge(resource.getKey(), resource.getValue(), (a, b) -> a + b);
    }
}
