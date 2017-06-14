package Client.UI.GUI.resources.gameComponents;

import Client.Datawarehouse;
import javafx.scene.Group;

/**
 * Created by andrea on 06/06/17.
 */
public class GameTablePlacesBlock extends Group {

    public GameTablePlacesBlock() {
        //Place Coverings / Clickable Positions
        int playingUsers = Datawarehouse.getInstance().getMatchAttendees().size();

        getChildren().add(new GameTablePlace(50, 4, 430, 78.5, 135, 57));
        getChildren().add(new GameTablePlace(42, 1, (playingUsers < 4 ? true : false), 90, 105, 680.5, 455.5, 684, 462, 43, 40));
        getChildren().add(new GameTablePlace(43, 1, (playingUsers < 4 ? true : false), 91.17, 108.2, 748.5, 520, 751.5, 531.5, 43, 40));
        getChildren().add(new GameTablePlace(41, 1, 595.5, 437, 43, 40));
        getChildren().add(new GameTablePlace(40, 1, 501.5, 436.5, 43, 40));


        GameTablePlace harvestPlace = new GameTablePlace(20, 1, 8, 584, 43, 40);
        getChildren().add(new GameTablePlace(21, 3, (playingUsers < 3 ? true : false), harvestPlace, 226, 111.8, 116.5, 575.5, 130.5, 587, 100, 35));
        getChildren().add(harvestPlace);

        GameTablePlace productionPlace = new GameTablePlace(30, 1, 7.5, 461.5, 43, 40);
        getChildren().add(new GameTablePlace(31, 3, (playingUsers < 3 ? true : false), productionPlace, 224, 105.7, 116, 457, 130, 465, 100, 35));
        getChildren().add(productionPlace);
    }

    /**
     * Method called when all domestic have to leave their occupied position.
     */
    public void freeAllPosition() {
        return;
    }
}
