package Game.UserObjects;

/**
 * Created by fiore on 11/05/2017.
 */
public enum FamilyColor {
    Green("verde"),
    Blue("blu"),
    Yellow("giallo"),
    Red("rosso"),
    Special("speciale");

    private String itaTranslate;

    FamilyColor(String itaTranslate) {
        this.itaTranslate = itaTranslate;
    }

    public String itaTranslate() {
        return itaTranslate;
    }
}
