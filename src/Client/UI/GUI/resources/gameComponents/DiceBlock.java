package Client.UI.GUI.resources.gameComponents;

import Client.UI.DiceController;
import javafx.application.Platform;
import javafx.scene.Group;

/**
 * Created by Io on 06/06/2017.
 */
public class DiceBlock extends Group implements DiceController {
    private Dice[] dice = new Dice[3];//Dice

    /**
     * Creates a group containing three dices
     */
    public DiceBlock() {
        dice[0] = new Dice(Dice.DiceType.BLACK);
        dice[1] = new Dice(Dice.DiceType.WHITE);
        dice[2] = new Dice(Dice.DiceType.ORANGE);

        getChildren().addAll(dice);
    }

    /**
     * Sets passed numbers to three dices animating them.
     *
     * @param first  first dice number
     * @param second second dice number
     * @param third  third dice number
     */
    @Override
    public void setNumbers(int first, int second, int third) {
        if (!Platform.isFxApplicationThread()) {
            Platform.runLater(() -> setNumbers(first, second, third));
            return;
        }

        dice[0].setNumber(first);
        dice[1].setNumber(second);
        dice[2].setNumber(third);
    }
}
