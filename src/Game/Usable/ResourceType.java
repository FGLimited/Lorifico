package Game.Usable;

/**
 * Created by fiore on 08/05/2017.
 */
public enum ResourceType {
    Slave("schiavo", "schiavi"),
    Wood("legno", "legna"),
    Rock("pietra", "pietre"),
    Gold("moneta", "monete"),
    Favor("favore", "favori"),
    VictoryPoint("punto vittoria", "punti vittoria"),
    MilitaryPoint("punto militare", "punti militare"),
    FaithPoint("punto fede", "punti fede");

    private String singolare;
    private String plurale;

    ResourceType(String singolare, String plurale) {
        this.singolare = singolare;
        this.plurale = plurale;
    }

    /**
     * Generates an itaTranslate string
     *
     * @param value
     * @return
     */
    public String toCostString(Integer value) {
        String word;
        if (value > 1) word = plurale;
        else word = singolare;
        return value + " " + word;
    }
}
