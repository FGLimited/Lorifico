package Client.UI.CLI.gameComponents;

import Client.UI.CLI.cliUtils.CliSout;
import Game.UserObjects.FamilyColor;

/**
 * Created by andrea on 16/06/17.
 */
public class FaithRoadCliController implements Client.UI.FaithRoadController {
    int first, second, third;

    @Override
    public void showFaithCards(int first, int second, int third) {
        this.first = first;
        this.second = second;
        this.third = third;
        CliSout.log(CliSout.LogLevel.Informazione, "Gli ID delle carte scomunica sono " + first + "," + second + "," + third);
    }

    public void printFaithCards() {
        CliSout.log(CliSout.LogLevel.Informazione, "Gli ID delle carte scomunica sono " + first + "," + second + "," + third);
    }

    @Override
    public void showFaithCube(FamilyColor familyColor, int cardOrdinal) {
        CliSout.log(CliSout.LogLevel.Informazione, "Cubo scomunica per il giocatore " + familyColor.itaTranslate());
    }
}
