package Client.UI.CLI.gameComponents;

import Action.BaseAction;
import Action.Move;
import Client.CommunicationManager;
import Client.Datawarehouse;
import Client.UI.CLI.UserInterfaceImplemCLI;
import Client.UI.CLI.cliUtils.CliSout;
import Client.UI.CLI.cliUtils.TextUtils;
import Client.UI.CLI.dialogs.ChooseCardCostDialog;
import Client.UI.PlayerStateObserver;
import Client.UI.TurnObserver;
import Client.UI.UserInterfaceFactory;
import Game.Cards.CardType;
import Game.UserObjects.Choosable;
import Game.UserObjects.PlayerState;
import Server.Game.Usable.Cost;
import Server.Game.UserObjects.Domestic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by andrea on 16/06/17.
 */
public class TowersCliController implements Client.UI.TowersController, PlayerStateObserver, TurnObserver {
    private Map<Integer, Integer> cardPerGamePositionMap = new HashMap<>();//gamePosition is key, value is cardNumber
    private Map<Integer, List<Choosable>> costsPerPositionMap = new HashMap<>();//gamePosition is key, List of costs is value
    private Map<Integer, Domestic> domesticPerPositionMap = new HashMap<>();//gamePosition is key, value is placed domestic.

    public TowersCliController() {
        Datawarehouse.getInstance().registerPlayerStateObserver(this);
        Datawarehouse.getInstance().registerTurnObserver(this);
    }

    @Override
    public void showCardOnTowers(int gamePosition, int cardNumber) {
        cardPerGamePositionMap.putIfAbsent(gamePosition, cardNumber);
    }

    @Override
    public void removeCardFromTower(int cardNumber) {
        cardPerGamePositionMap.values().remove(cardNumber);
    }

    @Override
    public void removeAllCardsFromTowers() {
        cardPerGamePositionMap.clear();
    }

    @Override
    public void setCostsPerPosition(Map<Integer, List<Choosable>> choosablePerPos) {
        this.costsPerPositionMap = choosablePerPos;
    }

    @Override
    public void addDomestic(Domestic domestic, int gamePosition) {
        domesticPerPositionMap.put(gamePosition, domestic);
    }

    @Override
    public void removeDomestic(Domestic domestic) {
        domesticPerPositionMap.values().remove(domestic);
    }

    @Override
    public void removeAllDomestics() {
        domesticPerPositionMap.clear();
    }

    @Override
    public void onPlayerStateUpdate(PlayerState playerState, String username) {
        //If a card is in a user's punchboard we have to remove it from towers
        playerState.getCards(CardType.Challenge).forEach(card -> cardPerGamePositionMap.values().remove(card.getNumber()));
        playerState.getCards(CardType.Personality).forEach(card -> cardPerGamePositionMap.values().remove(card.getNumber()));
        playerState.getCards(CardType.Territory).forEach(card -> cardPerGamePositionMap.values().remove(card.getNumber()));
        playerState.getCards(CardType.Building).forEach(card -> cardPerGamePositionMap.values().remove(card.getNumber()));
    }

    @Override
    public void onTurnChange(String username) {
        costsPerPositionMap.clear();
    }

    public void showTowersStatus() {
        int stringPadValue = 57;
        System.out.println(
                TextUtils.pad("Verde", stringPadValue) + "\t" +
                        TextUtils.pad("Gialla", stringPadValue) + "\t" +
                        TextUtils.pad("Blu", stringPadValue) + "\t" +
                        TextUtils.pad("Viola", stringPadValue) + "\t");

        for (int level = 4; level >= 1; level--) {
            for (int tower = 0; tower < 4; tower++) {
                int key = level + 4 * (tower);

                String out;//String to print for this position.
                out = CliSout.LogLevel.Informazione.colorCode + "Posiz. " + CliSout.LogLevel.colorReset + key + ", ";
                if (cardPerGamePositionMap.containsKey(key)) {
                    out = out + "Carta " + cardPerGamePositionMap.get(key) + " ";
                }
                if (domesticPerPositionMap.containsKey(key) && domesticPerPositionMap.get(key).getType() != null) {
                    out = out + "Familiare " + domesticPerPositionMap.get(key).getFamilyColor().itaTranslate();
                }
                System.out.print(TextUtils.pad(out, stringPadValue));
            }
            System.out.println();
        }

        //If we have costs (so we can buy something) let's print them.
        if (!costsPerPositionMap.isEmpty()) {
            costsPerPositionMap.forEach((integer, choosables) -> {
                if (integer <= 16) {//Check if it's a tower cost
                    if (!choosables.isEmpty()) {
                        System.out.println("Costo/i posizione " + integer);
                    }
                    choosables.forEach(choosable -> {
                        if (choosable instanceof Cost) {
                            Cost cost = (Cost) choosable;
                            TextUtils.printCost(cost);
                        }
                    });
                }
            });
        }
    }

    public void buyPositionOnTower(int position) {
        if (!costsPerPositionMap.containsKey(position)) {
            CliSout.log(CliSout.LogLevel.Errore, "Non puoi comprare la posizione " + position + " delle torri");
        }
        List<Choosable> choosableList = costsPerPositionMap.get(position);

        //If i can take it for free, buy it.
        List<Choosable> costList = new ArrayList<>();
        choosableList.forEach(choosable -> {
            if (choosable instanceof Cost && ((Cost) choosable).getResources().isEmpty()) {
                costList.add(choosable);
            }
        });

        //If i can only take it for one price, buy it.
        if (choosableList.size() == 1) {
            costList.add(choosableList.iterator().next());
        }

        //If we found a free cost or we have only a cost, send to server.
        if (costList.size() > 0) {
            BaseAction baseAction = new Move(position, costList);
            CommunicationManager.getInstance().sendMessage(baseAction);
            CliSout.log(CliSout.LogLevel.Informazione, "Hai comprato la posizione");
            return;
        }

        //Otherwise ask user to choose a cost and buy it.
        ChooseCardCostDialog chooseCardCostDialog = new ChooseCardCostDialog(position, choosableList);
        ((UserInterfaceImplemCLI) (UserInterfaceFactory.getInstance())).setCliPage(chooseCardCostDialog, false);
    }
}
