package Action;

import Client.UI.UserInterfaceFactory;
import Model.User.User;

/**
 * Created by Io on 23/05/2017.
 */
public class ChangeClientView implements BaseAction {
    private View view;//View to go to

    public ChangeClientView(View view) {
        this.view = view;
    }

    @Override
    public void doAction(User user) {
        switch (view) {
            case CHOOSE_AVATAR:
                UserInterfaceFactory.getInstance().getChooseAvatar().showPage();
                break;
            case LOBBY:
                UserInterfaceFactory.getInstance().getLobby().showPage();
                break;
            default:
                break;
        }


    }

    public enum View {
        CHOOSE_AVATAR,
        LOBBY,
        GAME
    }
}