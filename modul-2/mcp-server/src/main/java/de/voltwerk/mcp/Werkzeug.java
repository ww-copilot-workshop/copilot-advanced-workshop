package de.voltwerk.mcp;

import java.util.Map;

/**
 * Ein Werkzeug (MCP: <i>tool</i>), das der Server dem Agenten anbietet.
 *
 * <p>Das ist die Stelle, an der ihr in Lab B3 arbeitet: neue Werkzeuge = neue
 * Implementierungen dieses Interfaces, registriert in
 * {@link WerkstattMcpServer}.</p>
 */
public interface Werkzeug {

    /**
     * Technischer Name, unter dem der Agent das Werkzeug aufruft.
     * Konvention: {@code kleinbuchstaben_mit_unterstrich}.
     */
    String name();

    /**
     * Beschreibung für das Modell. <b>Das ist der wichtigste String im ganzen Server.</b>
     * Sie entscheidet, ob und wann das Werkzeug überhaupt aufgerufen wird.
     * Schreibt hinein, <i>was</i> es tut und <i>wann</i> man es benutzt.
     */
    String beschreibung();

    /**
     * JSON-Schema der Eingabe, als verschachtelte Map.
     *
     * <p>Beispiel:</p>
     * <pre>{@code
     * Json.objekt(
     *     "type", "object",
     *     "properties", Json.objekt(
     *         "paket", Json.objekt("type", "string", "description", "Maven-Koordinate groupId:artifactId")),
     *     "required", List.of("paket"));
     * }</pre>
     */
    Map<String, Object> eingabeSchema();

    /**
     * Führt das Werkzeug aus.
     *
     * @param argumente die vom Agenten übergebenen Argumente, nie {@code null}
     * @return das Ergebnis als Text; wird dem Modell als Tool-Ergebnis gezeigt
     * @throws WerkzeugFehler bei fachlichen Fehlern (falsche Eingabe, nichts gefunden)
     */
    String ausfuehren(Map<String, Object> argumente) throws WerkzeugFehler;

    /** Fachlicher Fehler eines Werkzeugs. Wird dem Agenten als Fehlertext zurückgegeben. */
    class WerkzeugFehler extends Exception {
        public WerkzeugFehler(String nachricht) {
            super(nachricht);
        }
    }
}
