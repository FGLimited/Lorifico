package Client.UI.CLI.gameComponents;

import Client.UI.CLI.cliUtils.CliSout;

/**
 * Created by andrea on 16/06/17.
 */
public class DicesCliController implements Client.UI.DiceController {
    int first, second, third;

    @Override
    public void setNumbers(int first, int second, int third) {
        this.first = first;
        this.second = second;
        this.third = third;
        CliSout.log(CliSout.LogLevel.Informazione, "I dati sono stati lanciati, i valori sono " + first + "," + second + "," + third);
    }

    public void printDices() {
        CliSout.log(CliSout.LogLevel.Informazione, "I valori dei dadi sono " + first + "," + second + "," + third);
    }
}
