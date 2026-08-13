package de.voltwerk.abrechnung;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Aggregiert abgerechnete Sitzungen zum Monatsreport für den Betreiber.
 *
 * <p>Der Report läuft jede Nacht zum Monatswechsel und geht per Mail an die
 * Betriebsleitung. Beschwerden dazu landen im Ticket-System als "Report stimmt nicht".</p>
 */
public class MonatsReport {

    private final AbrechnungsService abrechnung;

    public MonatsReport(AbrechnungsService abrechnung) {
        this.abrechnung = abrechnung;
    }

    /**
     * Summiert die Beträge je Ladepunkt.
     *
     * @param sitzungen alle Sitzungen des Monats
     * @return eine Zeile je Ladepunkt, in Reihenfolge des ersten Auftretens
     */
    public List<ReportZeile> proLadepunkt(List<Ladesitzung> sitzungen) {
        List<ReportZeile> zeilen = new ArrayList<>();

        for (Ladesitzung sitzung : sitzungen) {
            ReportZeile treffer = null;
            for (ReportZeile zeile : zeilen) {
                if (zeile.getLadepunktId() == sitzung.getLadepunktId()) {
                    treffer = zeile;
                    break;
                }
            }
            if (treffer == null) {
                treffer = new ReportZeile(sitzung.getLadepunktId());
                zeilen.add(treffer);
            }
            treffer.addiere(abrechnung.berechne(sitzung));
        }

        return zeilen;
    }

    /**
     * Kontrollrechnung für die Buchhaltung: stimmt die Summe der Zeilen mit der
     * erwarteten Gesamtsumme überein?
     *
     * @param zeilen        die Reportzeilen
     * @param erwarteteSumme die aus dem Vorsystem gemeldete Gesamtsumme
     * @return {@code true}, wenn beide Beträge übereinstimmen
     */
    public boolean summeStimmt(List<ReportZeile> zeilen, BigDecimal erwarteteSumme) {
        BigDecimal gesamt = BigDecimal.ZERO;
        for (ReportZeile zeile : zeilen) {
            gesamt = gesamt.add(zeile.getSumme());
        }
        return gesamt.equals(erwarteteSumme);
    }
}
