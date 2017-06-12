package Client.UI.GUI;

import Client.Datawarehouse;
import Client.UI.DomesticsController;
import Client.UI.GUI.resources.gameComponents.AddSlaveToDomesticDialog;
import Game.UserObjects.DomesticColor;
import Game.UserObjects.FamilyColor;
import Game.UserObjects.GameUser;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by andrea on 12/06/17.
 */
public class DomesticBoxController implements DomesticsController, TurnObserver {
    private HBox hBox;
    private Map<DomesticColor, StackPane> stackPaneMap = new HashMap<>();

    private Map<DomesticColor, Color> domesticColorColorMap = new HashMap<DomesticColor, Color>() {{
        put(DomesticColor.Black, Color.BLACK);
        put(DomesticColor.Orange, Color.ORANGE);
        put(DomesticColor.White, Color.WHITE);
        put(DomesticColor.Neutral, Color.GRAY);
    }};

    private Map<FamilyColor, Color> familyColorMap = new HashMap<FamilyColor, Color>() {{
        put(FamilyColor.Green, Color.GREEN);
        put(FamilyColor.Blue, Color.BLUE);
        put(FamilyColor.Yellow, Color.YELLOW);
        put(FamilyColor.Red, Color.RED);
    }};

    public DomesticBoxController(HBox domesticsHBox, StackPane orangeDomestic2D, StackPane blackDomestic2D, StackPane whiteDomestic2D, StackPane neutralDomestic2D) {
        this.hBox = domesticsHBox;
        stackPaneMap.put(DomesticColor.Orange, orangeDomestic2D);
        stackPaneMap.put(DomesticColor.Black, blackDomestic2D);
        stackPaneMap.put(DomesticColor.White, whiteDomestic2D);
        stackPaneMap.put(DomesticColor.Neutral, neutralDomestic2D);

        //Register as turn observer
        Datawarehouse.getInstance().registerTurnObserver(this);
    }

    /**
     * Updates 2D GUI Labels and updates callback
     *
     * @param domesticValues
     */
    @Override
    public void updateDomesticsValues(Map<DomesticColor, Integer> domesticValues) {
        if (!Platform.isFxApplicationThread()) {
            Platform.runLater(() -> updateDomesticsValues(domesticValues));
            return;
        }
        //Updates domestics in 2D UI with correct color.
        stackPaneMap.forEach(((domesticColor, stackPane) -> update2DGUIColors(stackPane, domesticColor)));

        //Updates domestic with current label and callback
        domesticValues.forEach((DomesticColor domesticColor, Integer integer) -> {
            StackPane domesticStackPane = stackPaneMap.get(domesticColor);

            //When a click on this domestic happens, call callback
            domesticStackPane.setOnMouseClicked(event -> new AddSlaveToDomesticDialog(domesticColor, integer));

            //Update label with current value
            domesticStackPane.getChildren().forEach(node ->
            {
                if (node instanceof Label) ((Label) node).setText(integer.toString());
            });
        });
    }

    /**
     * Called when turn changes
     *
     * @param username user playing current turn.
     */
    @Override
    public void onTurnChange(String username) {
        //If it's my turn, enable domesticsBox
        if (username.equals(Datawarehouse.getInstance().getMyUsername())) {
            hBox.setDisable(false);
        } else {
            hBox.setDisable(true);
        }
    }

    /**
     * Sets correct colors on 2D UI.
     *
     * @param element
     * @param domesticColor
     */
    private void update2DGUIColors(StackPane element, DomesticColor domesticColor) {
        GameUser gameUser = Datawarehouse.getInstance().getGameUser(Datawarehouse.getInstance().getMyUsername());
        FamilyColor familyColor = gameUser.getFamilyColor();

        boolean isNeutral = domesticColor.equals(DomesticColor.Neutral);

        //Search for bigger and smaller circle
        element.getChildren().forEach(node -> {
            if (node instanceof Circle) {
                Circle circle = (Circle) node;

                //This is the bigger circle
                if (circle.getRadius() > 20) {
                    circle.setFill((isNeutral ? domesticColorColorMap.get(DomesticColor.Neutral) : familyColorMap.get(familyColor)));
                } else {
                    circle.setFill((isNeutral ? familyColorMap.get(familyColor) : domesticColorColorMap.get(domesticColor)));
                }
            }
        });
    }

}
