package de.voltwerk.mcp;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Der Werkstatt-MCP-Server: beantwortet Fragen zum Artifactory-Proxy.
 *
 * <p><b>Hier arbeitet ihr in Lab B3.</b> Ein Werkzeug ist fertig und dient als Vorlage,
 * zwei weitere sind Übung. Der Rest des Pakets ist Infrastruktur.</p>
 *
 * <p>Starten:</p>
 * <pre>{@code
 * mvn -q -pl modul-2/mcp-server -am package
 * java -jar modul-2/mcp-server/target/werkstatt-mcp.jar
 * }</pre>
 */
public final class WerkstattMcpServer {

    public static void main(String[] args) throws IOException {
        PaketIndex index = PaketIndex.laden();

        new McpServer("werkstatt-mcp", "1.0.0")
                .registriere(new PaketVersionen(index))
                .registriere(new FreigabePruefen(index))
                .registriere(new RisikoReport(index))
                .starte();
    }

    // =====================================================================================
    // VORLAGE - fertig implementiert. Schaut euch an, wie Name, Beschreibung und Schema
    // zusammenspielen, bevor ihr die beiden folgenden Werkzeuge baut.
    // =====================================================================================

    /** Listet alle im Proxy bekannten Versionen eines Pakets. */
    record PaketVersionen(PaketIndex index) implements Werkzeug {

        @Override
        public String name() {
            return "paket_versionen";
        }

        @Override
        public String beschreibung() {
            return """
                   Listet alle Versionen eines Maven-Pakets, die im Artifactory-Proxy der \
                   Voltwerk bekannt sind, mit Freigabestatus, Lizenz und bekannten CVEs. \
                   Benutze dieses Werkzeug, wenn gefragt wird, welche Versionen einer \
                   Bibliothek verfügbar oder erlaubt sind, oder bevor du eine Abhängigkeit \
                   in eine pom.xml aufnimmst.""";
        }

        @Override
        public Map<String, Object> eingabeSchema() {
            return Json.objekt(
                    "type", "object",
                    "properties", Json.objekt(
                            "koordinate", Json.objekt(
                                    "type", "string",
                                    "description",
                                    "Maven-Koordinate als groupId:artifactId, z.B. "
                                            + "org.apache.commons:commons-lang3")),
                    "required", List.of("koordinate"));
        }

        @Override
        public String ausfuehren(Map<String, Object> argumente) throws WerkzeugFehler {
            String koordinate = Json.alsText(argumente.get("koordinate"), "");
            if (koordinate.isBlank()) {
                throw new WerkzeugFehler("Parameter 'koordinate' fehlt.");
            }

            PaketIndex.Paket paket = index.finde(koordinate).orElse(null);
            if (paket == null) {
                List<PaketIndex.Paket> treffer = index.suche(koordinate);
                if (treffer.isEmpty()) {
                    throw new WerkzeugFehler(
                            "Paket '" + koordinate + "' ist im Proxy nicht bekannt. "
                                    + "Ein Freigabeantrag läuft über das Ticket-System.");
                }
                return "Keine exakte Übereinstimmung. Ähnliche Pakete im Proxy:\n"
                        + treffer.stream().map(PaketIndex.Paket::koordinate)
                                 .reduce("", (a, b) -> a + "  - " + b + "\n");
            }

            StringBuilder sb = new StringBuilder();
            sb.append("Paket: ").append(paket.koordinate()).append("\n");
            sb.append("Proxy: ").append(index.proxy())
              .append(" (Stand ").append(index.stand()).append(")\n\n");
            for (PaketIndex.Version v : paket.versionen()) {
                sb.append("  ").append(v.version())
                  .append("  [").append(v.freigabe()).append("]")
                  .append("  Lizenz ").append(v.lizenz());
                if (!v.cves().isEmpty()) {
                    sb.append("  CVEs: ").append(String.join(", ", v.cves()));
                }
                if (!v.hinweis().isBlank()) {
                    sb.append("\n      Hinweis: ").append(v.hinweis());
                }
                sb.append("\n");
            }
            return sb.toString();
        }
    }

    // =====================================================================================
    // ÜBUNG B3-a - Werkzeug "freigabe_pruefen"
    //
    // Ziel: Eine konkrete Frage beantworten: "Darf ich Paket X in Version Y benutzen?"
    // Erwartet: Klare Antwort JA/NEIN mit Begründung; bei NEIN die nächste erlaubte
    // Version vorschlagen, falls es eine gibt.
    //
    // Lasst das die Copilot CLI bauen - aber prüft die Beschreibung selbst.
    // Die Beschreibung ist der Teil, den ein Modell nicht für euch erraten kann.
    // =====================================================================================

    /** Prüft, ob eine bestimmte Version eines Pakets benutzt werden darf. */
    record FreigabePruefen(PaketIndex index) implements Werkzeug {

        @Override
        public String name() {
            return "freigabe_pruefen";
        }

        @Override
        public String beschreibung() {
            // TODO(B3-a): Beschreibung schreiben. Was tut das Werkzeug, und wann
            //  soll das Modell es benutzen? Zwei bis vier Sätze reichen.
            return "TODO";
        }

        @Override
        public Map<String, Object> eingabeSchema() {
            // TODO(B3-a): Schema mit den Feldern 'koordinate' und 'version', beide Pflicht.
            return Json.objekt("type", "object", "properties", Json.objekt());
        }

        @Override
        public String ausfuehren(Map<String, Object> argumente) throws WerkzeugFehler {
            // TODO(B3-a): Implementieren.
            //  - Paket im Index suchen. ACHTUNG: Das Modell gibt oft nur den artifactId
            //    weiter ("spring-boot-starter-web"), nicht die volle Koordinate.
            //    PaketIndex bietet dafür suche(teil) neben finde(koordinate);
            //    paket_versionen oben zeigt das Muster. Wer nur finde() benutzt, baut
            //    ein Werkzeug, das an der Abnahmefrage scheitert.
            //  - Findest du gar nichts: WerkzeugFehler mit hilfreichem Text.
            //  - Version suchen, Freigabestatus auswerten.
            //  - Bei 'gesperrt' oder 'nur-test': Grund nennen und, wenn vorhanden,
            //    die HÖCHSTE freigegebene Version als Alternative vorschlagen.
            //    Semantisch vergleichen, nicht alphabetisch -- 3.17.0 ist höher als 3.9.
            throw new WerkzeugFehler("freigabe_pruefen ist noch nicht implementiert (Übung B3-a).");
        }
    }

    // =====================================================================================
    // ÜBUNG B3-b (Kür) - Werkzeug "risiko_report"
    //
    // Ziel: Über den gesamten Index laufen und alles auflisten, was Aufmerksamkeit
    // braucht: freigegebene Versionen mit CVEs, gesperrte Versionen, offene Anträge.
    // Ohne Parameter aufrufbar.
    //
    // Denkt an die Kosten: dieses Werkzeug kann viel Text zurückgeben, und jeder
    // zurückgegebene Text landet im Modellkontext. Begrenzt die Ausgabe sinnvoll.
    // =====================================================================================

    /** Überblick über auffällige Pakete im Proxy. */
    record RisikoReport(PaketIndex index) implements Werkzeug {

        @Override
        public String name() {
            return "risiko_report";
        }

        @Override
        public String beschreibung() {
            // TODO(B3-b): Beschreibung schreiben.
            return "TODO";
        }

        @Override
        public Map<String, Object> eingabeSchema() {
            return Json.objekt("type", "object", "properties", Json.objekt());
        }

        @Override
        public String ausfuehren(Map<String, Object> argumente) throws WerkzeugFehler {
            // TODO(B3-b): Implementieren.
            throw new WerkzeugFehler("risiko_report ist noch nicht implementiert (Übung B3-b).");
        }
    }

    private WerkstattMcpServer() {
    }
}
