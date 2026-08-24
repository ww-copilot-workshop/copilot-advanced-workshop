# VW-4714a — Nachttarif-Erkennung von Calendar auf java.time umstellen

## Ziel

Die Ermittlung der Stunde für den Nachttarif benutzt `java.time` statt
`java.util.Calendar`. Am berechneten Ergebnis ändert sich nichts.

## Hintergrund

`AbrechnungsService` ist die einzige Stelle im Repo, die noch `Calendar`
benutzt. `AGENTS.md` §3.3 schreibt `java.time` vor. Die Umstellung ist
klein und in sich abgeschlossen — sie eignet sich als erster Schritt,
bevor größere Umbauten anstehen.

## Fachliche Vorgabe

Die Regeln zum Nachttarif stehen in `modul-5/SPEC.md` und ändern sich
nicht. Dieses Ticket ändert **nur, womit** die Stunde ermittelt wird,
nicht **welche** Stunde gilt. Die Zeitzone bleibt dieselbe wie bisher.

## Abnahmekriterien

- [ ] `java.util.Calendar` kommt in `AbrechnungsService.java` nicht mehr vor.
- [ ] Der Import ist entfernt.
- [ ] Die Zeitzone wird weiterhin explizit gesetzt, nicht aus der
      Systemzeitzone abgeleitet.
- [ ] Alle bestehenden Tests laufen unverändert grün. Kein Test wird
      angepasst — wird einer rot, ist das ein Befund und gehört in den PR,
      nicht in eine Anpassung.
- [ ] Es wird nichts anderes geändert: keine Tarifsätze, keine Rundung,
      keine Struktur der Methode.

## Betroffene Pfade

* `modul-5/src/main/java/de/voltwerk/abrechnung/AbrechnungsService.java`

## Testbefehl

```bash
mvn -q -pl modul-5 -am test
```

## Hinweise

* Die Konventionen aus `AGENTS.md` gelten, insbesondere §3.3, der
  `Calendar` ausdrücklich als Migrationskandidaten führt.
* **Die Zeitzone steht hier nicht zur Debatte.** Ob die aktuell gesetzte
  Zone fachlich die richtige ist, ist eine andere Frage mit einem anderen
  Ticket. Wer sie in diesem Ticket mitändert, ändert Verhalten — und genau
  das soll hier nicht passieren.
* Dieses Ticket ist bewusst klein. Der große Umbau hat ein eigenes Ticket
  und wartet auf die Testabdeckung.