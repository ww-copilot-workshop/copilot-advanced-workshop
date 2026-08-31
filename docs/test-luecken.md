# Wöchentlicher Testlücken-Report

**Stand:** automatisiert erstellt, Basis: `mvn -q -B -ntp -DskipTests test-compile` (erfolgreich).

Methodik: alle produktiven Klassen unter `*/src/main/java` mit den Testklassen unter
`*/src/test/java` abgeglichen. Reine Datenträger (Records ohne Logik, z. B. `Messpunkt`),
Konfiguration und generierter Code sind nicht aufgeführt. Priorisierung nach Risiko:
Geld/Zeit-Berechnung > SPEC.md-Regeln > Verzweigung über Enum/Status > Rest.

## Lücken

| Klasse | Modul | Grund der Priorisierung | Vorgeschlagene Testfälle |
|---|---|---|---|
| `AbrechnungsService` | modul-5 | **Höchstes Risiko.** Berechnet Geld (`BigDecimal`-Rückgabe, aber `double`-Kern) und Zeit (`Calendar`/`Date`, feste `TimeZone.UTC`). Weicht laut Code-Review bereits an mehreren Stellen von `SPEC.md` ab: Nachtfenster wird binär anhand `ladeStart` in UTC geprüft statt linear über die Ladedauer in Ortszeit `Europe/Berlin` verteilt (S-1); DC-Zuschlag-Schwelle nutzt `>=` statt `>` (S-2 verlangt "echt über 150,0 kW"). Nur ein einziger Testfall (`standardSitzungWirdAbgerechnet`) vorhanden — alle Verzweigungen (BASIS/FLOTTE/NACHT), Blockiergebühr-Grenzfälle und die DC-Zuschlag-Schwelle sind ungetestet. | Je ein Fall pro Tarif (BASIS, FLOTTE, NACHT tags/nachts); NACHT-Sitzung, die das Nachtfenster über- und unterschreitet (Grenzfall linear vs. binär gemäß SPEC-Beispiel 04:00–08:00 → 19,60 EUR); DC-Zuschlag bei genau 150,0 kW (soll 0 EUR sein) und bei 150,1 kW; Blockiergebühr bei genau 240 und 241 Minuten Standzeit; `energieKwh < 0.1` liefert 0 EUR; `sitzung == null` wirft `IllegalArgumentException`; Rundungsverhalten bei kaufmännisch zu rundenden Zwischenwerten (z. B. `x,xx5`). |
| `MonatsReport` | modul-5 | Aggregiert die Geldbeträge aus `AbrechnungsService` je Ladepunkt und bildet eine Kontrollsumme (`summeStimmt`) für die Buchhaltung — direkte Geldlogik, aktuell **ohne jede Testklasse**. Enthält eigene Fehlerquelle: `BigDecimal.equals` ist skalensensitiv (12.50 ≠ 12.5), was laut AGENTS.md-Konvention (`compareTo` statt `equals`) ein Befund ist. | `proLadepunkt` mit mehreren Sitzungen auf demselben und auf verschiedenen Ladepunkten (Reihenfolge des ersten Auftretens prüfen); leere Sitzungsliste liefert leere Liste; `summeStimmt` mit exakt übereinstimmender Summe; `summeStimmt` mit gleichem Betrag aber unterschiedlicher Skala (z. B. `12.50` vs. `12.5`) als Regressionstest für die `equals`-Falle. |
| `ReportZeile` | modul-5 | Kapselt die Additionslogik der Geldsumme (`addiere`) je Ladepunkt; einfache, aber ungetestete Geldlogik, die von `MonatsReport` genutzt wird. | Mehrfaches `addiere` erhöht `anzahlSitzungen` korrekt und summiert `BigDecimal`-Beträge verlustfrei; `toString`-Format (falls fachlich relevant für Betriebsleitungs-Mail). |
| `WerkstattMcpServer.FreigabePruefen` | modul-2/mcp-server | Verzweigt über den Freigabestatus-Enum/-String (`freigegeben`/`gesperrt`/`nur-test`/unbekannt) einer Paketversion — genau die Art Statusverzweigung, die laut AGENTS.md kein Testfall überspringen darf. Aktuell nicht implementiert (`TODO`) und daher ungetestet; sobald implementiert, ist die Versionsvergleichslogik (semantisch, nicht alphabetisch: „3.17.0 > 3.9") eine typische Fehlerquelle. | Freigegebene Version → JA; gesperrte/nur-test-Version → NEIN mit Begründung und Vorschlag der höchsten freigegebenen Alternative; unbekanntes Paket bzw. nur Artifact-Id ohne Group-Id (Fallback auf `suche()`); Versionsvergleich `3.9.0` vs. `3.17.0` (semantisch, nicht alphabetisch). |
| `WerkstattMcpServer.RisikoReport` | modul-2/mcp-server | Läuft über den gesamten Index und muss je Version zwischen mehreren Zuständen unterscheiden (freigegeben+CVE, gesperrt, offener Antrag) — Statusverzweigung ohne jeden Test. Aktuell nicht implementiert (`TODO`). | Index mit gemischten Zuständen (freigegeben mit CVE, gesperrt, nur-test) erzeugt korrekt gruppierten Report; leerer Index liefert sinnvolle Leermeldung; Ausgabegröße bleibt bei großem Index begrenzt (laut Kommentar im Code explizit gefordert). |
| `LadepunktStatistik.durchschnittlicheLeistung` | modul-1 | Berechnungslogik über Leistungswerte (kW) einzelner Ladepunkte; aktuell nicht implementiert (`UnsupportedOperationException`, Übung A1). Bereits in `LadepunktStatistikTest` mit zwei Fällen vorgesehen (`durchschnittOhneMesspunkteIstNull`, `durchschnittProLadepunkt`), zählt hier trotzdem als Lücke, weil die Implementierung fehlt und die vorhandenen Tests daher nicht grün sein können. | (bereits in Testklasse angelegt — nach Implementierung nur verifizieren, u. a. Rundungs-/Mittelwertbildung bei ungleichen Messintervallen, falls fachlich relevant.) |
| `TelemetrieParser.parseZeile` | modul-1 | Zeitpunkt-Parsing (`Instant.parse`) ist Zeitlogik; Fehlerbehandlung bei defekten Feldern (leere Felder, zu wenige Spalten, ungültiges Zahlenformat, ungültiges Zeitformat) ist nur teilweise über `defekteZeileLiefertNull` abgedeckt. | Je ein Fall für: zu wenige Felder, nicht-numerisches `leistungKw`/`temperaturC`, ungültiger ISO-8601-Zeitstempel, führende/nachgestellte Leerzeichen in Feldern (wird laut Code getrimmt). |

Nicht aufgeführt (kein Befund): `Messpunkt`, `Kunde`, `Tarif` (reine Datenträger/Enum ohne
Verzweigungslogik), `Json` und `McpServer` (Infrastruktur, laut Modulbeschreibung nicht Teil
der fachlichen Übungen), `PaketIndex` (bereits indirekt über `McpServerTest` und die
JSON-Parsing-Pfade genutzt, primär Datenzugriff ohne eigene Geschäftsregeln).

## Top 3 für diese Woche

1. **`AbrechnungsService`** — größtes Geld- und Zeitrisiko im gesamten Repository; die
   erkennbare SPEC-Abweichung bei Nachttarif und DC-Zuschlag-Schwelle kann nur durch Tests
   *gegen die Spezifikation* sichtbar gemacht und später behoben werden.
2. **`MonatsReport`** — direkt nachgelagerte Geldaggregation für die Buchhaltung, komplett
   ungetestet, mit einer konkret benennbaren `BigDecimal.equals`-Falle.
3. **`WerkstattMcpServer.FreigabePruefen`** — einzige produktionsreife Statusverzweigung im
   MCP-Modul mit fachlicher Tragweite (Freigabeentscheidung für Abhängigkeiten), aktuell
   ohne jede Testabdeckung, da noch nicht implementiert.
