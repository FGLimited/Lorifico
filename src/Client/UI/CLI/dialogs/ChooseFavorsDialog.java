package Client.UI.CLI.dialogs;

import Action.BaseAction;
import Action.UseFavor;
import Client.CommunicationManager;
import Client.UI.CLI.UserInterfaceImplemCLI;
import Client.UI.CLI.cliUtils.CliSout;
import Client.UI.UserInterfaceFactory;
import Game.Effects.Effect;
import com.budhash.cliche.Command;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by andrea on 16/06/17.
 */
public class ChooseFavorsDialog {
    private int differentFavors;
    private List<Effect> councilFavors;

    private List<Effect> choosenFavours = new ArrayList<>();
    private Effect[] effects;

    public ChooseFavorsDialog(List<Effect> councilFavors, int differentFavors) {
        this.councilFavors = councilFavors;
        this.differentFavors = differentFavors;
        System.out.println("Scegli " + differentFavors + " diversi favori del consiglio utilizzando il comando 'scegli #ID'");

        effects = councilFavors.toArray(new Effect[councilFavors.size()]);
        for (int i = 0; i < effects.length; i++) {
            System.out.println(i + ") " + effects[i].getDescription());
        }
    }

    private void sendToServerAndGoBack() {
        ((UserInterfaceImplemCLI) (UserInterfaceFactory.getInstance())).setCliPage(UserInterfaceFactory.getInstance().getGameUI(), true);//Go back
        BaseAction action = new UseFavor(choosenFavours);
        CommunicationManager.getInstance().sendMessage(action);
    }

    @Command(description = "Scegli il favore del consiglio")
    public void scegli(String numero) {
        try {
            int i = Integer.valueOf(numero);
            if (effects[i] == null || choosenFavours.contains(effects[i])) {
                CliSout.log(CliSout.LogLevel.Errore, "Favore non valido");
                return;
            }

            choosenFavours.add(effects[i]);
            if (choosenFavours.size() == differentFavors) {
                sendToServerAndGoBack();
            } else {
                CliSout.log(CliSout.LogLevel.Informazione, "Puoi scegliere altri " + (differentFavors - choosenFavours.size()) + "favori");
            }

        } catch (NumberFormatException e) {
            CliSout.log(CliSout.LogLevel.Errore, "Favore non valido");
        }
    }
}