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
