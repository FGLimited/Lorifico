package Client.UI.GUI.resources.gameComponents;

import com.interactivemesh.jfx.importer.ImportException;
import com.interactivemesh.jfx.importer.tds.TdsModelImporter;
import javafx.scene.DepthTest;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Translate;

import java.net.URL;

/**
 * Created by andrea on 01/06/17.
 */
public abstract class Abstract3dsComponent extends Group {
    //Translation
    private Translate translate;

    /**
     * Loads a 3ds model into this group and moves whole group to specified position
     *
     * @param path   path to model
     * @param xPos   final group x position
     * @param yPos   final group  y position
     * @param zPos   final group  z position
     * @param xRot   model x rotation
     * @param yRot   model y rotation
     * @param zRot   model z rotation
     * @param xScale model x scaling factor
     * @param yScale model y scaling factor
     * @param zScale model z scaling factor
     */
    protected void load3ds(String path, double xPos, double yPos, double zPos, double xRot, double yRot, double zRot, double xScale, double yScale, double zScale) {

        TdsModelImporter tdsImporter = new TdsModelImporter();
        try {
            URL modelUrl = this.getClass().getResource(path);
            tdsImporter.read(modelUrl);
        } catch (ImportException e) {
            System.out.println("Orrore nel caricamento del modello");
            System.out.println(path);
            e.printStackTrace();
        }

        //Attach it to current Group
        Node[] rootNodes = tdsImporter.getImport();
        Group objGroup = new Group(rootNodes);
        getChildren().add(objGroup);

        //Enable Depth test
        objGroup.setDepthTest(DepthTest.ENABLE);
        setDepthTest(DepthTest.ENABLE);

        //Rotate model
        Rotate xRotation = new Rotate(xRot, Rotate.X_AXIS);
        Rotate yRotation = new Rotate(yRot, Rotate.Y_AXIS);
        Rotate zRotation = new Rotate(zRot, Rotate.Z_AXIS);

        //Model is too big, we have to scale it down
        Scale scaleModel = new Scale();
        scaleModel.setX(xScale);
        scaleModel.setY(yScale);
        scaleModel.setZ(zScale);

        //Add all above transformations to our model
        objGroup.getTransforms().addAll(xRotation, yRotation, zRotation, scaleModel);

        //Move group to specified pos
        translate = new Translate(xPos, yPos, zPos);
        getTransforms().add(translate);

    }

    public Translate getTranslate() {
        return translate;
    }
}
