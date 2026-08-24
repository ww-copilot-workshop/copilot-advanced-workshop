package de.voltwerk.abrechnung;

import java.math.BigDecimal;
import java.util.Date;
import java.util.TimeZone;

/**
 * Berechnet den Rechnungsbetrag einer Ladesitzung.
 *
 * <p><b>Achtung:</b> Die fachliche Wahrheit steht in {@code modul-5/SPEC.md}, nicht hier.
 * Diese Klasse ist über sechs Jahre gewachsen; mehrere Autoren, wenig Tests.</p>
 *
 * <p>Änderungshistorie (aus dem alten Confluence übernommen):</p>
 * <ul>
 *   <li>2019-03 Nachttarif ergänzt (Ticket VW-812)</li>
 *   <li>2020-11 Blockiergebühr ergänzt, weil Ladepunkte zugeparkt wurden</li>
 *   <li>2021-06 Flottentarif ergänzt</li>
 *   <li>2022-02 Rückgabetyp auf BigDecimal umgestellt, Kern blieb double</li>
 *   <li>2023-09 DC-Zuschlag ergänzt (Ticket VW-2290)</li>
 * </ul>
 */
public class AbrechnungsService {

    private static final double PREIS_BASIS = 0.59;
    private static final double PREIS_FLOTTE = 0.42;
    private static final double PREIS_NACHT = 0.39;

    private static final double DC_ZUSCHLAG_PRO_KWH = 0.08;
    private static final double DC_SCHWELLE_KW = 150.0;

    private static final int BLOCKIER_FREIMINUTEN = 240;
    private static final double BLOCKIER_PREIS_PRO_MINUTE = 0.10;

    /**
     * Der OCPP-Adapter liefert Timestamps in UTC. Damit die Stundenprüfung
     * "immer gleich" rechnet, wird hier fest mit UTC gearbeitet.
     */
    private static final TimeZone RECHEN_ZONE = TimeZone.getTimeZone("UTC");

    /**
     * Berechnet den Rechnungsbetrag einer Sitzung in EUR.
     *
     * @param sitzung die abgeschlossene Ladesitzung, nicht {@code null}
     * @return Betrag in EUR
     */
    public BigDecimal berechne(Ladesitzung sitzung) {
        if (sitzung == null) {
            throw new IllegalArgumentException("sitzung darf nicht null sein");
        }

        double kwh = sitzung.getEnergieKwh();

        // Fehlversuche nicht abrechnen (Ticket VW-1104)
        if (kwh < 0.1) {
            return BigDecimal.valueOf(0.0);
        }

        double betrag = 0.0;

        // ---------------------------------------------------------------------------
        // MODERNISIEREN (Lab D4): Diese Kaskade wächst mit jedem neuen Tarif weiter.
        // Konventionen des Teams stehen in AGENTS.md im Repo-Wurzelverzeichnis.
        // ---------------------------------------------------------------------------
        if (sitzung.getTarif() == Tarif.BASIS) {
            betrag = betrag + kwh * PREIS_BASIS;
        } else if (sitzung.getTarif() == Tarif.FLOTTE) {
            betrag = betrag + kwh * PREIS_FLOTTE;
        } else if (sitzung.getTarif() == Tarif.NACHT) {
            if (istNachts(sitzung.getLadeStart())) {
                betrag = betrag + kwh * PREIS_NACHT;
            } else {
                betrag = betrag + kwh * PREIS_BASIS;
            }
        } else {
            betrag = betrag + kwh * PREIS_BASIS;
        }

        // Schnelllade-Zuschlag (Ticket VW-2290)
        if (sitzung.getMaxLeistungKw() >= DC_SCHWELLE_KW) {
            betrag = betrag + kwh * DC_ZUSCHLAG_PRO_KWH;
        }

        betrag = betrag + blockiergebuehr(sitzung);

        return BigDecimal.valueOf(runde(betrag));
    }

    /**
     * Blockiergebühr für die Zeit, die das Fahrzeug am Ladepunkt stand.
     */
    private double blockiergebuehr(Ladesitzung sitzung) {
        Date start = sitzung.getLadeStart();
        Date abgesteckt = sitzung.getAbgestecktUm();
        if (start == null || abgesteckt == null) {
            return 0.0;
        }

        long millis = abgesteckt.getTime() - start.getTime();
        if (millis <= 0) {
            return 0.0;
        }

        long minuten = millis / 60000L;
        if (minuten >= BLOCKIER_FREIMINUTEN) {
            return (minuten - BLOCKIER_FREIMINUTEN) * BLOCKIER_PREIS_PRO_MINUTE;
        }
        return 0.0;
    }

    /**
     * Prüft, ob der Zeitpunkt im Nachtfenster liegt.
     */
    private boolean istNachts(Date zeitpunkt) {
        if (zeitpunkt == null) {
            return false;
        }
        int stunde = zeitpunkt.toInstant()
                .atZone(RECHEN_ZONE.toZoneId())
                .getHour();
        return stunde >= 22 || stunde < 6;
    }

    /**
     * Kaufmännisch runden auf zwei Nachkommastellen.
     */
    private double runde(double wert) {
        return ((int) (wert * 100)) / 100.0;
    }
}
