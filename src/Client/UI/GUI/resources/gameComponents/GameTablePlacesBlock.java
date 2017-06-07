package Client.UI.GUI.resources.gameComponents;

import javafx.scene.Group;

/**
 * Created by andrea on 06/06/17.
 */
public class GameTablePlacesBlock extends Group {

    public GameTablePlacesBlock() {
        //Place Coverings / Clickable Positions
        getChildren().add(new GameTablePlace(50, 4, 430, 78.5, 135, 57));
        getChildren().add(new GameTablePlace(42, 1, false, 90, 105, 680.5, 455.5, 684, 462, 43, 40));
        getChildren().add(new GameTablePlace(43, 1, false, 91.17, 108.2, 748.5, 520, 751.5, 531.5, 43, 40));
        getChildren().add(new GameTablePlace(41, 1, 595.5, 437, 43, 40));
        getChildren().add(new GameTablePlace(40, 1, 501.5, 436.5, 43, 40));
        getChildren().add(new GameTablePlace(30, 1, 7.5, 461.5, 43, 40));
        getChildren().add(new GameTablePlace(20, 1, 8, 584, 43, 40));
        getChildren().add(new GameTablePlace(21, 3, false, 226, 111.8, 116.5, 575.5, 130.5, 587, 100, 35));
        getChildren().add(new GameTablePlace(31, 3, false, 224, 105.7, 116, 457, 130, 465, 100, 35));
    }

    /**
     * Method called when all domestic have to leave their occupied position.
     */
    public void freeAllPosition() {
        return;
    }
}
