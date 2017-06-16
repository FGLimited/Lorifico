package Client.UI.CLI.dialogs;

import Action.BaseAction;
import Action.Move;
import Client.CommunicationManager;
import Client.UI.CLI.UserInterfaceImplemCLI;
import Client.UI.CLI.cliUtils.CliSout;
import Client.UI.UserInterfaceFactory;
import Game.Effects.Effect;
import Game.UserObjects.Choosable;
import com.budhash.cliche.Command;

import java.util.*;

/**
 * Created by andrea on 16/06/17.
 */
public class ChooseCardsEffectsDialog {
    private int position;
    private List<Choosable> choosables;
    private List<Choosable> choosenEffects = new ArrayList<>();

    private Effect[] activableEffectsArray;//Used to store data while user chooses the effect.
    private Iterator<Map.Entry<Integer, List<Choosable>>> iterator;


    public ChooseCardsEffectsDialog(int position, List<Choosable> choosables) {
        this.position = position;
        this.choosables = choosables;

        //Creates a new Map containing cardNumber in key, activable effects in a List in value
        Map<Integer, List<Choosable>> cardChoosableMap = new HashMap<>();
        choosables.forEach(choosable -> {
            if (choosable instanceof Effect) {
                Effect effect = (Effect) choosable;
                cardChoosableMap.putIfAbsent(effect.getCardNumber(), new ArrayList<Choosable>());
                cardChoosableMap.get(effect.getCardNumber()).add(effect);
            }
        });

        Iterator<Map.Entry<Integer, List<Choosable>>> iterator = cardChoosableMap.entrySet().iterator();

        if (iterator.hasNext()) {
            ChooseCardsEffectsDialog chooseCardsEffectsDialog = new ChooseCardsEffectsDialog(position, iterator, choosenEffects);
            ((UserInterfaceImplemCLI) (UserInterfaceFactory.getInstance())).setCliPage(chooseCardsEffectsDialog, false);
        } else {
            System.err.println("Non puoi comprare questa posizione.");
        }
    }

    private ChooseCardsEffectsDialog(int position, Iterator<Map.Entry<Integer, List<Choosable>>> iterator, List<Choosable> choosenEffects) {
        this.choosenEffects = choosenEffects;
        this.iterator = iterator;
        this.position = position;

        Map.Entry<Integer, List<Choosable>> entry = iterator.next();

        if (entry.getKey() == 0) {//Defaults effects... just skip them
            if (iterator.hasNext()) {
                entry.getValue().forEach(choosable -> choosenEffects.add(choosable));
                entry = iterator.next();
            } else {//If there's nothing more, send all to server and go back to gameUI
                sendToServerAndGoBack();
            }
        }

        int cardID = entry.getKey();
        activableEffectsArray = new Effect[entry.getValue().size()];
        System.arraycopy(entry.getValue().toArray(new Choosable[entry.getValue().size()]), 0, activableEffectsArray, 0, activableEffectsArray.length);


        System.out.println("Effetti attivabili per la carta " + cardID);

        for (int i = 0; i < activableEffectsArray.length; i++) {
            Effect effect = activableEffectsArray[i];
            System.out.println(i + ")\t" + effect.getDescription());
        }
        System.out.println();
        CliSout.log(CliSout.LogLevel.Informazione, "Scegli quale effetto vuoi attivare per questa carta scrivendo 'attiva #ID'\nAltrimenti scrivi 'skip'");
    }

    private void sendToServerAndGoBack() {
        ((UserInterfaceImplemCLI) (UserInterfaceFactory.getInstance())).setCliPage(UserInterfaceFactory.getInstance().getGameUI(), true);//Go back
        BaseAction action = new Move(position, choosenEffects);
        CommunicationManager.getInstance().sendMessage(action);
    }

    private void askNextCardOrSendToServer() {
        if (iterator.hasNext()) {
            ChooseCardsEffectsDialog chooseCardsEffectsDialog = new ChooseCardsEffectsDialog(position, iterator, choosenEffects);
            ((UserInterfaceImplemCLI) (UserInterfaceFactory.getInstance())).setCliPage(chooseCardsEffectsDialog, false);
        } else {
            sendToServerAndGoBack();
        }
    }

    @Command(description = "Sceglie la carta")
    public void attiva(String id) {
        try {
            int idInt = Integer.valueOf(id);
            Effect effect = activableEffectsArray[idInt];

            if (effect == null) {
                CliSout.log(CliSout.LogLevel.Errore, "Numero non valido");
                return;
            }

            choosenEffects.add(effect);
        } catch (IndexOutOfBoundsException e) {
            CliSout.log(CliSout.LogLevel.Errore, "Numero non valido");
        } catch (NumberFormatException e) {
            CliSout.log(CliSout.LogLevel.Errore, "Numero non valido");
        }
        askNextCardOrSendToServer();
    }

    @Command(description = "Salta alla carta successiva")
    public void skip() {
        askNextCardOrSendToServer();
    }

}
