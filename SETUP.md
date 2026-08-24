# Setup

Bitte **vor** dem Workshop erledigen. Rechnet mit 20 Minuten, wenn nichts installiert ist.

---

## 1. Was ihr braucht

| Werkzeug | Version | Prüfen mit |
|---|---|---|
| **GitHub Copilot CLI** | 1.0.79 oder neuer | `copilot --version` |
| **GitHub CLI** | 2.90.0 oder neuer | `gh --version` |
| **JDK** | 21 oder neuer | `java -version` |
| **Maven** | 3.9 oder neuer | `mvn -v` |
| **Git** | beliebig aktuell | `git --version` |
| **gh-aw-Extension** | 0.86.0 oder neuer | `gh aw --version` |

Dazu: eine **Copilot-Lizenz** mit AI Credits und Zugriff auf die Workshop-Organisation.

Unter Linux zusätzlich für die Sandbox-Übung: **bubblewrap** (`bwrap`) 0.5.0 oder neuer.
Ohne das meldet `/sandbox`, dass Sandboxing nicht unterstützt wird.

---

## 2. Installation

```bash
# Copilot CLI
npm install -g @github/copilot
copilot --version

# GitHub CLI (macOS)
brew install gh
# oder Update
brew upgrade gh

# gh-aw-Extension für Modul 4
gh extension install github/gh-aw
gh aw --version
```

Bereits installiert? Dann bitte aktualisieren — die CLI released fast täglich:

```bash
copilot update
gh extension upgrade github/gh-aw
```

---

## 3. Anmelden

```bash
gh auth login
copilot
```

und in der Session:

```
/login
```

Kurztest, dass alles zusammenspielt:

```
Nenne mir das aktuelle Verzeichnis und den Git-Branch.
```

---

## 4. Repository klonen und bauen

```bash
git clone REPO-URL
cd copilot-advanced-workshop
mvn -q test
```

**Erwartet:** `BUILD SUCCESS`, **neun Tests übersprungen**. Das ist Absicht — die
gehören zu Übung A1 und sind dort mit `@Disabled` abgeschaltet.

Legt euch gleich einen Arbeitsbranch an:

```bash
git checkout -b uebung/euer-name
```

---

## 5. Hinter dem Unternehmens-Proxy

Wenn Maven keine Abhängigkeiten auflösen kann, fehlt eure `~/.m2/settings.xml` mit
dem Artifactory-Mirror. Fragt euren üblichen Weg dafür ab — das ist dieselbe Datei,
die ihr für jedes andere Java-Projekt braucht.

Prüfen:

```bash
mvn -B -ntp -DskipTests dependency:go-offline
```

Läuft das durch, seid ihr für den ganzen Tag versorgt: alle Abhängigkeiten liegen
danach lokal.

**Für Copilot selbst** braucht ihr HTTPS zu den GitHub- und Copilot-Endpunkten.
Wenn `copilot` startet, aber keine Antwort kommt, ist das fast immer der Proxy.
Setzt `HTTPS_PROXY` und `NO_PROXY` wie für eure anderen CLI-Werkzeuge.

---

## 6. Für Modul 4 (Cloud Agent)

Diese Punkte kann nicht jeder selbst erledigen — sie werden im Workshop bereitgestellt
und am Morgen gemeinsam geprüft:

- [ ] Zugriff auf die Workshop-Organisation auf github.com
- [ ] Copilot Coding Agent ist für das Übungs-Repo aktiviert
- [ ] AI Credits stehen zur Verfügung
- [ ] `.github/workflows/copilot-setup-steps.yml` ist im Repo hinterlegt
      (läuft auf `ubuntu-latest`, GitHub-hosted — es braucht keine eigene Infrastruktur)

Wenn davon etwas fehlt, läuft Modul 4 im **Trial-Modus**:

```bash
```

Trial läuft gegen ein simuliertes Repository. Keine echten Issues, keine echten PRs.
Für die Übung reicht das vollständig.

---

## 7. Optionale Vorbereitung

**Experimental-Modus einmal ausprobieren.** Mehrere Übungen brauchen ihn:

```bash
copilot --experimental
```

Danach existieren `/sandbox`, `/worktree`, `/move`, `/every` und `/after`. Ohne den
Schalter meldet die CLI „Unknown command" — und sie tauchen auch in `copilot help
commands` nicht auf. Das ist kein Installationsfehler.

**Ins Changelog schauen.** Fünf Minuten auf `github.blog/changelog`. Zwischen dem
Schreiben dieser Datei und dem Workshop-Tag liegen Wochen; in dieser Zeit ändert sich
erfahrungsgemäß etwas.

---

## Fehlerbehebung

| Symptom | Ursache | Abhilfe |
|---|---|---|
| `copilot: command not found` | npm-Global-Bin nicht im PATH | `npm prefix -g` ausgeben, `/bin` anhängen, zum PATH hinzufügen (`npm bin -g` gibt es seit npm 9 nicht mehr) |
| `/sandbox` → „Unknown command" | Experimental-Modus aus | `copilot --experimental` oder `/settings experimental on` |
| `-w` bricht mit Fehler ab | dasselbe | siehe oben |
| `/agents` öffnet den falschen Dialog | `/agents` ist ein **Alias von `/subagents`** (Subagenten-Modelle) — nicht die Agentenauswahl | Für Agenten: `/agent [name]` |
| Skill greift nicht | `description` zu unspezifisch | `/skills info <name>`, dann Beschreibung schärfen |
| Agent hat „unknown tools" | VS-Code-Toolnamen kopiert | `/env` prüfen; gültige Namen stehen in `modul-2/README.md` |
| `gh aw` unbekannt | Extension fehlt | `gh extension install github/gh-aw` |
| `gh skill` unbekannt | gh älter als 2.90.0 | `brew upgrade gh` |
| `mvn` findet nichts | Artifactory-`settings.xml` fehlt | siehe Abschnitt 5 |
| Java-Version zu alt | Mehrere JDKs installiert | `JAVA_HOME` auf ein JDK 21+ setzen |
| MCP-Server antwortet nicht | Logausgabe geht nach stdout | Log gehört nach **stderr**; stdout gehört dem Protokoll |

**Der schnellste Diagnosebefehl des Tages** ist `/env`. Er zeigt, was tatsächlich
geladen ist: Instructions, MCP-Server, Skills, Agents, Hooks, LSPs, Plugins. Bei
„funktioniert nicht" ist er fast immer die Antwort.

---

## Am Workshop-Tag

Kommt mit einem Rechner, auf dem `mvn -q test` grün ist und `copilot` startet.
Alles andere lösen wir vor Ort. Wer hängt, arbeitet zunächst im Zweierteam mit —
wir fixen parallel am Trainer-Tisch vorne links.
