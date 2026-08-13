package de.voltwerk.abrechnung;

import java.math.BigDecimal;

/**
 * Eine Zeile des Monatsreports: ein Ladepunkt, seine Sitzungen, seine Summe.
 */
public class ReportZeile {

    private final Integer ladepunktId;
    private int anzahlSitzungen;
    private BigDecimal summe;

    public ReportZeile(Integer ladepunktId) {
        this.ladepunktId = ladepunktId;
        this.anzahlSitzungen = 0;
        this.summe = BigDecimal.ZERO;
    }

    public Integer getLadepunktId() {
        return ladepunktId;
    }

    public int getAnzahlSitzungen() {
        return anzahlSitzungen;
    }

    public BigDecimal getSumme() {
        return summe;
    }

    void addiere(BigDecimal betrag) {
        this.anzahlSitzungen++;
        this.summe = this.summe.add(betrag);
    }

    @Override
    public String toString() {
        return "Ladepunkt " + ladepunktId + ": " + anzahlSitzungen + " Sitzungen, " + summe + " EUR";
    }
}
