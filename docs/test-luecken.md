# Wöchentlicher Testlücken-Report

**Stand:** siehe Commit-Datum · **Scope:** `modul-1`, `modul-2/mcp-server`, `modul-5`

Grundlage: Abgleich aller produktiven Klassen unter `*/src/main/java` gegen alle
Testklassen unter `*/src/test/java`. Reine Datenträger ohne Logik (z. B. `Kunde`,
`Tarif`, `Messpunkt`) sowie generierter/Infrastruktur-Code sind ausgenommen.

> Hinweis: `mvn -DskipTests test-compile` konnte in dieser Umgebung nicht laufen
> (kein Zugriff auf `~/.m2/repository`). Die Analyse basiert daher auf manueller
> Durchsicht von Quell- und Testverzeichnissen statt auf einem Build-Artefakt.

## Gefundene Lücken

| Klasse | Modul | Grund der Priorisierung | vorgeschlagene Testfälle |
|---|---|---|---|
| `MonatsReport` | modul-5 | Setzt S-10 (Aggregation je Ladepunkt) und S-11 (Kontrollrechnung) aus `SPEC.md` um; keine Testklasse vorhanden | Mehrere Sitzungen mit gleicher `ladepunktId` → genau eine Zeile mit korrekter Summe; Reihenfolge der Zeilen entspricht erstem Auftreten; `summeStimmt` liefert `true` bei wertgleichem, aber unterschiedlich skaliertem Betrag (`12.5` vs. `12.50`); `summeStimmt` liefert `false` bei abweichender Summe; leere Sitzungsliste ergibt leere Zeilenliste |
| `ReportZeile` | modul-5 | Führt Beträge (`BigDecimal`) und Zähler; direkt an S-11 gekoppelt (Skalen-Wertgleichheit) | `addiere` erhöht `anzahlSitzungen` und summiert Beträge korrekt mit `compareTo`; mehrfaches `addiere` mit unterschiedlichen Skalen (z. B. `5.9` + `0.10`) ergibt korrekte Summe; initialer Zustand (`0` Sitzungen, Summe `0`) |
| `WerkstattMcpServer.FreigabePruefen` (Werkzeug `freigabe_pruefen`) | modul-2/mcp-server | Enthält Verzweigungslogik über Freigabestatus (`freigegeben`/`gesperrt`/`nur-test`) und Versionssemantik — laut Auftrag TODO, aber die Datei ist aktuell noch nicht implementiert (`ausfuehren` wirft immer `WerkzeugFehler`) | Sobald implementiert: JA-Fall bei `freigegeben`; NEIN mit Begründung bei `gesperrt`/`nur-test`; Vorschlag der höchsten freigegebenen Alternativversion bei semantischem Vergleich (z. B. `3.17.0` > `3.9.0`); kein Vorschlag, wenn keine freigegebene Version existiert; Fehlerfall bei unbekanntem Paket inkl. Teilstring-Suche über `artifactId` |
| `WerkstattMcpServer.RisikoReport` (Werkzeug `risiko_report`) | modul-2/mcp-server | Aggregiert über den gesamten Index (Verzweigung über Freigabestatus/CVEs); ebenfalls noch nicht implementiert | Sobald implementiert: enthält freigegebene Versionen mit CVEs; enthält gesperrte Versionen; enthält offene Anträge; Ausgabe bleibt bei großem Index begrenzt (Kürzung/Zusammenfassung) |
| `PaketIndex` | modul-2/mcp-server | Enthält Verzweigungslogik (`finde` case-insensitive exakt, `suche` Teilstring, Fallback-Defaults beim JSON-Parsing über `Json.alsText(..., ersatz)`); keine Testklasse vorhanden, obwohl `McpServerTest` sie indirekt nutzt | `finde` mit exakter Koordinate (Groß-/Kleinschreibung variiert); `finde` liefert `Optional.empty()` bei `null`/unbekannter Koordinate; `suche` mit Teilstring liefert mehrere Treffer; `ausJson` füllt Ersatzwerte (`"unbekannt"`, `"?"`) bei fehlenden Feldern; `istFreigegeben()` auf `Version` für `"freigegeben"` und andere Werte |

## Nicht als Lücke gewertet

* `Kunde`, `Tarif` — reine Datenträger ohne Verzweigungslogik.
* `Messpunkt` — reiner Datenträger.
* `Ladesitzung` — Datenträger mit Gettern/Settern, keine Geschäftslogik; wird über
  `AbrechnungsServiceTest` indirekt abgedeckt.
* `Json`, `Werkzeug`, `McpServer` — Infrastruktur/Interface bzw. bereits über
  `McpServerTest` mitgetestet (JSON-Parsing, Werkzeug-Registrierung).

## Top 3 für diese Woche

1. **`MonatsReport`** — bildet die einzige Aggregations- und Kontrolllogik für den
   monatlichen Betreiber-Report ab (S-10/S-11 aus `SPEC.md`); Fehler hier führen
   direkt zu falschen Abrechnungssummen gegenüber der Betriebsleitung.
2. **`ReportZeile`** — die stille Skalen-Fallstrick-Klasse für S-11: `addiere` mit
   `BigDecimal` unterschiedlicher Skala ist die Stelle, an der "stimmt die Summe"
   real geprüft werden muss, bevor `MonatsReport` sich darauf verlässt.
3. **`PaketIndex`** — trägt die Verzweigungslogik für Freigabestatus- und Versions-
   suche, auf der beide unfertigen MCP-Werkzeuge (`freigabe_pruefen`,
   `risiko_report`) aufbauen; ungetestete Fallback-Defaults hier würden sich sonst
   unbemerkt in beide Werkzeuge fortpflanzen.
