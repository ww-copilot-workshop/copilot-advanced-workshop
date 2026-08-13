# SPEC — Abrechnung einer Ladesitzung

**Dokument-ID:** VW-SPEC-ABR
**Version:** 2.3 (gültig ab 01.01.2026)
**Status:** freigegeben durch Produktmanagement und Rechnungswesen
**Geltungsbereich:** Voltwerk Ladepark-Suite, Modul „Abrechnungskern"

> Dieses Dokument ist die **einzige** fachliche Quelle für die Abrechnung.
> Wenn der Code etwas anderes tut als dieses Dokument, ist der Code falsch — nicht das Dokument.

---

## 0. Begriffe

| Begriff | Bedeutung |
|---|---|
| **Ladesitzung** | Ein Vorgang von Anstecken bis Abstecken an genau einem Ladepunkt. |
| **Ladedauer** | Zeitraum zwischen `ladeStart` und `ladeEnde` (Energieabgabe). |
| **Standzeit** | Zeitraum zwischen `ladeEnde` und `abgestecktUm` (Fahrzeug steht, lädt aber nicht mehr). |
| **Nachtfenster** | Täglich 22:00:00 bis 05:59:59 **Ortszeit Europe/Berlin**. |

---

## 1. Fachliche Regeln

### S-1 Energiepreis

Der Energieanteil ergibt sich aus `energieKwh × Preis je kWh`:

| Tarif | Preis je kWh |
|---|---|
| `BASIS` | 0,59 EUR |
| `FLOTTE` | 0,42 EUR |
| `NACHT` | 0,39 EUR — **nur für den Energieanteil, der im Nachtfenster geladen wurde** |

Für den Tarif `NACHT` gilt zusätzlich:

* Die geladene Energie wird **linear über die Ladedauer verteilt** (konstante Leistung
  wird unterstellt).
* Der Anteil, der **innerhalb** des Nachtfensters liegt, wird mit 0,39 EUR/kWh abgerechnet.
* Der Anteil **außerhalb** des Nachtfensters wird mit dem `BASIS`-Preis (0,59 EUR/kWh)
  abgerechnet.
* Beispiel: Ladung 04:00–08:00 Ortszeit, 40 kWh → 2 h im Nachtfenster (20 kWh × 0,39 EUR)
  + 2 h außerhalb (20 kWh × 0,59 EUR) = 19,60 EUR.

Maßgeblich ist immer die **Ortszeit Europe/Berlin**, nicht UTC und nicht die Systemzeitzone.

### S-2 Schnelllade-Zuschlag

Liegt `maxLeistungKw` **echt über** 150,0 kW, wird auf die **gesamte** geladene Energie ein
Zuschlag von 0,08 EUR/kWh erhoben.

Bei genau 150,0 kW fällt **kein** Zuschlag an.

### S-3 Blockiergebühr

* Die Standzeit wird auf **volle Minuten abgerundet**.
* Die ersten **240 Minuten sind frei**.
* **Ab der 241. Minute** kostet jede angefangene Minute 0,10 EUR.
  (Standzeit 240 Minuten → 0,00 EUR. Standzeit 241 Minuten → 0,10 EUR.)
* Die Blockiergebühr ist **je Sitzung auf 12,00 EUR gedeckelt**.
* Fehlt `abgestecktUm` oder liegt es vor `ladeEnde`, fällt keine Blockiergebühr an.

### S-4 Mindestbetrag

Wurde Energie abgegeben (`energieKwh > 0`) und ist die Sitzung kein Fehlversuch nach S-5,
beträgt der Rechnungsbetrag **mindestens 0,50 EUR**.

### S-5 Fehlversuch

Eine Sitzung ist ein **Fehlversuch** und wird mit 0,00 EUR abgerechnet, wenn **beide**
Bedingungen erfüllt sind:

1. `energieKwh < 0,1` **und**
2. Ladedauer < 2 Minuten.

Ist nur eine der beiden Bedingungen erfüllt, wird normal abgerechnet (inklusive
Mindestbetrag nach S-4).

### S-6 Rundung und Rückgabewert

* Zwischenergebnisse werden **ohne Zwischenrundung** gerechnet.
* Der Endbetrag wird **kaufmännisch** gerundet (`RoundingMode.HALF_UP`) auf
  **zwei Nachkommastellen**.
* Rückgabe als `BigDecimal`. Ein Vergleich per `compareTo` muss gegen den erwarteten
  Betrag 0 ergeben.

### S-7 Zeitzone und Sommerzeit

* Alle fachlichen Zeitfenster (S-1 Nachtfenster) beziehen sich auf **Europe/Berlin**.
* Dauern (Ladedauer, Standzeit) sind **tatsächlich verstrichene Zeit**. Eine Sitzung, die
  über die Sommerzeitumstellung läuft, hat entsprechend eine Stunde mehr oder weniger
  Wanduhrzeit, aber die real verstrichene Zeit zählt.

### S-8 Reihenfolge der Berechnung

1. Fehlversuch prüfen (S-5) → falls ja: 0,00 EUR, Ende.
2. Energieanteil (S-1).
3. Schnelllade-Zuschlag (S-2).
4. Blockiergebühr inklusive Deckel (S-3).
5. Summe bilden.
6. Mindestbetrag anwenden (S-4).
7. Runden (S-6).

---

## 2. Monatsreport

### S-10 Aggregation je Ladepunkt

`MonatsReport.proLadepunkt(...)` liefert **genau eine Zeile je unterschiedlicher
`ladepunktId`**, in der Reihenfolge des ersten Auftretens. Jede Zeile enthält die Anzahl
der Sitzungen und die Summe der Beträge dieses Ladepunkts.

Dies gilt für **alle** Ladepunkt-IDs, unabhängig von ihrem Zahlenwert.

### S-11 Kontrollrechnung

`MonatsReport.summeStimmt(...)` liefert `true`, wenn die Summe aller Zeilenbeträge dem
erwarteten Betrag **wertgleich** ist. Wertgleichheit heißt: gleicher Betrag, unabhängig
von der Darstellung (`12.5` und `12.50` sind derselbe Betrag).

---

## 3. API-Kontrakt (Ist-Stand, für Testerstellung verbindlich)

Diese Signaturen existieren heute im Code und dürfen für Tests als gegeben angenommen
werden. **Zum Schreiben der Soll-Tests muss `src/main` nicht gelesen werden.**

```java
package de.voltwerk.abrechnung;

public enum Tarif { BASIS, FLOTTE, NACHT }

public class Kunde {
    public Kunde(String kundennummer, String name);
    public String getKundennummer();
    public String getName();
    public void setName(String name);
}

public class Ladesitzung {
    public Ladesitzung();                    // Setter-basiert befüllbar
    public Ladesitzung(String sitzungsId,
                       Integer ladepunktId,
                       Kunde kunde,
                       Tarif tarif,
                       java.util.Date ladeStart,
                       java.util.Date ladeEnde,
                       java.util.Date abgestecktUm,
                       double energieKwh,
                       double maxLeistungKw);
    // dazu Getter und Setter für jedes Feld
}

public class AbrechnungsService {
    public java.math.BigDecimal berechne(Ladesitzung sitzung);   // wirft IllegalArgumentException bei null
}

public class ReportZeile {
    public ReportZeile(Integer ladepunktId);
    public Integer getLadepunktId();
    public int getAnzahlSitzungen();
    public java.math.BigDecimal getSumme();

    // paketprivat: aus einem Test im selben Package (de.voltwerk.abrechnung)
    // aufrufbar. Nötig, um für S-11 eine Zeile mit einem Betrag bestimmter
    // Skala zu konstruieren.
    void addiere(java.math.BigDecimal betrag);
}

public class MonatsReport {
    public MonatsReport(AbrechnungsService abrechnung);
    public java.util.List<ReportZeile> proLadepunkt(java.util.List<Ladesitzung> sitzungen);
    public boolean summeStimmt(java.util.List<ReportZeile> zeilen, java.math.BigDecimal erwarteteSumme);
}
```

**Hinweis zu Zeitangaben in Tests:** `java.util.Date` ist ein Zeitpunkt ohne Zeitzone.
Eine Ortszeit erzeugt ihr am saubersten so:

```java
Date d = Date.from(
    java.time.LocalDateTime.of(2026, 3, 15, 23, 30)
        .atZone(java.time.ZoneId.of("Europe/Berlin"))
        .toInstant());
```

---

## 4. Referenz-Beispiele

Diese Fälle sind vom Rechnungswesen abgenommen und gelten als verbindlich.

In allen Beispielen beträgt die **Ladedauer 60 Minuten**, sofern nicht anders angegeben.
„Standzeit" meint immer die Zeit **nach** `ladeEnde`.

| # | Sitzung | Erwarteter Betrag |
|---|---|---|
| B1 | `BASIS`, 10,0 kWh, 50 kW, keine Standzeit | **5,90 EUR** |
| B2 | `FLOTTE`, 33,0 kWh, 50 kW, keine Standzeit | **13,86 EUR** |
| B3 | `BASIS`, 3,456 kWh, 50 kW, keine Standzeit | **2,04 EUR** |
| B4 | `BASIS`, 20,0 kWh, **150,0 kW**, keine Standzeit | **11,80 EUR** |
| B5 | `BASIS`, 20,0 kWh, **150,1 kW**, keine Standzeit | **13,40 EUR** |
| B6 | `BASIS`, 10,0 kWh, 50 kW, Standzeit **240 min** | **5,90 EUR** |
| B7 | `BASIS`, 10,0 kWh, 50 kW, Standzeit **241 min** | **6,00 EUR** |
| B8 | `BASIS`, 10,0 kWh, 50 kW, Standzeit **600 min** | **17,90 EUR** |
| B9 | `BASIS`, 0,05 kWh, 11 kW, **Ladedauer 1 min**, keine Standzeit | **0,00 EUR** |
| B10 | `BASIS`, 0,05 kWh, 11 kW, **Ladedauer 45 min**, keine Standzeit | **0,50 EUR** |
| B11 | `NACHT`, 15.01.2026 **23:00–01:00** Ortszeit, 20,0 kWh, 11 kW | **7,80 EUR** |
| B12 | `NACHT`, 15.01.2026 **04:00–08:00** Ortszeit, 40,0 kWh, 11 kW | **19,60 EUR** |
| B13 | `NACHT`, 15.07.2026 **22:30–23:30** Ortszeit (Sommerzeit), 10,0 kWh, 11 kW | **3,90 EUR** |

> B8 zur Kontrolle: 5,90 EUR Energie + Blockiergebühr für 360 Minuten über der Freigrenze
> = 36,00 EUR, gedeckelt auf 12,00 EUR → 17,90 EUR.
>
> B12 zur Kontrolle: 4 h Ladedauer, davon 04:00–06:00 im Nachtfenster. Bei linearer
> Verteilung sind das 20 kWh × 0,39 EUR + 20 kWh × 0,59 EUR = 19,60 EUR.

---

## 5. Was dieses Dokument **nicht** regelt

* Monatliche Grundgebühren (stehen im Vertragsmodul, nicht hier).
* Umsatzsteuer (wird nachgelagert im Rechnungslauf aufgeschlagen).
* Roaming-Abrechnung über Fremdnetze (Modul „Roaming", eigene Spec).
* Währungsumrechnung — es wird ausschließlich in EUR abgerechnet.
