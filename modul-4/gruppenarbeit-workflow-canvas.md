# Gruppenarbeit Modul 4 — Entwerft euren eigenen Agentic Workflow

**10 Minuten · Vierergruppen · eine Vorlage pro Gruppe**
*(7 Min entwerfen · 3 Min Speed-Feedback mit der Nachbargruppe)*

**Gruppe:** ______________________  **Owner (Name, nicht „das Team"):** ______________

---

## 1. Wofür?

Ein Satz. Welches wiederkehrende Ärgernis aus eurem Alltag verschwindet, wenn dieser
Workflow läuft?

> ______________________________________________________________________________

> ______________________________________________________________________________

---

## 2. Trigger

Was löst ihn aus? Genau eines ankreuzen. **Alle drei stehen unter `on:`**, nicht
daneben — das ist der häufigste Compiler-Fehler.

- [ ] **Zeitplan** → `schedule:` ______________________ (`daily`, `weekly on monday`, Cron)
- [ ] **Ereignis** → `pull_request:` / `issues:` / ______________________
- [ ] **Auf Zuruf** → `slash_command: { name: ______________________ }`

> **Ereignis angekreuzt? Dann fehlt eine Zeile.** Auf Pull Requests reagiert der
> Workflow auch bei **fremden** PRs aus Forks. `roles:` entscheidet, für wen er
> überhaupt anläuft — und gehört ebenfalls unter `on:`.
>
> `roles: [` ______________________ `]`   ← z. B. `admin, maintainer, write`

> Wenn ihr „bei jedem Push" ankreuzt: rechnet einmal durch, wie oft das am Tag ist,
> und multipliziert es mit euren Credits. Die meisten Entwürfe werden hier billiger.

---

## 3. Der Auftrag — in zwei Sätzen

Nicht mehr. Wenn ihr drei braucht, sind es zwei Workflows.

> ______________________________________________________________________________

> ______________________________________________________________________________

---

## 4. Erlaubte Werkzeuge

Least Privilege. Was streicht ihr wieder, nachdem ihr es aufgeschrieben habt?

| | Braucht er? | Wenn ja: wie eng? |
|---|---|---|
| `github` (Toolsets) | ☐ | ______________________ |
| `bash` | ☐ | Allowlist: ______________________ |
| `edit` | ☐ | nur diese Datei: ______________________ |
| `web-fetch` | ☐ | Domains: ______________________ |
| eigener MCP-Server | ☐ | ______________________ |

> Erinnerung aus Lab C: `bash: true` ist **nicht** die brave Variante — das ist dasselbe
> wie „alles". Nur eine echte Liste schränkt ein.

> **`web-search` steht bewusst nicht in der Tabelle.** Mit `engine: copilot` gibt es
> das Werkzeug nicht; der Compiler warnt, und euer Ziel „0 Warnungen" ist dahin.
> Braucht ihr wirklich Suche, ist das eine Frage an die Engine — nicht an die Tabelle.

---

## 5. Safe Output

Was darf am Ende **wirklich** passieren? Genau eines.

- [ ] `create-issue` — max. ______ pro Lauf
- [ ] `add-comment` — Ziel: ______________________
- [ ] `create-pull-request` — Draft? ☐ ja  ☐ nein
- [ ] anderes: ______________________

**Permissions:** der Agent selbst bleibt `read`. Was schreibt, ist der Safe-Output-Job.

> Nehmt zusätzlich **`missing-tool:`** dazu. Dann meldet der Agent, wenn ihm etwas
> fehlt, statt sich still etwas auszudenken. Kostet eine Zeile.

---

## 6. Budget

| | |
|---|---|
| Erwartete Läufe pro Monat | ______ |
| Geschätzte Credits pro Lauf | ______ |
| **Summe pro Monat** | ______ |
| Actions-Minuten pro Monat | ______ |
| `timeout-minutes` je Lauf | ______ |
| Wer schaut monatlich drauf? | ______________________ |

Der Deckel ist die eigentliche Bremse: Ein Agent, der sich festbeißt, kostet so lange,
bis ihn jemand stoppt. `timeout-minutes` stoppt ihn ohne jemanden.

`gh aw forecast` projiziert die Credits — aber aus **vergangenen** Läufen. Vor dem
allerersten Lauf gibt es nichts zu sampeln; da bleibt es bei eurer Schätzung.

---

## 7. Die Pflichtfrage

**Was ist der Schaden, wenn dieser Workflow Unsinn liefert?**

> ______________________________________________________________________________

**Wer fängt ihn am Gate ab? Name, nicht Rolle.**

> ______________________________________________________________________________

**Woran merkt ihr, dass er Unsinn liefert — bevor es jemand anderes merkt?**

> ______________________________________________________________________________

---

## 8. Erster Schritt nach dem Workshop

Etwas, das eine Person an einem Vormittag schafft.

> ______________________________________________________________________________

---

### Speed-Feedback der Nachbargruppe

Zwei Fragen, keine Diskussion — nur aufschreiben:

**Was ist zu weit gefasst?**

> ______________________________________________________________________________

**Was fehlt, das im Betrieb weh tun wird?**

> ______________________________________________________________________________
