package Client.UI.GUI.resources.gameComponents;

import Game.UserObjects.Choosable;
import Game.UserObjects.DomesticColor;
import Game.UserObjects.FamilyColor;
import Server.Game.UserObjects.Domestic;
import javafx.scene.Node;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.transform.Translate;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by andrea on 31/05/17.
 */
public class GameTablePlace extends StackPane {
    private final String COVERINGS_BASE_URL = "Client/UI/GUI/resources/images/gameTable";
    private final int Z_POS = -1;
    private Translate translate;
    private int id;//id of this covering
    private int capacity;//Max capacity of this position
    private Map<Domestic, Domestic3D> occupantsDomesticMap = new HashMap<>();
    private AtomicBoolean areTablePlacesEnabled;//boolean shared with all other coverings.

    private double xRegionPos, yRegionPos, radiusX, radiusY;//Position of clickable region

    private GameTablePlace binding = null;//GameTablePlace we are binded to, if that
    // binding place is free then no domestic can be placed in this


    protected GameTablePlace(int id, int capacity, boolean isCovered, GameTablePlace binding, double width, double height, double xCoveringPos, double yCoveringPos, double xRegionPos, double yRegionPos, double radiusX, double radiusY, AtomicBoolean areTablePlacesEnabled) {
        this(id, capacity, isCovered, width, height, xCoveringPos, yCoveringPos, xRegionPos, yRegionPos, radiusX, radiusY, areTablePlacesEnabled);
        this.binding = binding;
        this.capacity = capacity;
    }

    /**
     * Creates a gameTablePlace that could be covered or be click-able.
     *
     * @param id
     * @param isCovered
     * @param width
     * @param height
     * @param xCoveringPos
     * @param yCoveringPos
     * @param xRegionPos
     * @param yRegionPos
     * @param radiusX
     * @param radiusY
     */
    protected GameTablePlace(int id, int capacity, boolean isCovered, double width, double height, double xCoveringPos, double yCoveringPos, double xRegionPos, double yRegionPos, double radiusX, double radiusY, AtomicBoolean areTablePlacesEnabled) {
        this.capacity = capacity;
        this.id = id;
        this.areTablePlacesEnabled = areTablePlacesEnabled;
        this.xRegionPos = xRegionPos;
        this.yRegionPos = yRegionPos;
        this.radiusX = radiusX;
        this.radiusY = radiusY;
        if (isCovered) {
            final String url = COVERINGS_BASE_URL + "/covering" + id + ".png";
            createCoveringStackPane(url, width, height, xCoveringPos, yCoveringPos, Z_POS);
        } else {
            createClickableRegion(xRegionPos, yRegionPos, Z_POS, radiusX, radiusY);
        }
    }

    /**
     * Creates a gameTablePlace always active.
     *
     * @param id
     * @param xRegionPos
     * @param yRegionPos
     * @param radiusX
     * @param radiusY
     */
    protected GameTablePlace(int id, int capacity, double xRegionPos, double yRegionPos, double radiusX, double radiusY, AtomicBoolean areTablePlacesEnabled) {
        createClickableRegion(xRegionPos, yRegionPos, Z_POS, radiusX, radiusY);
        this.capacity = capacity;
        this.id = id;
        this.areTablePlacesEnabled = areTablePlacesEnabled;
        this.xRegionPos = xRegionPos;
        this.yRegionPos = yRegionPos;
        this.radiusX = radiusX;
        this.radiusY = radiusY;
    }


    /**
     * Loads the covering image and places it
     *
     * @param url
     * @param width
     * @param height
     * @param xPos
     * @param yPos
     * @param zPos
     */
    private void createCoveringStackPane(String url, double width, double height, double xPos, double yPos, double zPos) {
        Image image = new Image(url, true);
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(height);
        imageView.setFitWidth(width);
        getChildren().add(imageView);
        setPrefSize(width, height);
        translate = new Translate(xPos, yPos, zPos);
        getTransforms().add(translate);
    }

    /**
     * Creates a trasparent figure and places it
     *
     * @param xPos
     * @param yPos
     * @param zPos
     * @param radiusX
     * @param radiusY
     */
    private void createClickableRegion(double xPos, double yPos, double zPos, double radiusX, double radiusY) {
        Ellipse ellipse = new Ellipse(radiusX, radiusY);
        ellipse.setFill(Color.WHITE);
        ellipse.setOpacity(0);//it's trasparent

        getChildren().add(ellipse);
        ellipse.radiusXProperty().bind(prefWidthProperty());
        ellipse.radiusYProperty().bind(prefHeightProperty());
        setPrefSize(radiusX, radiusY);
        translate = new Translate(xPos, yPos, zPos);
        getTransforms().add(translate);


        //We highlight it when mouse is over it
        ellipse.setOnMouseEntered(event -> {
            //If table places aren't enabled stop animation
            /*
            if (!areTablePlacesEnabled.get())
                return;

            //Check if this position is bound to another one
            if (binding != null) {
                //If position we are bound to is not full, abort
                if (!binding.isFull()) return;
            }

            if (isFull()) return;//if we are full, stop
*/
            ColorAdjust colorAdjust = new ColorAdjust();
            colorAdjust.setContrast(0.1);
            colorAdjust.setHue(-0.05);
            colorAdjust.setBrightness(0.8);
            colorAdjust.setSaturation(0.3);
            ellipse.setEffect(colorAdjust);
            ellipse.setOpacity(0.35);
        });

        ellipse.setOnMouseExited(event -> {
            ellipse.setEffect(null);
            ellipse.setOpacity(0);
        });

        //Callback on click
        ellipse.setOnMouseClicked(event -> {
            //If table places aren't enabled stop animation
            if (!areTablePlacesEnabled.get())
                return;

            //Check if this position is bound to another one
            if (binding != null) {
                System.out.println("this pos has binding");
                //If position we are bound to is not full, abort
                if (!binding.isFull()) return;
                System.out.println("binding is full, go on");
            }

            if (isFull()) return;//if we are full, stop

            //Else send message to server asking to occupy this position
            int position = (this.id + occupantsDomesticMap.size());
            //BaseAction action = new Move(position, Collections.emptyList());
            //CommunicationManager.getInstance().sendMessage(action);
            addDomestic(new Domestic(FamilyColor.Blue, DomesticColor.Orange, 3), position);
            System.out.println("Asking server to occupy " + position);
        });

    }

    public boolean isFull() {
        if (occupantsDomesticMap.size() >= capacity) return true;
        return false;
    }

    /**
     * Removes all domestic from this position
     */
    public void freeAllPosition() {
        for (Iterator<Node> iterator = getChildren().iterator(); iterator.hasNext(); ) {
            Node node = iterator.next();
            if (node instanceof Domestic3D) {
                iterator.remove();
                occupantsDomesticMap.values().remove(node);
            }
        }
    }


    public void addDomestic(Domestic domestic, int positionNumber) {
        if (positionNumber < id || positionNumber >= id + capacity) return;//Position is not in this covering

        //Calculate xPos
        double xPos;
        if (capacity == 1) xPos = 0;
        else xPos = ((radiusX * 2) / capacity) * occupantsDomesticMap.size();

        Domestic3D domestic3D = new Domestic3D(domestic);
        occupantsDomesticMap.putIfAbsent(domestic, domestic3D);//Link Domestic3D to server's domestic
        domestic3D.setPos(xPos, 0, -20);
        getChildren().add(domestic3D);//Add domestic to Tower
        System.out.println("Added");
    }

    public void setCostPerPosition(Map<Integer, List<Choosable>> choosablePerPos) {
    }
}
