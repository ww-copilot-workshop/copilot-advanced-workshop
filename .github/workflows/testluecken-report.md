---
description: Wöchentlicher Testlücken-Report als Draft-Pull-Request

on:
  schedule: weekly on monday
  workflow_dispatch:

permissions:
  contents: read
  issues: read
  pull-requests: read

engine: copilot

network:
  allowed:
    - defaults
    - java

timeout-minutes: 30

strict: true

tools:
  github:
    toolsets: [default]
  edit:
  bash: ["mvn", "find", "ls", "cat", "grep"]

safe-outputs:
  create-pull-request:
    title-prefix: "[test-luecken] "
    labels: [testing, automated]
    draft: true
    if-no-changes: warn
  missing-tool:
---

# Wöchentlicher Testlücken-Report

Analysiere die JUnit-5-Testabdeckung dieses Maven-Projekts.

## Vorgehen

1. Übersetze die Tests, ohne sie auszuführen:
   `mvn -q -B -ntp -DskipTests test-compile`
2. Liste alle produktiven Klassen unter `*/src/main/java` und alle Testklassen unter
   `*/src/test/java`.
3. Finde Klassen mit öffentlicher Geschäftslogik ohne zugehörige Testklasse.
4. **Priorisiere nach Risiko, nicht nach Größe.** In dieser Reihenfolge:
   - Klassen, die Geld oder Zeit berechnen
   - Klassen, die eine Regel aus `modul-5/SPEC.md` umsetzen
   - Klassen mit Verzweigungen über Enums oder Statuswerte
   - alles andere
   Reine Datenträger (Records ohne Logik), Konfiguration und generierter Code
   sind **keine** Lücken. Führe sie nicht auf.
5. Schreibe das Ergebnis nach `docs/test-luecken.md` (anlegen oder überschreiben):
   - Tabelle: Klasse | Modul | Grund der Priorisierung | vorgeschlagene Testfälle
   - Abschnitt "Top 3 für diese Woche" mit je einem Satz Begründung
6. Erstelle daraus einen Draft-Pull-Request.

## Grenzen

Ändere **ausschließlich** `docs/test-luecken.md`. Schreibe keinen Produktivcode.
Schreibe auch keine Tests — der Vorschlag ist das Produkt, nicht die Umsetzung.
