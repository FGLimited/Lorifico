package Client.UI;

/**
 * Created by Io on 27/05/2017.
 */
public interface GameUI {
    void showPage();

    TowersController getTowersController();

    DiceController getDiceController();

    FaithRoadController getFaithController();
}
