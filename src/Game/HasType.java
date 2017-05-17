package Game;

/**
 * Created by fiore on 08/05/2017.
 */
public interface HasType<T extends Enum> {

    /**
     * Get type of this instance
     *
     * @return Instance type
     */
    T getType();

}
