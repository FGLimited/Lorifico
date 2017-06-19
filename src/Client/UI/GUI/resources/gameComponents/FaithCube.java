package Client.UI.GUI.resources.gameComponents;

import Game.UserObjects.FamilyColor;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.transform.Translate;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by andrea on 19/06/17.
 */
public class FaithCube extends Group {
    private static int[] occupants = {0, 0, 0};
    //Map each family color to real color
    private Map<FamilyColor, Color> familyColorMap = new HashMap<FamilyColor, Color>() {{
        put(FamilyColor.Green, Color.GREEN);
        put(FamilyColor.Blue, Color.BLUE);
        put(FamilyColor.Yellow, Color.YELLOW);
        put(FamilyColor.Red, Color.RED);
    }};
    private Translate translate;

    /**
     * Places a faith cube on specified card
     *
     * @param familyColor             color of the cube
     * @param onWhichFaithCardOrdinal 1°, 2° or 3° card.
     */
    public FaithCube(FamilyColor familyColor, int onWhichFaithCardOrdinal) {
        if (onWhichFaithCardOrdinal > 3 || onWhichFaithCardOrdinal < 0) return;

        //Create new box with specified color
        Box box = new Box(15, 15, 15);
        PhongMaterial phongMaterial = new PhongMaterial(familyColorMap.get(familyColor));
        box.setMaterial(phongMaterial);

        //Get cube base position
        CubePosition cubePosition = CubePosition.values()[occupants[onWhichFaithCardOrdinal - 1]];

        //Calculate right position:
        double xPos = cubePosition.getxPos();
        double yPos = cubePosition.getyPos();

        //If we are on second or third card, let's adjust it:
        if (onWhichFaithCardOrdinal == 3) {
            xPos = xPos + 157.5;
        } else if (onWhichFaithCardOrdinal == 2) {
            xPos = xPos + 78.5;
            yPos = yPos + 10.5;
        }
        translate = new Translate(xPos, yPos, cubePosition.getzPos());
        getTransforms().add(translate);

        //Attach box to group
        getChildren().add(box);
        occupants[onWhichFaithCardOrdinal - 1]++;//We placed a cube, so we have a new occupant
    }

    private enum CubePosition {
        FIRSTCUBE(138.5, -172.0, -11.5),
        SECONDCUBE(160.5, -172.0, -11.5),
        THIRDCUBE(138.5, -151.0, -11.5),
        FOURTH(160.5, -151.0, -11.5);

        private double xPos, yPos, zPos;

        CubePosition(double xPos, double yPos, double zPos) {
            this.xPos = xPos;
            this.yPos = yPos;
            this.zPos = zPos;
        }

        public double getxPos() {
            return xPos;
        }

        public double getyPos() {
            return yPos;
        }

        public double getzPos() {
            return zPos;
        }
    }
}
