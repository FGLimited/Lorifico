package Client.UI.GUI.resources.gameComponents;

import Client.Datawarehouse;
import Client.UI.FaithRoadController;
import Client.UI.PlayerStateObserver;
import Game.Usable.ResourceType;
import Game.UserObjects.FamilyColor;
import Game.UserObjects.PlayerState;
import Logging.Logger;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.transform.Translate;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by andrea on 01/06/17.
 */
public class FaithBlock extends Group implements FaithRoadController, PlayerStateObserver {
    private final double STEP_IN_PIXEL = 46;//Single step in pixels to move a pawn
    private Map<FamilyColor, FaithCylindricalPawn> map = new HashMap<FamilyColor, FaithCylindricalPawn>();
    private Integer[] occupantsFaithPosition = new Integer[16];

    /**
     * Creates a new faith road and places it on the map
     */
    public FaithBlock() {
        //Load Cylindrical Pawns
        occupantsFaithPosition[0] = 0;//At the beginning of time there are 0 pawns in 0 position
        map.put(FamilyColor.Blue, new FaithCylindricalPawn(Color.BLUE, 0, occupantsFaithPosition, 0, 0));
        map.put(FamilyColor.Green, new FaithCylindricalPawn(Color.GREEN, 0, occupantsFaithPosition, 0, 0));
        map.put(FamilyColor.Yellow, new FaithCylindricalPawn(Color.YELLOW, 0, occupantsFaithPosition, 0, 0));
        map.put(FamilyColor.Red, new FaithCylindricalPawn(Color.RED, 0, occupantsFaithPosition, 0, 0));

        //Attach pawns to FaithBlock
        map.forEach(((familyColor, cylindricalPawn) -> getChildren().add(cylindricalPawn)));

        //Move Faith Block
        getTransforms().add(new Translate(34, 368, 0));

        //Say Datawarehouse we are interested in playerstate
        Datawarehouse.getInstance().registerPlayerStateObserver(this);
    }

    /**
     * Shows passed cards in UI
     *
     * @param first  faith card of first turn
     * @param second faith card of second turn
     * @param third  faith card of third turn
     */
    @Override
    public void showFaithCards(int first, int second, int third) throws IndexOutOfBoundsException {
        if (first > 21 || second > 21 || third > 21) throw new IndexOutOfBoundsException("Carta inesistente");
        if (first < 0 || second < 0 || third < 0) throw new IndexOutOfBoundsException("ID carta negativo");

        if (!Platform.isFxApplicationThread()) {
            Platform.runLater(() -> showFaithCards(first, second, third));
            return;
        }

        getChildren().add(new FaithCard(first));
        getChildren().add(new FaithCard(second));
        getChildren().add(new FaithCard(third));
    }

    /**
     * Shows a faith cube on 1st 2nd or 3rd card
     *
     * @param familyColor color of cube
     * @param cardOrdinal may be 1,2,3
     */
    @Override
    public void showFaithCube(FamilyColor familyColor, int cardOrdinal) {
        getChildren().add(new FaithCube(familyColor, cardOrdinal));
    }

    /**
     * Call received when PlayerState is updated
     *
     * @param playerState
     * @param username
     */
    @Override
    public void onPlayerStateUpdate(PlayerState playerState, String username) {
        //If we received my playerState, update faithPosition
        Logger.log(Logger.LogLevel.Normal, "FaithBlock just received a new PlayerState");

        //Get received user's faithPosition and color
        int faithPosition = playerState.getResources().get(ResourceType.FaithPoint);
        FamilyColor familyColor = Datawarehouse.getInstance().getFamilyColor(username);

        if (familyColor != null) {
            moveToPosition(familyColor, faithPosition);
        } else {
            Logger.log(Logger.LogLevel.Error, "FaithBlock: I don't know my familyColor");
        }

    }

    /**
     * Moves a cylindrical pawn to given faith position.
     *
     * @param familyColor family color to move
     * @param position position to go to
     */
    private void moveToPosition(FamilyColor familyColor, int position) throws IndexOutOfBoundsException {
        if (!Platform.isFxApplicationThread()) {
            Platform.runLater(() -> moveToPosition(familyColor, position));
            return;
        }

        if (position > 15 || position < 0) throw new IndexOutOfBoundsException("Posizione inesistente");

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
            super(color, xPos, yPos, -1);

            //initially pawns are placed off screen
            this.stackPosition = -1;
            this.faithPosition = -1;
            this.occupantsFaithPosition = occupantsFaithPosition;//set reference to occupantsArray
        }

        public int getFaithPosition() {
            return faithPosition;
        }

        /**
         * Sets pawn position, calculates x,y pos and calls animation
         *
         * @param faithPosition desired pawn position
         */
        public void setFaithPosition(int faithPosition) {
            if (faithPosition == this.faithPosition) return; //We are not moving

            //If it was placed on table we have to decrement occupants of his position
            if (this.stackPosition >= 0)
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
         * @param xPos desired x pos
         * @param yPos desired y pos
         * @param stackPosition position in stack (used if there are other pawn under this)
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