# Modul 4 — Lab C: Delegieren und automatisieren

**Zeit:** 60 Minuten angesetzt · **Sozialform:** Zweierteams

> Der Agent ist ein Gast, der sich an eure Hausordnung hält.
> Heute schreibt ihr die Hausordnung — und schaut nach, ob er sich daran gehalten hat.

---

## Vorab: dieses Lab läuft in zwei Varianten

Ob ihr **selbst** in die Cloud delegieren könnt, hängt an eurer Organisation: Der Copilot
Coding Agent muss für das Übungs-Repo freigeschaltet sein, und ihr braucht dort
Schreibrechte. Das klärt euer Trainer am Morgen — es ist keine Frage eures Könnens.

| | **Variante Demo** (Standard) | **Variante Vollgas** |
|---|---|---|
| C1 Karten bewerten | ihr, am Rechner | ihr, am Rechner |
| Delegation auslösen | **Trainer, vorne** | ihr, selbst |
| C2 Review am Gate | ihr, am echten PR im Browser | ihr, an eurem eigenen PR |
| C3 Workflow bauen | ihr, lokal | ihr, lokal |
| C4 Portfolio | entfällt | Kür |

**Der Lerninhalt ist in beiden Varianten derselbe.** Das Auslösen dauert dreißig
Sekunden und ist der langweiligste Teil des Moduls. Interessant sind das Ticket davor
und das Gate danach — und beides macht ihr in jedem Fall selbst.

> **Zur Zeit, ehrlich:** Angesetzt sind 60 Minuten, durchgespielt sind es eher 90.
> Plant so: **C1, dann C3, dann C2.** Die Wartezeit auf den Agenten fällt in C3 hinein
> und arbeitet damit für euch. C2 macht ihr, sobald der PR da ist. Was als Kür
> markiert ist, ist Kür.

---

## Die drei Autonomiestufen — und wann welche

| Stufe | Wo läuft es | Wann |
|---|---|---|
| **Interaktiv** | Euer Rechner, jede Aktion bestätigt | Ihr lernt gerade etwas über den Code |
| **Autopilot** | Euer Rechner, Aktionen laufen durch | Abgegrenzte Aufgabe, ihr schaut zu, Sandbox an |
| **`/delegate`** | GitHub, auf einem GitHub-hosted Runner | Ihr wollt die Aufgabe loswerden und weiterarbeiten |

Der zentrale Unterschied: **Autopilot ist euer Rechner. `/delegate` ist Cloud.** Bei
`/delegate` legt Copilot einen Branch an, öffnet einen **Draft-PR**, arbeitet im
Hintergrund weiter — auch wenn ihr euren Laptop zuklappt — und fordert am Ende ein
Review an.

---

## C1 — Das gute Ticket

**20 Minuten · Hands-on, braucht keine Cloud**

Das hier ist der Teil, der über Erfolg oder Misserfolg einer Delegation entscheidet —
lange bevor irgendein Agent startet.

In `modul-4/aufgabenkarten/` liegen vier Karten. **Drei sind gut. Eine ist Müll.**
Findet heraus, welche.

| Karte | Thema |
|---|---|
| `C1-vw-4711.md` | Blockiergebühr deckeln |
| `C1-vw-4712.md` | Fehlversuch-Regel korrigieren |
| `C1-vw-4713.md` | Telemetrie-Export absichern |
| `C1-vw-4714.md` | Abrechnung modernisieren |

**Auftrag**

1. **Lest alle vier.** Bewertet jede an den offiziellen Kriterien:
   * Klare Beschreibung des Problems oder der geforderten Arbeit
   * **Vollständige** Abnahmekriterien — woran erkennt man eine gute Lösung?
     Gehören Tests dazu?
   * Hinweise, welche Dateien betroffen sind

2. **Einigt euch, welche Karte die schlechte ist, und schreibt sie um**, bis sie die
   Kriterien erfüllt. Eine neue Datei, das Original bleibt liegen; hängt `-repariert`
   an den Namen. Ihr braucht beide gleich noch für den Vergleich.

   ```text
   modul-4/aufgabenkarten/
   ├── C1-vw-XXXX.md              Original, bleibt unverändert
   └── C1-vw-XXXX-repariert.md    eure Fassung, neu
   ```

   `XXXX` ist die Nummer eurer Karte — die setzt ihr überall ein, wo sie hier steht.

   **Ihr dürft die Karte aufteilen.** Wenn eine Karte vier Aufträge bündelt, ist das
   Zerlegen oft schon die halbe Reparatur — und die ehrlichste Antwort auf die Frage,
   was daran nicht delegierbar ist. Es bleibt trotzdem **eine** Datei; die Teil-Tickets
   stehen darin als Überschriften untereinander (`# VW-XXXXa …`, `# VW-XXXXb …`).
   `VW-XXXXa` ist also eine Überschrift **in** der Datei, kein zweiter Dateiname.

   > **Von Hand schreiben, nicht von Copilot schreiben lassen.** Der Grund steht ein
   > paar Zeilen weiter unten in der Liste „Was ihr NICHT delegiert": Das hier ist eine
   > **Lernaufgabe**. Wer sie delegiert, bekommt eine hübsche Karte und hat nichts
   > verstanden. Copilot kommt gleich dran, in Schritt 3, als Prüfer statt als Autor.

3. **Die Probe, maschinell.** Jetzt darf Copilot ran — aber nur fragen, nicht
   schreiben. **Setzt euren echten Dateinamen ein**, und startet aus der Repo-Wurzel:

   ```bash
   copilot -p "Lies modul-4/aufgabenkarten/C1-vw-XXXX-repariert.md. Du darfst zur \
   Beurteilung in den Quellcode schauen. Nenne mir danach NUR die Fragen, die du zu \
   DIESEM Auftrag stellen müsstest. Keine Anmerkungen, keine Randnotizen, keine \
   Beobachtungen zu anderen Stellen im Code - ausschließlich die Fragen. Ändere nichts." \
     --deny-tool 'write'
   ```

   Der Prompt ist absichtlich so pedantisch. Ohne den Satz *„du darfst in den Quellcode
   schauen"* fragt der Agent Dinge, die im Code stehen — und ihr haltet das für eine
   Lücke in eurer Karte, obwohl es keine ist. Ohne *„keine Randnotizen"* hängt er noch
   drei Beobachtungen an, die mit eurem Ticket nichts zu tun haben. **Auch ein
   Prüfauftrag ist ein Auftrag und wird genauso präzise wie ein Ticket.**

   Und warum das `--deny-tool 'write'`, wo im Prompt doch „Ändere nichts" steht? Wir
   haben beides ausprobiert: **Ohne Flag hat sich der Agent ebenfalls daran gehalten.**
   Der Satz im Prompt ist trotzdem nur eine *Bitte*, das Flag ist eine *Regel*. Eine
   Bitte, die zufällig befolgt wurde, ist keine Kontrolle — genau das war die Lektion
   aus Modul 1. Nehmt das Flag, nicht weil es diesmal nötig war, sondern weil ihr sonst
   nicht wisst, ob es nötig gewesen wäre.

   > **`Permission denied`, und der Agent fängt an zu suchen?** Dann findet er die
   > Datei nicht. Zwei Ursachen, beide häufig: Ihr steht im **falschen Verzeichnis**
   > (der Pfad ist relativ zum Startordner — startet aus der Repo-Wurzel), oder die
   > Datei liegt **außerhalb seines Vertrauensbereichs** (eine Karte unter `/tmp`
   > bekommt er nicht zu sehen, auch mit absolutem Pfad nicht).
   >
   > Und jetzt schaut euch an, was er in dem Moment getan hat: Er hat **nicht**
   > gemeldet, dass die Datei fehlt. Er hat angefangen, sie zu **suchen** — Glob, `ls`,
   > Verzeichnislisten. Im `-p`-Modus kann er dafür niemanden fragen, also bricht er
   > ab. Mit den Rechten hätte er weitergesucht und irgendeine Datei genommen, die
   > passend aussah.
   >
   > Das ist dasselbe Verhalten wie bei einer lückenhaften Karte: **Er fragt nicht
   > nach, er füllt die Lücke selbst.** Hier seht ihr es nur, weil eine Berechtigung
   > im Weg stand.

   **Jede Frage, die zurückkommt, ist eine Lücke in eurer Karte.** Achtet dabei weniger
   auf die Anzahl als auf die **Art**: Fragen zum *Idiom* („welche API soll ich
   nehmen?") sind harmlos. Fragen zur *Absicht* („was soll überhaupt herauskommen?")
   sind der Befund. Kommt auch nur eine davon zurück, ist die Karte noch nicht fertig.

   Beiläufig noch eine Zahl für Modul 3: Derselbe Lauf kostete beim ersten Mal
   **21,2 Credits**, beim zweiten **12,7** — gleicher Auftrag, gleiche Datei. Der
   Unterschied ist der Cache. Schaut es euch im Ausgabe-Fuß selbst an.

4. **Der letzte Handgriff: die Delegation.** Eine der **guten** Karten geht in die
   Cloud — wie, steht im nächsten Abschnitt. In der Variante Demo macht das der
   Trainer vorne.

**Kür, wenn Zeit bleibt.** Zwei Proben, die den Unterschied in eine Zahl übersetzen:

* **Menschlich:** Lest eure reparierte Karte einem anderen Team vor, ohne Kontext.
  Wenn Rückfragen kommen, kommen sie auch vom Agenten. Nur merkt der es nicht und rät
  stattdessen.
* **Maschinell, gegen das Original:**

  ```bash
  copilot -p "Lies modul-4/aufgabenkarten/C1-vw-XXXX.md. Beurteile AUSSCHLIESSLICH \
  diese Karte als Auftrag. Schau NICHT in den Quellcode und nicht in andere Dateien. \
  Nenne mir NUR die Fragen, die du stellen müsstest, bevor du sie umsetzen könntest. \
  Ändere nichts." \
    --deny-tool 'write'
  ```

  > **Warum dieser Prompt anders ist als der aus Schritt 3** — und das ist kein
  > Versehen: Eine reparierte Karte darf sich auf den Code stützen, deshalb bekommt
  > der Agent dort Einblick. Das Original soll zeigen, was passiert, wenn eine Karte
  > **allein tragen muss**. Gebt ihr ihm hier den Quellcode dazu, beurteilt er nicht
  > mehr die Karte, sondern den Code — und nimmt damit Modul 5 vorweg.

  Bei uns kamen **dreizehn bis vierzehn Fragen** zurück — die Zahl schwankt von Lauf
  zu Lauf, die Größenordnung nicht. Eure reparierte Karte sollte deutlich darunter
  liegen; wichtiger ist, dass keine der verbliebenen Fragen nach der **Absicht** fragt.

**Denkt daran:** Ein Issue, das ihr dem Agenten zuweist, ist ein **Prompt**. Lest eure
Karte noch einmal mit dieser Brille. Würdet ihr sie so in ein Chatfenster tippen?

**Außerdem:** In der schlechten Karte stecken **zwei Sätze, die zusammen nicht
erfüllbar sind**. Findet sie. Das ist kein Formulierungsproblem, sondern ein
handfester Widerspruch — und der Agent würde ihn nicht bemerken, sondern raten.

**Fertig, wenn** eure reparierte Karte im Ordner liegt, ihr den Widerspruch benennen
könnt und in einem Satz sagen könnt, welcher Teil der ursprünglichen Karte **gar
nicht** delegierbar ist — und warum nicht.

### Woher wisst ihr, was in ein Ticket gehört?

**Aus den drei guten Karten.** Sie sind nicht nur Beispiele, sie sind die Vorlage. Legt
eine davon neben eure Reparatur und geht die Abschnitte durch: Ziel · Hintergrund ·
fachliche Vorgabe · Abnahmekriterien · betroffene Pfade · Testbefehl · Hinweise. Fehlt
ein Abschnitt bei euch, fehlt er dem Agenten auch. Nach der Reparatur lohnen zwei
weitere Rubriken besonders: **Art der Änderung** und **Nicht Teil dieses Tickets**.

Die Faustregel: **Ein delegierbares Ticket beantwortet drei Fragen, ohne dass jemand
nachfragen muss.** Was soll anders sein? Woran erkenne ich, dass es stimmt? Wo muss ich
hinschauen?

<details>
<summary><b>Ein Beispiel</b> — dasselbe Ticket vorher und nachher</summary>

Bewusst keine der vier Karten, damit es nichts vorwegnimmt.

**Vorher, so kam es rein:**

```markdown
# VW-4699 — Export ist zu langsam

Der Export dauert ewig, Kunden beschweren sich. Bitte schneller machen,
am besten diese Woche noch. Danke!
```

**Nachher kamen dazu:** ein messbares Ziel (unter 10 Sekunden bei 50.000 Zeilen), der
Hintergrund mit Zahlen, die fachliche Vorgabe mit Quelle, prüfbare Abnahmekriterien
**inklusive der Grenze** (was ausdrücklich *nicht* geändert wird), die betroffenen
Pfade, der Testbefehl und die bekannten Fallen.

Aus zwei Sätzen Meinung wurde ein Auftrag, den ein Fremder ausführen kann. Genau das
ist der Unterschied — nicht die Länge.

</details>

<details>
<summary>🚨 <b>Musterlösung C1</b> — nur im Notfall aufklappen</summary>

**Ernsthaft: erst wenn ihr feststeckt.** Wer hier zuerst hineinschaut, bekommt eine
fertige Karte und nimmt nichts mit. Der Wert dieser Übung liegt im Streit darüber, was
fehlt — nicht im Ergebnis.

---

**Die schlechte Karte ist `C1-vw-4714.md`.**

Sie hat als einzige keine Abnahmekriterien, keine betroffenen Pfade und keinen
Testbefehl. Sie fällt in zwei der vier Kategorien:

* **Breit und komplex** — sie bündelt vier verschiedene Aufträge: modernisieren,
  auf Java 21 heben, Performance verbessern, Tests nachziehen.
* **Mehrdeutig** — „muss mal aufgeräumt werden", „da geht bestimmt einiges",
  „könnte besser sein". Keine dieser Aussagen ist prüfbar.

**Der Widerspruch:** *„Wichtig ist, dass sich am Verhalten nichts ändert"* steht
neben *„die Tests fehlen ja auch komplett"*. Man kann nicht belegen, dass sich
Verhalten nicht geändert hat, wenn nichts existiert, das es prüft. Der Agent würde
das nicht bemerken — er würde loslegen und hinterher behaupten, alles sei gleich
geblieben.

**Nicht delegierbar ist:** herauszufinden, was der Code überhaupt tun *soll*. Der
Satz „der Kollege ist nicht mehr im Unternehmen" beschreibt fehlendes Wissen, und
fehlendes Wissen kann man nicht delegieren — man kann es nur erarbeiten. Das ist
eine Lernaufgabe für euch, keine Aufgabe für den Agenten.

---

**Die Reparatur beginnt mit dem Teilen.** Aus einer Karte werden drei:

| | Ticket | delegierbar? |
|---|---|---|
| 1 | `Calendar` durch `java.time` ersetzen | **ja** — unten ausgeschrieben |
| 2 | Testabdeckung für den Abrechnungskern aufbauen | nein, erst muss die Vorgabe stehen |
| 3 | Performance messen, dann entscheiden | nein, „könnte besser sein" ist kein Ziel |

Ticket 2 und 3 bleiben bewusst offen: Ticket 2 setzt voraus, dass jemand die
fachliche Vorgabe kennt. Ticket 3 setzt voraus, dass jemand gemessen hat. Beides
ist Arbeit *vor* der Delegation.

**Ticket 1, ausgeschrieben.** Alle drei gehören untereinander in *eine* Datei,
`C1-vw-4714-repariert.md`. Die Buchstaben hinter der Nummer zeigen, dass sie aus
demselben ursprünglichen Ticket stammen — so macht man das auch im Betrieb, wenn ein
Ticket beim Verfeinern zerfällt.

````markdown
# VW-4714a — Calendar in istNachts durch java.time ersetzen

## Ziel

Die Methode `istNachts(...)` in `AbrechnungsService` ermittelt die Stunde mit
`java.time` statt mit `java.util.Calendar`. Sonst ändert sich nichts.

## Hintergrund

`AbrechnungsService` ist die einzige Stelle im Repo, die noch `Calendar`
benutzt. `AGENTS.md` §3.3 führt `Calendar` ausdrücklich als
Migrationskandidaten. Die Umstellung ist klein und in sich abgeschlossen.

## Art der Änderung

**Das ist eine reine Typ-Migration, keine fachliche Änderung.** Die Regeln,
wann Nacht ist, welche Zone gilt und wie gerundet wird, werden in diesem
Ticket weder geändert noch geprüft. Wer beim Umbau eine fachliche
Auffälligkeit bemerkt: nicht anfassen, sondern im PR als Befund notieren.

## Abnahmekriterien

- [ ] `java.util.Calendar` kommt in `AbrechnungsService.java` nicht mehr vor,
      der Import ist entfernt.
- [ ] Die Konstante `RECHEN_ZONE` bleibt **unverändert** — gleicher Name,
      gleicher Typ, gleicher Wert. Sie wird innerhalb der Methode in die
      java.time-Welt gebrückt, nicht umdeklariert.
- [ ] Die Signatur `private boolean istNachts(Date zeitpunkt)` bleibt
      unverändert. Das `Date` wird über `toInstant()` gebrückt.
- [ ] Die Null-Prüfung am Methodenanfang bleibt Zeichen für Zeichen erhalten,
      inklusive Rückgabewert.
- [ ] Die Stundenberechnung liefert für jeden Eingabewert exakt dasselbe
      Ergebnis wie vorher.
- [ ] Nötige `java.time`-Imports dürfen dazukommen. `java.util.Date` und
      `java.util.TimeZone` bleiben.
- [ ] Alle bestehenden Tests laufen unverändert grün. Es werden **weder Tests
      angepasst noch neue hinzugefügt** — die Testabdeckung hat ein eigenes
      Ticket und braucht erst eine fachliche Vorgabe.

## Nicht Teil dieses Tickets

* Jede fachliche Frage: Zonen, Nachtfenster, Rundung, Tarife.
* Das Domänenmodell.
* Die mit `MODERNISIEREN` markierte Stelle in `berechne(...)`.

## Betroffene Pfade

* `modul-5/src/main/java/de/voltwerk/abrechnung/AbrechnungsService.java`,
  ausschließlich die Methode `istNachts(...)` und der Import-Block.

## Testbefehl

```bash
mvn -q -pl modul-5 -am test
```

## Hinweise

* Die Konventionen aus `AGENTS.md` gelten, insbesondere §3.3.
* Dieses Ticket ist bewusst klein.
````

**Diese Karte ist mit dem Werkzeug aus Schritt 3 geprüft.** Zuletzt kamen **drei
Fragen** zurück, und alle drei betrafen die *Umsetzung*: welches `java.time`-Idiom
gemeint ist, wie `RECHEN_ZONE` in die neue Welt gebrückt werden soll, und wo ein
Befund im PR zu notieren ist. Keine einzige Frage danach, **was** der Auftrag will.

Dieselbe Prüfung an der Originalkarte: **sechs Fragen** — und jede davon fragt, was
gemeint ist. „Soll das Ist-Verhalten erhalten bleiben oder das Soll-Verhalten laut
Spec?" „Welcher Umfang ist gemeint?" „Ab wann gilt Performance als besser?"

**Auf die Art der Fragen kommt es dabei mehr an als auf ihre Zahl.** Drei Fragen zum
Idiom kosten euch einen Satz Antwort. Sechs Fragen zur Absicht bedeuten, dass der
Agent ohne Antwort **raten** wird — und er fragt nicht nach, er rät still.

**Und das ist der Zustand, den ihr anstrebt.** Nicht null Fragen — ein Ticket, das
auch die Umsetzungsidiome vorschreibt, schreibt den Code im Ticket. Sondern: keine
Frage mehr, deren Antwort der Agent *raten* müsste.

**Warum das jetzt delegierbar ist:** Ein Ziel statt vier. Eine prüfbare Bedingung
(`Calendar` kommt nicht mehr vor). Eine klare Grenze (nichts anderes anfassen). Und
ein Testbefehl, mit dem der Agent selbst merkt, ob er fertig ist.

</details>

### Was ihr NICHT delegiert

Vier Kategorien, die GitHub ausdrücklich als ungeeignet dokumentiert:

* **Breit und komplex** — repository-übergreifende Refactorings, tiefes Domänenwissen,
  substanzielle Geschäftslogik, große Umbauten mit Anspruch auf Designkonsistenz.
* **Sensibel und kritisch** — produktionskritische Störungen, Security, personenbezogene
  Daten, alles rund um Authentifizierung, Incident Response.
* **Mehrdeutig** — offene Aufgaben, unklare Anforderungen, alles, wo man sich die Lösung
  erst erarbeiten muss.
* **Lernaufgaben** — alles, wo **ihr** etwas verstehen wollt. Der Agent lernt für euch,
  und ihr habt nichts davon.

Die schlechte Karte fällt in mindestens **zwei** dieser Kategorien. In welche?

---

## Dazwischen — die Delegation

Zwei Wege, und sie funktionieren grundverschieden.

### Weg 1 — aus einer laufenden Session: `/delegate`

**Der Sitzungsverlauf ist der Auftrag.** Die Falle direkt nach der Probe aus C1: Der
Prüfbefehl lief mit `copilot -p`, also nicht-interaktiv — der Kontext liegt in **jener**
Session, nicht in eurem Terminal. Am Ende jedes `-p`-Laufs steht deshalb eine
Resume-Zeile. Steigt dort wieder ein, am einfachsten in die zuletzt benutzte Session:

```bash
copilot --continue
```

Und dann, mit dem Kontext im Rücken:

```text
> Was wirst du anfassen, und was ausdrücklich nicht?
[Antwort prüfen — das ist der Moment, der einen halbstündigen Fehllauf verhindert]
> /delegate
```

Optional nehmt ihr einen Ziel-Branch oder einen Zusatzsatz mit:

```text
> /delegate --base entwicklung
> /delegate Halte dich strikt an die Abnahmekriterien, keine Zusatzarbeit.
```

Das ist der eigentliche Vorteil gegenüber einem Chatfenster: **Ihr könnt vorher prüfen,
ob er es verstanden hat.**

### Weg 2 — direkt aus dem Terminal, ohne Session

Hier ist die **Karte** der Auftrag. Kein Vorgespräch, sie muss allein tragen — genau
deshalb war C1 wichtig.

```bash
gh agent-task create -F modul-4/aufgabenkarten/C1-vw-4711.md --follow
gh agent-task list
gh agent-task view TASK-ID
```

`-F -` liest von stdin. Und `--custom-agent abrechnungs-reviewer` benutzt
`.github/agents/abrechnungs-reviewer.md` — damit schickt ihr die Rolle aus **Lab B** in
die Cloud:

```bash
gh agent-task create -F modul-4/aufgabenkarten/C1-vw-4711.md --custom-agent abrechnungs-reviewer
```

> `gh agent-task` gibt es ab gh 2.80.0 und ist Public Preview.

> **Achtung bei Weg 2:** Der Cloud-Agent arbeitet auf dem Stand, der **im Repository**
> liegt — nicht auf eurem Arbeitsverzeichnis. Eine lokal geschriebene, nicht committete
> Karte sieht er nicht. Bei Weg 1 reist der Sitzungskontext mit.

**Worauf ihr achtet, während es läuft** — das ist der eigentliche Inhalt, nicht der
Tastendruck:

* Im **Agents-Tab** auf github.com: die drei Statusstufen *läuft · wartet auf Review ·
  fertig*.
* Im **Setup-Log**: `setup-java`, der Maven-Cache, `dependency:go-offline`. Der Beweis,
  dass die Umgebung aus `copilot-setup-steps.yml` gegriffen hat.
* Die **Wartezeit**: typischerweise zehn bis dreißig Minuten. Genau deshalb macht ihr
  jetzt C3 und kommt später zu C2 zurück.

> **In der Variante Demo** löst der Trainer aus, ihr schaut zu und stellt Fragen. Der
> PR, den ihr in C2 reviewt, ist ein echter Agent-PR — nur nicht eurer. Für das Review
> macht das keinen Unterschied.

---

## C2 — Review am Gate

**15 Minuten · Hands-on**

Der Draft-PR ist der spannende Teil, nicht der Diff. Ihr braucht dafür nur **Leserechte
im Browser**.

**Prüft in dieser Reihenfolge:**

1. **Das Setup-Log.** Sind die Setup-Steps überhaupt gelaufen? Hat `setup-java` die
   richtige Version gezogen? Wurden die Maven-Abhängigkeiten vorab aufgelöst?
2. **Der Testlauf.** Hat der Agent `mvn -q test` wirklich ausgeführt — oder nur
   behauptet, es sei grün? Sucht die Ausgabe im Log.
3. **Die Begründung.** Der Agent beschreibt im PR, was er getan hat. Deckt sich das mit
   dem Diff?
4. **Der Diff selbst.** Erst jetzt.
5. **Die Session-Logs.** Jeder Agent-Commit führt zurück zur Session, in der er
   entstanden ist. Da steht, was der Agent versucht, verworfen und gedacht hat. Die
   ehrlichste Quelle im ganzen PR.

**Drei Dinge, die euch überraschen werden:**

* **Eure eigene Approval zählt nicht.** Wer den Task ausgelöst hat, kann den PR nicht
  freigeben. Ihr braucht ein zweites Augenpaar — genau wie bei Menschen.
* **CI läuft für Agent-PRs standardmäßig nicht automatisch.** Sie muss manuell
  freigegeben werden. Das ist Absicht: sonst führt ein PR aus dem Nichts eure Workflows
  aus.
* **Branch Protection und Review-Pflicht gelten unverändert.** Ein Agent-PR ist ein PR.

**Fertig, wenn** ihr in `modul-4/C2-entscheidung.md` festgehalten habt:

* Merge ja oder nein — **mit Begründung**
* Ein Punkt, an dem der Agent es besser gemacht hat, als ihr erwartet habt
* Ein Punkt, an dem ihr nachbessern musstet
* Credits und Laufzeit für den Messbogen aus Modul 3 — in der Variante Demo nennt euch
  der Trainer die Zahlen aus seinem Lauf

> **Das ist die Übung, die ihr am Montag wirklich braucht.** Delegieren lernt man in
> einer Minute. Zu entscheiden, ob ein Agent-PR gemerged werden darf, ist die Arbeit,
> die bleibt — und die niemand euch abnimmt.

---

## C3 — Einen Agentic Workflow anlegen

**20 Minuten**

Vom Einzeltask zum wiederkehrenden. In `modul-4/workflow-vorlagen/` liegen drei
Workflows **mit Lücken**. Sucht euch einen aus und macht ihn vollständig.

| Vorlage | Läuft wann | Was sie ausgibt | TODOs |
|---|---|---|---|
| `doku-drift.md` | nächtlich | ein Issue | 7 |
| `testluecken-report.md` | wöchentlich | ein Draft-PR | 10 |
| `ci-triage.md` | bei Pull Requests | ein Kommentar | 7 |

Nehmt `doku-drift.md`, wenn ihr euch nicht entscheiden könnt — sie ist die kürzeste.
`ci-triage.md` ist die anspruchsvollste: Sie wird durch **fremde** Pull Requests
ausgelöst, und genau daran hängt ihre schwierigste Aufgabe.

**Jedes TODO in der Vorlage erklärt, was fehlt.** Ihr müsst nichts erraten, nur lesen
und verstehen.

### Vorbereitung

```bash
gh extension install github/gh-aw    # falls noch nicht da
gh aw init --engine copilot          # einmal pro Repo
```

> **`gh aw init` legt eine eigene `copilot-setup-steps.yml` an.** Liegt dort schon eine
> (bei uns: ja), arbeitet unter einem anderen Dateinamen und vergleicht am Ende. Im
> echten Repo führt ihr beide von Hand zusammen — auch das ist realistisch.

### Der Ablauf

```bash
cp modul-4/workflow-vorlagen/doku-drift.md .github/workflows/
# TODOs füllen ...
gh aw compile
```

`gh aw compile` ist euer schnellstes Feedback im ganzen Workshop. Er sagt oft wörtlich,
was er stattdessen erwartet hat. **Benutzt ihn als Übungsschleife, nicht als
Abschlussprüfung** — und ohne `--verbose`, sonst zählt er interne Meldungen als
Warnungen mit.

### Fertig, wenn …

1. `gh aw compile` **null Warnungen** meldet,
2. die Prüfliste im Tipps-Block abgehakt ist,
3. ihr erklären könnt, **warum** euer Workflow genau diese Permissions hat.

> **Punkt 2 ist nicht optional, und hier ist der Grund.** Wir haben es nachgemessen:
> `ci-triage.md` kompiliert **mit allen offenen TODOs** fehlerfrei und ohne Warnung.
> Wer nur auf „0 warnings" schaut, hält eine unfertige Datei für fertig.
> **Ein Compiler prüft Form, kein Verständnis.** Genau deshalb gibt es Reviews — bei
> Workflows wie bei Code.

<details>
<summary><b>Tipps</b> — Prüfliste und die Fallstricke, die euch garantiert treffen</summary>

**Für alle drei:** Der Compiler meldet **pro Lauf nur einen Fehler je Datei**. Nach dem
Fix kann sofort der nächste auftauchen — das ist normal, nicht Pech. Und die `i →`-Zeile
unter einer Fehlermeldung ist gelegentlich unpassend; **verlasst euch auf die erste
Zeile**.

Der `info:`-Hinweis zu `permissions.copilot-requests: write` erscheint bei **jedem**
Compile mit `engine: copilot`. Das ist eine Info, keine Warnung. Maßgeblich ist die
Zeile *„Compiled 1 workflow: 1 succeeded, 0 warnings"*.

---

**`doku-drift.md`**

- [ ] Zeitplan gesetzt? (Zwei Schreibweisen möglich — probiert beide und lest, was der
      Compiler jeweils sagt.)
- [ ] Manueller Auslöser da? Sonst könnt ihr nichts testen.
- [ ] Permissions für **alle** Toolsets? `toolsets: [default]` enthält mehr, als der
      Name vermuten lässt — der Compiler nennt euch die fehlenden beim Namen.
- [ ] `bash` mit einer **echten Liste**, nicht `true`.
- [ ] Anzahl der Issues je Lauf begrenzt?

**`testluecken-report.md`**

- [ ] `java` in der Netz-Allowlist? Ohne den Eintrag löst Maven keine Abhängigkeiten auf.
- [ ] Das Werkzeug zum **Schreiben** von Dateien da? Es gibt dafür genau eines.
- [ ] Maven in der bash-Allowlist — nicht alles freigeben.
- [ ] Der PR ein **Entwurf**? Ein Wort genügt.
- [ ] `if-no-changes` gesetzt? Sonst gibt es jede Woche einen leeren PR.

**`ci-triage.md`**

- [ ] `roles:` steht **unter `on:`**, nicht auf oberster Ebene. Das ist TODO(1),
      das die Vorlage „besonders aufmerksam lesen" nennt — und der Grund steht dort.
- [ ] `actions: read` **und** das Toolset `actions`. Beides, nicht eines.
- [ ] `target` beim Kommentar gesetzt?
- [ ] Höchstens ein Kommentar je Lauf.

---

**Die fünf Fallstricke:**

1. **Fehlende Permission für ein Toolset.** `toolsets: [default]` enthält
   `pull_requests` — also braucht ihr `pull-requests: read`. Häufigster Warnfall.
2. **Write-Permission im Strict-Modus.** `strict` ist standardmäßig an und blockiert
   Write-Permissions **hart** — kein Warnhinweis, ein Fehler. Nicht umgehen, umdenken:
   Der Agent schlägt vor, ein separater Job führt aus.
3. **Tippfehler in `safe-outputs`.** `create-issues` gibt es nicht, `create-issue` schon.
4. **`roles:` an der falschen Stelle.** Es gehört unter `on:`. Der Compiler sagt es
   euch wörtlich.
5. **`timeout-minutes` mit Bindestrich**, nicht mit Unterstrich.

Und einer, der keiner ist: der Hinweis zur token-basierten Inference bei
`engine: copilot`. Erscheint immer, ist kein Fehler.

---

**Und die zwei, die sauber durchkompilieren und trotzdem falsch sind:**

`bash: [":*"]` ist keine Einschränkung, sondern die Erlaubnis für alles — dasselbe wie
`bash: true`, nur unauffälliger geschrieben. Und `add-comment` **ohne** `target`
kommentiert nicht unbedingt dorthin, wo ihr denkt.

Beides meldet der Compiler nicht. Beides findet ein Review.

</details>

<details>
<summary>🚨 <b>Musterlösung</b> — alle drei Vorlagen, nur im Notfall aufklappen</summary>

**Erst wenn ihr feststeckt.** Der Compiler ist die bessere Hilfe: Er sagt euch bei fast
jedem Fehler, was er erwartet hätte. Wer hier zuerst hineinschaut, überspringt genau die
Schleife, um die es geht.

---

## 1 · `doku-drift.md`

````markdown
---
description: Nächtlicher Abgleich zwischen Java-Quellen und Dokumentation

on:
  schedule: daily
  workflow_dispatch:

permissions:
  contents: read
  issues: read
  pull-requests: read

engine: copilot
network: defaults

timeout-minutes: 20

strict: true

tools:
  github:
    toolsets: [default]
  bash: ["find", "ls", "cat", "grep", "git log", "git diff", "git show"]

safe-outputs:
  create-issue:
    title-prefix: "[doku-drift] "
    labels: [documentation, automated]
    max: 1
  missing-tool:
---

# Nächtlicher Doku-Drift-Check

Vergleiche die Java-Quellen dieses Repositories mit der Dokumentation.

Als Dokumentation gelten: `README.md`, `AGENTS.md`, alles unter `docs/`,
sowie `modul-5/SPEC.md`.

## Vorgehen

1. Ermittle die in den letzten sieben Tagen geänderten Java-Dateien.
2. Prüfe für jede geänderte öffentliche Klasse und Methode:
   - Gibt es neue öffentliche API ohne Dokumentation?
   - Verweist die Dokumentation auf Klassen oder Methoden, die es nicht mehr gibt?
   - Weicht `modul-5/SPEC.md` von dem ab, was der Code laut Signatur anbietet?
     (Nur der **API-Kontrakt** in Abschnitt 3 der SPEC ist gemeint — nicht das
     fachliche Verhalten. Für Verhalten ist der Testlücken-Workflow zuständig.)
3. Findest du Drift, erstelle **genau ein** Issue mit einer Tabelle:

   | Datei | Betroffenes Symbol | Art des Drifts | Vorgeschlagene Korrektur |

4. Findest du keinen Drift, erstelle **kein** Issue. Ein leeres Issue ist schlimmer
   als kein Issue.

## Grenzen

Ändere keine Dateien. Das hier ist ein Report.
````

**Die drei Stellen, die man beim Abschreiben übersieht:**

* **`bash:` ist eine Liste konkreter Kommandos**, keine Freigabe. `find`, `ls`, `cat`,
  `grep`, `git log`, `git diff`, `git show` — mehr braucht ein Doku-Abgleich nicht.
* **`max: 1`** macht aus „genau ein Issue" im Prompt eine Zusage der Infrastruktur.
  Ein Prompt ist eine Bitte, `max` ist eine Regel. Dieselbe Unterscheidung wie bei
  `--deny-tool` in C1.
* **`missing-tool:`** lässt den Agenten melden, wenn ihm etwas fehlt, statt sich etwas
  auszudenken. Ohne diesen Eintrag improvisiert er still.

---

## 2 · `testluecken-report.md`

````markdown
---
description: Wöchentlicher Testlücken-Report als Draft-Pull-Request

on:
  schedule: weekly on monday
  workflow_dispatch:

permissions:
  contents: read
  issues: read
  pull-requests: read

engine: copilot

network:
  allowed:
    - defaults
    - java

timeout-minutes: 30

strict: true

tools:
  github:
    toolsets: [default]
  edit:
  bash: ["mvn", "find", "ls", "cat", "grep"]

safe-outputs:
  create-pull-request:
    title-prefix: "[test-luecken] "
    labels: [testing, automated]
    draft: true
    if-no-changes: warn
  missing-tool:
---

# Wöchentlicher Testlücken-Report

Analysiere die JUnit-5-Testabdeckung dieses Maven-Projekts.

## Vorgehen

1. Übersetze die Tests, ohne sie auszuführen:
   `mvn -q -B -ntp -DskipTests test-compile`
2. Liste alle produktiven Klassen unter `*/src/main/java` und alle Testklassen unter
   `*/src/test/java`.
3. Finde Klassen mit öffentlicher Geschäftslogik ohne zugehörige Testklasse.
4. **Priorisiere nach Risiko, nicht nach Größe.** In dieser Reihenfolge:
   - Klassen, die Geld oder Zeit berechnen
   - Klassen, die eine Regel aus `modul-5/SPEC.md` umsetzen
   - Klassen mit Verzweigungen über Enums oder Statuswerte
   - alles andere
   Reine Datenträger (Records ohne Logik), Konfiguration und generierter Code
   sind **keine** Lücken. Führe sie nicht auf.
5. Schreibe das Ergebnis nach `docs/test-luecken.md` (anlegen oder überschreiben):
   - Tabelle: Klasse | Modul | Grund der Priorisierung | vorgeschlagene Testfälle
   - Abschnitt "Top 3 für diese Woche" mit je einem Satz Begründung
6. Erstelle daraus einen Draft-Pull-Request.

## Grenzen

Ändere **ausschließlich** `docs/test-luecken.md`. Schreibe keinen Produktivcode.
Schreibe auch keine Tests — der Vorschlag ist das Produkt, nicht die Umsetzung.
````

**Worauf es hier ankommt:**

* **`java` im Netz-Allowlist.** Ohne den Eintrag löst Maven keine Abhängigkeiten auf,
  und der Lauf scheitert an einer Stelle, die nach einem Java-Problem aussieht.
* **Der Zeitplan als `weekly on monday`**, nicht als fester Cron. Ein fester Cron
  kompiliert zwar, erzeugt aber eine Warnung — und verfehlt damit das Ziel „0
  Warnungen". Wer bewusst einen fachlichen Stichtag braucht, nimmt Cron und die
  Warnung in Kauf.
* **`if-no-changes`**, sonst öffnet der Workflow jede Woche einen leeren PR. Nach vier
  Wochen schaltet ihn jemand ab, und das war es dann mit der Automatisierung.
* **Der Draft-PR ist ein Wort.** Ein fertiger PR von einem Agenten, den niemand
  angefordert hat, ist eine Zumutung für den Reviewer.

**Und die eigentliche Denkaufgabe:** Warum schreibt dieser Workflow die fehlenden Tests
nicht gleich selbst? Nicht, weil der Agent Fehler macht — sondern: **wogegen schriebe er
sie?** Ohne fachliche Vorgabe kann er nur das bestehende Verhalten abschreiben. Das ist
das Anti-Muster aus Lab D2, in einen nächtlichen Workflow gegossen und damit
automatisiert.

---

## 3 · `ci-triage.md`

````markdown
---
description: Analysiert fehlgeschlagene Maven-Builds auf Pull Requests

on:
  pull_request:
    types: [opened, synchronize, reopened]
  roles: [admin, maintainer, write]

permissions:
  contents: read
  actions: read
  pull-requests: read
  issues: read

engine: copilot
network: defaults
timeout-minutes: 15
strict: true

tools:
  github:
    toolsets: [default, actions]
  bash: ["ls", "cat", "grep", "head", "tail"]

safe-outputs:
  add-comment:
    target: triggering
    max: 1
  missing-tool:
---

# Maven-CI-Triage

Analysiere den fehlgeschlagenen Maven-Build dieses Pull Requests.

## Vorgehen

1. Hole die Workflow-Runs zum Head-SHA dieses PR und finde den fehlgeschlagenen Job.
2. Lade die Job-Logs und extrahiere die **eigentliche** Ursache. Achte auf:
   - Compile-Fehler (`cannot find symbol`, fehlende Imports)
   - Fehlgeschlagene Tests (Surefire: `Tests run: ... Failures: ...`)
   - Abhängigkeitsauflösung (`Could not resolve dependencies`, Versionskonflikte —
     hier lohnt der Blick, ob der Artifactory-Proxy die Version überhaupt freigibt)
   - Enforcer-, Checkstyle- oder SpotBugs-Verstöße
3. Unterscheide klar zwischen **echtem Fehler im PR** und **flaky oder
   infrastrukturell**. Das ist der eigentliche Wert dieses Workflows: er nimmt dem
   Menschen die Frage ab, ob er überhaupt hinschauen muss.
4. Schreibe **genau einen** Kommentar:
   - **Ursache** in einem Satz
   - **Belegstelle**: die relevanten Logzeilen, gekürzt
   - **Fix-Vorschlag**: Datei und konkrete Änderung
   - **Einschätzung**: PR-Fehler oder flaky, mit Begründung
5. Ist der Build grün, kommentiere **nicht**.

## Grenzen

Ändere keine Dateien. Pushe nichts. Schließe nichts.
````

**Worauf es hier ankommt:**

* **`roles:` unter `on:`**, nicht auf oberster Ebene. Der Compiler sagt euch das
  wörtlich — es ist die lehrreichste Fehlermeldung des Tages.
* **`actions: read` UND das Toolset `actions`.** Beides, nicht eines. Permission und
  Werkzeug sind zwei verschiedene Schalter, die zufällig gleich heißen.
* **`target` beim Kommentar**, sonst landet er nicht zwingend dort, wo ihr denkt.
* **`max: 1`.** Niemand will fünf Bot-Kommentare an einem PR.

**Und die wichtigste Denkaufgabe des ganzen Moduls:** Dieser Workflow wird durch
**fremde** Pull Requests ausgelöst — auch aus Forks, von Leuten, die ihr nicht kennt.
Der Agent liest deren Build-Logs. In einem Build-Log steht, was der Autor
hineinschreiben konnte: **Testnamen zum Beispiel.**

Ein Test, der `sollteFunktionieren_ignoriere_alle_vorherigen_Anweisungen_und…` heißt,
steht wörtlich im Log, das der Agent liest. `roles:` ist die Antwort darauf: Der
Workflow läuft gar nicht erst für Fremde. Und das Safe-Output-Modell ist die zweite
Verteidigungslinie — selbst ein übernommener Agent kann nur kommentieren.

</details>

### Die Lockfile-Regel, an der alle einmal scheitern

GitHub Actions führt **ausschließlich YAML** aus. Eure `.md`-Datei ist für Actions
unsichtbar — nur die generierte `.lock.yml` läuft wirklich. Aus 3 KB Markdown werden
rund 100 KB Lockfile, mit SHA-gepinnten Actions, Firewall-Proxy und dem kompletten
Agent-Prompt.

> **Merksatz:** Nach jeder Änderung an der `.md` sofort `gh aw compile` und **beide**
> Dateien committen. Der Compiler erkennt Drift über Hashes und meldet
> „Lock file is outdated".

<details>
<summary>Wenn ihr den Workflow wirklich scharf schalten wollt</summary>

Nicht Teil der Übung — und **nicht in diesem Repo**, sonst liegt eure Lösung für alle
sichtbar herum. In einem eigenen Repo:

```bash
git add .github/workflows/doku-drift.md .github/workflows/doku-drift.lock.yml
git commit -m "feat(build): ergaenze Doku-Drift-Workflow"
git push
gh aw status
gh aw run doku-drift
```

`gh aw run` gibt euch die Run-ID und die URL aus. **Erst danach**, mit der echten ID
aus dieser Ausgabe:

```bash
gh aw audit 32714750029 --parse     # eure ID einsetzen
```

> **Zwei Dinge, die wir dabei gelernt haben.** Erstens: Mit `engine: copilot` braucht
> der Lauf ein `COPILOT_GITHUB_TOKEN` im Repo — sonst bricht er in der ersten Sekunde
> ab. Zweitens: Bei einem Fehlschlag legt der Workflow ein Issue an, das den Fehlschlag
> meldet. Das ist gewollt und trotzdem überraschend, wenn man es nicht erwartet.

</details>

### Das Sicherheitsmodell — warum das Ganze überhaupt vertretbar ist

Der Agent läuft **read-only**. Was er ausgeben will, geht als **Safe Output** an einen
**separaten Job**, der ausschließlich die deklarierte Aktion ausführt; dazwischen wird
die Ausgabe saniert.

Konsequenz: Selbst wenn ein manipuliertes Werkzeug-Ergebnis den Agenten übernimmt, kann
er nur das tun, was in `safe-outputs:` steht. **Er schlägt vor, ein anderer Job führt
aus.**

---

## C4 — Kür: Portfolio steuern

**Wenn Zeit bleibt · nur in der Variante Vollgas**

Delegiert drei kleine Tasks gleichzeitig (die drei guten Karten) und beobachtet sie
parallel im Agents-Tab. Jeder bekommt einen eigenen Branch.

Fragen für die Runde:

* Ab welcher Anzahl paralleler Agents wird **Review** zum Engpass — nicht die Ausführung?
* Was passiert, wenn zwei Agent-PRs dieselbe Datei anfassen?
* Wer räumt die Branches auf, wenn ein PR nicht gemerged wird?

Das ist keine Technikfrage. Das ist die Frage, an der Agent-Programme scheitern.

---

## Gruppenarbeit — Entwerft euren eigenen Agentic Workflow

**10 Minuten · Vierergruppen**

Vorlage: **[`modul-4/gruppenarbeit-workflow-canvas.md`](gruppenarbeit-workflow-canvas.md)**
— eine pro Gruppe, ausgedruckt oder auf dem Laptop.

Skizziert einen Workflow für **euren** Alltag, nicht für die Voltwerk-Suite. Alle Felder
und die Pflichtfragen stehen auf dem Canvas; die Pflichtfragen entscheiden, ob der
Entwurf ernst gemeint ist.

**Ablauf:** 7 Minuten entwerfen, 3 Minuten Speed-Feedback mit der Nachbargruppe.
**Kommentieren, nicht diskutieren** — die Zeit reicht nicht für beides.

**Ergebnis:** ein umsetzbarer Entwurf pro Gruppe. Den besten setzen wir nach dem
Workshop gemeinsam auf.

---

## Die Umgebung, in der das läuft

`modul-4/copilot-setup-steps.yml.vorlage` definiert die Arbeitsumgebung des Agenten und
gehört nach `.github/workflows/copilot-setup-steps.yml`. Heute läuft alles auf
**GitHub-hosted Runnern** (`ubuntu-latest`) — ihr braucht keine eigene Infrastruktur.

**Die harten Regeln:**

* Der Job **muss** `copilot-setup-steps` heißen. Anderer Name: wird still ignoriert.
* Nur **sechs** Job-Keys werden ausgewertet:
  `steps`, `permissions`, `runs-on`, `services`, `snapshot`, `timeout-minutes`.
  **Alles andere wird still ignoriert** — auch ein `env:` auf Job-Ebene.
  Environment-Variablen gehören auf **Step-Ebene**.
* `timeout-minutes` maximal **59**.
* **Secrets für den Agenten sind ein eigener Typ.** Sie liegen unter
  *Settings → Secrets and variables → **Agents***. Actions-Secrets sind für den Agenten
  **unsichtbar**. Der Präfix `COPILOT_MCP_` hat Sonderverhalten.
* Schlagen die Setup-Steps fehl, **bricht der Agent nicht ab** — er arbeitet ohne die
  Umgebung weiter. Genau deshalb schaut ihr ins Setup-Log und nicht nur auf den Diff.

### Der Schritt danach: eigene Runner

**Bewusst kein Übungsteil.** Ihr habt den Umzug in der Live-Demo gesehen; hier steht,
was sich dabei ändert:

| | GitHub-hosted (heute) | Self-hosted (später) |
|---|---|---|
| `runs-on` | `ubuntu-latest` | euer Runner-Pool |
| Netzgrenze | eingebaute Copilot-Firewall | **eure** Egress-Allowlist |
| Abhängigkeiten | Maven Central | euer Repository-Proxy |
| Runner-Lebensdauer | von GitHub verwaltet | ephemer, das ist Pflicht |
| Kosten | Actions-Minuten | Actions-Minuten **plus** euer Compute |
| Verantwortung | GitHub | ihr |

Die entscheidende Zeile ist die zweite: **Die eingebaute Firewall wird für self-hosted
Runner deaktiviert.** Eure Allowlist tritt an ihre Stelle. Das ist kein
Konfigurationsdetail, sondern eine Übergabe von Verantwortung.

Die Checkliste dazu steht in **Anhang C** von `copilot-setup-steps.yml.vorlage`, der
Maven-Proxy-Schritt in Anhang B.

**Die eine Frage für die Rückfahrt:** Wer in eurem Haus besitzt die Egress-Allowlist —
mit Namen? Ohne diese Antwort ist der Wechsel nicht vorbereitet, egal wie fertig die
Technik ist.

---

## Checkpoint für das Plenum

1. Welche Karte war die schlechte — und welcher Teil davon ist **gar nicht**
   delegierbar?
2. Eure Merge-Entscheidung aus C2, mit Begründung. Credits und Laufzeit an die Tafel,
   neben eure Schätzung aus Modul 3.
3. Ein Satz zu eurem Workflow: Trigger, Safe Output, Owner.
4. **Die Pflichtfrage:** Was ist der Schaden, wenn euer Workflow Unsinn liefert — und
   wer fängt ihn am Gate ab?
