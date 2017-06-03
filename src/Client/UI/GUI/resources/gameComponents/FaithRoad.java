package Client.UI.GUI.resources.gameComponents;

import Game.UserObjects.FamilyColor;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.transform.Translate;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by andrea on 01/06/17.
 */
public class FaithRoad extends Group {
    private final double STEP_IN_PIXEL = 51.8;//Single step in pixels to move a pawn
    private Map<FamilyColor, FaithCylindricalPawn> map = new HashMap<FamilyColor, FaithCylindricalPawn>();
    private Integer[] occupantsFaithPosition = new Integer[16];

    /**
     * Creates a new faith road and places it on the map
     */
    public FaithRoad() {

        //Creates single Cylindrical Pawn
        occupantsFaithPosition[0] = 0;//At the beginning of time there are 0 pawns in 0 position
        map.put(FamilyColor.Blue, new FaithCylindricalPawn(Color.BLUE, 0, occupantsFaithPosition, 0, 0));
        map.put(FamilyColor.Green, new FaithCylindricalPawn(Color.GREEN, 0, occupantsFaithPosition, 0, 0));
        map.put(FamilyColor.Yellow, new FaithCylindricalPawn(Color.YELLOW, 0, occupantsFaithPosition, 0, 0));
        map.put(FamilyColor.Red, new FaithCylindricalPawn(Color.RED, 0, occupantsFaithPosition, 0, 0));

        //Attach faith road to game table
        getTransforms().add(new Translate(34, 368, 0));


        //Debug
        map.forEach(((familyColor, cylindricalPawn) -> {
            getChildren().add(cylindricalPawn);

            cylindricalPawn.setOnMouseClicked(event -> {
                this.moveToPosition(familyColor, cylindricalPawn.getFaithPosition() + 1);
            });

        }));

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
            //map.forEach(((familyColor, faithCylindricalPawn) -> ));


            if (occupantsFaithPosition[faithPosition] == null) occupantsFaithPosition[faithPosition] = 0;
            this.faithPosition = faithPosition;
            animateToPosition(STEP_IN_PIXEL * (double) faithPosition, 0, occupantsFaithPosition[faithPosition]);
            occupantsFaithPosition[faithPosition]++;//We just placed our pawn in this position.
        }

        public int getStackPosition() {
            return stackPosition;
        }


    }
}
