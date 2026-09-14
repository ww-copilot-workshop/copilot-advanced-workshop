# Testlücken-Report

**Stand:** automatisch erzeugt, wöchentlicher Lauf
**Vorgehen:** `mvn -q -B -ntp -DskipTests test-compile` (Übersetzung geprüft, keine Tests
ausgeführt), anschließend Abgleich aller produktiven Klassen unter `*/src/main/java`
gegen die vorhandenen Testklassen unter `*/src/test/java`.

> Reine Datenträger (`Tarif`, `Messpunkt`, `Werkzeug`-Interface), Konfiguration und
> generierter Code sind hier bewusst **nicht** aufgeführt.

---

## Lücken

| Klasse | Modul | Grund der Priorisierung | Vorgeschlagene Testfälle |
|---|---|---|---|
| `AbrechnungsService` | modul-5 | **Höchstes Risiko:** berechnet Geld nach `SPEC.md` (S-1 bis S-8). Der einzige vorhandene Test (`AbrechnungsServiceTest`) prüft laut eigenem Kommentar bewusst einen **falschen, 2022 an das Ist-Verhalten angepassten** Wert (2,03 statt der lt. SPEC korrekten 2,04 EUR für B3) — ein dokumentierter Verstoß gegen Regel 4 aus `AGENTS.md`. Zusätzlich rechnet die Implementierung die Nachtstunden-Prüfung fest in `UTC` (S-7 verlangt `Europe/Berlin`) und teilt beim `NACHT`-Tarif die Energie nicht linear über die Ladedauer auf (S-1), sondern wertet nur `ladeStart` binär aus. | Je ein Test für die Referenzbeispiele B1–B13 aus `SPEC.md` Abschnitt 4 (inkl. B11–B13 für den Nachttarif mit Zeitzone/Sommerzeit); Fehlversuch nach S-5 (B9); Mindestbetrag nach S-4 (B10); Blockiergebühr mit Deckel (B7, B8); Schnelllade-Zuschlag exakt an der Schwelle 150,0 kW (B4 vs. B5); `IllegalArgumentException` bei `sitzung == null`; Rundung `HALF_UP` per `compareTo`, nicht `equals`. |
| `MonatsReport` | modul-5 | Aggregiert Geldbeträge über eine ganze Abrechnungsperiode (S-10, S-11) und hat **keine einzige Testklasse**. Fehler hier wirken sich auf den gesamten Monatsreport aus, nicht nur auf eine Sitzung. | `proLadepunkt`: eine Zeile je `ladepunktId` in Reihenfolge des ersten Auftretens, auch bei gemischten/wiederholten IDs; korrekte Summierung mehrerer Sitzungen je Ladepunkt. `summeStimmt`: `true` bei wertgleichen, aber unterschiedlich skalierten Beträgen (`12.5` vs. `12.50`, siehe S-11); `false` bei abweichender Summe; Verhalten bei leerer Zeilenliste. |
| `ReportZeile` | modul-5 | Trägt die akkumulierte Geldsumme je Ladepunkt (`addiere`) und ist paketprivat für Tests aus `MonatsReport` gedacht (siehe API-Kontrakt in `SPEC.md` Abschnitt 3) — ebenfalls ohne eigene Testklasse. | Mehrfaches `addiere` erhöht `anzahlSitzungen` und summiert `getSumme()` korrekt (Vergleich per `compareTo`); Ausgangszustand ist `0` Sitzungen und `BigDecimal.ZERO`. |
| `PaketIndex` | modul-2/mcp-server | Enthält Geschäftslogik mit Verzweigung über den Freigabestatus (`Version.istFreigegeben()`) sowie Such-/Auflösungslogik (`finde`, `suche`), die von den Werkstatt-Werkzeugen (u. a. der noch zu bauenden `freigabe_pruefen`-Übung) direkt genutzt wird. Aktuell nur indirekt über `McpServerTest` (ein Paket, ein Werkzeug) mitgetestet — keine eigene Testklasse für die Indexlogik selbst. | `finde` case-insensitive und mit Trimmen der Koordinate; `finde` liefert `Optional.empty()` bei unbekannter Koordinate und bei `null`; `suche` findet Teilstring-Treffer case-insensitive; `istFreigegeben()` unterscheidet `"freigegeben"` von anderen Statuswerten (`"gesperrt"`, `"nur-test"`, unbekannt); `ausJson` mit fehlenden/optionalen Feldern (z. B. leere `cves`-Liste, fehlender `hinweis`). |

---

## Top 3 für diese Woche

1. **`AbrechnungsService`** — hier steckt das größte fachliche und finanzielle Risiko: ein Test, der laut eigenem Kommentar wissentlich einen falschen Wert erwartet, plus mindestens zwei konkrete SPEC-Abweichungen (Zeitzone, lineare Nachttarif-Aufteilung), die bislang durch keinen Test aufgedeckt werden.
2. **`MonatsReport`** — aggregiert die Beträge aus `AbrechnungsService` über den ganzen Monat und hat aktuell null Testabdeckung; ein Fehler in der Zuordnung je Ladepunkt (S-10) oder der Kontrollrechnung (S-11) würde erst der Betriebsleitung auffallen, nicht der Pipeline.
3. **`PaketIndex`** — die einzige Stelle, an der der Freigabestatus von Paketen ausgewertet wird; die noch zu bauende Übung `freigabe_pruefen` verlässt sich vollständig auf diese Logik, die selbst aber ungetestet ist.
