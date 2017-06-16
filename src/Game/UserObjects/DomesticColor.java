package Game.UserObjects;

/**
 * Created by fiore on 08/05/2017.
 */
public enum DomesticColor {
    Orange("arancione"),
    White("bianco"),
    Black("nero"),
    Neutral("neutro");

    private String itaColore;

    DomesticColor(String itaColore) {
        this.itaColore = itaColore;
    }

    public String itaTranslate() {
        return itaColore;
    }
}
