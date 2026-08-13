#!/usr/bin/env bash
#
# Erzeugt lokales Branch-Chaos für Übung A2 (Freigaben-Golf).
#
#   ./A2-branch-chaos.sh              -> legt die Übungsbranches an
#   ./A2-branch-chaos.sh --aufraeumen -> löscht sie wieder
#
# Alle erzeugten Branches tragen das Präfix "chaos/". Das Skript fasst nichts an,
# was nicht so heißt — insbesondere nicht euren eigenen Arbeitsbranch.

set -euo pipefail

PRAEFIX="chaos/"
WURZEL="$(git rev-parse --show-toplevel)"
cd "$WURZEL"

AUSGANGS_BRANCH="$(git rev-parse --abbrev-ref HEAD)"

case "$AUSGANGS_BRANCH" in
  "${PRAEFIX}"*)
    echo "Ihr steht auf einem Übungsbranch ($AUSGANGS_BRANCH)." >&2
    echo "Wechselt erst auf euren Arbeitsbranch zurück." >&2
    exit 1
    ;;
esac

if [[ "${1:-}" == "--aufraeumen" ]]; then
  echo "Lösche alle Branches mit Präfix '${PRAEFIX}' ..."
  anzahl=0
  while read -r branch; do
    [[ -z "$branch" ]] && continue
    [[ "$branch" == "$AUSGANGS_BRANCH" ]] && continue
    git branch -D "$branch"
    anzahl=$((anzahl + 1))
  done < <(git branch --list "${PRAEFIX}*" --format='%(refname:short)')
  rm -rf "modul-1/uebungen/.chaos"
  echo "Fertig, $anzahl Branch(es) gelöscht."
  exit 0
fi

if [[ -n "$(git status --porcelain)" ]]; then
  echo "Arbeitsverzeichnis ist nicht sauber. Bitte erst committen oder stashen." >&2
  exit 1
fi

anlegen() {
  local name="$1"
  local datei="$2"
  local inhalt="$3"
  local nachricht="$4"

  git checkout -q -b "${PRAEFIX}${name}" "$AUSGANGS_BRANCH"
  mkdir -p "$(dirname "$datei")"
  printf '%s\n' "$inhalt" > "$datei"
  # -f, weil .chaos/ absichtlich in .gitignore steht: die Dateien sollen nur
  # auf diesen Übungsbranches existieren, nicht im Arbeitsverzeichnis stören.
  git add -f "$datei"
  git -c user.name="Übungsdaten" -c user.email="uebung@voltwerk.invalid" \
      commit -q -m "$nachricht"
  git checkout -q "$AUSGANGS_BRANCH"
}

echo "Lege Übungsbranches an ..."

anlegen "feature/lastmanagement" \
        "modul-1/uebungen/.chaos/lastmanagement.md" \
        "Entwurf Lastmanagement, halbfertig." \
        "wip: Lastmanagement angefangen"

anlegen "feature/roaming-adapter" \
        "modul-1/uebungen/.chaos/roaming.md" \
        "Roaming-Adapter, wartet auf Spec." \
        "wip: Roaming-Adapter Grundgerüst"

anlegen "bugfix/VW-2290-dc-zuschlag" \
        "modul-1/uebungen/.chaos/vw-2290.md" \
        "Fix ist im Main, Branch vergessen." \
        "fix(abrechnung): DC-Zuschlag korrigiert"

anlegen "bugfix/VW-1533-rundung" \
        "modul-1/uebungen/.chaos/vw-1533.md" \
        "Rundung, nie fertig geworden." \
        "fix(abrechnung): Rundung angefasst"

anlegen "experiment/kotlin-spike" \
        "modul-1/uebungen/.chaos/spike.md" \
        "Spike von 2024, abgebrochen." \
        "spike: Kotlin evaluiert"

anlegen "release/2.2.x" \
        "modul-1/uebungen/.chaos/release-2.2.md" \
        "Alter Release-Branch." \
        "chore: Release 2.2.4"

anlegen "hotfix/portal-token" \
        "modul-1/uebungen/.chaos/hotfix.md" \
        "Hotfix, gemerged, Branch blieb liegen." \
        "hotfix: Portal-Token rotiert"

anlegen "dependabot/maven/jackson-2.17.2" \
        "modul-1/uebungen/.chaos/dependabot.md" \
        "Automatischer Update-Branch." \
        "build(deps): jackson-databind 2.17.2"

echo
echo "Angelegt:"
git branch --list "${PRAEFIX}*"
echo
echo "Ihr steht wieder auf '${AUSGANGS_BRANCH}'."
echo "Aufräumen später mit: $0 --aufraeumen"
