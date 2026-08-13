# Modul 4 — Lab C: Delegieren auf eurer Infrastruktur

**Zeit:** ca. 60 Minuten · **Sozialform:** Zweierteams

> Der Agent ist Gast auf eurer Infrastruktur und hält sich an eure Hausordnung.
> Heute schreibt ihr die Hausordnung.

---

## Die drei Autonomiestufen — und wann welche

| Stufe | Wo läuft es | Wann |
|---|---|---|
| **Interaktiv** | Euer Rechner, jede Aktion bestätigt | Ihr lernt gerade etwas über den Code |
| **Autopilot** | Euer Rechner, Aktionen laufen durch | Abgegrenzte Aufgabe, ihr schaut zu, Sandbox an |
| **`/delegate`** | GitHub, auf **eurem** Runner | Ihr wollt die Aufgabe loswerden und weiterarbeiten |

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

## C1 — Der erste echte Lauf

**15 Minuten**

In `modul-4/aufgabenkarten/` liegen vier Karten. **Drei sind gut. Eine ist Müll.**
Findet heraus, welche — bevor ihr delegiert.

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
3. Delegiert **eine gute Karte**:

   ```
   /delegate
   ```

   oder

   ```bash
   gh agent-task create -F modul-4/aufgabenkarten/C1-vw-4711.md
   ```

4. Verfolgt den Lauf im **Agents-Tab** auf github.com (in der Doku heißt er so; die
   Blogposts nennen ihn „Agent HQ" oder „Mission Control" — dasselbe Ding).
   Oder im Terminal mit `gh agent-task view <id>`.

**Denkt daran:** Ein Issue, das ihr dem Agenten zuweist, ist ein **Prompt**. Lest eure
Karte noch einmal mit dieser Brille. Würdet ihr sie so in ein Chatfenster tippen?

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

## C2 — Review am Gate

**15 Minuten**

Der Draft-PR ist der spannende Teil, nicht der Diff.

**Prüft in dieser Reihenfolge:**

1. **Das Setup-Log.** Sind die Setup-Steps gelaufen? Kam die Dependency-Auflösung über
   Artifactory? Steht dort euer ARC Scale Set als Runner?
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
* Credits und Laufzeit (für den Messbogen aus Modul 3)

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

Trial-Modus läuft gegen ein simuliertes Repository. Keine echten Issues, keine echten PRs.
**Das ist der richtige Weg für den ersten Lauf** — im Workshop und bei euch daheim.

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

**Fertig, wenn** `gh aw compile` **null Warnungen** meldet und ihr erklären könnt, warum
euer Workflow genau die Permissions hat, die er hat.

> Eine Zeile bleibt trotzdem stehen: der `info:`-Hinweis zu
> `permissions.copilot-requests: write`. Das ist eine **Info**, keine Warnung, und
> erscheint bei jedem Compile mit `engine: copilot`. Die Zusammenfassung am Ende zählt
> ihn nicht mit — achtet auf die Zeile „Compiled 1 workflow: 1 succeeded, 0 warnings".
>
> Und: **benutzt `gh aw compile` ohne `--verbose`.** Mit `--verbose` zählt der Compiler
> je Datei eine interne Meldung („Schema validation available but skipped") als Warnung
> mit. Ihr hättet die Aufgabe gelöst und würdet es nicht merken.

**Und der unbequeme Teil, den ihr nicht übersehen dürft:** Zwei sicherheitsrelevante
Falschlösungen kompilieren **fehlerfrei und ohne Warnung** —

* `bash: [":*"]` (der Agent darf jedes Shell-Kommando ausführen)
* `add-comment` ohne `target` (der Kommentar kann irgendwo landen)

**Der Compiler ist kein Policy-Werkzeug.** Er prüft Syntax und Permissions-Konsistenz,
nicht eure Absicht. „Kompiliert sauber" heißt nicht „ist in Ordnung". Genau dafür gibt
es das Review — bei Workflows genauso wie bei Code.

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

**Wenn Zeit bleibt**

Delegiert drei kleine Tasks gleichzeitig (die drei guten Karten) und beobachtet sie
parallel im Agents-Tab. Jeder bekommt einen eigenen Branch.

Fragen für die Runde:

* Ab welcher Anzahl paralleler Agents wird **Review** zum Engpass — nicht die Ausführung?
* Was passiert, wenn zwei Agent-PRs dieselbe Datei anfassen?
* Wer räumt die Branches auf, wenn ein PR nicht gemerged wird?

Das ist keine Technikfrage. Das ist die Frage, an der Agent-Programme scheitern.

---

## Die Infrastruktur, auf der das läuft

`modul-4/copilot-setup-steps.yml.vorlage` ist die Datei, die eure Umgebung definiert.
Sie gehört nach `.github/workflows/copilot-setup-steps.yml`.

**Die harten Regeln:**

* Der Job **muss** `copilot-setup-steps` heißen. Anderer Name = wird ignoriert.
* Nur **sechs** Job-Keys werden ausgewertet:
  `steps`, `permissions`, `runs-on`, `services`, `snapshot`, `timeout-minutes`.
  **Alles andere wird still ignoriert** — auch ein `env:` auf Job-Ebene.
  Environment-Variablen gehören auf **Step-Ebene**.
* `timeout-minutes` maximal **59**.
* `runs-on` zeigt auf euer ARC Scale Set.
* Unterstützt sind **Ubuntu x64** und Windows 64-bit. Persistente Runner sind tabu —
  jeder Lauf ist ephemer.
* Für self-hosted Runner muss die **eingebaute Copilot-Firewall deaktiviert** werden.
  Eure Egress-Allowlist übernimmt deren Rolle. Das ist eine bewusste Übergabe von
  Verantwortung — schreibt auf, wer sie hat.
* **Secrets für den Agenten sind ein eigener Typ.** Sie liegen unter
  *Settings → Secrets and variables → **Agents***. Actions-Secrets sind für den Agenten
  **unsichtbar**. Der Präfix `COPILOT_MCP_` hat Sonderverhalten.
* `actions/setup-java` hat **keinen** Input für Maven-Mirrors. Die `settings.xml` für
  Artifactory müsst ihr selbst schreiben — die Vorlage zeigt wie, inklusive des
  gequoteten Heredocs, damit `${env.X}` von Maven und nicht von der Shell aufgelöst wird.
* Schlagen die Setup-Steps fehl, **bricht der Agent nicht ab** — er arbeitet ohne die
  Umgebung weiter. Genau deshalb müsst ihr ins Setup-Log schauen und nicht nur auf den Diff.

---

## Checkpoint für das Plenum

1. Welche Karte war die schlechte — und was genau hat gefehlt?
2. Credits und Laufzeit eures delegierten Tasks (an die Tafel, neben eure Schätzung).
3. Ein Satz zu eurem Workflow: Trigger, Safe Output, Owner.
4. **Die Pflichtfrage:** Was ist der Schaden, wenn euer Workflow Unsinn liefert — und
   wer fängt ihn am Gate ab?
