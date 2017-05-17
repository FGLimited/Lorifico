package Game;

/**
 * Created by fiore on 08/05/2017.
 */
public interface HasValue<T> {

    /**
     * Get current value
     *
     * @return Current instance value
     */
    T getValue();

    /**
     * Update current value
     *
     * @param newValue New instance value
     */
    void setValue(T newValue);
}
