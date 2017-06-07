package Server.Game.UserObjects;

import Game.HasType;
import Game.HasValue;
import Game.UserObjects.DomesticColor;
import Game.UserObjects.FamilyColor;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by fiore on 09/05/2017.
 */
public class Domestic implements HasValue<Integer>, HasType<DomesticColor> {

    private final FamilyColor familyColor;

    private final DomesticColor type;

    private volatile int value = 0;

    private final AtomicBoolean inPosition;

    /**
     * Initialize a new domestic with given color and initial value
     *
     * @param color Domestic's color
     * @param value Initial domestic value
     */
    public Domestic(FamilyColor familyColor, DomesticColor color, int value) {
        this.familyColor = familyColor;
        type = color;
        this.value = value > 0 ? value : 0;
        inPosition = new AtomicBoolean(false);
    }

    /**
     * Create new domestic maintaining inPosition value bind with given domestic instance
     *
     * @param toBind Domestic to bind to
     */
    public Domestic(Domestic toBind) {
        familyColor = toBind.familyColor;
        type = toBind.type;
        value = toBind.value;
        inPosition = toBind.inPosition;
    }

    /**
     * Gson constructor
     */
    private Domestic() {
        familyColor = null;
        type = null;
        inPosition = null;
    }

    @Override
    public Integer getValue() {
        return value;
    }

    @Override
    public void setValue(Integer newValue) {
        value = newValue;
    }

    @Override
    public DomesticColor getType() {
        return type;
    }

    /**
     * Get domestic's family color
     *
     * @return Domestic's family color
     */
    public FamilyColor getFamilyColor() {
        return familyColor;
    }

    /**
     * Check if domestic is in position
     *
     * @return True if in position, false else
     */
    public boolean isInPosition() {
        return inPosition.get();
    }

    /**
     * Set in position
     *
     * @param inPosition In position value
     */
    public void setInPosition(boolean inPosition) {
        this.inPosition.set(inPosition);
    }

}
