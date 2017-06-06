package Client.UI.GUI.resources.gameComponents;

/**
 * Created by andrea on 06/06/17.
 */
public class FaithCard extends AbstractImageComponent {
    public static FaithCard last;

    public FaithCard(int number) {
        //From server notation to client's:
        int turn = (number - 1) / 7 + 1;
        int id = (number - 1) % 7 + 1;

        //Array of positions on gui:
        CardPosition[] cardPositions = new CardPosition[3];
        cardPositions[0] = new CardPosition(-13, -451);
        cardPositions[1] = new CardPosition(64, -420);
        cardPositions[2] = new CardPosition(143.5, -450.0);

        CardPosition cardPosition = cardPositions[turn - 1];//Get position of current card

        //Loads image
        loadImage("Client/UI/GUI/resources/images/carteScomunica/excomm_" + turn + "_" + id + ".png", cardPosition.getxPos(), cardPosition.getyPos(), -3, 0.22, 0, 0, 0);
    }

    /**
     * Inner class used to store positions
     */
    private class CardPosition {
        private double xPos;
        private double yPos;

        public CardPosition(double xPos, double yPos) {
            this.xPos = xPos;
            this.yPos = yPos;
        }

        public double getxPos() {
            return xPos;
        }

        public double getyPos() {
            return yPos;
        }
    }
}