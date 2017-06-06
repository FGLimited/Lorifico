package Client.UI.GUI.resources.gameComponents;

import Client.UI.FaithRoadController;
import Game.UserObjects.FamilyColor;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.transform.Translate;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by andrea on 01/06/17.
 */
public class FaithRoadBlock extends Group implements FaithRoadController {
    private final double STEP_IN_PIXEL = 46;//Single step in pixels to move a pawn
    private Map<FamilyColor, FaithCylindricalPawn> map = new HashMap<FamilyColor, FaithCylindricalPawn>();
    private Integer[] occupantsFaithPosition = new Integer[16];

    /**
     * Creates a new faith road and places it on the map
     */
    public FaithRoadBlock() {

        //Creates single Cylindrical Pawn
        occupantsFaithPosition[0] = 0;//At the beginning of time there are 0 pawns in 0 position
        map.put(FamilyColor.Blue, new FaithCylindricalPawn(Color.BLUE, 0, occupantsFaithPosition, 0, 0));
        map.put(FamilyColor.Green, new FaithCylindricalPawn(Color.GREEN, 0, occupantsFaithPosition, 0, 0));
        map.put(FamilyColor.Yellow, new FaithCylindricalPawn(Color.YELLOW, 0, occupantsFaithPosition, 0, 0));
        map.put(FamilyColor.Red, new FaithCylindricalPawn(Color.RED, 0, occupantsFaithPosition, 0, 0));

        //Attach pawns to FaithRoadBlock
        map.forEach(((familyColor, cylindricalPawn) -> getChildren().add(cylindricalPawn)));

        //Attach faith road to game table
        getTransforms().add(new Translate(34, 368, 0));
    }

    /**
     * Moves a cylindrical pawn to given faith position.
     *
     * @param familyColor
     * @param position
     */
    public void moveToPosition(FamilyColor familyColor, int position) {
        if (position > 15 || position < 0) return;

        FaithCylindricalPawn faithCylindricalPawn = map.get(familyColor);
        faithCylindricalPawn.setFaithPosition(position);
    }


    /**
     * Class used to handle Faith Cylindrical Pawns
     */
    private class FaithCylindricalPawn extends CylindricalPawn {
        private int faithPosition;//pawn actual position.
        private int stackPosition;//position in stack
        private Integer[] occupantsFaithPosition;//Reference to array used to keep a counter of pawns in every position

        public FaithCylindricalPawn(Color color, int faithPosition, Integer[] occupantsFaithPosition, double xPos, double yPos) {
            super(color, xPos, yPos, occupantsFaithPosition[faithPosition]);
            this.stackPosition = occupantsFaithPosition[faithPosition];
            this.faithPosition = faithPosition;
            this.occupantsFaithPosition = occupantsFaithPosition;
            occupantsFaithPosition[faithPosition]++;
        }

        public int getFaithPosition() {
            return faithPosition;
        }

        /**
         * Updates pawn position
         *
         * @param faithPosition
         */
        public void setFaithPosition(int faithPosition) {
            occupantsFaithPosition[this.faithPosition]--;//We removed or pawn from a position
            if (occupantsFaithPosition[faithPosition] == null) occupantsFaithPosition[faithPosition] = 0;
            this.faithPosition = faithPosition;
            this.stackPosition = occupantsFaithPosition[faithPosition];
            animateToPosition(faithPosition, 0, stackPosition);
            occupantsFaithPosition[faithPosition]++;//We just placed our pawn in this position.
        }

        /**
         * Adjusts movements coords
         *
         * @param xPos
         * @param yPos
         * @param stackPosition
         */
        @Override
        public void animateToPosition(double xPos, double yPos, int stackPosition) {
            if (xPos <= 2) {
                xPos = STEP_IN_PIXEL * (double) faithPosition;
            } else if (xPos > 2 && xPos <= 5) {
                xPos = 150 + 77 * (faithPosition - 3);
            } else {//xPos >= 6
                xPos = 361 + STEP_IN_PIXEL * (faithPosition - 6);
            }
            super.animateToPosition(xPos, yPos, stackPosition);
        }
    }
}
