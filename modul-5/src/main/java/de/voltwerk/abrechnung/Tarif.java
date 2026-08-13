package de.voltwerk.abrechnung;

/**
 * Tarifarten der Ladepark-Suite.
 *
 * <p>Historie: NACHT kam 2019 dazu, FLOTTE 2021. Die Preise stehen bewusst hier und nicht
 * in der Konfiguration -- "das war damals schneller". Siehe SPEC.md, Abschnitt S-1.</p>
 */
public enum Tarif {

    BASIS,
    FLOTTE,
    NACHT
}
