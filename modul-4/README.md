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
> Plant so: **C1, dann C3** — die Wartezeit auf den Agenten fällt in C3 hinein und ist
> dadurch nicht verloren. C2 macht ihr, sobald der PR da ist.

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

```
/autopilot Refactoriere die Tarif-Kaskade --max-ai-credits 5
/delegate  Behebe VW-4711 wie in der Aufgabenkarte beschrieben
```

`/delegate --base <branch>` wählt den Ziel-Branch des PR.

Aus dem Terminal geht dasselbe ohne Session:

```bash
gh agent-task create "Behebe VW-4711 ..." --follow
gh agent-task create -F modul-4/aufgabenkarten/C1-vw-4711.md
gh agent-task create -F modul-4/aufgabenkarten/C1-vw-4711.md --custom-agent abrechnungs-reviewer
gh agent-task list
gh agent-task view <id>
```

`-F -` liest von stdin. `--custom-agent my-agent` benutzt `.github/agents/my-agent.md` —
damit könnt ihr eure Rolle aus Lab B in die Cloud schicken.

> `gh agent-task` gibt es ab gh 2.80.0 und ist Public Preview.

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
| `C1-vw-4714.md` | „Abrechnung modernisieren" |

**Auftrag**

1. Lest alle vier. Bewertet jede an den offiziellen Kriterien:
   * Klare Beschreibung des Problems oder der geforderten Arbeit
   * **Vollständige** Abnahmekriterien — woran erkennt man eine gute Lösung?
     Gehören Tests dazu?
   * Hinweise, welche Dateien betroffen sind
2. Einigt euch, welche Karte die schlechte ist, und **schreibt sie um**, bis sie die
   Kriterien erfüllt. Ablegen als `modul-4/aufgabenkarten/C1-vw-4714-repariert.md`.
3. **Die Probe:** Lest eure reparierte Karte einem anderen Team vor — ohne Kontext.
   Wenn Rückfragen kommen, kommen sie auch vom Agenten. Nur merkt der es nicht und
   rät stattdessen.

**Denkt daran:** Ein Issue, das ihr dem Agenten zuweist, ist ein **Prompt**. Lest eure
Karte noch einmal mit dieser Brille. Würdet ihr sie so in ein Chatfenster tippen?

**Fertig, wenn** `C1-vw-4714-repariert.md` existiert und ihr in einem Satz sagen könnt,
welcher Teil der ursprünglichen Karte **gar nicht** delegierbar ist — und warum nicht.

### Was ihr NICHT delegiert

Vier Kategorien, die GitHub ausdrücklich als ungeeignet dokumentiert:

* **Breit und komplex** — repository-übergreifende Refactorings, tiefes Domänenwissen,
  substanzielle Geschäftslogik, große Umbauten mit Anspruch auf Designkonsistenz.
* **Sensibel und kritisch** — produktionskritische Störungen, Security, personenbezogene
  Daten, alles rund um Authentifizierung, Incident Response.
* **Mehrdeutig** — offene Aufgaben, unklare Anforderungen, alles, wo man sich die Lösung
  erst erarbeiten muss.
* **Lernaufgaben** — alles, wo *ihr* etwas verstehen wollt. Der Agent lernt für euch,
  und ihr habt nichts davon.

Karte `C1-vw-4714.md` fällt in mindestens zwei dieser Kategorien. In welche?

---

## Dazwischen — Die Delegation

**5 Minuten · Demo vorne, oder ihr selbst**

Jetzt geht eine der **guten** Karten in die Cloud. Aus einer laufenden Session:

```
/delegate
```

oder direkt aus dem Terminal, ohne Session:

```bash
gh agent-task create -F modul-4/aufgabenkarten/C1-vw-4711.md --follow
gh agent-task list
gh agent-task view <id>
```

Danach legt Copilot einen Branch an, öffnet einen **Draft-PR** und arbeitet im
Hintergrund weiter — auch wenn der Laptop zugeklappt wird.

**Worauf ihr achtet, während es läuft** (das ist der eigentliche Inhalt dieser fünf
Minuten, nicht der Tastendruck):

* Im **Agents-Tab** auf github.com: die drei Statusstufen *läuft · wartet auf Review ·
  fertig*. Mehrere Läufe stehen dort nebeneinander.
* Im **Setup-Log**: `setup-java`, der Maven-Cache, `dependency:go-offline`. Das ist der
  Beweis, dass die Umgebung aus `copilot-setup-steps.yml` wirklich gegriffen hat.
* Die **Wartezeit**. Ein Lauf dauert typischerweise zehn bis dreißig Minuten. Genau
  deshalb macht ihr jetzt C3 und kommt später zu C2 zurück.

> **In der Demo-Variante** löst der Trainer aus, ihr schaut zu und stellt Fragen.
> Der PR, den ihr gleich in C2 reviewt, ist ein **echter** Agent-PR — nur nicht eurer.
> Für das Review macht das keinen Unterschied. Für die Frage „darf das gemerged
> werden?" erst recht nicht.

---

## C2 — Review am Gate

**15 Minuten · Hands-on**

Der Draft-PR ist der spannende Teil, nicht der Diff. Ihr braucht dafür nur **Leserechte
im Browser** — das reicht für alles, was hier zu tun ist.

**Prüft in dieser Reihenfolge:**

1. **Das Setup-Log.** Sind die Setup-Steps überhaupt gelaufen? Hat `setup-java` die
   richtige Version gezogen? Wurden die Maven-Abhängigkeiten vorab aufgelöst?
2. **Der Testlauf.** Hat der Agent `mvn -q test` wirklich ausgeführt — oder nur behauptet,
   es sei grün? Sucht die Ausgabe im Log.
3. **Die Begründung.** Der Agent beschreibt im PR, was er getan hat. Deckt sich das mit
   dem Diff?
4. **Der Diff selbst.** Erst jetzt.
5. **Die Session-Logs.** Jeder Agent-Commit führt zurück zur Session, in der er
   entstanden ist. Da steht, was der Agent versucht, verworfen und gedacht hat.
   Das ist die ehrlichste Quelle im ganzen PR.

**Drei Dinge, die euch überraschen werden:**

* **Eure eigene Approval zählt nicht.** Wer den Task ausgelöst hat, kann den PR nicht
  freigeben. Ihr braucht ein zweites Augenpaar — genau wie bei Menschen.
* **CI läuft für Agent-PRs standardmäßig nicht automatisch.** Sie muss manuell
  freigegeben werden. Das ist Absicht: sonst führt ein PR aus dem Nichts eure Workflows aus.
* **Branch Protection und Review-Pflicht gelten unverändert.** Ein Agent-PR ist ein PR.

**Fertig, wenn** ihr in `modul-4/C2-entscheidung.md` festgehalten habt:

* Merge ja oder nein — **mit Begründung**
* Ein Punkt, an dem der Agent es besser gemacht hat, als ihr erwartet habt
* Ein Punkt, an dem ihr nachbessern musstet
* Credits und Laufzeit für den Messbogen aus Modul 3 — in der Demo-Variante nennt
  euch der Trainer die Zahlen aus seinem Lauf

> **Das ist die Übung, die ihr am Montag wirklich braucht.** Delegieren lernt man in
> einer Minute. Zu entscheiden, ob ein Agent-PR gemerged werden darf, ist die Arbeit,
> die bleibt — und die niemand euch abnimmt.

---

## C3 — Einen Agentic Workflow anlegen

**20 Minuten**

Jetzt vom Einzeltask zum wiederkehrenden. In `modul-4/workflow-vorlagen/` liegen drei
Workflows — **mit Lücken**. Eure Aufgabe: eine Vorlage vollständig machen, bis sie
kompiliert.

| Vorlage | Auslöser | Safe Output |
|---|---|---|
| `doku-drift.md` | nächtlich | `create-issue` |
| `testluecken-report.md` | wöchentlich | `create-pull-request` (Draft) |
| `ci-triage.md` | bei Pull Requests | `add-comment` |

**Vorbereitung**

```bash
gh extension install github/gh-aw     # falls noch nicht da
gh aw --version
gh aw init --engine copilot           # einmal pro Repo
```

> ⚠️ **`gh aw init` legt eine eigene `.github/workflows/copilot-setup-steps.yml` an** —
> für seine eigenen Zwecke. Wenn ihr die Vorlage aus `modul-4/` dorthin kopiert,
> überschreibt ihr sie (oder sie eure), und niemand warnt euch.
>
> Reihenfolge, die funktioniert: **erst `gh aw init`, dann hineinschauen.** Wenn dort
> schon eine Datei liegt, führt eure Setup-Steps-Aufgabe an einem anderen Dateinamen
> durch und vergleicht am Ende. Im echten Repo müsst ihr die beiden von Hand
> zusammenführen — auch das ist eine realistische Erfahrung.

**Ablauf**

```bash
cp modul-4/workflow-vorlagen/doku-drift.md .github/workflows/
# Lücken füllen ...
gh aw compile
```

`gh aw compile` ist euer schnellstes Feedback im ganzen Workshop: es sagt euch **exakt**,
was falsch ist, oft mit „Did you mean". Nutzt das als Übungsschleife, nicht als
Abschlussprüfung.

**Das Sicherheitsmodell — der Grund, warum das Ganze überhaupt vertretbar ist**

Der Agent läuft **read-only**. Er darf nichts schreiben. Was er ausgeben will, geht als
**Safe Output** an einen **separaten Job**, der ausschließlich die deklarierte Aktion
ausführt — Issue anlegen, kommentieren, Draft-PR öffnen. Dazwischen wird die Ausgabe
saniert.

Konsequenz: Selbst wenn ein manipuliertes Tool-Ergebnis den Agenten übernimmt, kann er
nur das tun, was in `safe-outputs:` steht. **Er schlägt vor, ein anderer Job führt aus.**

Der `strict`-Modus (Standard: an) blockiert deshalb Write-Permissions hart. Wer
`contents: write` schreibt, bekommt einen Compile-Fehler — kein Warnhinweis, einen Fehler.

**Die Lockfile-Regel, an der alle einmal scheitern**

```bash
gh aw compile     # erzeugt .github/workflows/<name>.lock.yml
```

GitHub Actions führt **ausschließlich YAML** in `.github/workflows` aus. Eure `.md`-Datei
ist für Actions unsichtbar. Nur die generierte `.lock.yml` läuft wirklich.

**Beide Dateien werden committet.** Aus 3 KB Markdown werden rund 100 KB Lockfile mit
SHA-gepinnten Actions, Digest-gepinnten Containern, Firewall-Proxy, MCP-Gateway und dem
kompletten Agent-Prompt. `gh aw init` markiert die Datei als `linguist-generated`, damit
sie im Diff eingeklappt ist — aber **versioniert** bleibt.

> **Merksatz:** Nach jeder Änderung an der `.md` sofort `gh aw compile` und beide
> Dateien committen. Der Compiler erkennt Drift über Hashes und meldet
> „Lock file is outdated".

**Ausprobieren, ohne etwas anzurichten**

```bash
gh aw trial ./.github/workflows/doku-drift.md --clone-repo <org>/<repo> --delete-host-repo-after
```

Trial-Modus läuft gegen ein **simuliertes** Repository: keine echten Issues, keine
echten PRs im Zielrepo. **Das ist der richtige Weg für den ersten Lauf.**

> Präzisierung, damit niemand überrascht wird: `gh aw trial` legt dafür ein
> **temporäres privates Repository in eurem GitHub-Konto** an. Es ist also nicht
> „nichts passiert", sondern „nichts passiert *dort, wo es weh tut*".
> `--delete-host-repo-after` räumt es wieder weg — benutzt das Flag.

**Wenn ihr scharf schalten wollt**

```bash
git add .github/workflows/doku-drift.md .github/workflows/doku-drift.lock.yml .gitattributes
git commit -m "feat(build): ergaenze Doku-Drift-Workflow"
git push
gh aw status
gh aw run doku-drift
gh aw logs doku-drift
gh aw audit <run-id> --parse
```

**Fertig, wenn** `gh aw compile` **null Warnungen** meldet, ihr erklären könnt, warum
euer Workflow genau die Permissions hat, die er hat — **und die Prüfliste unten
abgehakt ist.**

> ⚠️ **Der Compiler allein reicht als Abnahme nicht.** Wir haben es nachgemessen:
> **`ci-triage.md` kompiliert mit ALLEN sechs offenen TODOs fehlerfrei und ohne
> Warnung.** Wer nur auf „0 warnings" schaut, hält eine unfertige Datei für fertig.
>
> Das ist kein Fehler der Vorlage, sondern die Lektion: **Ein Compiler prüft Form,
> kein Verständnis.** Genau deshalb gibt es Reviews.
>
> Prüfliste je Vorlage — geht sie zu zweit durch:
>
> * `doku-drift.md` — Zeitplan gesetzt? Manueller Auslöser da? Permissions für **alle**
>   Toolsets? `bash` mit **echter Liste** (nicht `true`)? `max` beim Issue?
> * `testluecken-report.md` — `java` im Netz-Allowlist? `edit`-Tool da? Draft-PR?
>   `if-no-changes` gesetzt?
> * `ci-triage.md` — `roles` **unter `on:`**? `actions: read` **und** Toolset `actions`?
>   `target` beim Kommentar? `max: 1`?
>
> Und die zwei, die sauber durchkompilieren und trotzdem falsch sind:
> **`bash: [":*"]`** und **`add-comment` ohne `target`**.

> Eine Zeile bleibt trotzdem stehen: der `info:`-Hinweis zu
> `permissions.copilot-requests: write`. Das ist eine **Info**, keine Warnung, und
> erscheint bei jedem Compile mit `engine: copilot`. Die Zusammenfassung am Ende zählt
> ihn nicht mit — achtet auf die Zeile „Compiled 1 workflow: 1 succeeded, 0 warnings".
>
> Und: **benutzt `gh aw compile` ohne `--verbose`.** Mit `--verbose` zählt der Compiler
> je Datei eine interne Meldung („Schema validation available but skipped") als Warnung
> mit. Ihr hättet die Aufgabe gelöst und würdet es nicht merken.

**Der Compiler ist kein Policy-Werkzeug.** Er prüft Syntax und Permissions-Konsistenz,
nicht eure Absicht. „Kompiliert sauber" heißt nicht „ist in Ordnung". Genau dafür gibt
es das Review — bei Workflows genauso wie bei Code.

> Noch zwei Beobachtungen aus dem Testlauf, damit ihr nicht rätselt:
> Der Compiler meldet **pro Datei nur einen Fehler je Lauf** — nach dem Fix kann also
> sofort der nächste auftauchen. Und die `i →`-Zeile unter einer Fehlermeldung ist
> gelegentlich unpassend; **verlasst euch auf die erste Zeile**, nicht auf den Nachsatz.

### Die fünf Fallstricke, die euch garantiert treffen

1. **Fehlende Permission für ein Toolset.** `toolsets: [default]` enthält
   `pull_requests` — also braucht ihr `pull-requests: read`. Häufigster Warnfall.
2. **Write-Permission im Strict-Modus.** Siehe oben. Nicht umgehen — umdenken.
3. **Tippfehler in `safe-outputs`.** `create-issues` gibt es nicht, `create-issue` schon.
4. **`roles:` an der falschen Stelle.** Es gehört unter `on:`, nicht auf die oberste
   Ebene. Der Compiler sagt es euch wörtlich.
5. **`timeout-minutes` mit Bindestrich**, nicht mit Unterstrich.

Und einer, der keiner ist: bei `engine: copilot` erscheint bei **jedem** Compile ein
Hinweis zur token-basierten Inference. Das ist kein Fehler.

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

**Der Auftrag:** Skizziert einen Workflow für **euren** Alltag, nicht für die
Voltwerk-Suite. Sechs Felder müssen ausgefüllt sein:

**Trigger** (Zeitplan oder Ereignis) · **Auftrag in zwei Sätzen** · **erlaubte Tools** ·
**Safe Output** (Issue, Kommentar oder Draft-PR) · **Owner** · **Budget**

**Ablauf**

* 7 Minuten entwerfen
* 3 Minuten Speed-Feedback: Entwurf mit der Nachbargruppe tauschen und kommentieren.
  **Kommentieren, nicht diskutieren** — die Zeit reicht nicht für beides.

**Die Pflichtfrage am Ende — und die ist keine Formalie:**

> Was ist der Schaden, wenn dieser Workflow Unsinn liefert?
> Und wer fängt ihn am Gate ab — mit Namen, nicht mit Rolle?

Wer den Schaden nicht benennen kann, hat den Workflow nicht zu Ende gedacht. Das ist
kein Vorwurf, sondern der billigste Zeitpunkt, es zu merken.

**Ergebnis:** ein umsetzbarer Entwurf pro Gruppe. Den besten setzen wir nach dem
Workshop gemeinsam auf.

---

## Die Umgebung, in der das läuft

`modul-4/copilot-setup-steps.yml.vorlage` ist die Datei, die die Arbeitsumgebung des
Agenten definiert. Sie gehört nach `.github/workflows/copilot-setup-steps.yml`.

Heute läuft alles auf **GitHub-hosted Runnern** (`ubuntu-latest`). Ihr braucht dafür
keine eigene Infrastruktur — nur ein Repo, in dem der Coding Agent aktiviert ist.

**Die harten Regeln:**

* Der Job **muss** `copilot-setup-steps` heißen. Anderer Name = wird ignoriert.
* Nur **sechs** Job-Keys werden ausgewertet:
  `steps`, `permissions`, `runs-on`, `services`, `snapshot`, `timeout-minutes`.
  **Alles andere wird still ignoriert** — auch ein `env:` auf Job-Ebene.
  Environment-Variablen gehören auf **Step-Ebene**.
* `timeout-minutes` maximal **59**.
* `runs-on` ist heute **`ubuntu-latest`** — GitHub-hosted, keine eigene Infrastruktur nötig.
* **Secrets für den Agenten sind ein eigener Typ.** Sie liegen unter
  *Settings → Secrets and variables → **Agents***. Actions-Secrets sind für den Agenten
  **unsichtbar**. Der Präfix `COPILOT_MCP_` hat Sonderverhalten.
* Schlagen die Setup-Steps fehl, **bricht der Agent nicht ab** — er arbeitet ohne die
  Umgebung weiter. Genau deshalb müsst ihr ins Setup-Log schauen und nicht nur auf den Diff.

---

## Der Schritt danach: eigene Runner

**3 Minuten · Konzept, kein Lab**

Heute läuft der Agent auf GitHub-hosted Runnern. Das ist der richtige Anfang: Ihr
lernt Delegation, Review und Gate, ohne gleichzeitig Infrastruktur zu debuggen.

Der Coding Agent unterstützt aber auch **self-hosted Runner** (GA). Das ist der Schritt,
den ihr geht, sobald der Agent an Code arbeiten soll, der euer Netz nicht verlässt, oder
Abhängigkeiten braucht, die nur intern erreichbar sind.

**Was sich dann ändert — und was es kostet:**

| | GitHub-hosted (heute) | Self-hosted (später) |
|---|---|---|
| `runs-on` | `ubuntu-latest` | euer Runner-Pool |
| Netzgrenze | eingebaute Copilot-Firewall | **eure** Egress-Allowlist |
| Abhängigkeiten | Maven Central | euer Repository-Proxy |
| Runner-Lebensdauer | von GitHub verwaltet | ephemer, das ist Pflicht |
| Kosten | Actions-Minuten | Actions-Minuten **plus** euer Compute |
| Verantwortung | GitHub | ihr |

Die entscheidende Zeile ist die vorletzte: **Die eingebaute Firewall wird für
self-hosted Runner deaktiviert.** Eure Allowlist tritt an ihre Stelle. Das ist keine
Konfigurationsdetail, sondern eine Übergabe von Verantwortung.

**Die Frage für eure Rückfahrt:** Wer in eurem Haus besitzt diese Allowlist, und wer darf
sie ändern? Wenn ihr darauf keine Antwort mit einem Namen habt, ist der Wechsel auf
eigene Runner noch nicht vorbereitet — egal wie fertig die Technik ist.

Anhang C der Vorlage `copilot-setup-steps.yml.vorlage` enthält die Checkliste dazu.

---

## Checkpoint für das Plenum

1. Welche Karte war die schlechte — und welcher Teil davon ist **gar nicht**
   delegierbar?
2. Eure Merge-Entscheidung aus C2, mit Begründung. Credits und Laufzeit an die Tafel,
   neben eure Schätzung aus Modul 3.
3. Ein Satz zu eurem Workflow: Trigger, Safe Output, Owner.
4. **Die Pflichtfrage:** Was ist der Schaden, wenn euer Workflow Unsinn liefert — und
   wer fängt ihn am Gate ab?
