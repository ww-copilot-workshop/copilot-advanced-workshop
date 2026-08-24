# VW-4714a — Calendar in istNachts durch java.time ersetzen

## Ziel

Die Methode `istNachts(...)` in `AbrechnungsService` ermittelt die Stunde mit
`java.time` statt mit `java.util.Calendar`. Sonst ändert sich nichts.

## Hintergrund

`AbrechnungsService` ist die einzige Stelle im Repo, die noch `Calendar`
benutzt. `AGENTS.md` §3.3 führt `Calendar` ausdrücklich als
Migrationskandidaten. Die Umstellung ist klein und in sich abgeschlossen.

## Art der Änderung

**Das ist eine reine Typ-Migration, keine fachliche Änderung.** Die Regeln,
wann Nacht ist, welche Zone gilt und wie gerundet wird, werden in diesem
Ticket weder geändert noch geprüft. Wer beim Umbau eine fachliche
Auffälligkeit bemerkt: nicht anfassen, sondern im PR als Befund notieren.

## Abnahmekriterien

- [ ] `java.util.Calendar` kommt in `AbrechnungsService.java` nicht mehr vor,
      der Import ist entfernt.
- [ ] Die Konstante `RECHEN_ZONE` bleibt **unverändert** — gleicher Name,
      gleicher Typ, gleicher Wert. Sie wird innerhalb der Methode in die
      java.time-Welt gebrückt, nicht umdeklariert.
- [ ] Die Signatur `private boolean istNachts(Date zeitpunkt)` bleibt
      unverändert. Das `Date` wird über `toInstant()` gebrückt.
- [ ] Die Null-Prüfung am Methodenanfang bleibt Zeichen für Zeichen erhalten,
      inklusive Rückgabewert.
- [ ] Die Stundenberechnung liefert für jeden Eingabewert exakt dasselbe
      Ergebnis wie vorher.
- [ ] Nötige `java.time`-Imports dürfen dazukommen. `java.util.Date` und
      `java.util.TimeZone` bleiben.
- [ ] Alle bestehenden Tests laufen unverändert grün. Kein Test wird angepasst.

## Nicht Teil dieses Tickets

* Jede fachliche Frage: Zonen, Nachtfenster, Rundung, Tarife.
* Das Domänenmodell.
* Die mit `MODERNISIEREN` markierte Stelle in `berechne(...)`.

## Betroffene Pfade

* `modul-5/src/main/java/de/voltwerk/abrechnung/AbrechnungsService.java`,
  ausschließlich die Methode `istNachts(...)` und der Import-Block.

## Testbefehl

```bash
mvn -q -pl modul-5 -am test
```

## Hinweise

* Die Konventionen aus `AGENTS.md` gelten, insbesondere §3.3.
* Dieses Ticket ist bewusst klein.