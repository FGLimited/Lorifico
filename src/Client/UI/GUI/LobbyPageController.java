package Client.UI.GUI;

import Client.UI.Lobby;
import Client.UI.UserInterfaceFactory;
import Model.User.User;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by Io on 23/05/2017.
 */
public class LobbyPageController implements Lobby, Initializable {
    private Timeline countDownTimeline;

    @FXML
    private StackPane root;

    @FXML
    private Label playersLabel;

    @FXML
    private Label countDownLabel;


    @Override
    public void showPage() {
        ((UserInterfaceImplemJFX) (UserInterfaceFactory.getInstance())).changeScene("Lobby", "fxml/LobbyPage.fxml", 415, 415, true, this);

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ((UserInterfaceImplemJFX) UserInterfaceFactory.getInstance()).setStackPane(root);//Updates reference to root stack pane in UserInterface, this way popus will be displayed in this page.
    }


    @Override
    public void setMatchAttendees(List<User> userList) {
        if (!Platform.isFxApplicationThread()) {
            Platform.runLater(() -> setMatchAttendees(userList));
            return;
        }

        if (userList.size() < 2)
            if (countDownTimeline != null)
                countDownTimeline.stop();

        if (playersLabel == null) return;

        playersLabel.setText("Utenti in attesa: ");
        userList.forEach(user -> {
            playersLabel.setText(playersLabel.getText() + ", " + user.getUsername());
        });
    }

    @Override
    public void restartTimer() {
        if (!Platform.isFxApplicationThread()) {
            Platform.runLater(() -> restartTimer());
            return;
        }
        if (playersLabel == null) return;
        countDownTimeline = new Timeline(new KeyFrame(
                Duration.millis(1000),
                ae -> decrementCountdown()));
        countDownTimeline.setCycleCount(Animation.INDEFINITE);
        countDownTimeline.play();
    }

    private void decrementCountdown() {
        countDownLabel.setText((Integer.valueOf(countDownLabel.getText()) - 1) + "");
    }

}
