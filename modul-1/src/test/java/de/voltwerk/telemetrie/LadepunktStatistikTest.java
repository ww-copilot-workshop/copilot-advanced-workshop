package de.voltwerk.telemetrie;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Abnahmetests für Übung A1.
 *
 * <p>Diese Tests sind mit {@code @Disabled} abgeschaltet, damit der Build im
 * Auslieferungszustand grün ist. <b>Entfernt die Annotation</b>, sobald ihr an A1
 * arbeitet — sie sind euer Abnahmekriterium.</p>
 */
@Disabled("Übung A1: Annotation entfernen, sobald LadepunktStatistik implementiert wird")
class LadepunktStatistikTest {

    private final LadepunktStatistik statistik = new LadepunktStatistik();

    private static Messpunkt mp(int id, String zeit, double kw, double grad) {
        return new Messpunkt(id, Instant.parse(zeit), kw, grad);
    }

    @Test
    void spitzenleistungJeLadepunkt() {
        List<Messpunkt> punkte = List.of(
                mp(7, "2026-07-25T09:00:00Z", 40.0, 30.0),
                mp(7, "2026-07-25T09:05:00Z", 51.5, 32.0),
                mp(9, "2026-07-25T09:00:00Z", 11.0, 25.0));

        Map<Integer, Double> spitzen = statistik.spitzenleistungJeLadepunkt(punkte);

        assertEquals(2, spitzen.size());
        assertEquals(51.5, spitzen.get(7), 0.0001);
        assertEquals(11.0, spitzen.get(9), 0.0001);
    }

    @Test
    void durchschnittOhneMesspunkteIstNull() {
        assertEquals(0.0, statistik.durchschnittlicheLeistung(List.of(), 7), 0.0001);
    }

    @Test
    void durchschnittProLadepunkt() {
        List<Messpunkt> punkte = List.of(
                mp(7, "2026-07-25T09:00:00Z", 40.0, 30.0),
                mp(7, "2026-07-25T09:05:00Z", 60.0, 32.0),
                mp(9, "2026-07-25T09:00:00Z", 11.0, 25.0));

        assertEquals(50.0, statistik.durchschnittlicheLeistung(punkte, 7), 0.0001);
    }

    @Test
    void dreiMessungenInFolgeUeberDerSchwelleSindVerdacht() {
        List<Messpunkt> punkte = List.of(
                mp(7, "2026-07-25T09:00:00Z", 50.0, 71.0),
                mp(9, "2026-07-25T09:00:00Z", 50.0, 90.0),
                mp(7, "2026-07-25T09:05:00Z", 50.0, 72.0),
                mp(9, "2026-07-25T09:05:00Z", 50.0, 40.0),
                mp(7, "2026-07-25T09:10:00Z", 50.0, 73.0),
                mp(9, "2026-07-25T09:10:00Z", 50.0, 91.0));

        // Ladepunkt 7: drei Messungen in Folge über 70 -> Verdacht.
        // Ladepunkt 9: 90, 40, 91 -> unterbrochen, kein Verdacht.
        assertEquals(List.of(7), statistik.ueberhitzungsverdacht(punkte, 70.0));
    }

    @Test
    void zweiMessungenInFolgeReichenNicht() {
        List<Messpunkt> punkte = List.of(
                mp(7, "2026-07-25T09:00:00Z", 50.0, 71.0),
                mp(7, "2026-07-25T09:05:00Z", 50.0, 72.0),
                mp(7, "2026-07-25T09:10:00Z", 50.0, 60.0));

        assertEquals(List.of(), statistik.ueberhitzungsverdacht(punkte, 70.0));
    }

    @Test
    void serieZaehltJeLadepunkt_nichtUeberDieGanzeListe() {
        // Drei heisse Messungen hintereinander in der LISTE, aber nur zwei davon
        // gehoeren zu Ladepunkt 7. Wer global zaehlt statt je Ladepunkt, meldet hier
        // faelschlich einen Verdacht.
        List<Messpunkt> punkte = List.of(
                mp(7, "2026-07-25T09:00:00Z", 50.0, 80.0),
                mp(9, "2026-07-25T09:00:00Z", 50.0, 80.0),
                mp(7, "2026-07-25T09:05:00Z", 50.0, 80.0));

        assertEquals(List.of(), statistik.ueberhitzungsverdacht(punkte, 70.0));
    }

    @Test
    void schwelleIstExklusiv() {
        // Genau auf der Schwelle ist noch kein Verdacht: das Javadoc sagt "exklusiv".
        List<Messpunkt> punkte = List.of(
                mp(7, "2026-07-25T09:00:00Z", 50.0, 70.0),
                mp(7, "2026-07-25T09:05:00Z", 50.0, 70.0),
                mp(7, "2026-07-25T09:10:00Z", 50.0, 70.0));

        assertEquals(List.of(), statistik.ueberhitzungsverdacht(punkte, 70.0));
    }

    @Test
    void ergebnisIstAufsteigendSortiertUndDoppelfrei() {
        // Ladepunkt 900 taucht in der Liste zuerst auf und hat zwei getrennte Serien.
        // Erwartet: [12, 900] -- aufsteigend, jede ID genau einmal.
        List<Messpunkt> punkte = List.of(
                mp(900, "2026-07-25T09:00:00Z", 50.0, 90.0),
                mp(900, "2026-07-25T09:05:00Z", 50.0, 90.0),
                mp(900, "2026-07-25T09:10:00Z", 50.0, 90.0),
                mp(900, "2026-07-25T09:15:00Z", 50.0, 20.0),
                mp(900, "2026-07-25T09:20:00Z", 50.0, 90.0),
                mp(900, "2026-07-25T09:25:00Z", 50.0, 90.0),
                mp(900, "2026-07-25T09:30:00Z", 50.0, 90.0),
                mp(12, "2026-07-25T09:00:00Z", 50.0, 90.0),
                mp(12, "2026-07-25T09:05:00Z", 50.0, 90.0),
                mp(12, "2026-07-25T09:10:00Z", 50.0, 90.0));

        assertEquals(List.of(12, 900), statistik.ueberhitzungsverdacht(punkte, 70.0));
    }

    @Test
    void listenreihenfolgeZaehlt_nichtDerZeitstempel() {
        // Die Messpunkte von Ladepunkt 7 stehen in der Liste in nicht-chronologischer
        // Reihenfolge. Der Kontrakt sagt: es gilt die Reihenfolge in der LISTE.
        // Wer vorher nach Zeitstempel sortiert, bekommt ein anderes Ergebnis.
        List<Messpunkt> punkte = List.of(
                mp(7, "2026-07-25T09:20:00Z", 50.0, 90.0),
                mp(7, "2026-07-25T09:10:00Z", 50.0, 90.0),
                mp(7, "2026-07-25T09:00:00Z", 50.0, 20.0),
                mp(7, "2026-07-25T09:30:00Z", 50.0, 90.0));

        // In Listenreihenfolge: 90, 90, 20, 90 -> nie drei in Folge.
        // Nach Zeitstempel sortiert:  20, 90, 90, 90 -> drei in Folge.
        assertEquals(List.of(), statistik.ueberhitzungsverdacht(punkte, 70.0));
    }
}
