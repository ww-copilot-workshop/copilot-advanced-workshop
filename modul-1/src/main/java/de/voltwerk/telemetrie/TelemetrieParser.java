package de.voltwerk.telemetrie;

import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Liest den CSV-Telemetriestrom der Ladepunkte ein.
 *
 * <p>Format je Zeile: {@code ladepunktId;zeitpunktIso8601;leistungKw;temperaturC}</p>
 *
 * <p>Beispiel: {@code 7;2026-07-25T09:15:00Z;49.8;31.5}</p>
 */
public class TelemetrieParser {

    // TODO(VW-3011): Trennzeichen konfigurierbar machen, der neue Adapter liefert Komma.
    private static final String TRENNER = ";";

    /**
     * Parst mehrere Zeilen. Leere Zeilen und Kommentarzeilen (beginnend mit {@code #})
     * werden übersprungen.
     *
     * @param zeilen Rohzeilen aus der Telemetriedatei
     * @return die erkannten Messpunkte in Eingabereihenfolge
     */
    public List<Messpunkt> parse(List<String> zeilen) {
        List<Messpunkt> ergebnis = new ArrayList<>();
        for (String zeile : zeilen) {
            if (zeile == null || zeile.isBlank() || zeile.startsWith("#")) {
                continue;
            }
            Messpunkt punkt = parseZeile(zeile);
            if (punkt != null) {
                ergebnis.add(punkt);
            }
        }
        return ergebnis;
    }

    /**
     * Parst eine einzelne Zeile.
     *
     * @return der Messpunkt oder {@code null}, wenn die Zeile nicht lesbar war
     */
    public Messpunkt parseZeile(String zeile) {
        String[] felder = zeile.split(TRENNER);
        if (felder.length < 4) {
            // TODO(VW-3012): Defekte Zeilen zählen und am Ende einmal melden,
            //  statt sie still zu verschlucken.
            return null;
        }
        try {
            int ladepunktId = Integer.parseInt(felder[0].trim());
            Instant zeitpunkt = Instant.parse(felder[1].trim());
            double leistungKw = Double.parseDouble(felder[2].trim());
            double temperaturC = Double.parseDouble(felder[3].trim());
            return new Messpunkt(ladepunktId, zeitpunkt, leistungKw, temperaturC);
        } catch (NumberFormatException | DateTimeParseException e) {
            return null;
        }
    }
}
