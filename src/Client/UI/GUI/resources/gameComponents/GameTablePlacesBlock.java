package Client.UI.GUI.resources.gameComponents;

import Client.Datawarehouse;
import Game.UserObjects.Choosable;
import Server.Game.UserObjects.Domestic;
import javafx.scene.Group;

import java.util.List;
import java.util.Map;

/**
 * Created by andrea on 06/06/17.
 */
public class GameTablePlacesBlock extends Group {


    public GameTablePlacesBlock() {
        //Place Coverings / Clickable Positions

        boolean isActive = false;
        int playingUsers = Datawarehouse.getInstance().getMatchAttendees().size();//number of active players

        getChildren().add(new GameTablePlace(50, 4, 562.5, 136.5, 135, 57, isActive));
        getChildren().add(new GameTablePlace(42, 1, (playingUsers < 4 ? true : false), 90, 105, 680.5, 455.5, 728.5, 503.0, 43, 40, isActive));
        getChildren().add(new GameTablePlace(43, 1, (playingUsers < 4 ? true : false), 91.17, 108.2, 748.5, 520, 793.5, 572.5, 43, 40, isActive));
        getChildren().add(new GameTablePlace(41, 1, 639.0, 476.5, 43, 40, isActive));
        getChildren().add(new GameTablePlace(40, 1, 544.0, 476.0, 43, 40, isActive));


        GameTablePlace harvestPlace = new GameTablePlace(20, 1, 52, 624, 43, 40, isActive);
        getChildren().add(new GameTablePlace(21, 3, (playingUsers < 3 ? true : false), harvestPlace, 226, 111.8, 116.5, 575.5, 226, 620.5, 100, 35, isActive));
        getChildren().add(harvestPlace);

        GameTablePlace productionPlace = new GameTablePlace(30, 1, 53.0, 502.5, 43, 40, isActive);
        getChildren().add(new GameTablePlace(31, 3, (playingUsers < 3 ? true : false), productionPlace, 224, 105.7, 116, 457, 228, 503, 100, 35, isActive));
        getChildren().add(productionPlace);
    }

    /**
     * Method called when all domestic have to leave their occupied position.
     */
    public void freeAllPosition() {
        getChildren().forEach(node -> {
            if (node instanceof GameTablePlace) {
                ((GameTablePlace) node).freeAllPosition();
            }
        });
    }

    /**
     * Add a domestic to specified place
     *
     * @param occupant
     * @param positionNumber
     */
    public void addDomestic(Domestic occupant, int positionNumber) {
        getChildren().forEach(node -> {
            if (node instanceof GameTablePlace) {
                ((GameTablePlace) node).addDomestic(occupant, positionNumber);
            }
        });
    }

    /**
     * Sets cost per each position and enables places
     * @param choosablePerPos
     */
    public void setCostPerPosition(Map<Integer, List<Choosable>> choosablePerPos) {
        //Send costs / effects to each covering.
        getChildren().forEach(node -> {
            if (node instanceof GameTablePlace) {
                ((GameTablePlace) node).setCostPerPosition(choosablePerPos);
            }
        });
    }
}
