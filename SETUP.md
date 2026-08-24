# Setup

Bitte **vor** dem Workshop erledigen. Rechnet mit 20 Minuten, wenn nichts installiert ist.

> **Diese Anleitung ist für Windows geschrieben.** Für macOS und Linux stehen die
> Abweichungen jeweils in einem Kasten darunter.

---

## 0. Welche Shell? — bitte einmal lesen

Die Übungen benutzen durchgängig Bash-Syntax: einfache Anführungszeichen, `*`-Muster
in Freigabe-Flags, ein `.sh`-Skript in Übung A2. In der PowerShell werden
Anführungszeichen und Klammern anders behandelt — das kostet euch in A2
(„Freigaben-Golf") Zeit an einer Stelle, an der es um etwas ganz anderes geht.

**Arbeitet deshalb in Git Bash oder in WSL**, nicht in PowerShell oder CMD.

- **Git Bash** kommt mit Git für Windows mit (siehe Abschnitt 2) — das genügt für
  den ganzen Tag und ist der kürzeste Weg.
- **WSL** ist die Alternative, wenn ihr sie ohnehin nutzt. Dann installiert ihr alle
  Werkzeuge *innerhalb* der WSL-Distribution, nicht unter Windows.

Nur die Installationsbefehle in Abschnitt 2 laufen in PowerShell. Alles danach in
Git Bash.

---

## 1. Was ihr braucht

| Werkzeug | Version | Prüfen mit |
|---|---|---|
| **GitHub Copilot CLI** | 1.0.79 oder neuer | `copilot --version` |
| **GitHub CLI** | 2.90.0 oder neuer | `gh --version` |
| **Node.js** | 22 oder neuer | `node --version` |
| **JDK** | 21 oder neuer | `java -version` |
| **Maven** | 3.9 oder neuer | `mvn -v` |
| **Git für Windows** (bringt Git Bash mit) | beliebig aktuell | `git --version` |
| **gh-aw-Extension** | 0.86.0 oder neuer | `gh aw --version` |

Dazu: eine **Copilot-Lizenz** mit AI Credits und Zugriff auf die Workshop-Organisation.

### Sandbox-Übung (A3.2) — eine Einschränkung unter Windows

Die GitHub-Doku sagt dazu wörtlich: *„Local sandboxing is available on macOS and
Linux, and on Windows Insiders builds."* Unter Windows nutzt sie das
ProcessContainer-Backend, und das gibt es derzeit nur im Insider-Ring des
**Betriebssystems** (nicht zu verwechseln mit VS Code Insiders). Auf einem normalen
Windows-Build steht die lokale Sandbox also nicht zur Verfügung.

**Das ist kein Installationsfehler und kein Beinbruch.** A3 ist im Workshop als
Anschau-Übung eingeplant. Setzt euch dafür mit jemandem zusammen, der macOS oder
Linux hat — alles andere an dem Tag funktioniert unter Windows normal.

> **macOS:** nutzt Seatbelt (`sandbox-exec`), läuft ohne Zusatzinstallation.
> **Linux:** braucht **bubblewrap** (`bwrap`) 0.5.0 oder neuer auf dem `PATH`.

---

## 2. Installation

In **PowerShell** (einmalig; danach ein neues Fenster öffnen, damit der `PATH` greift):

```powershell
# Git für Windows — bringt Git Bash mit
winget install --id Git.Git --source winget

# Node.js 22+ (Voraussetzung für die Copilot CLI)
winget install --id OpenJS.NodeJS.LTS --source winget

# GitHub Copilot CLI
winget install --id GitHub.Copilot --source winget

# GitHub CLI
winget install --id GitHub.cli --source winget
```

Ab hier in **Git Bash**:

```bash
copilot --version
gh --version

# gh-aw-Extension für Modul 4
gh extension install github/gh-aw
gh aw --version
```

> **Achtung, zwei ähnliche winget-Pakete:** `GitHub.Copilot` ist die **CLI**, die ihr
> braucht. `GitHub.CopilotApp` ist die Desktop-App — die zeigen wir im Workshop, ihr
> müsst sie nicht installieren.

> **macOS / Linux:**
> ```bash
> brew install --cask copilot-cli   # oder: npm install -g @github/copilot
> brew install gh
> gh extension install github/gh-aw
> ```

Bereits installiert? Dann bitte aktualisieren — die CLI released fast täglich:

```bash
copilot update
gh extension upgrade github/gh-aw
```

`copilot update` funktioniert unabhängig davon, wie ihr installiert habt.
`gh` selbst aktualisiert ihr in PowerShell mit
`winget upgrade --id GitHub.cli --source winget` (macOS: `brew upgrade gh`).

---

## 3. Anmelden

In Git Bash:

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

**Erwartet:** Exit-Code `0` (bei `-q` gibt es bei Erfolg keine sichtbare Ausgabe).
Wer die Zusammenfassung sehen will, lässt `-q` weg (`mvn test`) und sieht dann
`BUILD SUCCESS` sowie **neun Tests übersprungen**. Das ist Absicht — die gehören zu
Übung A1 und sind dort mit `@Disabled` abgeschaltet.

Legt euch gleich einen Arbeitsbranch an:

```bash
git checkout -b uebung/euer-name
```

> **Windows-Hinweis:** Klont das Repo in einen Pfad **ohne Leerzeichen und ohne
> Umlaute** (z. B. `C:\dev\`), nicht auf einen OneDrive-synchronisierten Desktop.
> Beides führt sonst in Maven und in den Übungsskripten zu schwer lesbaren Fehlern.

---

## 5. Hinter dem Unternehmens-Proxy

Wenn Maven keine Abhängigkeiten auflösen kann, fehlt eure `settings.xml` mit dem
Artifactory-Mirror. Unter Windows liegt sie hier:

```
%USERPROFILE%\.m2\settings.xml
```

In Git Bash also `~/.m2/settings.xml` — dasselbe Verzeichnis. Fragt euren üblichen
Weg dafür ab; das ist dieselbe Datei, die ihr für jedes andere Java-Projekt braucht.

Prüfen:

```bash
mvn -B -ntp -DskipTests dependency:go-offline
```

Läuft das durch, seid ihr für den ganzen Tag versorgt: alle Abhängigkeiten liegen
danach lokal.

**Für Copilot selbst** braucht ihr HTTPS zu den GitHub- und Copilot-Endpunkten.
Wenn `copilot` startet, aber keine Antwort kommt, ist das fast immer der Proxy.
Setzt `HTTPS_PROXY` und `NO_PROXY` wie für eure anderen CLI-Werkzeuge:

```bash
# Git Bash — für die laufende Sitzung
export HTTPS_PROXY=http://proxy.example:8080
export NO_PROXY=localhost,127.0.0.1,.example.de
```

```powershell
# PowerShell — dauerhaft für euren Benutzer
setx HTTPS_PROXY "http://proxy.example:8080"
setx NO_PROXY "localhost,127.0.0.1,.example.de"
```

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
gh aw trial ./.github/workflows/doku-drift.md \
  --clone-repo ORG/REPO \
  --delete-host-repo-after
```

Trial läuft gegen ein simuliertes Repository. Keine echten Issues, keine echten PRs.
Für die Übung reicht das vollständig.

---

## 7. Optionale Vorbereitung

**Experimental-Modus einmal ausprobieren.** Mehrere Übungen brauchen ihn:

```bash
copilot --experimental
```

In einer laufenden Session geht es auch ohne Neustart: `/settings experimental on`.

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
| `copilot: command not found` | PATH nicht aktualisiert | Neues Terminal öffnen. Bei npm-Installation: `npm prefix -g` ausgeben, `/bin` anhängen, zum PATH hinzufügen (`npm bin -g` gibt es seit npm 9 nicht mehr) |
| Anführungszeichen-Fehler in A2 | PowerShell statt Git Bash | In Git Bash arbeiten, siehe Abschnitt 0 |
| `./A2-branch-chaos.sh` startet nicht | PowerShell statt Git Bash | dasselbe |
| `/sandbox` → „Unknown command" | Experimental-Modus aus | `copilot --experimental` oder `/settings experimental on` |
| Sandbox lässt sich nicht aktivieren | Windows ohne Insider-Build | Erwartet, kein Fehler. A3.2 im Team mit macOS/Linux ansehen (Abschnitt 1) |
| `-w` bricht mit Fehler ab | Experimental-Modus aus | siehe oben |
| `/agents` öffnet den falschen Dialog | `/agents` ist ein **Alias von `/subagents`** (Subagenten-Modelle) — nicht die Agentenauswahl | Für Agenten: `/agent [name]` |
| Skill greift nicht | `description` zu unspezifisch | `/skills info <name>`, dann Beschreibung schärfen |
| Agent hat „unknown tools" | VS-Code-Toolnamen kopiert | `/env` prüfen; gültige Namen stehen in `modul-2/README.md` |
| `gh aw` unbekannt | Extension fehlt | `gh extension install github/gh-aw` |
| `gh skill` unbekannt | gh älter als 2.90.0 | `winget upgrade --id GitHub.cli --source winget` (macOS: `brew upgrade gh`) |
| `mvn` findet nichts | Artifactory-`settings.xml` fehlt | siehe Abschnitt 5 |
| Java-Version zu alt | Mehrere JDKs installiert | `JAVA_HOME` auf ein JDK 21+ setzen |
| Maven-Fehler mit kryptischen Pfaden | Leerzeichen, Umlaute oder OneDrive im Pfad | Repo nach `C:\dev\` klonen, siehe Abschnitt 4 |
| MCP-Server antwortet nicht | Logausgabe geht nach stdout | Log gehört nach **stderr**; stdout gehört dem Protokoll |

**Der schnellste Diagnosebefehl des Tages** ist `/env`. Er zeigt, was tatsächlich
geladen ist: Instructions, MCP-Server, Skills, Agents, Hooks, LSPs, Plugins. Bei
„funktioniert nicht" ist er fast immer die Antwort.

---

## Am Workshop-Tag

Kommt mit einem Rechner, auf dem `mvn -q test` grün ist und `copilot` startet.
Alles andere lösen wir vor Ort. Wer hängt, arbeitet zunächst im Zweierteam mit —
wir fixen parallel am Trainer-Tisch vorne links.
