package Client.UI.GUI.resources.gameComponents;

import Client.UI.RoundOrderController;
import Game.UserObjects.FamilyColor;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.paint.Color;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by andrea on 07/06/17.
 */

/**
 * This class controls Round Order Pawns on the right of gameTable
 */
public class RoundOrderPawnsBlock extends Group implements RoundOrderController {
    private Map<FamilyColor, CylindricalPawn> cylindricalPawnMap = new HashMap<FamilyColor, CylindricalPawn>() {{
        put(FamilyColor.Blue, new CylindricalPawn(Color.BLUE, 0, 0, 0));
        put(FamilyColor.Green, new CylindricalPawn(Color.GREEN, 0, 0, 0));
        put(FamilyColor.Yellow, new CylindricalPawn(Color.YELLOW, 0, 0, 0));
        put(FamilyColor.Red, new CylindricalPawn(Color.RED, 0, 0, 0));
    }};

    public RoundOrderPawnsBlock() {
    }

    /**
     * Reorders pawns on gameTable
     *
     * @param familyColorOrderList list containing order of pawns
     */
    @Override
    public void setGameOrder(List<FamilyColor> familyColorOrderList) {
        if (!Platform.isFxApplicationThread()) {
            Platform.runLater(() -> setGameOrder(familyColorOrderList));
            return;
        }

        cylindricalPawnMap.forEach(((familyColor, cylindricalPawn) -> {

            //If received user list contains this familyColor we can place/move his pawn on the map
            if (familyColorOrderList.contains(familyColor)) {

                //If pawn is not shown in gameTable let's add it
                if (!getChildren().contains(cylindricalPawn)) getChildren().add(cylindricalPawn);

                //Calculate yPosition
                double yPos = 74d + familyColorOrderList.indexOf(familyColor) * 54.5d;

                //Update pawn position
                cylindricalPawn.setPosition(778.5, yPos, 0);
            } else {
                //Check if there are pawns on gameTable which now have left the game and remove them.
                if (getChildren().contains(cylindricalPawn)) getChildren().remove(cylindricalPawn);
            }
        }));
    }
}