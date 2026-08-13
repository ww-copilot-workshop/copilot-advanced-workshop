# GitHub Copilot Advanced-Workshop — Übungsrepository

**25.08.2026 · Cluster Reply, Agentic DevOps**

Willkommen bei der **Voltwerk GmbH**. Ihr übernehmt heute die Ladepark-Suite: ein
Java-System zur Verwaltung, Telemetrie und Abrechnung öffentlicher Ladeparks.

Das System ist erfunden. Die Probleme darin sind es nicht.

---

## In 5 Minuten startklar

```bash
copilot --version          # 1.0.79 oder neuer
gh --version               # 2.90.0 oder neuer
java -version              # 21 oder neuer
mvn -q test                # muss grün sein
```

Ausführliche Anleitung inklusive Fehlerbehebung: **[SETUP.md](SETUP.md)**

Wenn `mvn -q test` grün ist und `copilot` startet, seid ihr fertig. Alles andere
klären wir am Trainer-Tisch, während ihr im Zweierteam mitarbeitet.

---

## Der Tag

| Zeit | Modul | Ordner | Ergebnis |
|---|---|---|---|
| 08:55–10:35 | **1 — CLI Deep-Dive II** | [`modul-1/`](modul-1/README.md) | Ihr steuert Autonomie, Freigaben und Sandbox bewusst |
| 10:45–12:15 | **2 — MCP & Skills** | [`modul-2/`](modul-2/README.md) | Ein Skill, ein Agent, ein eigener MCP-Server |
| 13:10–13:45 | **3 — Usage-Based Billing** | [`modul-3/`](modul-3/README.md) | Ihr wisst, was eure Läufe kosten — gemessen, nicht geschätzt |
| 13:45–15:30 | **4 — Cloud Agent & Workflows** | [`modul-4/`](modul-4/README.md) | Ein delegierter Task, ein Agentic Workflow |
| 15:40–17:10 | **5 — Legacy-Sprint** | [`modul-5/`](modul-5/README.md) | Landkarte, Soll-Tests, ein Bugfix, ein Umbau |

Jeder Modulordner hat eine eigene `README.md` mit den Aufgaben. **Fangt dort an.**

---

## Wie ihr arbeitet

**Primär über die Copilot CLI.** Nicht, weil die IDE schlechter wäre — sondern weil im
Terminal sichtbar ist, was passiert: jeder Tool-Aufruf, jede Freigabe, jeder Credit.
Was ihr hier versteht, findet ihr in jeder Oberfläche wieder. Umgekehrt nicht.

**Im Zweierteam.** Einer tippt, einer denkt. Nach jeder Übung tauschen. Wer nur zuschaut,
lernt nichts — und wer nur tippt, auch nicht.

**Vom Repo-Wurzelverzeichnis aus.** Startet `copilot` oben, nicht im Modulordner.
Der Agent liest `AGENTS.md` aus der Wurzel, und die gilt heute.

**Kommandozeile statt Copy-Paste-Prompts.** Die Prompts in den Aufgaben sind
Startpunkte, keine Zaubersprüche. Ändert sie. Das ist der Punkt.

---

## Die drei Dateien, die alles steuern

| Datei | Rolle |
|---|---|
| **[`AGENTS.md`](AGENTS.md)** | Die Konventionen. Gelten immer, für Menschen und Agenten. Modul 5 baut darauf auf. |
| **[`docs/commit-konventionen.md`](docs/commit-konventionen.md)** | Die Rohquelle für den Skill aus Lab B1. |
| **[`modul-5/SPEC.md`](modul-5/SPEC.md)** | Die fachliche Wahrheit der Abrechnung. Wichtiger als der Code. |

Lest `AGENTS.md` einmal, bevor ihr anfangt. Zehn Minuten, die sich in Modul 5 auszahlen.

---

## Struktur

```
.
├── AGENTS.md                     Konventionen für Menschen und Agenten
├── SETUP.md                      Voraussetzungen und Fehlerbehebung
├── pom.xml                       Maven-Parent (Java 21)
├── docs/
│   ├── CHEATSHEET.md             CLI-Spickzettel, verifiziert gegen 1.0.79
│   └── commit-konventionen.md    Rohquelle für Lab B1
├── modul-1/                      Telemetrie-Parser — Übungsreihe A
│   ├── src/                      Java-Code mit offenen Stellen
│   └── uebungen/                 Branch-Chaos-Skript, Review-Patch
├── modul-2/                      MCP & Skills — Lab B
│   ├── mcp-server/               Vollständiger MCP-Server in Java, zwei Werkzeuge offen
│   └── vorlagen/                 SKILL.md- und Agent-Vorlagen
├── modul-3/                      Kostenlabor
│   └── messbogen.md              Messbogen zum Ausfüllen
├── modul-4/                      Cloud Agent & Workflows — Lab C
│   ├── aufgabenkarten/           Vier Karten. Drei gute, eine schlechte.
│   ├── workflow-vorlagen/        Drei gh-aw-Workflows mit Lücken
│   └── copilot-setup-steps.yml.vorlage
└── modul-5/                      Abrechnungskern (Legacy) — Lab D
    ├── SPEC.md                   Die fachliche Wahrheit
    ├── tickets/VW-4715.md        Ein gemeldeter Fehler
    └── src/                      Gewachsener Code, ein einziger Test
```

---

## Hausordnung

1. **Branch statt main.** `git checkout -b uebung/<euer-name>` bevor ihr loslegt.
2. **`mvn -q test` vor und nach jeder Änderung.** Grün ist eine Behauptung, die man
   belegen kann.
3. **Keine Lösungen im Repo.** Die liegen beim Trainer. Wer sie vorher haben will,
   verschenkt den lehrreichen Teil.
4. **Fragt früh.** Zehn Minuten Feststecken sind Lernen. Zwanzig sind Verschwendung.

---

## Ein Hinweis zur Ehrlichkeit

Ein Teil dessen, was ihr heute seht, ist **Public Preview oder experimentell**. Verfügbarkeit
und Umfang können sich ändern — das liegt außerhalb unseres Einflusses. Wo es klemmt,
haben wir Alternativwege.

Die Aufgaben sind gegen **Copilot CLI 1.0.79** und **`gh aw` 0.86.2** geprüft. Wenn ein
Befehl bei euch anders heißt: `copilot help commands` und `gh aw --help` sind die
Wahrheit, nicht diese Datei.

**Konzepte sind stabil, Oberflächen sind beweglich.** Beides auseinanderzuhalten ist
selbst eine Lernaufgabe des Tages.

---

*Trainer: Ahmed Diab & Daniel Snell · Cluster Reply, Agentic DevOps*
