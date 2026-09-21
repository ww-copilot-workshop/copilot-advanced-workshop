# Wöchentlicher Testlücken-Report

**Stand:** 2026-09-21
**Grundlage:** `mvn -q -B -ntp -DskipTests test-compile` (erfolgreich, JDK 21), manuelle
Durchsicht aller produktiven Klassen unter `*/src/main/java` gegen die vorhandenen
Testklassen unter `*/src/test/java`.

> Hinweis: Dieser Report ändert nur `docs/test-luecken.md`. Es wurde kein Produktivcode
> und kein Test geschrieben — das ist bewusst so beauftragt.

---

## Befund vorab: roter Test in Produktion

`AbrechnungsServiceTest.standardSitzungWirdAbgerechnet()` erwartet **2,03 EUR**. Nach
`SPEC.md` (S-1, S-6, S-8) ergibt die Beispielsitzung (BASIS, 3,456 kWh, 50 kW) **2,04 EUR**
— siehe Referenzbeispiel B3 in der Spec, das exakt diesen Fall abdeckt. Der Test-Kommentar
selbst dokumentiert, dass der Erwartungswert 2022 an das (falsche) Ist-Verhalten angepasst
wurde, statt den Code zu korrigieren (VW-1533). Das ist laut `AGENTS.md` Abschnitt 4
("Tests werden gegen die Spezifikation geschrieben, nicht gegen den Bestand") ein Verstoß
und wird hiermit gemeldet, nicht stillschweigend mitgefixt.

Zusätzlich verwendet `AbrechnungsService.istNachts(...)` `TimeZone.getTimeZone("UTC")`
statt der laut Spec (S-1, S-7) verbindlichen Ortszeit **Europe/Berlin**. Das ist keine
Testlücke im engeren Sinn, aber es erklärt, warum ein korrekter Test für B11–B13
(NACHT-Tarif) mit hoher Wahrscheinlichkeit fehlschlagen wird — ein weiterer Beleg dafür,
warum `AbrechnungsService` oberste Priorität hat.

---

## Tabelle: Klassen mit fehlender oder unzureichender Testabdeckung

| Klasse | Modul | Grund der Priorisierung | Vorgeschlagene Testfälle |
|---|---|---|---|
| `AbrechnungsService` | modul-5 | Berechnet Geld (BigDecimal-Rückgabe, aber `double`-Kern) und Zeit; setzt SPEC.md S-1 bis S-8 um; einzige vorhandene Testmethode hat einen Erwartungswert, der nachweislich der Spec widerspricht (VW-1533) | Alle Referenzbeispiele B1–B13 aus SPEC.md als Einzeltests; S-2 Grenzfall exakt 150,0 kW vs. 150,1 kW; S-3 Grenzfälle 240/241/600 Minuten und Deckel bei 12,00 EUR; S-4/S-5 Fehlversuch-Kombinationen (kwh<0,1 UND Dauer<2min vs. nur eine Bedingung erfüllt); S-1 Nachtfenster mit Ortszeit Europe/Berlin statt Systemzeit/UTC, inkl. Sommerzeitfall B13; `null`-Sitzung wirft `IllegalArgumentException`; fehlendes `abgestecktUm` bzw. `abgestecktUm` vor `ladeEnde` ergibt keine Blockiergebühr |
| `LadepunktStatistik` | modul-1 | Verzweigt über Messreihen (Serienerkennung „drei in Folge"); Abnahmetests existieren bereits vollständig, sind aber mit `@Disabled` deaktiviert — die Klasse selbst wirft aktuell nur `UnsupportedOperationException` | Kein neuer Testvorschlag nötig: `LadepunktStatistikTest` ist bereits fertig und deckt Spitzenleistung, Durchschnitt, Serienerkennung, Sortierung/Doppelfreiheit und Reihenfolge-vs-Zeitstempel ab. Sobald A1 implementiert ist, genügt das Entfernen von `@Disabled` |
| `MonatsReport` | modul-5 | Aggregiert Geldbeträge je Ladepunkt (S-10); `proLadepunkt` vergleicht `Integer`-Ladepunkt-IDs mit `==` statt `.equals(...)`, was bei Werten außerhalb des Integer-Caches (typischerweise > 127) zu doppelten Zeilen für denselben Ladepunkt führen kann — ein konkretes Bug-Risiko, kein Stiltadel | Zwei Sitzungen mit gleicher `ladepunktId` als großer `Integer`-Wert (z. B. 5000) landen in einer Zeile, nicht zwei; Reihenfolge der Zeilen folgt dem ersten Auftreten (S-10) bei gemischten Ladepunkt-IDs; leere Sitzungsliste liefert leere Zeilenliste; `summeStimmt` liefert `true` bei wertgleicher, aber unterschiedlich skalierter Summe (`12.5` vs. `12.50`, S-11); `summeStimmt` liefert `false` bei abweichender Summe |
| `ReportZeile` | modul-5 | Führt eine Geldsumme über `addiere(BigDecimal)`; paketprivate Methode ist laut SPEC.md API-Kontrakt explizit für Tests vorgesehen, wird aber nirgends direkt getestet | Mehrfaches `addiere(...)` erhöht `anzahlSitzungen` korrekt und summiert `BigDecimal`-Werte ohne Rundungsverlust; `addiere` mit unterschiedlichen Skalen (z. B. `1.5` und `2.50`) ergibt eine wertgleiche Summe |
| `PaketIndex` | modul-2/mcp-server | Enthält Geschäftslogik mit Verzweigungen über Freigabestatus-Strings (`istFreigegeben()`) und Fallback-Regeln beim Parsen (`Json.alsText(..., ersatz)`); wird aktuell nur indirekt über `McpServerTest` mit einem winzigen Testindex berührt, nie direkt | `finde(koordinate)` ist groß-/kleinschreibungsunabhängig; `finde(null)` liefert `Optional.empty()`; `suche(teil)` findet Teiltreffer case-insensitive und liefert bei `null`/leerem Teilstring alle Pakete; `ausJson(...)` mit fehlenden Feldern setzt die dokumentierten Ersatzwerte (`"unbekannt"`, `"?"`); `Version.istFreigegeben()` ist `true` nur bei exakt `"freigegeben"` |
| `Kunde` | modul-5 | Dünne Logik, aber echtes Verhalten: `equals` vergleicht nur über `kundennummer`, nicht über Objektidentität oder `name` — das ist die Grundlage der Aggregation im Monatsreport | Zwei `Kunde`-Instanzen mit gleicher `kundennummer`, aber unterschiedlichem `name` sind gleich; `equals(null)` und `equals(anderer Typ)` liefern `false`; `setName(...)` ändert `getName()`, aber nicht `equals`-Verhalten |

**Nicht aufgeführt (bewusst ausgeschlossen):** `Ladesitzung` (reiner Datenträger mit
Gettern/Settern), `Tarif` (Enum ohne Verhalten), `Messpunkt` (Record mit nur einer
Null-Prüfung, bereits indirekt durch `TelemetrieParserTest` und die deaktivierten A1-Tests
abgedeckt), `Werkzeug` (Interface), `Json` und `McpServer` (Infrastruktur, laut
Kommentar im Code „für Lab B müsst ihr hier nichts ändern", `McpServerTest` deckt die
Protokollpfade bereits ab), `WerkstattMcpServer.FreigabePruefen`/`RisikoReport` (als
`TODO`-Übungsstubs markiert, noch nicht implementiert — hier fehlt kein Test, sondern die
Implementierung selbst).

---

## Top 3 für diese Woche

1. **`AbrechnungsService`** — höchste Priorität: Geld- und Zeitlogik nach SPEC.md, mit
   einem nachweislich falschen Erwartungswert im einzigen vorhandenen Test und einer
   falschen Zeitzone für das Nachtfenster; das Risiko realer Fehlbeträge in der
   Kundenabrechnung ist unmittelbar.
2. **`MonatsReport`** — die `==`-statt-`equals()`-Falle bei `Integer`-Ladepunkt-IDs ist ein
   stiller Bug, der erst bei Ladepunkt-IDs über dem Integer-Cache-Bereich auffällt und
   Beschwerden über „Report stimmt nicht" erklären könnte, wie im Klassenkommentar bereits
   angedeutet.
3. **`LadepunktStatistik`** — die Tests sind vollständig vorhanden und decken die
   fachlich kniffligen Fälle (Serienerkennung je Ladepunkt, Sortierung, Reihenfolge vs.
   Zeitstempel) bereits ab; es fehlt nur die Implementierung und das Entfernen von
   `@Disabled` — geringer Aufwand, hoher Abnahmewert.
