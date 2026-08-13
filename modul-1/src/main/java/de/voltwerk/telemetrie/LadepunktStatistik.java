package de.voltwerk.telemetrie;

import java.util.List;
import java.util.Map;

/**
 * Auswertungen über den Telemetriestrom.
 *
 * <p><b>Status:</b> Contract steht, Implementierung fehlt. Das ist die Spielwiese für
 * Übung A1 — zwei Lösungswege, ein Vergleich.</p>
 */
public class LadepunktStatistik {

    /**
     * Höchste gemessene Leistung je Ladepunkt.
     *
     * @param messpunkte alle Messpunkte, darf leer sein
     * @return Map von {@code ladepunktId} auf die Spitzenleistung in kW;
     *         Ladepunkte ohne Messpunkt tauchen nicht auf
     */
    public Map<Integer, Double> spitzenleistungJeLadepunkt(List<Messpunkt> messpunkte) {
        throw new UnsupportedOperationException("Übung A1");
    }

    /**
     * Durchschnittliche Ladeleistung eines Ladepunkts über alle seine Messpunkte.
     *
     * @param messpunkte  alle Messpunkte
     * @param ladepunktId der gesuchte Ladepunkt
     * @return Durchschnitt in kW, oder {@code 0.0}, wenn es keine Messpunkte gibt
     */
    public double durchschnittlicheLeistung(List<Messpunkt> messpunkte, int ladepunktId) {
        throw new UnsupportedOperationException("Übung A1");
    }

    /**
     * Findet Ladepunkte mit einem Überhitzungsverdacht.
     *
     * <p>Verdacht besteht, wenn die Kabeltemperatur eines Ladepunkts in
     * <b>mindestens drei aufeinanderfolgenden</b> Messpunkten dieses Ladepunkts über der
     * Schwelle liegt. Die Messpunkte eines Ladepunkts gelten in der Reihenfolge, in der
     * sie in der Liste stehen.</p>
     *
     * @param messpunkte alle Messpunkte, gemischt über alle Ladepunkte
     * @param schwelleC  Temperaturschwelle in Grad Celsius (exklusiv)
     * @return aufsteigend sortierte, doppelfreie Liste der betroffenen Ladepunkt-IDs
     */
    public List<Integer> ueberhitzungsverdacht(List<Messpunkt> messpunkte, double schwelleC) {
        throw new UnsupportedOperationException("Übung A1");
    }
}
