# Copilot CLI — Spickzettel

Verifiziert gegen **@github/copilot 1.0.79**, **gh 2.97.0**, **gh aw 0.86.2**.
Bei Abweichungen gilt die Binary, nicht dieses Blatt:
`copilot help commands` · `copilot help permissions` · `copilot help config`

---

## Slash-Commands

### Umgebung
```
/init          Copilot-Instructions für dieses Repo anlegen
/agent [name]  Agents durchsehen und auswählen   (Singular!)
/skills        Skills verwalten                   (Plural!)
/mcp           MCP-Server verwalten
/plugin        Plugins und Marktplätze
/env           Was ist geladen: Instructions, MCP, Skills, Agents, Hooks, Plugins
/instructions  Instruction-Dateien ansehen und umschalten
```

### Agenten und Modelle
```
/model         Modell wählen ('auto' lässt Copilot wählen); zeigt Preise je Token
/delegate      Session an GitHub geben, Copilot erzeugt einen PR (--base <branch>)
/autopilot     Autopilot umschalten
/fleet         Parallele Subagent-Ausführung
/tasks         Subagenten und Shell-Kommandos verwalten
/subagents     Modelle für Subagenten konfigurieren
/plan          Erst planen, dann coden
```

### Code
```
/diff             Änderungen im aktuellen Verzeichnis reviewen
/review           Code-Review-Agent auf die Änderungen
/security-review  Sicherheitsanalyse der gestagten und ungestagten Änderungen
/rubber-duck      Unabhängige Kritik an eurer aktuellen Arbeit
/pr               Pull Requests des aktuellen Branch
/ide              Mit einem IDE-Workspace verbinden
/lsp              Language-Server-Konfiguration
```

### Berechtigungen
```
/permissions          Berechtigungsmodus umschalten
/allow-all            Alles erlauben (Tools, Pfade, URLs)
/add-dir              Verzeichnis zur Dateizugriffs-Allowlist
/list-dirs            Erlaubte Verzeichnisse anzeigen
/cwd                  Arbeitsverzeichnis wechseln oder anzeigen
/reset-allowed-tools  Liste erlaubter Tools zurücksetzen
```

### Session
```
/resume    Zu einer anderen Session wechseln (ID, Task-ID oder Name)
/fork      Aktuelle Session verzweigen
/rename    Session umbenennen
/session   Sessions ansehen und verwalten
/context   Kontextfenster nach Verursachern
/usage     AI Credits, Tokens, Cache-Anteil, Limit-Fortschritt
/limits    Session-Limits ansehen und setzen
/compact   Historie zusammenfassen (optional mit Fokus-Anweisung)
/rewind    Letzte Runde zurücknehmen, inklusive Dateiänderungen
/share     Session als Markdown, HTML, Gist oder Link teilen
/clear     Session verwerfen und neu anfangen
/new       Neue Unterhaltung
```

### Sonstiges
```
/ask           Zwischenfrage, ohne die Historie zu verschmutzen
/refine        Rohen Prompt in einen klaren umschreiben (Ctrl+X)
/research      Tiefenrecherche über GitHub-Suche und Web
/settings      Einstellungen ansehen und setzen (--repo / --local)
/statusline    Statuszeile konfigurieren (Optionen: quota, ai-used)
/experimental  Experimentelle Features anzeigen und umschalten
/memory        Sitzungsübergreifendes Gedächtnis
/diagnose      Aktuelles Session-Log analysieren
/changelog     Changelog anzeigen ('summarize' für eine KI-Zusammenfassung)
```

### Nur im Experimental-Modus
```
copilot --experimental        oder    /settings experimental on

/sandbox [enable|disable]   OS-Sandbox für Shell-Kommandos
/worktree [branch|task]     Git-Worktree ab HEAD anlegen und hineinwechseln
/worktree new [PROMPT]      Worktree plus Auftrag
/move [branch|task]         Uncommittete Änderungen in einen neuen Worktree schieben
/every 1h <prompt>          Wiederkehrender Auftrag
/after 30m <prompt>         Einmaliger Auftrag später
```

> Diese Befehle erscheinen **nicht** in `copilot help commands`, solange der Modus aus
> ist. Sie fehlen nicht — sie sind versteckt.

---

## Flags

### Berechtigungen
```
--allow-tool 'shell(git:*)'      Tool erlauben, ohne Rückfrage
--deny-tool 'shell(rm:*)'        Tool verbieten — schlägt IMMER jedes Allow
--allow-url  'https://github.com'
--deny-url   'https://böse.example'
--allow-all-tools                alle Tools ohne Rückfrage (env: COPILOT_ALLOW_ALL)
--allow-all-paths                Pfadprüfung aus
--allow-all-urls                 alle URLs
--allow-all  /  --yolo           alle drei auf einmal
--available-tools view,grep      NUR diese Tools für das Modell sichtbar
--excluded-tools shell,write     diese Tools für das Modell unsichtbar
--add-dir <verzeichnis>          Verzeichnis freigeben (mehrfach möglich)
--disallow-temp-dir              kein automatischer Zugriff auf /tmp
--secret-env-vars=A,B            Werte aus Umgebung und Ausgabe redigieren
```

**`--deny-all-tools` gibt es nicht.** Das Gegenstück ist `--available-tools` (Whitelist)
oder `--excluded-tools` (Blacklist).

Unterschied, den man einmal verstanden haben muss:
**`--available-tools` / `--excluded-tools`** steuern, was das Modell überhaupt **sieht**.
**`--allow-tool` / `--deny-tool`** steuern nur die **Rückfragen**.

### Tool-Muster
```
shell(git)              nur "git" ohne Subkommando
shell(git log)          Freigabe auf Ebene des ersten Subkommandos
shell(git:*)            alle git-Subkommandos  (matcht "git push", nicht "gitea")
shell()  bzw. shell     alle Shell-Kommandos
write                   alle schreibenden Tools (außer Shell-Umleitungen)
write(.env)             relativer Pfad matcht auf ENDkomponenten — .env überall
write(/abs/pfad/.env)   genau diese Datei
url(https://github.com) exakte URL oder Protokoll+Domain
url(https://*.github.com)
werkstatt(paket_versionen)   genau ein Tool eines MCP-Servers
werkstatt                    alle Tools dieses Servers
```

URL-Regeln sind **protokoll-scharf**: `https://x.de` erlaubt **nicht** `http://x.de`.
Domains ohne Protokoll bedeuten `https://`.

### Session und Modus
```
-p, --prompt "<text>"       nicht-interaktiv, beendet sich danach
-i, --interactive "<text>"  interaktiv starten und Prompt ausführen
-s, --silent                nur die Antwort, keine Statistik (für Skripte)
--output-format json        JSONL, ein JSON-Objekt je Zeile
-r, --resume[=<id|name>]    frühere Session fortsetzen
--continue                  letzte Session fortsetzen
-n, --name <name>           Session benennen
--session-id <id>           Session-ID vorgeben oder fortsetzen
--model <modell>            Modell setzen ('auto' möglich)
--effort <stufe>            none | minimal | low | medium | high | xhigh | max
--mode <modus>              interactive | plan | autopilot
--plan                      im Plan-Modus starten
--autopilot                 im Autopilot starten
--max-ai-credits <n>        Credit-Deckel dieser Session (Minimum 30)
--max-autopilot-continues 5 wie oft sich Autopilot selbst weiterschickt
--agent <name>              Custom Agent verwenden
-C <verzeichnis>            Arbeitsverzeichnis wechseln
--context <tier>            default | long_context
--no-custom-instructions    AGENTS.md und Co. ignorieren
--experimental              experimentelle Features an
--sandbox / --no-sandbox    Sandbox nur für diese Session (braucht --experimental)
```

---

## Terminal-Subcommands

```bash
copilot mcp list
copilot mcp add NAME -- KOMMANDO [args...]         # lokal (stdio)
copilot mcp add --transport http NAME URL          # remote
copilot mcp add NAME --tools "tool_a,tool_b" -- ...  # Tool-Allowlist
copilot mcp get NAME
copilot mcp remove NAME

copilot skill list
copilot skill add <datei|url|verzeichnis> [--project]
copilot skill remove NAME

copilot plugin marketplace browse awesome-copilot      # vorkonfiguriert
copilot plugin install NAME@awesome-copilot
copilot plugin list                                    # was ist installiert
#  (`copilot plugins` ist feature-gated und meldet meist
#   "The plugins command is not available.")

copilot help commands | permissions | config | environment
copilot help sandbox | limits | billing | logging | monitoring | providers
```

---

## Dateien und Orte

| Zweck | Ort |
|---|---|
| Einstellungen | `~/.copilot/settings.json` (Basis über `COPILOT_HOME`) |
| MCP-Server (Benutzer) | `~/.copilot/mcp-config.json` |
| MCP-Server (Projekt) | `.mcp.json` oder `.github/mcp.json` |
| Skills (Projekt) | `.github/skills/`, `.agents/skills/`, `.claude/skills/` |
| Skills (persönlich) | `~/.copilot/skills/`, `~/.agents/skills/` |
| Custom Agents | `.github/agents/<name>.md` oder `<name>.agent.md` |
| Instructions | `AGENTS.md`, `.github/copilot-instructions.md`, `*.instructions.md` |
| Logs | `~/.copilot/logs/` |

`XDG_CONFIG_HOME` wird **nicht** unterstützt — nur `COPILOT_HOME`.

---

## Frontmatter auf einen Blick

**Skill** — `.github/skills/<name>/SKILL.md`

```yaml
---
name: mein-skill            # PFLICHT, kleingeschrieben, [a-z0-9-], max 64
description: '...'          # PFLICHT, max 1024 — entscheidet über die Auswahl
allowed-tools: read, search # optional, String ODER YAML-Array von Strings
user-invocable: true        # optional, Standard true
---
```

**Custom Agent** — `.github/agents/<name>.md`

```yaml
---
name: mein-agent            # optional
description: '...'          # PFLICHT
tools: [read, search]       # optional — WEGLASSEN HEISST ALLES ERLAUBT
mcp-servers: {}             # optional
infer: true                 # optional, Standard true
---
```

Gültige Tool-Namen im CLI: `read`, `search`, `edit`, `execute`, `shell`, `bash`,
`powershell`, `agent`, `task`, `<server>/<tool>`, `<server>/*`.
**Nicht** gültig: `web`, `todo`, `githubRepo`, `problems`, `vscodeAPI` (IDE-only).

---

## gh aw

```bash
gh extension install github/gh-aw
gh aw init --engine copilot
gh aw new NAME --engine copilot
gh aw compile                 # .md  ->  .lock.yml   (BEIDE committen!)
gh aw validate                # nur prüfen, kein Lockfile
gh aw trial ./.github/workflows/x.md --delete-host-repo-after
gh aw run NAME
gh aw logs NAME
gh aw audit RUN-ID --parse
gh aw status | health | forecast | domains | doctor
```

**Frontmatter-Stolpersteine**

```yaml
timeout-minutes: 20     # Bindestrich, nicht Unterstrich
on:
  pull_request: { types: [opened, synchronize] }
  roles: [admin, maintainer, write]    # roles gehört UNTER on:, nicht daneben
  slash_command: { name: java-review } # command: ist deprecated
engine: copilot         # claude | codex | copilot | gemini | pi
network:
  allowed: [defaults, java]            # 'java' deckt die Maven-Repos ab
tools:
  bash: ["mvn", "grep"] # Allowlist -- die EINZIGE Variante, die einschraenkt
  bash: []              # gar keine Shell
  bash: true            # ALLES (identisch zu [":*"] und zu "Key weggelassen")
  bash: [":*"]          # ALLES
mcp-servers:            # TOP-LEVEL, nicht unter tools:
  meiner: { command: npx, args: [...] }
safe-outputs:
  create-issue: { max: 1 }   # Singular!
```

---

## Kosten in einer Zeile

```
Footer   laufendes Budget
/usage   Credits, Tokens, Cache-Anteil dieser Session
/context wohin das Kontextfenster geht
/model   Preis je Token, vor dem Wechsel
/limits  Deckel setzen
/exit    Abschlussbericht
```

`--max-ai-credits` ist ein **weicher** Deckel: Verbrauch ist erst nach der Antwort
bekannt. Ein Aufruf kann überziehen, blockiert wird erst der nächste.
Subagenten teilen sich das Limit der Elternsession.
