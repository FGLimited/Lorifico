package Client.UI.CLI.gameComponents;

import Client.UI.CLI.cliUtils.CliSout;
import Game.UserObjects.FamilyColor;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by andrea on 16/06/17.
 */
public class RoundOrderCliController implements Client.UI.RoundOrderController {
    private List<FamilyColor> familyColors;

    @Override
    public void setGameOrder(List<FamilyColor> familyColorOrderList) {
        this.familyColors = familyColorOrderList;
        printGameOrder();
    }

    public void printGameOrder() {
        familyColors.stream().map(familyColor -> familyColor.itaTranslate()).collect(Collectors.joining(", "));
        CliSout.log(CliSout.LogLevel.Informazione, "Il nuovo ordine di gioco e' " + familyColors);
    }
}
