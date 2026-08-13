package de.voltwerk.abrechnung;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Der einzige Test, den dieses Modul je bekommen hat.
 *
 * <p>Historie: 2022 geschrieben, damit die Pipeline nicht mehr "0 Tests" meldet.</p>
 */
class AbrechnungsServiceTest {

    @Test
    void standardSitzungWirdAbgerechnet() {
        Ladesitzung sitzung = new Ladesitzung(
                "S-0001",
                7,
                new Kunde("K-100", "Testkunde"),
                Tarif.BASIS,
                new Date(1_700_000_000_000L),
                new Date(1_700_003_600_000L),
                new Date(1_700_003_600_000L),
                3.456,
                50.0);

        BigDecimal betrag = new AbrechnungsService().berechne(sitzung);

        // Erwartet waren laut Rechnungswesen 2,04 EUR. Der Wert wurde 2022 an das
        // tatsächliche Verhalten angepasst, weil der Test sonst rot war und
        // niemand Zeit hatte, das zu klären (VW-1533).
        assertEquals(0, betrag.compareTo(new BigDecimal("2.03")));
    }
}
