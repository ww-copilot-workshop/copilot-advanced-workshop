package de.voltwerk.abrechnung;

/**
 * Ein Vertragskunde. Wird im Monatsreport als Schlüssel für die Aggregation benutzt.
 */
public class Kunde {

    private String kundennummer;
    private String name;

    public Kunde(String kundennummer, String name) {
        this.kundennummer = kundennummer;
        this.name = name;
    }

    public String getKundennummer() {
        return kundennummer;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || !(o instanceof Kunde)) {
            return false;
        }
        Kunde andere = (Kunde) o;
        return kundennummer != null && kundennummer.equals(andere.kundennummer);
    }

    @Override
    public String toString() {
        return name + " (" + kundennummer + ")";
    }
}
