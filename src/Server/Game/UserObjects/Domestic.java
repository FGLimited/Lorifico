package Server.Game.UserObjects;

import Game.HasType;
import Game.HasValue;
import Game.UserObjects.DomesticColor;
import Game.UserObjects.FamilyColor;

/**
 * Created by fiore on 09/05/2017.
 */
public class Domestic implements HasValue<Integer>, HasType<DomesticColor> {

    private final FamilyColor familyColor;

    private final DomesticColor type;

    private volatile int value;

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

}
