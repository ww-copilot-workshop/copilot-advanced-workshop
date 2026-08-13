# Modul 3 — Das Kostenlabor

**Zeit:** ca. 35 Minuten · **Sozialform:** Zweierteams, danach Vierergruppen

> Die Faustregel des Moduls: **Der teuerste Lauf ist der, den niemand reviewt und der
> deshalb wiederholt werden muss.** Alles andere ist Optimierung an der zweiten
> Nachkommastelle.

---

## Das Cockpit — wo die Zahlen stehen

| Ort | Was er zeigt |
|---|---|
| **Footer** | Laufend: verbleibendes AI-Credit-Budget |
| `/usage` | AI Credits dieser Session, Tokens nach Input / Output / Cached, Limit-Fortschritt |
| `/context` | Kontextfenster nach Verursachern: System-Prompt, Tools, Nachrichten |
| `/model` | Modellauswahl **mit Preisschild** — Kosten je Token für Input, Cached Input, Output |
| `/limits` | Limits dieser Session ansehen und setzen |
| `/statusline` | Option `quota` (Budget gegen Plan-Limit) und `ai-used` (Credits dieser Session) |
| `/exit` | Abschlussbericht der Session |

Zwei Begriffe, die ihr nicht verwechseln dürft:

* **AI Credits** — token-basiert, nach Input, Output und Cache-Anteil. Das ist der
  aktuelle Modus.
* **Premium Requests** — die ältere, request-basierte Abrechnung mit Modell-Multiplikator.
  Wenn ihr die in eurem Footer seht, seid ihr noch auf der alten Plattform. Die
  Konzepte unten gelten trotzdem, nur die Einheit ist eine andere.

**Session-Limits** sind Opt-in und ein **weicher** Deckel:

```bash
copilot --max-ai-credits 30           # Minimum sind 30 Credits
```

```
/limits                               # ansehen und bearbeiten
/limits set max-ai-credits 30
```

Was „weich" bedeutet: Der Verbrauch ist erst bekannt, **nachdem** eine Antwort
zurückkommt. Ein einzelner Aufruf kann das Limit also überschreiten; blockiert wird
erst der **nächste**. Subagenten teilen sich das Limit der Elternsession. Auch
unsichtbare Arbeit wie das Kompaktieren zählt mit. In einer interaktiven Session
setzt `/clear` die Zählung zurück, das Limit bleibt.

Für Autopilot gibt es einen zweiten Deckel: `--max-autopilot-continues` (Standard 5)
begrenzt, wie oft sich der Agent selbst weiterschickt, bevor er anhält.

---

## C0 — Das Kostenlabor: derselbe Auftrag, vier Wege

**15 Minuten · Wettbewerb**

**Der Auftrag** — für alle vier Läufe **wortgleich**:

> Vergleiche `modul-5/src/main/java/de/voltwerk/abrechnung/AbrechnungsService.java`
> mit `modul-5/SPEC.md`. Liste jede Abweichung als Zeile:
> Spec-Regel | beobachtetes Verhalten | erwartetes Verhalten.
> Keine Codeänderungen, keine Zusammenfassung.

**Die vier Läufe.** Vor jedem Lauf `/clear`, danach `/usage` ablesen und eintragen.

| # | Variante | Wie |
|---|---|---|
| 1 | **Naiv** | Größtes verfügbares Modell, kein Pfad genannt, kein Kontext-Trimming. Einfach den Auftrag hineinwerfen. |
| 2 | **Gezielt** | Dasselbe Modell, aber die beiden Dateien explizit im Prompt nennen. |
| 3 | **Sparsam** | `/model` auf ein kleines Modell, sonst wie Lauf 2. |
| 4 | **Auto** | `/model auto` — die CLI wählt selbst, sonst wie Lauf 2. |

Tragt alles in `modul-3/messbogen.md` ein.

**Die eigentliche Kennzahl** steht ganz rechts:

```
Credits je brauchbarem Befund = Credits / Anzahl korrekter Abweichungen
```

Ein Lauf, der viermal so viel kostet und doppelt so viele echte Befunde liefert, ist
**teurer**, nicht besser. Ein Lauf, der billig ist und Unsinn erzählt, ist am teuersten,
weil ihr ihn wiederholen müsst.

> Zur Kalibrierung: Der Code weicht an **acht** Stellen von der Spec ab. Die genaue
> Liste bekommt ihr erst nach Modul 5 — schätzt bis dahin selbst, was ein echter Befund
> ist und was Halluzination.

**Beobachtet außerdem:**

* **Cache-Anteil.** Der zweite Lauf mit demselben Kontext ist deutlich billiger als der
  erste. Genau deshalb ist `/fork` billig und ein `/clear` teuer.
* **`/context` vor und nach dem Lauf.** Wie viel des Fensters geht für Tools und
  System-Prompt drauf, bevor überhaupt ein Wort eurer Frage ankommt? Bei installierten
  Skills und MCP-Servern verschiebt sich das spürbar.
* **Der Preisunterschied im `/model`-Picker.** Schaut ihn einmal an. Die Spanne zwischen
  dem größten und dem kleinsten Modell ist größer, als die meisten schätzen.

---

## C1 — Drei Schätzungen

**10 Minuten · Vierergruppen**

Jede Gruppe schätzt **Credits** und **Actions-Minuten** für drei Szenarien. Erst
schätzen, dann diskutieren — nicht umgekehrt.

| Szenario | Credits | Actions-Min. | Erste Stellschraube |
|---|---|---|---|
| **(a)** Ein delegierter Bugfix auf eurem Azure-Runner: mittelgroßes Java-Modul, Test vorhanden, Draft-PR am Ende | | | |
| **(b)** Der nächtliche Doku-Drift-Workflow, einen Monat lang, 30 Läufe | | | |
| **(c)** Ein Sprint: fünf parallele Tasks pro Tag, zehn Arbeitstage | | | |

Dazu je Szenario: **welche Stellschraube setzt ihr zuerst?**

1. **Session-Limits** — Credit-Obergrenze pro Session in CLI und SDK. Ideal für alles,
   was unbeaufsichtigt läuft.
2. **Budgets mit Warnstufen** — Enterprise, Cost Center und pro Nutzer. Alarme bei
   75 / 90 / 100 Prozent. **Budget 0 stoppt sofort.** Der inkludierte Pool je Cost
   Center lässt sich per REST-API deckeln.
3. **Workflows budgetieren** — jeder wiederkehrende Agent-Lauf bekommt ein eigenes
   Preisschild und einen namentlichen Owner.
4. **Attribution** — Reports trennen inkludiert und zusätzlich. Kosten je Team und je
   Workflow sichtbar machen, bevor jemand fragt.

**Die zwei Zähler.** Cloud-Läufe kosten immer **beides**: AI Credits für die Arbeit des
Agenten **und** Actions-Minuten auf eurem Runner. Wer nur auf einen Zähler schaut,
unterschätzt systematisch. Bei einem self-hosted ARC-Pool auf Azure kommen die
Compute-Kosten des Scale Sets noch obendrauf — die stehen in **keinem** GitHub-Report.

**Nach Lab C füllt ihr die vierte Spalte:** die echten Zahlen. Die Spannbreite zwischen
Schätzung und Messung ist der interessante Teil, nicht der Mittelwert.

---

## C2 — Vorhersagen statt raten

**5 Minuten · zum Anschauen, für Modul 4 zum Mitnehmen**

Für Agentic Workflows gibt es einen eingebauten Kostenschätzer:

```bash
gh aw forecast
```

Er sagt den AI-Credit-Verbrauch eurer Workflows voraus, **bevor** ihr sie scharf
schaltet. Kombiniert mit

```bash
gh aw health          # Erfolgsquoten je Workflow
gh aw audit <run-id>  # was ein einzelner Lauf wirklich getan hat
```

habt ihr die drei Zahlen, die eine Betriebsentscheidung tragen: **Was kostet er, wie oft
funktioniert er, was hat er beim letzten Mal getan?**

Ein Workflow ohne diese drei Zahlen ist kein Betriebsmittel, sondern ein Hobby.

---

## Sieben Sparhebel, in der Reihenfolge ihrer Wirkung

1. **Nicht wiederholen müssen.** Ein präziser Auftrag mit Abnahmekriterien schlägt jede
   Modelloptimierung. Alles andere auf dieser Liste ist Kosmetik dagegen.
2. **Kontext gezielt setzen.** Pfade nennen statt „schau dich mal um". `/add-dir` nur,
   wo es gebraucht wird.
3. **Cache nutzen.** Gleicher Kontext ist billig. `/fork` statt `/clear`, wenn ihr auf
   dem Aufgebauten weiterarbeitet.
4. **Kleines Modell für kleine Aufgaben.** `/model auto` respektiert dabei eure
   Org-Policies.
5. **`/compact` statt endloser Sessions** — aber wissen, dass Kompaktieren selbst
   Credits kostet und mit aufs Session-Limit geht.
6. **Skills und MCP-Server schlank halten.** Jeder geladene Skill und jedes registrierte
   Tool sitzt im Kontext, bevor ihr das erste Wort tippt. `/context` zeigt es.
7. **Limits setzen, bevor etwas unbeaufsichtigt läuft.** `--max-ai-credits` und
   `--max-autopilot-continues` kosten euch nichts und retten euch einmal im Quartal.

---

## Checkpoint für das Plenum

Zahlen an die Tafel:

1. Die vier Zeilen aus dem Messbogen — welcher Weg hatte die besten Credits je Befund?
2. Die drei Schätzungen je Gruppe. Wir sammeln die **Spannbreite**, nicht den Mittelwert.
3. Eine Stellschraube, die ihr diese Woche tatsächlich setzt.
