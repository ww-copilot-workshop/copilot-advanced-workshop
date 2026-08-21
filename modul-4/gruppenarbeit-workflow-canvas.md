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

Was löst ihn aus? Genau eines ankreuzen, dazu den konkreten Wert.

- [ ] **Zeitplan** → `schedule:` ______________________ (täglich / wöchentlich / Cron)
- [ ] **Ereignis** → `on:` ______________________ (`pull_request`, `issues`, …)
- [ ] **Auf Zuruf** → `slash_command:` `/______________________`

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
| `web-fetch` / `web-search` | ☐ | ______________________ |
| eigener MCP-Server | ☐ | ______________________ |

> Erinnerung aus Lab C: `bash: true` ist **nicht** die brave Variante — das ist dasselbe
> wie „alles". Nur eine echte Liste schränkt ein.

---

## 5. Safe Output

Was darf am Ende **wirklich** passieren? Genau eines.

- [ ] `create-issue` — max. ______ pro Lauf
- [ ] `add-comment` — Ziel: ______________________
- [ ] `create-pull-request` — Draft? ☐ ja  ☐ nein
- [ ] anderes: ______________________

**Permissions:** der Agent selbst bleibt `read`. Was schreibt, ist der Safe-Output-Job.

---

## 6. Budget

| | |
|---|---|
| Erwartete Läufe pro Monat | ______ |
| Geschätzte Credits pro Lauf | ______ |
| **Summe pro Monat** | ______ |
| Actions-Minuten pro Monat | ______ |
| Wer schaut monatlich drauf? | ______________________ |

`gh aw forecast` sagt euch die Credits voraus, bevor ihr scharf schaltet.

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
