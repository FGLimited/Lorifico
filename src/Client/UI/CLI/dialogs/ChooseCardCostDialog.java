package Client.UI.CLI.dialogs;

import Action.BaseAction;
import Action.Move;
import Client.CommunicationManager;
import Client.UI.CLI.UserInterfaceImplemCLI;
import Client.UI.CLI.cliUtils.CliSout;
import Client.UI.CLI.cliUtils.TextUtils;
import Client.UI.UserInterfaceFactory;
import Game.UserObjects.Choosable;
import Server.Game.Usable.Cost;
import com.budhash.cliche.Command;
import com.budhash.cliche.Param;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by andrea on 16/06/17.
 */
public class ChooseCardCostDialog {
    private int position;
    private List<Choosable> choosables;
    private Cost[] costs;

    public ChooseCardCostDialog(int position, List<Choosable> choosableList) {
        this.position = position;
        this.choosables = choosableList;
        CliSout.log(CliSout.LogLevel.Informazione, "Con quale dei seguenti costi vuoi comprare la carta?");
        CliSout.log(CliSout.LogLevel.Avvertimento, "Digita 'paga' seguito dall'id del costo (esempio: 'paga 2'");


        //Display all cost:
        costs = new Cost[choosableList.size()];
        System.arraycopy(choosableList.toArray(new Choosable[choosableList.size()]), 0, costs, 0, costs.length);

        for (int i = 0; i < costs.length; i++) {
            System.out.print(i + ") ");
            TextUtils.printCost(costs[i]);
        }
    }

    @Command(description = "Paga il prezzo scelto")
    public void paga(
            @Param(name = "costo", description = "Numero del costo scelto")
                    String idCosto) {
        try {
            int idCostoInt = Integer.valueOf(idCosto);
            Cost cost = costs[idCostoInt];

            List<Choosable> costList = new ArrayList<>();
            costList.add(cost);
            BaseAction action = new Move(position, costList);
            CommunicationManager.getInstance().sendMessage(action);
            CliSout.log(CliSout.LogLevel.Informazione, "La posizione " + position + " ora e' tua!");
            ((UserInterfaceImplemCLI) (UserInterfaceFactory.getInstance())).setCliPage(UserInterfaceFactory.getInstance().getGameUI(), true);
        } catch (IndexOutOfBoundsException e) {
            CliSout.log(CliSout.LogLevel.Errore, "Il prezzo scelto non esiste");
        } catch (NumberFormatException e) {
            CliSout.log(CliSout.LogLevel.Errore, "Inserisci un numero");
        }
    }
}