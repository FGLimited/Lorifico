package Client.UI.CLI.gameComponents;

import Action.BaseAction;
import Action.SetInUseDomestic;
import Client.CommunicationManager;
import Client.Datawarehouse;
import Client.UI.CLI.cliUtils.CliSout;
import Game.Usable.ResourceType;
import Game.UserObjects.DomesticColor;
import Server.Game.UserObjects.Domestic;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by andrea on 16/06/17.
 */
public class DomesticsCliController implements Client.UI.DomesticsController {
    private Map<DomesticColor, Integer> domesticValues;
    private List<DomesticColor> playedDomestics = new ArrayList<>();

    @Override
    public void updateDomesticsValues(Map<DomesticColor, Integer> domesticValues) {
        this.domesticValues = domesticValues;
        playedDomestics.clear();
        CliSout.log(CliSout.LogLevel.Informazione, "Il valore dei tuoi nuovi Familiari e'\n" + getDomesticValues());
    }

    private String getDomesticValues() {
        return domesticValues.entrySet().stream().map(domesticColorIntegerEntry -> domesticColorIntegerEntry.getKey().itaTranslate()
                + " vale " + domesticColorIntegerEntry.getValue().toString()).collect(Collectors.joining("\n"));
    }

    public void printPlayableDomestics() {
        String playables =
                domesticValues.entrySet().stream().map(domesticColorIntegerEntry ->
                        (!playedDomestics.contains(domesticColorIntegerEntry.getKey()) ? domesticColorIntegerEntry.getKey().toString()
                                + " vale " + domesticColorIntegerEntry.getValue().toString() : null)).
                        collect(Collectors.joining("\n"));
        CliSout.log(CliSout.LogLevel.Informazione, "I Familiari che puoi giocare sono:\n" + playables);
    }

    public void setInUseDomestic(String domesticColorString, int slaves) {
        try {
            DomesticColor domesticColor = DomesticColor.valueOf(domesticColorString);//Gets enum value

            //Check if user has enough slaves
            if (slaves > Datawarehouse.getInstance().getPlayerState(Datawarehouse.getInstance().getMyUsername()).getResources().get(ResourceType.Slave)) {
                CliSout.log(CliSout.LogLevel.Errore, "Non hai " + slaves + " schiavi");
                return;
            }

            //Create our domestic
            Domestic domestic = new Domestic(Datawarehouse.getInstance().getFamilyColor(Datawarehouse.getInstance().getMyUsername()), domesticColor, domesticValues.get(domesticColor));
            playedDomestics.add(domesticColor);

            //Send request to server
            BaseAction action = new SetInUseDomestic(domestic, slaves);
            CommunicationManager.getInstance().sendMessage(action);


            CliSout.log(CliSout.LogLevel.Informazione, "Stai giocando con il familiare " + domesticColor.toString());
        } catch (IllegalArgumentException e) {
            CliSout.log(CliSout.LogLevel.Errore, domesticColorString + " non esiste.");
        }
    }
}
