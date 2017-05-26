package Server.Game.Usable;

import Game.Usable.ResourceType;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by fiore on 11/05/2017.
 */
public class UsableHelper {

    /**
     * Edit given state resources using toEdit resources list
     *
     * @param src Map of resources to add/remove
     * @param dest Map to update
     * @param removeAdd True to add resources, false to remove them
     * @param <T> Key type
     */
    public static <T> void editResources(Map<T, Integer> src, Map<T, Integer> dest, boolean removeAdd) {

        for(Map.Entry<T, Integer> resource : src.entrySet())
            dest.merge(resource.getKey(), resource.getValue(), (a, b) -> removeAdd ? a + b : (a - b >= 0 ? a - b : 0));
    }

    /**
     * Deep clone of given map
     *
     * @param map Map to clone
     * @return Cloned map
     */
    public static Map<ResourceType, Integer> cloneMap(Map<ResourceType, Integer> map) {

        return map.entrySet().parallelStream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().intValue()));

    }
}
