package de.voltwerk.mcp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Minimaler MCP-Server über stdio (JSON-RPC 2.0, eine Nachricht je Zeile).
 *
 * <p>Unterstützt genau so viel vom Protokoll, wie ein Werkzeug-Server braucht:
 * {@code initialize}, {@code tools/list}, {@code tools/call}, {@code ping}.</p>
 *
 * <p><b>Wichtig:</b> stdout gehört dem Protokoll. Jede Log-Ausgabe muss nach stderr,
 * sonst bricht die Verbindung zur CLI. Das ist der häufigste Anfängerfehler bei
 * MCP-Servern — und ein schönes Debugging-Erlebnis, wenn man ihn einmal gemacht hat.</p>
 *
 * <p>Das ist <b>Infrastruktur</b>. Für Lab B müsst ihr hier nichts ändern.</p>
 */
public class McpServer {

    private static final String STANDARD_PROTOKOLL = "2025-06-18";

    private final String name;
    private final String version;
    private final Map<String, Werkzeug> werkzeuge = new LinkedHashMap<>();

    public McpServer(String name, String version) {
        this.name = name;
        this.version = version;
    }

    /** Registriert ein Werkzeug. Doppelte Namen überschreiben sich. */
    public McpServer registriere(Werkzeug werkzeug) {
        werkzeuge.put(werkzeug.name(), werkzeug);
        return this;
    }

    /** Startet die Schleife auf stdin/stdout und läuft, bis stdin geschlossen wird. */
    public void starte() throws IOException {
        starte(System.in, System.out);
    }

    void starte(InputStream in, OutputStream out) throws IOException {
        BufferedReader leser = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
        Writer schreiber = new java.io.OutputStreamWriter(out, StandardCharsets.UTF_8);
        PrintStream log = System.err;

        log.println("[werkstatt-mcp] bereit, " + werkzeuge.size() + " Werkzeuge registriert");

        String zeile;
        while ((zeile = leser.readLine()) != null) {
            if (zeile.isBlank()) {
                continue;
            }
            String antwort;
            try {
                antwort = verarbeite(zeile);
            } catch (RuntimeException e) {
                log.println("[werkstatt-mcp] Fehler: " + e);
                antwort = Json.schreibe(fehler(null, -32700, "Nachricht nicht lesbar: " + e.getMessage()));
            }
            if (antwort != null) {
                schreiber.write(antwort);
                schreiber.write("\n");
                schreiber.flush();
            }
        }
        log.println("[werkstatt-mcp] stdin geschlossen, Ende");
    }

    /**
     * Verarbeitet eine einzelne JSON-RPC-Nachricht.
     *
     * @return die Antwort als JSON-Zeile, oder {@code null} bei Notifications
     */
    String verarbeite(String nachricht) {
        Map<String, Object> anfrage = Json.alsObjekt(Json.parse(nachricht));
        Object id = anfrage.get("id");
        String methode = Json.alsText(anfrage.get("method"), "");
        Map<String, Object> params = Json.alsObjekt(anfrage.get("params"));

        // Notifications (ohne id) werden nicht beantwortet.
        if (id == null) {
            return null;
        }

        return switch (methode) {
            case "initialize" -> Json.schreibe(ergebnis(id, initialisiere(params)));
            case "ping" -> Json.schreibe(ergebnis(id, Map.of()));
            case "tools/list" -> Json.schreibe(ergebnis(id, werkzeugListe()));
            case "tools/call" -> Json.schreibe(ergebnis(id, werkzeugAufruf(params)));
            default -> Json.schreibe(fehler(id, -32601, "Unbekannte Methode: " + methode));
        };
    }

    private Map<String, Object> initialisiere(Map<String, Object> params) {
        String protokoll = Json.alsText(params.get("protocolVersion"), STANDARD_PROTOKOLL);
        return Json.objekt(
                "protocolVersion", protokoll,
                "capabilities", Json.objekt("tools", Json.objekt()),
                "serverInfo", Json.objekt("name", name, "version", version));
    }

    private Map<String, Object> werkzeugListe() {
        List<Object> liste = new ArrayList<>();
        for (Werkzeug w : werkzeuge.values()) {
            liste.add(Json.objekt(
                    "name", w.name(),
                    "description", w.beschreibung(),
                    "inputSchema", w.eingabeSchema()));
        }
        return Json.objekt("tools", liste);
    }

    private Map<String, Object> werkzeugAufruf(Map<String, Object> params) {
        String werkzeugName = Json.alsText(params.get("name"), "");
        Map<String, Object> argumente = Json.alsObjekt(params.get("arguments"));

        Werkzeug werkzeug = werkzeuge.get(werkzeugName);
        if (werkzeug == null) {
            return inhalt("Unbekanntes Werkzeug: " + werkzeugName, true);
        }
        try {
            return inhalt(werkzeug.ausfuehren(argumente), false);
        } catch (Werkzeug.WerkzeugFehler e) {
            return inhalt(e.getMessage(), true);
        } catch (RuntimeException e) {
            return inhalt("Unerwarteter Fehler in " + werkzeugName + ": " + e, true);
        }
    }

    private static Map<String, Object> inhalt(String text, boolean fehlerhaft) {
        return Json.objekt(
                "content", List.of(Json.objekt("type", "text", "text", text)),
                "isError", fehlerhaft);
    }

    private static Map<String, Object> ergebnis(Object id, Object wert) {
        return Json.objekt("jsonrpc", "2.0", "id", id, "result", wert);
    }

    private static Map<String, Object> fehler(Object id, int code, String nachricht) {
        return Json.objekt("jsonrpc", "2.0", "id", id,
                "error", Json.objekt("code", code, "message", nachricht));
    }
}
