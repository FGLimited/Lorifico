package Client.UI.CLI.gameComponents;

import Action.BaseAction;
import Action.Move;
import Client.CommunicationManager;
import Client.Datawarehouse;
import Client.UI.CLI.cliUtils.CliSout;
import Client.UI.CLI.dialogs.ChooseCardsEffectsDialog;
import Client.UI.TurnObserver;
import Game.UserObjects.Choosable;
import Server.Game.UserObjects.Domestic;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by andrea on 16/06/17.
 */
public class GameTableCliController implements Client.UI.GameTable, TurnObserver {
    private Map<Integer, List<Choosable>> choosablePerPos = new HashMap<>();
    private Map<Integer, Domestic> domesticPerPositionMap = new HashMap<>();
    private boolean marketDeny = false;

    public GameTableCliController() {
        Datawarehouse.getInstance().registerTurnObserver(this);
    }

    @Override
    public void freeAllPositions() {
        domesticPerPositionMap.clear();
    }

    @Override
    public void addDomestic(Domestic occupant, int positionNumber) {
        domesticPerPositionMap.put(positionNumber, occupant);
    }

    @Override
    public void setCostsPerPosition(Map<Integer, List<Choosable>> choosablePerPos) {
        this.choosablePerPos = choosablePerPos;
    }

    @Override
    public void marketDeny() {
        marketDeny = true;
    }

    @Override
    public void onTurnChange(String username) {
        choosablePerPos.clear();
    }

    public void showGameTableStatus() {
        if (domesticPerPositionMap.isEmpty()) {
            System.out.println("Tutte le posizioni sono libere");
        }

        domesticPerPositionMap.forEach((integer, domestic) -> System.out.println("Posizione " + integer + " occupata dal familiare " + domestic.getFamilyColor().itaTranslate()));

        //If we have costs, print them:
        choosablePerPos.forEach((integer, choosables) -> {
            if (integer > 16) {//Be sure we are on gametable
                if (choosables.size() > 0) {//if i can buy this position...
                    //If it's a haverst/production position, i can buy it only if left one is busy
                    if ((integer >= 30 && integer <= 33) || (integer >= 20 && integer <= 23) || (integer >= 50 && integer <= 53)) {
                        if (isLeftOneBusy(integer)) {
                            System.out.println("Posizione: " + integer + " disponibile all'acquisto.");
                        }
                    } else {//if it's not a harvest/product position, simply sout it.
                        if (marketDeny && (integer < 40 && integer > 43)) {//If we have a market deny print only non market pos.
                            System.out.println("Posizione: " + integer + " disponibile all'acquisto.");
                        } else if (!marketDeny) {
                            System.out.println("Posizione: " + integer + " disponibile all'acquisto.");
                        }
                    }
                }
            }
        });
    }

    private boolean isLeftOneBusy(int pos) {
        if (pos == 30)
            return !(choosablePerPos.get(30).isEmpty());

        if (pos == 20)
            return !(choosablePerPos.get(20).isEmpty());

        if (pos == 50)
            return !(choosablePerPos.get(50).isEmpty());

        if (!choosablePerPos.get(pos - 1).isEmpty()) {
            return false;
        }
        return true;
    }

    public void buyPositionOnGameTable(int position) {
        if (position < 40 && position >= 20) {//if it's a harvest / production place
            new ChooseCardsEffectsDialog(position, choosablePerPos.get(position));
            return;
        }

        //If it's not harvest or production simply buy it.
        BaseAction baseAction = new Move(position, Collections.emptyList());
        CommunicationManager.getInstance().sendMessage(baseAction);
        CliSout.log(CliSout.LogLevel.Informazione, "Posizione occupata con successo.");
    }
}
