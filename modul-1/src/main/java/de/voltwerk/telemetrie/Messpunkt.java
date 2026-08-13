package de.voltwerk.telemetrie;

import java.time.Instant;

/**
 * Ein einzelner Messpunkt aus dem Telemetrie-Strom eines Ladepunkts.
 *
 * @param ladepunktId fachliche ID des Ladepunkts
 * @param zeitpunkt   Messzeitpunkt (UTC)
 * @param leistungKw  momentane Ladeleistung in kW
 * @param temperaturC Temperatur des Ladekabels in Grad Celsius
 */
public record Messpunkt(int ladepunktId, Instant zeitpunkt, double leistungKw, double temperaturC) {

    public Messpunkt {
        if (zeitpunkt == null) {
            throw new IllegalArgumentException("zeitpunkt darf nicht null sein");
        }
    }
}
