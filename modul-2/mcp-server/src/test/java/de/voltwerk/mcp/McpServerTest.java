package de.voltwerk.mcp;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Protokoll-Tests für die Infrastruktur.
 *
 * <p>Diese Tests laufen ohne die CLI und sind eure schnelle Rückmeldung, ob der Server
 * überhaupt spricht. Sie sagen nichts darüber, ob eure Werkzeuge fachlich stimmen.</p>
 */
class McpServerTest {

    private static final String TEST_INDEX = """
            {"proxy":"test","stand":"2026-01-01",
             "pakete":[{"koordinate":"a.b:c","versionen":[
               {"version":"1.0.0","freigabe":"freigegeben","lizenz":"MIT","cves":[]}]}]}
            """;

    private McpServer server() {
        PaketIndex index = PaketIndex.ausJson(TEST_INDEX);
        return new McpServer("test-mcp", "0.0.1")
                .registriere(new WerkstattMcpServer.PaketVersionen(index));
    }

    @Test
    void initializeAntwortetMitServerInfo() {
        String antwort = server().verarbeite(
                """
                {"jsonrpc":"2.0","id":1,"method":"initialize",
                 "params":{"protocolVersion":"2025-06-18","capabilities":{},
                           "clientInfo":{"name":"test","version":"1"}}}""");

        Map<String, Object> ergebnis = Json.alsObjekt(Json.alsObjekt(Json.parse(antwort)).get("result"));
        assertEquals("2025-06-18", ergebnis.get("protocolVersion"));
        assertEquals("test-mcp", Json.alsObjekt(ergebnis.get("serverInfo")).get("name"));
    }

    @Test
    void notificationBekommtKeineAntwort() {
        assertNull(server().verarbeite("""
                {"jsonrpc":"2.0","method":"notifications/initialized"}"""));
    }

    @Test
    void toolsListLiefertRegistrierteWerkzeuge() {
        String antwort = server().verarbeite("""
                {"jsonrpc":"2.0","id":2,"method":"tools/list","params":{}}""");

        Map<String, Object> ergebnis = Json.alsObjekt(Json.alsObjekt(Json.parse(antwort)).get("result"));
        List<Object> tools = Json.alsListe(ergebnis.get("tools"));
        assertEquals(1, tools.size());
        assertEquals("paket_versionen", Json.alsObjekt(tools.get(0)).get("name"));
    }

    @Test
    void toolsCallLiefertTextinhalt() {
        String antwort = server().verarbeite("""
                {"jsonrpc":"2.0","id":3,"method":"tools/call",
                 "params":{"name":"paket_versionen","arguments":{"koordinate":"a.b:c"}}}""");

        Map<String, Object> ergebnis = Json.alsObjekt(Json.alsObjekt(Json.parse(antwort)).get("result"));
        assertEquals(Boolean.FALSE, ergebnis.get("isError"));
        String text = Json.alsText(Json.alsObjekt(Json.alsListe(ergebnis.get("content")).get(0)).get("text"), "");
        assertTrue(text.contains("1.0.0"), "Version sollte in der Antwort stehen, war: " + text);
    }

    @Test
    void unbekannteMethodeLiefertJsonRpcFehler() {
        String antwort = server().verarbeite("""
                {"jsonrpc":"2.0","id":4,"method":"gibtsNicht","params":{}}""");

        Map<String, Object> fehler = Json.alsObjekt(Json.alsObjekt(Json.parse(antwort)).get("error"));
        assertEquals(-32601.0, fehler.get("code"));
    }
}
