package de.voltwerk.telemetrie;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class TelemetrieParserTest {

    private final TelemetrieParser parser = new TelemetrieParser();

    @Test
    void liestGueltigeZeile() {
        Messpunkt punkt = parser.parseZeile("7;2026-07-25T09:15:00Z;49.8;31.5");

        assertEquals(7, punkt.ladepunktId());
        assertEquals(Instant.parse("2026-07-25T09:15:00Z"), punkt.zeitpunkt());
        assertEquals(49.8, punkt.leistungKw(), 0.0001);
        assertEquals(31.5, punkt.temperaturC(), 0.0001);
    }

    @Test
    void ignoriertKommentareUndLeerzeilen() {
        List<Messpunkt> punkte = parser.parse(List.of(
                "# ladepunktId;zeitpunkt;leistungKw;temperaturC",
                "",
                "7;2026-07-25T09:15:00Z;49.8;31.5"));

        assertEquals(1, punkte.size());
    }

    @Test
    void defekteZeileLiefertNull() {
        assertNull(parser.parseZeile("7;kaputt;49.8;31.5"));
    }
}
