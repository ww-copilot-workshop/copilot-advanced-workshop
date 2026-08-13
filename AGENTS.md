# AGENTS.md — Arbeitsregeln für Agenten in diesem Repository

Dieses Dokument gilt für jeden Agenten, der in diesem Repo arbeitet: Copilot CLI,
Copilot Coding Agent, Agentic Workflows, IDE-Agent. Menschen dürfen auch mitlesen.

> **Workshop-Hinweis:** Diese Datei ist bewusst Teil der Übung. In Modul 5 sollt ihr
> Code modernisieren, **ohne ein Entwurfsmuster zu benennen**. Was dabei entsteht,
> entsteht aus diesen Konventionen — nicht aus dem Wort „Strategy" im Prompt.

---

## 1. Projekt

**Voltwerk Ladepark-Suite** — Verwaltung, Telemetrie und Abrechnung öffentlicher
Ladeparks. Java 21, Maven Multi-Modul, keine Frameworks im Kern.

| Modul | Inhalt |
|---|---|
| `modul-1` | Telemetrie-Parser und Auswertungen |
| `modul-2/mcp-server` | Werkstatt-MCP-Server (Artifactory-Abfragen) |
| `modul-5` | Abrechnungskern (gewachsen, wird gerade modernisiert) |

## 2. Bauen und Testen

```bash
mvn -q test                 # alles
mvn -q -pl modul-5 -am test # nur ein Modul plus Abhängigkeiten
mvn -q verify               # vor jedem Push
```

**Regel:** Wer Produktionscode ändert, führt vorher und nachher `mvn -q test` aus und
nennt das Ergebnis im PR. „Sollte laufen" ist kein Ergebnis.

## 3. Java-Konventionen

### 3.1 Typen und Zustand

* **Komposition vor Vererbung.** Abstrakte Basisklassen nur, wenn es einen echten
  gemeinsamen Zustand gibt.
* **Unveränderlich als Standard.** Neue Datenträger sind `record`. Felder `final`,
  Sammlungen defensiv kopieren oder unveränderlich zurückgeben.
* **Kein `null` als Rückgabewert.** Entweder `Optional`, eine leere Sammlung oder ein
  eigener Ergebnistyp. `null` als Parameter ist ein Programmierfehler und wird mit
  `IllegalArgumentException` quittiert.
* **Keine öffentlichen veränderlichen statischen Felder.** Nie. Auch nicht kurz.

### 3.2 Verzweigungen

* **Guard Clauses statt Verschachtelung.** Sonderfälle oben abfangen, dann geradeaus
  weiterschreiben. Mehr als zwei Einrückungsebenen in einer Methode sind ein Befund.
* **Keine `if`/`else`-Kaskade und kein `switch` über einen Typ-Enum, wenn sich hinter
  jedem Zweig eigenes Verhalten verbirgt.** In diesem Fall gehört das Verhalten an den
  Typ. Zulässige Werkzeuge dafür: `sealed interface` plus `record`-Implementierungen,
  Pattern-Matching-`switch` über den versiegelten Typ, oder eine Abbildung von Schlüssel
  auf Verhalten. Der Punkt ist: **ein neuer Fall darf keine bestehende Methode aufblähen.**
* **Kein Fallthrough**, keine leeren `catch`-Blöcke, kein `catch (Exception)` ohne
  Weitergabe oder bewusste, kommentierte Entscheidung.

### 3.3 Geld und Zeit

* **Geld ist `BigDecimal`.** Nie `double`, nie `float`. Rundung immer explizit mit
  `RoundingMode`, nie implizit.
* **Zeit ist `java.time`.** `Instant` für Zeitpunkte, `ZonedDateTime` für fachliche
  Ortszeiten, `Duration` für Dauern. `java.util.Date`, `Calendar` und `SimpleDateFormat`
  sind in Neucode verboten und in Altcode Migrationskandidaten.
* **Zeitzonen sind fachlich.** Nie `ZoneId.systemDefault()`. Die fachlich gültige Zone
  steht in der Spec.

### 3.4 Sprache

* Bezeichner, Kommentare und Javadoc auf **Deutsch**, wie im bestehenden Code.
* Fachbegriffe der Domäne bleiben deutsch (`Ladesitzung`, `Blockiergebühr`,
  `Freigabe`). Technische Begriffe bleiben englisch (`record`, `Optional`, `Stream`).

## 4. Tests

* **JUnit 5**, `assertEquals` mit sprechender Meldung bei nicht offensichtlichen Fällen.
* Ein Test prüft **eine** Aussage. Testname beschreibt das erwartete Verhalten, nicht die
  Methode: `blockiergebuehrIstBeiVierStundenNochNull()`, nicht `testBerechne3()`.
* **Beträge werden mit `compareTo` verglichen**, nie mit `equals` — `12.5` und `12.50`
  sind derselbe Betrag.
* **Tests werden gegen die Spezifikation geschrieben, nicht gegen den Bestand.** Wenn ein
  Test rot wird, ist das zunächst ein Befund über den Code, kein Fehler des Tests.
  Erwartungswerte dürfen **nie** an das Ist-Verhalten angepasst werden, um grün zu werden.
  (Ja, das ist im Repo schon einmal passiert. Siehe `modul-5`.)

## 5. Commits und Pull Requests

Die Commit-Konventionen stehen in [`docs/commit-konventionen.md`](docs/commit-konventionen.md).
Ein PR enthält: was, warum, wie getestet. Nicht: eine Aufzählung der geänderten Dateien —
die steht im Diff.

## 6. Regeln für Agenten im Besonderen

1. **Erst verstehen, dann ändern.** Bei fremdem Code zuerst eine Landkarte erzeugen und
   als Markdown im Repo ablegen. Was nur im Chatverlauf steht, ist morgen weg.
2. **Kein Fix ohne roten Test davor und grünen Test danach.**
3. **Die Spec schlägt den Code.** Weicht der Code von `SPEC.md` ab, ist das ein Befund und
   wird berichtet — nicht stillschweigend „mitgefixt", solange es nicht der Auftrag ist.
4. **Keine Secrets im Repo.** Keine Tokens, keine Passwörter, keine internen Hostnamen in
   Beispielcode. Wer welche findet, meldet sie im PR statt sie zu verschieben.
5. **Keine neuen Abhängigkeiten ohne Freigabe.** Der Artifactory-Proxy ist die einzige
   Quelle; ob eine Version freigegeben ist, beantwortet der Werkstatt-MCP-Server.
6. **Kleine Schritte.** Ein PR, ein Thema. Ein Refactoring und ein Bugfix gehören nicht in
   denselben Commit.
