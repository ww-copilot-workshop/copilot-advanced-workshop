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
