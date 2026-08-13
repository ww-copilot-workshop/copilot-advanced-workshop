# Messbogen — Kostenlabor

**Team:** ______________________  **Datum:** ______________

Vor jedem Lauf `/clear`. Nach jedem Lauf `/usage` und `/context` ablesen.

---

## Der Auftrag (für alle vier Läufe wortgleich)

> Vergleiche `modul-5/src/main/java/de/voltwerk/abrechnung/AbrechnungsService.java`
> mit `modul-5/SPEC.md`. Liste jede Abweichung als Zeile:
> Spec-Regel | beobachtetes Verhalten | erwartetes Verhalten.
> Keine Codeänderungen, keine Zusammenfassung.

---

## Messung

| # | Variante | Modell | AI Credits | Input-Tok. | Output-Tok. | Cached-Tok. | Cache-% | Dauer | Befunde gesamt | davon echt | **Credits / echtem Befund** |
|---|---|---|---|---|---|---|---|---|---|---|---|
| 1 | Naiv, großes Modell, kein Pfad | | | | | | | | | | |
| 2 | Gezielt, Pfade genannt | | | | | | | | | | |
| 3 | Sparsam, kleines Modell | | | | | | | | | | |
| 4 | `/model auto` | | | | | | | | | | |

**Kontextfenster vor dem ersten Wort** (`/context`, frische Session):

| Verursacher | Tokens |
|---|---|
| System-Prompt | |
| Tools / MCP-Server | |
| Skills | |
| Nachrichten | |
| **Summe** | |

---

## Auswertung

**Gewinner nach Credits je echtem Befund:** ______________________

**Faktor zwischen teuerstem und billigstem Lauf:** ______

**Wie viele Befunde haben *alle* vier Läufe gefunden?** ______
*(Das ist der belastbare Kern. Alles darüber hinaus braucht einen Menschen.)*

**Welcher Lauf hat etwas erfunden?** ______________________

---

## Schätzungen (Gruppenarbeit C1)

| Szenario | Schätzung Credits | Schätzung Actions-Min. | Erste Stellschraube | **Ist (nach Lab C)** |
|---|---|---|---|---|
| (a) Delegierter Bugfix, einmalig | | | | |
| (b) Doku-Drift nightly, 30 Läufe | | | | |
| (c) Sprint, 5 Tasks/Tag × 10 Tage | | | | |

**Unsere größte Fehleinschätzung war:** ______________________________________

**Weil wir übersehen haben:** _________________________________________________

---

## Drei Sätze für Montag

1. Diese Stellschraube setzen wir zuerst: _____________________________________
2. Diese Zahl wollen wir monatlich sehen: _____________________________________
3. Dieser Workflow bekommt einen Owner: _______________________________________
