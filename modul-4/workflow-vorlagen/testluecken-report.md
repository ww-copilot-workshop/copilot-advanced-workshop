---
# =============================================================================
# VORLAGE C3-2 — Wöchentlicher Testlücken-Report als Draft-PR
#
# Ziel: Einmal pro Woche die Testabdeckung nach Risiko bewerten und das Ergebnis
#       als Draft-Pull-Request vorlegen. Ein Mensch entscheidet.
#
# Diese Vorlage ist die anspruchsvollste der drei: der Agent muss bauen dürfen,
# also braucht er Netz zu Maven — und er muss schreiben dürfen, aber nur genau
# eine Datei.
# =============================================================================

description: Wöchentlicher Testlücken-Report als Draft-Pull-Request

on:
  # TODO(1): Wöchentlich, montags früh.
  #
  # Achtung, hier ist die Vorlage aus doku-drift.md NICHT übertragbar: Ein fester
  # Wochen-Cron wie `[{ cron: "0 6 * * 1" }]` kompiliert zwar, erzeugt aber IMMER
  # eine Warnung ("Schedule uses fixed weekly time … Consider using fuzzy schedule").
  # Auch eine gestreute Minute hilft nicht.
  #
  # Wenn ihr null Warnungen wollt, führt hier nur ein Weg hin. Der Compiler nennt
  # ihn wörtlich in der Warnung. Probiert erst den Cron, lest die Meldung, korrigiert.
  #
  # TODO(2): Manueller Auslöser für den Workshop.

permissions:
  contents: read
  # TODO(3): zwei weitere Lese-Permissions fehlen. Der Compiler nennt sie beim Namen.

engine: copilot

network:
  allowed:
    - defaults
    # TODO(4): Ohne diesen Eintrag kann Maven keine Abhängigkeiten auflösen und
    #  jeder Lauf scheitert im Build. Es ist EIN Wort — der Ecosystem-Identifier
    #  für Java. Einzelne Domains wie repo.maven.apache.org funktionieren auch,
    #  der Compiler rät euch dann aber zum Ecosystem-Namen.

# TODO(5): Laufzeitbegrenzung. Ein Maven-Build braucht länger als ein Doku-Check —
#  30 Minuten sind realistisch.

strict: true

tools:
  github:
    toolsets: [default]
  # TODO(6): Der Agent muss eine Datei SCHREIBEN dürfen. Es gibt dafür genau einen
  #  Tool-Key, und er heißt nicht "write".
  # TODO(7): Er muss außerdem Maven ausführen. Allowlist, nicht alles.

safe-outputs:
  create-pull-request:
    title-prefix: "[test-luecken] "
    labels: [testing, automated]
    # TODO(8): Der PR soll ein ENTWURF sein, kein fertiger PR. Ein Wort.
    # TODO(9): In einer Woche ohne Änderung soll der Lauf NICHT fehlschlagen.
    #  Der passende Key heißt `if-no-changes`. Welchen Wert wollt ihr?
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

<!--
  TODO(10) — die Denkaufgabe:

  Dieser Workflow schlägt Tests vor. Ein naheliegender nächster Schritt wäre,
  ihn die Tests gleich schreiben zu lassen.

  Diskutiert in eurem Team, warum das eine schlechte Idee wäre — und zwar mit
  dem Argument aus Modul 5, nicht mit "der Agent macht Fehler".

  (Stichwort: Wogegen würde er die Tests schreiben? Gegen die Spezifikation
  oder gegen den Bestand? Und was zementiert er damit?)
-->
