# Modul 5 — Lab D: Der geführte Legacy-Sprint

**Zeit:** ca. 45 Minuten · **Sozialform:** Zweierteams

> **Verstehen → Absichern → Verändern → Verifizieren.** Immer im Kreis, immer in
> dieser Reihenfolge. Wer bei Schritt 3 anfängt, zementiert Fehler nur schneller.

---

## Die Ausgangslage

`modul-5` ist der Abrechnungskern der Ladepark-Suite. Über sechs Jahre gewachsen,
mehrere Autoren, ein einziger Test. Der Code berechnet Geld. Aus Zeitangaben.

Was ihr habt:

| Datei | Was drinsteht |
|---|---|
| `SPEC.md` | Die **fachliche Wahrheit**. Freigegeben vom Rechnungswesen. |
| `src/main/java/.../` | Der Bestand. Tut nicht, was in SPEC.md steht. |
| `tickets/VW-4715.md` | Ein gemeldeter Fehler aus dem Betrieb. |
| `AGENTS.md` (Repo-Wurzel) | Die Konventionen des Teams. |

**Die Regel des Tages:** Wenn Code und `SPEC.md` sich widersprechen, ist der Code falsch.
Nicht die Spec.

---

## D1 — Die Landkarte

**10 Minuten**

Bevor ihr irgendetwas anfasst: versteht, was da ist.

**Auftrag**

Lasst Copilot eine Landkarte des Moduls erzeugen und **als Datei einchecken**:
`modul-5/NOTES.md`.

Fragen, die tragen — deutlich besser als „erkläre mir den Code":

```
Erstelle eine Modul-Landkarte für modul-5: Klassen, ihre Verantwortlichkeiten,
ihre Abhängigkeiten untereinander. Nenne für jede Klasse in einem Satz, was sie
tut, und in einem zweiten, was sie NICHT tut.
```

```
Welche Annahmen macht dieser Code über seine Umgebung?
Zeitzone, Datentypen, Wertebereiche, Reihenfolge von Aufrufen.
```

```
Wo wird Geld gerechnet, und in welchem Datentyp?
Wo wird Zeit gerechnet, und mit welchen Klassen?
```

**Nützlich dabei:** `/add-dir` zieht Nachbarmodule in den Kontext, wenn ihr sie
wirklich braucht. Gezielte Pfade statt „schau dich mal um" — jedes zusätzliche
Verzeichnis kostet Tokens in **jeder** Runde, nicht nur in der ersten.

**Fertig, wenn** `NOTES.md` existiert und mindestens enthält:

* eine Tabelle der Klassen mit Verantwortlichkeit
* eine Liste der Umgebungsannahmen
* **drei Stellen, an denen ihr ein ungutes Gefühl habt** — ohne dass ihr schon wisst,
  warum

**Die Regel dahinter:** Verständnis-Artefakte werden eingecheckt. Was nur im Chatverlauf
steht, ist morgen weg — und der Kollege, der das Modul im November anfasst, fängt bei
null an.

---

## D2 — Das Sicherheitsnetz: Soll-Tests aus der Spec

**15 Minuten · das Herzstück des Moduls**

### Das Anti-Pattern zuerst

> „Schreibe Tests für diese Klasse."

Das ist der Standardprompt, und für Modernisierung ist er **gefährlich**. Er erzeugt
Tests, die das **heutige** Verhalten festschreiben — Bugs inklusive. Danach habt ihr ein
Sicherheitsnetz mit Löchern an genau den Stellen, an denen ihr eines gebraucht hättet.

Schaut euch `AbrechnungsServiceTest` an. Genau das ist dort 2022 passiert: Der
Erwartungswert wurde an das Ist-Verhalten angepasst, weil der Test sonst rot war. Der
Kommentar steht noch drin. Der Test ist seitdem grün und wertlos.

### Die Technik

1. **Die fachliche Vorgabe ist die einzige Quelle.** `SPEC.md`, sonst nichts.
2. **Frische Session.** `/clear`. Die Implementierung wird **nicht gelesen**.
3. **Erst die Testfall-Liste, dann der Code.** Die Liste reviewt ihr.
4. **Rote Tests sind Befunde über den Bestand, keine Fehler der Tests.**

Der Prompt, wörtlich:

```
Lies ausschließlich modul-5/SPEC.md. Öffne KEINE Datei unter modul-5/src.

Erzeuge zuerst nur eine LISTE von Testfällen, die die Regeln S-1 bis S-8
vollständig abdecken, inklusive der Referenzbeispiele B1 bis B13 aus
Abschnitt 4. Für jeden Fall: Regel, Eingabe, erwartetes Ergebnis.

Die Regeln S-10 und S-11 zum Monatsreport lässt du weg.

Schreibe noch keinen Code.
```

> Warum S-10 und S-11 hier ausgeklammert sind: Der Monatsreport ist Gegenstand von
> D3 und der Aufgabenkarte VW-4713. Wer ihn jetzt schon durchtestet, nimmt sich die
> nächste Übung vorweg.

**Reviewt die Liste.** Fehlt eine Regel? Fehlen die Grenzfälle — genau 150,0 kW,
genau 240 Minuten, genau 0,1 kWh? Fehlt die Sommerzeit? Erst wenn die Liste steht:

```
Setze diese Liste als JUnit-5-Tests um, in
modul-5/src/test/java/de/voltwerk/abrechnung/SpecKonformitaetTest.java.

Der API-Kontrakt steht in Abschnitt 3 von SPEC.md. Du brauchst src/main nicht
zu öffnen. Beträge mit compareTo vergleichen, nicht mit equals.
Ortszeiten über ZoneId.of("Europe/Berlin") konstruieren.
```

Dann:

```bash
mvn -q -pl modul-5 -am test
```

### Und jetzt der wichtige Teil

**Es wird rot. Das ist der Erfolg, nicht der Fehlschlag.**

Haltet jeden roten Test als **Befund** fest, in `modul-5/BEFUNDE.md`:

| Nr. | Spec-Regel | Erwartet | Beobachtet | Bewertung |
|---|---|---|---|---|
| F1 | S-? | | | Befund über den Code / Fehler im Test |

Die letzte Spalte ist die eigentliche Arbeit. Nicht jeder rote Test ist ein Codefehler —
manchmal hat das Modell die Spec falsch gelesen. **Diese Unterscheidung kann euch
niemand abnehmen.** Sie ist der Grund, warum ihr im Raum seid.

> **Kalibrierung:** `AbrechnungsService` weicht an **acht** Stellen von den Regeln
> S-1 bis S-8 ab. Findet ihr alle acht — ohne eine neunte zu erfinden?
>
> **Sechs** davon lassen sich mit **je einem** Referenzbeispiel aus Abschnitt 4
> eindeutig belegen. Die restlichen zwei nicht: der fehlende **12-EUR-Deckel** (S-3)
> und der fehlende **Mindestbetrag** (S-4) werden in den vorhandenen Beispielen von
> je einem anderen Fehler überlagert. Für die beiden braucht ihr **eigene**
> Testfälle, die ihr aus der Spec ableitet.
>
> Das ist kein Zufall, sondern die Lektion: **Referenzbeispiele sind Stichproben,
> keine Testsuite.** Wer nur die dreizehn Beispiele umsetzt, findet sechs von acht
> Fehlern und hält sich für fertig.

**Fertig, wenn** `BEFUNDE.md` gepflegt ist und ihr für jeden roten Test sagen könnt,
ob er ein Befund oder ein Testfehler ist.

**Merksatz:** Grün heißt „entspricht der Vorgabe", nicht „entspricht dem Bestand".

---

## D3 — Bugjagd

**10 Minuten**

`modul-5/tickets/VW-4715.md` ist ein echtes Ticket aus dem Betrieb. Es enthält alles,
was ihr braucht, und eine falsche Fährte.

**Der Ablauf, nicht verhandelbar:**

1. Stacktrace und Kontext hinein — Hypothesen heraus. Lasst euch **mehrere** geben.
2. Prüft jede Hypothese gegen die Beobachtung. *„Funktioniert bis 96, kaputt ab 1000."*
   Eine Hypothese, die das nicht erklärt, ist falsch — egal wie plausibel sie klingt.
3. **Minimaler Reproduzierer als Test.** Rot.
4. Erst jetzt der Fix.
5. Grün.

**Der Verstärker:** Bevor ihr den Fix baut, lasst ihn zerlegen:

```
/rubber-duck
```

Der Rubber-Duck-Agent liefert eine unabhängige Kritik an eurer aktuellen Arbeit — und
er ist bewusst unangenehm. Nehmt das Gegenargument ernst. Wenn es euch überzeugt, habt
ihr eine Stunde gespart. Wenn nicht, wisst ihr jetzt, warum euer Weg richtig ist. Beides
ist mehr wert als das, was ihr vorher hattet.

**Fertig, wenn** der Reproduzierer im Repo liegt, `mvn -q test` grün ist und ihr in
einem Satz erklären könnt, **warum die Grenze genau dort liegt, wo sie liegt.**

---

## D4 — Modernisieren, ohne ein Muster zu nennen

**10 Minuten · und die Übung, an die ihr euch erinnern werdet**

In `AbrechnungsService.berechne(...)` steht ein Block mit dieser Markierung:

```java
// MODERNISIEREN (Lab D4): Diese Kaskade wächst mit jedem neuen Tarif weiter.
```

**Die Regel dieser Übung: Ihr dürft kein Entwurfsmuster benennen.**

Nicht „Strategy". Nicht „Polymorphie". Nicht „Sealed Interface". Nicht „State Pattern".
Kein Musternamen im Prompt. Wer eines nennt, hat verloren.

**Warum?** Weil ihr Probleme lösen wollt, keine Schlagworte erfüllen. Ein Prompt, der ein
Muster nennt, bekommt das Muster — auch dann, wenn es nicht passt. Ein Prompt, der die
**Kräfte** beschreibt, bekommt eine Lösung.

Beschreibt also die Kräfte:

```
Refactoriere den markierten Block in AbrechnungsService.

Kräfte:
- Ein neuer Tarif soll hinzukommen können, ohne dass berechne() länger wird.
- Das Verhalten je Tarif gehört an einer Stelle, nicht verteilt über mehrere Methoden.
- Der Nachttarif braucht Zugriff auf die Ladezeiten, die anderen nicht.
- Das Verhalten darf sich nicht ändern: die Tests aus D2 müssen danach exakt
  dasselbe Ergebnis liefern wie davor.

Halte dich an AGENTS.md.
```

**Dann die Auflösung.** Schaut euch an, was entstanden ist, und beantwortet:

1. **Welches Muster ist entstanden?** (Es hat mit hoher Wahrscheinlichkeit einen Namen.)
2. **Woher kam es?** Ihr habt es nicht genannt. Also: aus welcher Zeile in `AGENTS.md`
   stammt es? Lest Abschnitt 3.2 noch einmal.
3. **Passt es?** Oder ist es überdimensioniert für drei Tarife? Das ist eine ernst
   gemeinte Frage — die richtige Antwort kann „das war zu viel" sein.

**Der Effekt, um den es geht:** Der Vorschlag trägt euer Muster, obwohl niemand es
benannt hat. Konventionen und Beispielcode im Repo wirken als Few-Shot. Das Review prüft
**Angemessenheit**, nicht Musternamen.

Genau deshalb ist `AGENTS.md` die wichtigste Datei in eurem Repo — wichtiger als jeder
Prompt, den ihr euch merkt.

**Fertig, wenn** `mvn -q -pl modul-5 -am test` **genau dieselben** Tests grün und rot
zeigt wie vor dem Umbau. Ein Refactoring, das die Befundlage ändert, war keins.

---

## Wenn Zeit bleibt

**Der ehrliche Zustand des Moduls.** Die acht Abweichungen aus D2 sind noch nicht
behoben — das war nicht der Auftrag. Schreibt aus `BEFUNDE.md` **drei Tickets** im
Format der Karten aus `modul-4/aufgabenkarten/`. Jedes mit Abnahmekriterien und
Testbefehl. Zwei davon sind delegierbar, eines nicht. Welches, und warum?

**Java-Upgrades mit Plan.** Für größere Sprünge — JDK-Versionen, Jakarta EE, Spring Boot —
gibt es Werkzeuge, die Upgrade-Blocker analysieren, einen Plan ins Repo legen und
OpenRewrite-Transformationen fahren. Zwei davon laufen direkt in der CLI:

```bash
copilot plugin marketplace browse awesome-copilot
copilot plugin install github-copilot-modernization@awesome-copilot
copilot plugin install java-modernization-studio@awesome-copilot
```

`github-copilot-modernization` fährt Java-Upgrades (8 → 21, Spring Boot 2.x → 3.x) über
eine Hierarchie aus Orchestrator, Koordinatoren und Executors und kann dabei ein
Regelwerk eurer Organisation einbeziehen. `java-modernization-studio` ist die geführte
Oberfläche dazu: Readiness, Assessment, priorisierter Plan, Validierungs-Gates.

**Fasst das heute nicht an, wenn ihr in D1 bis D4 noch nicht durch seid.** Ein
Modernisierungslauf ohne die Soll-Tests aus D2 ist genau der Fehler, den dieses Modul
verhindern soll.

**Die Einordnung ist wichtiger als das Werkzeug:** So etwas beschleunigt **Schritt 3**
der Schleife. Landkarte und Soll-Tests bleiben eure Aufgabe. Ein Werkzeug, das ohne
Sicherheitsnetz transformiert, macht die Fehler nur schneller.

---

## Gruppenarbeit — Euer Modernisierungs-Backlog

**10 Minuten · Vierergruppen**

Vorlage: **[`modul-5/gruppenarbeit-backlog-canvas.md`](gruppenarbeit-backlog-canvas.md)**

**Der Auftrag:** Sammelt reale Kandidaten aus **euren** Systemen und ordnet jeden in die
Schleife ein:

* Wo fehlt noch **Verständnis** (Schritt 1)?
* Wo fehlen **Soll-Tests** (Schritt 2)?
* Was ist reif für den **Umbau** (Schritt 3)?

**Top 3 pro Gruppe**, jeweils mit dem konkreten ersten Schritt, dem passenden Werkzeug
von heute und einem Namen.

**Ablauf**

* 7 Minuten sammeln und einordnen
* 3 Minuten Galerie-Rundgang: die Backlogs hängen nebeneinander, jeder liest mit.
  Sucht euch einen Kandidaten der Nachbargruppe und beantwortet schriftlich:
  *warum steht der in Schritt X und nicht in Schritt Y?*

**Der Fehler, den fast jede Gruppe macht:** Kandidaten in Schritt 3 einsortieren, weil
der Code hässlich aussieht — obwohl niemand die Spec kennt. Das ist Schritt 1.
**Hässlich ist kein Reifegrad.**

**Ergebnis:** Kandidat 1 wird Tag 5 eurer Sieben-Tage-Liste. Nicht alle drei. Einer.

---

## Checkpoint für das Plenum

Jedes Team bringt mit:

1. **Ein Aha aus D2** — welcher Befund hat euch überrascht?
2. **Die Grenze aus D3** — warum genau dort?
3. **Das Muster aus D4** — welches ist entstanden, und aus welcher AGENTS.md-Zeile?
4. Und die unbequeme Frage: **Wie viele eurer eigenen Module haben eine SPEC.md?**
