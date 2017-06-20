package Client.UI.CLI;

import Action.DisplayPopup;
import Action.SetInUseDomestic;
import Client.CommunicationManager;
import Client.Datawarehouse;
import Client.UI.CLI.cliUtils.CliSout;
import Client.UI.CLI.dialogs.ChooseFavorsDialog;
import Client.UI.CLI.gameComponents.*;
import Client.UI.*;
import Game.Cards.CardType;
import Game.Effects.Effect;
import Server.Game.UserObjects.Domestic;
import com.budhash.cliche.Command;
import com.budhash.cliche.Param;

import java.util.List;

/**
 * Created by andrea on 16/06/17.
 */
public class GameUIPage implements Client.UI.GameUI {
    private TowersCliController towersController;
    private DicesCliController diceController;
    private FaithRoadCliController faithRoadController;
    private RoundOrderCliController roundOrderController;
    private DomesticsCliController domesticsController;

    @Override
    public void showPage() {
        ((UserInterfaceImplemCLI) (UserInterfaceFactory.getInstance())).setCliPage(this, false);
        UserInterfaceFactory.getInstance().displayPopup(DisplayPopup.Level.Normal, "Stato partita", "La partita e' iniziata");
    }

    //--------- CLI COMMANDS

    @Command(description = "Mostra lo stato delle torri")
    public void showTowers() {
        towersController.showTowersStatus();
    }

    @Command(description = "Mostra lo stato del tavolo da gioco")
    public void showGameTable() {
        ((GameTableCliController) (UserInterfaceFactory.getInstance().getGameTable())).showGameTableStatus();
    }

    @Command(description = "Stampa il valore dei dadi")
    public void printDices() {
        diceController.printDices();
    }

    @Command(description = "Stampa i valori delle carte scomunica")
    public void printFaithCards() {
        faithRoadController.printFaithCards();
    }

    @Command(description = "Stampa l'ordine di gioco")
    public void printGameOrder() {
        roundOrderController.printGameOrder();
    }

    @Command(description = "Mostra i familiari giocabili")
    public void printPlayableDomestics() {
        domesticsController.printPlayableDomestics();
    }

    @Command(description = "Seleziona il Familiare da giocare")
    public void setInUseDomestic(@Param(name = "Colore", description = "Colore del familiare in inglese")
                                         String domesticColor,
                                 @Param(name = "Schiavi", description = "Schiavi per aumentare il valore del dado")
                                         String slaves) {
        try {
            domesticsController.setInUseDomestic(domesticColor, Integer.valueOf(slaves));

        } catch (NumberFormatException e) {
            CliSout.log(CliSout.LogLevel.Errore, "Non hai inserito un numero");
        }
    }

    @Command(description = "Compra una posizione")
    public void buyPosition(@Param(name = "Numero posizione", description = "Numero della posizione da prendere") String positionID) {
        try {
            int position = Integer.valueOf(positionID);
            if (position > 16) {
                ((GameTableCliController) (UserInterfaceFactory.getInstance().getGameTable())).buyPositionOnGameTable(position);
            } else {
                towersController.buyPositionOnTower(position);
            }
        } catch (NumberFormatException e) {
            CliSout.log(CliSout.LogLevel.Errore, "Non hai inserito un numero");
        }
    }

    @Command(description = "Mostra la mia situazione")
    public void showMyPlayerState() {
        Game.UserObjects.PlayerState playerState = Datawarehouse.getInstance().getPlayerState(Datawarehouse.getInstance().getMyUsername());
        playerState.getResources().forEach((resourceType, integer) -> System.out.println(resourceType.toCostString(integer)));
        playerState.getCards(CardType.Territory).forEach(card -> System.out.println("Carta Territorio " + card.getNumber()));
        playerState.getCards(CardType.Personality).forEach(card -> System.out.println("Carta Personalita' " + card.getNumber()));
        playerState.getCards(CardType.Challenge).forEach(card -> System.out.println("Carta Sfida " + card.getNumber()));
        playerState.getCards(CardType.Building).forEach(card -> System.out.println("Carta Costruzioni " + card.getNumber()));
    }


    //--------- game stuff

    @Override
    public TowersController getTowersController() {
        if (towersController == null) towersController = new TowersCliController();
        return towersController;
    }

    @Override
    public DiceController getDiceController() {
        if (diceController == null) diceController = new DicesCliController();
        return diceController;
    }

    @Override
    public FaithRoadController getFaithController() {
        if (faithRoadController == null) faithRoadController = new FaithRoadCliController();
        return faithRoadController;
    }

    @Override
    public RoundOrderController getRoundOrderController() {
        if (roundOrderController == null) roundOrderController = new RoundOrderCliController();
        return roundOrderController;
    }

    @Override
    public DomesticsController getDomesticsChoiceBoxController() {
        if (domesticsController == null) domesticsController = new DomesticsCliController();
        return domesticsController;
    }

    @Override
    public void addSlaveToSpecialDomestic(Domestic domestic, SetInUseDomestic setInUseDomesticAction) {
        setInUseDomesticAction.setSlaves(0);
        CommunicationManager.getInstance().sendMessage(setInUseDomesticAction);
    }

    @Override
    public void askCouncilFavours(List<Effect> councilFavors, int differentFavors) {
        ChooseFavorsDialog chooseFavorsDialog = new ChooseFavorsDialog(councilFavors, differentFavors);
        ((UserInterfaceImplemCLI) (UserInterfaceFactory.getInstance())).setCliPage(chooseFavorsDialog, false);
    }

    @Override
    public void askFaithRoad() {

    }
}
