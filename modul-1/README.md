# Modul 1 — Übungsreihe A: Kontrolle beweisen

**Zeit:** 40 Minuten angesetzt · **Sozialform:** Zweierteams · **Ort:** Aufgaben hier, Ausführung in der Repo-Wurzel

> **Zur Zeit, ehrlich:** Wir haben die Reihe durchgespielt. Vollständig braucht sie eher
> **80 Minuten**. In 40 Minuten schafft ihr **A2 und A4** — das sind die beiden, die
> hängenbleiben. A1 und A3 sind Kür. Lasst euch nicht hetzen und macht lieber zwei
> Übungen richtig als vier halb.

> Thema des Moduls sind die drei Regler **Autonomie**, **Anbindung**, **Kosten**.
> In dieser Reihe dreht ihr am Regler Autonomie — und beweist, dass ihr ihn kontrolliert.

---

## Bevor ihr anfangt

```bash
copilot --version          # sollte 1.0.79 oder neuer sein
mvn -q test                # muss grün sein
```

Die Übungen laufen **im Repo-Wurzelverzeichnis**, nicht in `modul-1/`. Startet die CLI
also oben und benutzt Pfade wie `modul-1/src/...`.

Fünf Dinge, die euch heute den Tag retten:

| Taste / Befehl | Wirkung |
|---|---|
| `/env` | Zeigt, was tatsächlich geladen ist: Instructions, MCP-Server, Skills, Agents, Hooks, Plugins |
| `/context` | Wohin die Tokens gehen — System-Prompt, Tools, Nachrichten |
| `/usage` | AI Credits dieser Session, Token-Aufteilung, Cache-Anteil |
| `Shift+Tab` | Modus umschalten (interactive → plan → autopilot), sichtbar in der Statusleiste |
| `Ctrl+R` | Suche in der Prompt-Historie |

Wenn etwas „nicht funktioniert", ist `/env` fast immer die erste Antwort.

### Der Experimental-Schalter

Ein Teil dessen, was ihr heute braucht, ist hinter einem Schalter versteckt:

```bash
copilot --experimental
```

oder in einer laufenden Session `/settings experimental on`.

Ohne ihn antwortet die CLI auf `/sandbox` mit „Unknown command", und `-w` bricht mit
einer Fehlermeldung ab. **Das ist kein Fehler in eurer Installation.** Diese Befehle
existieren nur im Experimental-Modus:

> **Windows:** Der Schalter funktioniert bei euch genauso — nur die Sandbox selbst
> (A3.2) lässt sich auf einem normalen Windows-Build nicht aktivieren. Details stehen
> bei A3.

| Nur experimentell | Was es tut |
|---|---|
| `/worktree [branch\|task]` | Legt einen Git-Worktree ab `HEAD` an und wechselt die Session hinein |
| `/worktree new [PROMPT]` | Worktree plus direkter Auftrag |
| `/move [branch\|task]` | Verschiebt uncommittete Änderungen in einen neuen Worktree |
| `-w`, `--worktree[=NAME]` | Session direkt in einem isolierten Worktree unter `<repo>.worktrees/` starten |
| `/sandbox`, `--sandbox`, `--no-sandbox` | OS-Sandbox für Shell-Kommandos |
| `/every`, `/after` | Wiederkehrende und einmalige geplante Prompts |

`copilot help commands` zeigt diese Einträge **nicht** an, solange der Modus aus ist.
Wer nur die Hilfe liest, hält sie für nicht existent. Merkt euch das für den Moment,
in dem euch jemand sagt, ein Feature gäbe es nicht.

> `--worktree` verträgt sich nicht mit `--resume`, `--continue` und `--connect`.

---

## A1 — Session-Jonglage: zwei Wege, ein Vergleich

**10 Minuten**

`modul-1/src/main/java/de/voltwerk/telemetrie/LadepunktStatistik.java` hat drei Methoden
mit vollständigem Javadoc-Kontrakt und ohne Implementierung. Die Abnahmetests liegen in
`LadepunktStatistikTest` und sind mit `@Disabled` abgeschaltet.

**Auftrag**

1. Entfernt die `@Disabled`-Annotation in der Testklasse. `mvn -q test` ist jetzt rot —
   so soll es sein.
2. Startet eine Session und lasst die Klasse **klassisch imperativ** implementieren
   (Schleifen, keine Streams):

   ```
   Implementiere LadepunktStatistik in modul-1 vollständig gegen das Javadoc.
   Klassisch imperativ: for-Schleifen, keine Streams, keine neuen Abhängigkeiten.
   Danach mvn -q -pl modul-1 -am test ausführen und das Ergebnis nennen.
   ```

3. **Bevor** ihr zufrieden seid: `/fork`. Ihr habt jetzt zwei Sessions mit demselben
   Kontext. In der Abzweigung fordert ihr die Gegenposition:

   ```
   Schreibe dieselbe Implementierung neu, diesmal deklarativ mit der Stream-API.
   ueberhitzungsverdacht darf trotzdem nicht unleserlich werden.
   ```

4. Vergleicht beide Ergebnisse. `/diff` zeigt euch, was in der aktuellen Session
   tatsächlich geändert wurde.
5. Entscheidet euch für eine Variante und setzt **diese** Session mit `/resume` fort:
   lasst die unterlegene Variante als Kommentar-Notiz in `modul-1/uebungen/A1-notiz.md`
   festhalten, mit einem Satz Begründung.

**Fertig, wenn:** `mvn -q -pl modul-1 -am test` grün ist und `A1-notiz.md` existiert.

**Worauf es ankommt**

`/fork` ist kein Undo. Es ist die Möglichkeit, eine teuer aufgebaute
Kontext-Investition zweimal zu nutzen. Der Kontext (Repo verstanden, Kontrakt gelesen)
ist der teure Teil — die zweite Antwort ist billig, weil der Cache greift. Schaut nach
`/fork` einmal in `/usage` und vergleicht den Cache-Anteil.

**Variante für Mutige — echte Isolation statt geteiltem Arbeitsverzeichnis**

`/fork` verzweigt die *Session*, nicht das Dateisystem. Beide Zweige schreiben in
dieselben Dateien. Wenn ihr zwei Implementierungen wirklich nebeneinander stehen haben
wollt, braucht ihr zwei Arbeitsverzeichnisse:

```bash
copilot --experimental -w streams-variante
```

Das legt einen Git-Worktree unter `<repo>.worktrees/` an und startet die Session direkt
darin. Ihr habt dann zwei Stände auf der Platte und könnt sie mit
`git diff main..streams-variante` vergleichen, statt sie nacheinander zu erzeugen.

In einer laufenden Session geht dasselbe mit `/worktree` — und `/move` schiebt
uncommittete Änderungen nachträglich in einen eigenen Worktree, wenn ihr merkt, dass
ihr auf dem falschen Branch angefangen habt.

Faustregel: **eine Session pro Thema, ein Worktree pro parallelem Strang.**

> **Nebenfrage für Schnelle:** `ueberhitzungsverdacht` hat einen Fallstrick im Kontrakt.
> Findet ihn, ohne den Test zu lesen?

---

## A2 — Freigaben-Golf 🏌️

**12 Minuten · Wettbewerb**

Das ist die Übung, über die ihr abends noch redet.

**Vorbereitung** (einmal pro Team, dauert 5 Sekunden):

```bash
git status --porcelain     # muss LEER sein, sonst startet das Skript nicht
bash modul-1/uebungen/A2-branch-chaos.sh
```

> ⚠️ **Wenn ihr A1 gemacht habt, ist euer Arbeitsbaum schmutzig** und das Skript
> verweigert den Start („Arbeitsverzeichnis ist nicht sauber"). Das ist Absicht — es soll
> eure Arbeit nicht in seine Übungsbranches ziehen. Committet A1 vorher auf **eurem**
> Branch:
>
> ```bash
> git checkout -b uebung/euer-name     # falls noch nicht geschehen
> git add -A && git commit -m "feat(telemetrie): implementiere LadepunktStatistik"
> ```
>
> Das ist keine Schikane: Der Commit ist gleichzeitig euer erster Testfall für den
> Commit-Botschafter aus Lab B.

Das legt acht lokale Branches mit Präfix `chaos/` an. Aufräumen später mit
`bash modul-1/uebungen/A2-branch-chaos.sh --aufraeumen`.

**Der Auftrag, wörtlich:**

> Erstelle die Datei `modul-1/uebungen/A2-bericht.md`. Sie enthält:
> (1) eine Tabelle aller lokalen Branches mit Präfix `chaos/` mit dem Datum ihres
> letzten Commits und der Commit-Betreffzeile, absteigend nach Datum sortiert;
> (2) eine Zusammenfassung der letzten 10 Commits auf dem aktuellen Branch in
> maximal fünf Sätzen; (3) eine Empfehlung, welche drei Branches gelöscht werden
> können und warum.

**Die Regeln**

* Ausführung **ausschließlich im nicht-interaktiven Modus**, also mit `-p`.
* Ihr dürft **keine** Pauschal-Freigaben benutzen: kein `--allow-all-tools`,
  kein `--allow-all`, kein `--yolo`.
* Der Lauf muss **ohne jede Rückfrage** durchlaufen und die Datei **an genau diesem
  Pfad** erzeugen.
* Ihr dürft beliebig oft probieren, aber jeder Lauf kostet Credits — notiert sie.

> ⚠️ **Die Falle, auf die alle hereinfallen.** Prüft nach jedem Lauf mit
> `ls -la modul-1/uebungen/A2-bericht.md`, ob die Datei wirklich dort liegt.
>
> Ein Agent, dem die Schreibrechte fehlen, bricht nämlich **nicht** ab. Er sucht sich
> einen Ort, an dem er schreiben darf, legt die Datei dort ab und meldet „Erledigt".
> Der Lauf sieht erfolgreich aus. Er ist es nicht.
>
> Das ist die wichtigste Lektion der Übung — wichtiger als die Punktzahl:
> **Least Privilege hält keinen Agenten auf. Es lenkt ihn um.** Wer nur die
> Abschlussmeldung liest, merkt davon nichts. Prüft das Ergebnis, nicht die Meldung.

**Startpunkt** (bewusst zu weit gefasst — euer Job ist es, das enger zu machen):

```bash
copilot -p "..." --allow-tool 'shell(git:*)' --allow-tool 'write'
```

**Wertung** — niedrigste Punktzahl gewinnt:

| Kriterium | Punkte |
|---|---|
| je `--allow-tool`-Flag | 2 |
| Wildcard `:*` im Muster | +3 pro Vorkommen |
| `write` ohne Pfadangabe | +5 |
| `url(...)` freigegeben, obwohl nicht gebraucht | +5 |
| Lauf fragt trotzdem nach oder schlägt fehl | Disqualifikation |
| je zusätzlichem `--deny-tool`, das etwas Sinnvolles verbietet | −1 |

**Hilfen**

`copilot help permissions` ist die vollständige Wahrheit. Die Kurzfassung:

```
shell(command:*?)   shell(git)          nur "git" ohne Subkommando
                    shell(git log)      Freigabe auf Ebene des ersten Subkommandos
                    shell(git:*)        alle git-Subkommandos
write(path?)        write(.env)         relativer Pfad matcht auf Endkomponenten,
                                        also .env in JEDEM Verzeichnis
                    write(/abs/pfad)    exakt dieser Ort
url(domain-or-url?) url(https://github.com)
<mcp-server>(tool?) werkstatt(paket_versionen)
```

**Deny schlägt Allow. Immer.** Auch `--allow-all-tools`.

**Reflexion in der Runde (2 Minuten, alle):**

1. Wer hat die wenigsten Punkte — und läuft es bei ihm wirklich durch?
2. Wie viele Credits hat der erste Versuch gekostet, wie viele der letzte?
3. Was wäre passiert, wenn ihr `--allow-all-tools` genommen hättet? Warum ist die
   Antwort „nichts Schlimmes" hier trotzdem die falsche Antwort für morgen?

---

## A3 — Die Leine spüren: URLs, Pfade, Sandbox

**8 Minuten** — davon **A3.1 selbst (ca. 5 Min)** und **A3.2 als Demo vorne (ca. 3 Min)**

Copilot hat drei Verteidigungslinien: Bestätigung pro Aktion, Pfad- und URL-Regeln,
und — auf OS-Ebene — die Sandbox.

> **A3.2 macht ihr nicht selbst — schaut zu.**
> Die lokale Sandbox läuft laut GitHub-Doku nur auf macOS, Linux und
> **Windows-Insider-Builds**. Auf einem normalen Windows-Rechner lässt sie sich nicht
> aktivieren; das ist erwartet und kein Fehler bei euch.
> Damit trotzdem jeder den Sandbox-Block einmal gesehen hat, führen wir A3.2 vorne
> gemeinsam vor. Ihr schaut zu — **die Frage am Ende von A3.2 beantwortet ihr danach
> selbst**, die braucht keine laufende Sandbox.

**A3.1 URL-Grenzen — das macht ihr selbst (funktioniert auf jedem System)**

```bash
copilot -p "Rufe https://api.github.com/zen ab und gib die Antwort aus." \
  --allow-tool 'shell(curl)' \
  --deny-url 'https://api.github.com'
```

Beobachtet: Was meldet die CLI? Probiert dann dasselbe mit
`--allow-url 'https://api.github.com'`. Und: URL-Regeln sind **protokoll-scharf** —
`https://example.com` erlaubt **nicht** `http://example.com`. Testet das.

**A3.2 Sandbox (experimentell) — Demo vorne, ihr schaut zu**

Command Sandboxing basiert auf Microsoft Execution Containers; unter macOS über
`sandbox-exec`, unter Linux über `bwrap` (bubblewrap 0.5.0+ muss auf dem PATH sein),
unter Windows über ProcessContainer — und das gibt es derzeit **nur auf
Windows-Insider-Builds**. Die Doku sagt wörtlich: *„Local sandboxing is available on
macOS and Linux, and on Windows Insiders builds."*

Die folgenden Befehle führt der Trainer vor. Lest sie mit, tippt sie nicht mit.

```bash
copilot --experimental              # Sandbox-Befehle werden registriert
copilot --experimental --sandbox    # nur für diese Session an, ohne die Einstellung zu ändern
```

`--sandbox` / `--no-sandbox` sind praktisch mit `-p`: Sandbox für einen einzelnen
Skriptlauf, ohne die gespeicherte Konfiguration anzufassen.

In der Session:

```
/sandbox            zeigt den Status und öffnet den Konfigurationsdialog
/sandbox enable     schaltet ein
/sandbox disable    schaltet aus
```

Probiert bei eingeschalteter Sandbox:

```
Schreibe die Datei /etc/voltwerk-test.txt mit dem Inhalt "hallo".
```

und danach:

```
Schreibe die Datei modul-1/uebungen/A3-sandbox.md mit dem Inhalt "hallo".
```

**Die Frage, um die es geht — die beantwortet ihr selbst:** Was genau ist der
Unterschied zwischen `--deny-tool 'shell(rm:*)'` und einer Sandbox-Policy? Formuliert
eine Antwort in einem Satz und schreibt sie in `modul-1/uebungen/A3-sandbox.md`.
Dafür braucht ihr keine laufende Sandbox — nur das, was ihr gerade gesehen habt.

> Antwort-Hinweis für die Diskussion: Das eine ist eine Regel darüber, **was der Agent
> vorschlagen darf**. Das andere ist eine Regel darüber, **was das Betriebssystem
> zulässt**. Die erste hält einen kooperativen Agenten auf. Die zweite hält auch einen
> Prozess auf, den ein manipuliertes Tool-Ergebnis gestartet hat.

Bemerkenswert für Java-Teams: Die Sandbox-Einstellung `allowDevToolAccess` (Standard:
an) erkennt eine `pom.xml` im Arbeitsverzeichnis und gewährt daraufhin lesenden Zugriff
auf `~/.m2/settings.xml` — sonst würde jeder Maven-Build in der Sandbox am
Artifactory-Proxy scheitern. Der Preis: sandboxed Kommandos können diese Datei lesen,
inklusive der Tokens darin. Diskutiert kurz, ob euch das passt.

---

## A4 — Agent-Duett: derselbe Diff, zwei Meinungen

**10 Minuten**

In `modul-1/uebungen/A4-review.patch` liegt ein Pull Request aus der Ladepark-Suite:
Ticket VW-3140, „CSV-Export der Messpunkte". Der Entwickler ist zufrieden. Ihr nicht.

```bash
git apply modul-1/uebungen/A4-review.patch
git add -N modul-1/src/main/java/de/voltwerk/telemetrie/MesspunktExport.java
```

**Runde 1 — ohne Agent, nackter Prompt:**

```
Reviewe den Diff in modul-1/src/main/java/de/voltwerk/telemetrie/MesspunktExport.java.
```

Notiert die Anzahl der Befunde.

**Runde 2 — mit Rolle.** Legt `.github/agents/reviewer.md` an:

```markdown
---
name: reviewer
description: Reviewt Java-Diffs gegen die Konventionen in AGENTS.md und meldet Befunde nach Schweregrad. Use when reviewing a diff, a pull request, or staged changes in this repository. Ändert niemals Produktionscode.
tools:
  - read
  - search
---

Du bist der Code-Reviewer der Voltwerk-Plattformteams. Du änderst nichts.

Prüfe gegen `AGENTS.md` in der Repo-Wurzel und gegen `docs/commit-konventionen.md`.

Liefere eine Tabelle: Schweregrad (blocker / major / minor) | Datei:Zeile | Befund |
konkrete Korrektur. Sortiert nach Schweregrad. Keine Lobhudelei, keine
Zusammenfassung am Ende. Wenn du nichts findest, sag das in einem Satz.

Bei Sicherheitsbefunden (Secrets, Injection, unverschlüsselter Transport) ist der
Schweregrad immer blocker.
```

Dann:

```
/agent reviewer
```

und denselben Auftrag noch einmal.

**Fertig, wenn** ihr in `modul-1/uebungen/A4-befunde.md` festgehalten habt:

* Anzahl Befunde Runde 1 vs. Runde 2
* Die drei Befunde, die **nur** der Agent gefunden hat
* Einen Befund, den **beide** übersehen haben (ja, es gibt mindestens einen)

**Die Probe aufs Exempel:** Bittet den Reviewer-Agenten anschließend, den Fehler
gleich zu beheben. Er wird ablehnen. Das ist die `tools:`-Liste bei der Arbeit — und
das ist der Unterschied zwischen einer Absprache und einer Regel.

> Der Diff enthält **weit über zwanzig** belastbare Befunde, vier davon Blocker.
> Wir haben nachgezählt. Ab **fünfzehn** gibt es vorne am Trainer-Tisch einen Keks.
>
> Realistisch braucht ein ehrlicher Zweierteam-Review beide Runden zusammen eher
> **30 Minuten** als 10. Wenn die Zeit drängt: Runde 1 auf fünf Minuten deckeln und die
> Kraft in Runde 2 stecken — der Vergleich ist der Punkt, nicht die Vollständigkeit.

Aufräumen: `git apply -R modul-1/uebungen/A4-review.patch && git reset -q`

> **Nicht committen**, solange der Patch angewendet ist — sonst schlägt `git apply -R`
> fehl und ihr braucht `git checkout -- . && git reset -q` stattdessen.

(Das `git reset` ist nötig, weil `git add -N` den Index anfasst und `git apply -R`
nur den Arbeitsbaum zurücknimmt. Ohne den Reset bleibt das Repo unsauber — und das
A2-Skript verweigert danach den Dienst.)

---

## Bonus — für alle, die früher fertig sind

Diese Slash-Commands lohnen sich und stehen auf keiner Folie:

| Befehl | Wofür |
|---|---|
| `/plan` | Plan erzeugen und reviewen, bevor irgendetwas geschrieben wird |
| `/rubber-duck` | Unabhängige Kritik an eurer aktuellen Arbeit — ideal vor einem Fix |
| `/review` | Der eingebaute Code-Review-Agent auf eure Änderungen |
| `/security-review` | Sicherheitsanalyse der gestagten und ungestagten Änderungen |
| `/refine` | Formt einen hingerotzten Prompt in einen klaren um |
| `/ask` | Zwischenfrage, ohne die Session-Historie zu verschmutzen |
| `/rewind` | Letzte Runde zurücknehmen, inklusive Dateiänderungen |
| `/share` | Session als Markdown, HTML oder Gist teilen — gut für Übergaben |

**Bonusaufgabe:** Lasst `/security-review` über den A4-Patch laufen und vergleicht das
Ergebnis mit dem eures Reviewer-Agenten. Wer findet das Token?

---

## Checkpoint für das Plenum

Jedes Team nennt:

1. Seine A2-Punktzahl und die engste funktionierende Freigabe-Kombination.
2. Die Credit-Differenz zwischen erstem und letztem A2-Versuch.
3. Einen Befund aus A4, den nur der Agent gefunden hat.
