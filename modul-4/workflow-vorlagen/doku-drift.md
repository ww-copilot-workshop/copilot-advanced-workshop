---
# =============================================================================
# VORLAGE C3-1 — Nächtlicher Doku-Drift-Report
#
# Ziel: Jede Nacht prüfen, ob Code und Dokumentation auseinandergelaufen sind.
#       Ergebnis: genau ein Issue, wenn es Drift gibt. Sonst nichts.
#
# So arbeitet ihr damit:
#   cp modul-4/workflow-vorlagen/doku-drift.md .github/workflows/
#   # TODOs füllen
#   gh aw compile          <- euer Feedback. Null Warnungen ist das Ziel.
#
# Der Compiler sagt euch bei fast jedem Fehler, was er stattdessen erwartet hat,
# inklusive "Did you mean". Nutzt ihn als Übungsschleife, nicht als Prüfung.
# =============================================================================

description: Nächtlicher Abgleich zwischen Java-Quellen und Dokumentation

on:
  # TODO(1): Zeitplan ergänzen. Zwei Schreibweisen sind möglich:
  #   schedule: daily                     <- "fuzzy schedule": gh-aw streut die
  #                                          Startzeiten, damit nicht alle Repos
  #                                          einer Org um 03:00 gleichzeitig laufen
  #   schedule: [{ cron: "17 3 * * *" }]  <- klassischer Cron
  #
  # Nehmt `daily`, wenn euer Repo ein Git-Remote hat — sonst warnt der Compiler,
  # dass er die Streuung nicht eindeutig berechnen kann. Ohne Remote nehmt Cron.
  # Probiert ruhig beides aus und lest, was der Compiler jeweils sagt.
  #
  # TODO(2): Ergänzt einen manuellen Auslöser, damit ihr nicht bis 3 Uhr warten müsst.
  #   (Der Key heißt genauso wie bei normalen Actions-Workflows.)

permissions:
  contents: read
  # TODO(3): Es fehlt mindestens eine Permission. Der Compiler sagt euch welche,
  #  sobald ihr `tools.github.toolsets: [default]` gesetzt habt — das Standard-Toolset
  #  enthält mehr, als sein Name vermuten lässt.

engine: copilot
network: defaults

# TODO(4): Laufzeitbegrenzung. Achtung auf die Schreibweise — Bindestrich, nicht
#  Unterstrich. Der Compiler kennt hier kein Erbarmen. 20 Minuten sind ein guter Wert.

strict: true

tools:
  github:
    toolsets: [default]
  # TODO(5): Der Agent muss Dateien lesen und im Git-Log stöbern. Gebt ihm eine
  #  *Allowlist* von Shell-Kommandos — nicht alles. Überlegt: welche braucht er
  #  wirklich für die Schritte unten?
  #
  #  Schreibweisen — und Vorsicht, hier täuscht die Intuition:
  #    bash: ["find", "grep", "git log"]  -> genau diese, plus das Default-Safe-Set
  #    bash: []                           -> gar keine Shell
  #    bash: true                         -> ALLES. Erzeugt dasselbe Lockfile wie [":*"]
  #    bash: [":*"]                       -> ALLES
  #    (Key ganz weglassen)               -> ALLES
  #
  #  Das ist nachgeprüft: `bash: true`, `bash: [":*"]` und ein fehlender Key erzeugen
  #  byte-identische Lockfiles, in denen der Agent mit --allow-all-tools gestartet wird.
  #  Nur eine echte Liste schränkt wirklich ein. Wer "true" für die brave Variante hält,
  #  hat versehentlich alles freigegeben — und der Compiler sagt dazu nichts.
  #  (Das Default-Safe-Set kommt zu jeder Allowlist hinzu:
  #   echo, printf, ls, pwd, cat, head, tail, grep, wc, sort, uniq, date, yq)

safe-outputs:
  create-issue:
    title-prefix: "[doku-drift] "
    labels: [documentation, automated]
    # TODO(6): Begrenzt die Anzahl der Issues je Lauf. Ein Workflow, der über Nacht
    #  vierzig Issues anlegt, wird am nächsten Tag abgeschaltet — von Hand, für immer.
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

<!--
  TODO(7) — die eigentliche Denkaufgabe, keine Syntax:

  Beantwortet vor dem ersten scharfen Lauf drei Fragen und schreibt die Antworten
  in den PR, mit dem ihr diesen Workflow einführt:

    a) Was ist der Schaden, wenn dieser Workflow Unsinn liefert?
    b) Wer fängt ihn am Gate ab — namentlich, nicht "das Team"?
    c) Was kostet er im Monat? (`gh aw forecast` gibt euch eine Vorhersage.)

  Ein Workflow ohne diese drei Antworten ist kein Betriebsmittel, sondern ein Hobby.
-->
