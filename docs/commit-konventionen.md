# Commit-Konventionen der Voltwerk-Plattformteams

Verbindlich seit 01.03.2024, abgestimmt zwischen Plattform, Abrechnung und Betrieb.
Diese Seite ist die **Rohquelle**. In Lab B1 verpackt ihr sie zu einem Skill.

---

## 1. Aufbau einer Commit-Message

```
<typ>(<bereich>): <betreff>

<rumpf>

<fussnoten>
```

### Betreffzeile

* **Typ** aus der Liste in Abschnitt 2. Kleingeschrieben.
* **Bereich** ist das Maven-Modul oder das fachliche Paket: `abrechnung`, `telemetrie`,
  `mcp`, `build`, `docs`. Bei mehreren Bereichen den führenden nennen.
* **Betreff** auf Deutsch, **Imperativ**, ohne Punkt am Ende, maximal 72 Zeichen.
  „korrigiere Rundung", nicht „Rundung korrigiert" und nicht „fixed rounding".
* Betreff beschreibt die **Wirkung**, nicht die Tätigkeit.
  Gut: `fix(abrechnung): runde Endbetrag kaufmännisch statt abzuschneiden`
  Schlecht: `fix(abrechnung): AbrechnungsService.java angepasst`

### Rumpf

* Nur, wenn er etwas erklärt, das im Diff nicht steht: **warum**, welche Alternative
  verworfen wurde, welche Auswirkung auf laufende Systeme.
* Zeilenumbruch bei 72 Zeichen.
* Keine Aufzählung der geänderten Dateien.

### Fußnoten

* `Ticket: VW-1234` — Pflicht bei `fix` und `feat`.
* `Spec: VW-SPEC-ABR §S-6` — Pflicht, wenn fachliches Verhalten sich ändert.
* `Breaking-Change: <Beschreibung>` — bei inkompatiblen Änderungen.
* `Co-authored-by:` bei Pair- oder Agent-Arbeit.

---

## 2. Erlaubte Typen

| Typ | Wofür | Beispiel |
|---|---|---|
| `feat` | neue fachliche Funktion | `feat(telemetrie): erkenne Überhitzungsverdacht` |
| `fix` | Fehlerbehebung mit Ticketbezug | `fix(abrechnung): deckele Blockiergebühr bei 12 EUR` |
| `refactor` | Umbau ohne Verhaltensänderung | `refactor(abrechnung): ersetze Tarif-Kaskade durch versiegelten Typ` |
| `test` | nur Tests | `test(abrechnung): ergänze Soll-Tests aus SPEC` |
| `docs` | nur Dokumentation | `docs(abrechnung): halte Befunde zur Nachtlogik fest` |
| `build` | Maven, Abhängigkeiten, CI | `build(deps): hebe jackson-databind auf 2.17.2` |
| `chore` | Aufräumen ohne Produktionscode | `chore: entferne verwaiste Übungsbranches` |
| `perf` | Laufzeit- oder Speicherverhalten | `perf(telemetrie): vermeide doppeltes Parsen` |

Nicht erlaubt: `wip`, `misc`, `update`, `stuff`, `various fixes`, leerer Betreff,
Emoji im Betreff.

---

## 3. Regeln, die uns wichtig sind

1. **Ein Commit, ein Gedanke.** Refactoring und Bugfix werden getrennt. Wer beides
   zusammen committet, macht den Fix unreviewbar.
2. **Kein `fix` ohne Ticket.** Wenn es kein Ticket gibt, ist es kein Fix, sondern ein
   `refactor` oder ein `chore`.
3. **Verhaltensänderungen nennen die Spec-Stelle.** Wer `§S-3` ändert, schreibt `§S-3`
   in die Fußnote. Das Rechnungswesen liest diese Zeilen wirklich.
4. **Breaking Changes stehen in der Fußnote, nicht im Betreff.** Der Betreff bleibt lesbar.
5. **Agent-Commits sind normale Commits.** Sie folgen denselben Regeln und tragen
   zusätzlich `Co-authored-by`. Ein Commit, den niemand reviewt hat, wird nicht gemerged.

---

## 4. Beispiele

### Gut

```
fix(abrechnung): deckele Blockiergebühr bei 12 EUR je Sitzung

Der Deckel aus der Spec war nie implementiert. Bei Sitzungen über
26 Stunden Standzeit entstanden Rechnungen über 100 EUR, die der
Support anschliessend manuell storniert hat.

Ticket: VW-4711
Spec: VW-SPEC-ABR §S-3
```

```
refactor(abrechnung): ersetze Tarif-Kaskade durch versiegelten Typ

Jeder neue Tarif hat bisher die Methode berechne() verlängert.
Das Verhalten liegt jetzt am Tarif selbst; berechne() kennt nur
noch den Vertrag.

Verhalten unverändert, Soll-Tests aus SPEC.md laufen wie vorher.
```

### Schlecht

```
fix: bugfix
```
Kein Bereich, kein Inhalt, kein Ticket.

```
feat(abrechnung): AbrechnungsService.berechne() umgebaut und Rundung gefixt und Tests ergänzt
```
Drei Gedanken in einem Commit, Betreff zu lang, Vergangenheitsform.

```
refactor(abrechnung): apply strategy pattern
```
Englisch, und der Mustername sagt nichts über die Wirkung.
