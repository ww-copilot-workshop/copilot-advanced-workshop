package de.voltwerk.abrechnung;

import java.util.Date;

/**
 * Eine einzelne Ladesitzung an einem Ladepunkt.
 *
 * <p>Datenherkunft: OCPP-Adapter, der die Sitzung beim Abstecken abschließt.
 * Die Felder sind absichtlich so benannt wie im Adapter-JSON.</p>
 */
public class Ladesitzung {

    private String sitzungsId;
    private Integer ladepunktId;
    private Kunde kunde;
    private Tarif tarif;

    /** Beginn der Energieabgabe. */
    private Date ladeStart;

    /** Ende der Energieabgabe (Fahrzeug voll oder Abbruch). */
    private Date ladeEnde;

    /** Zeitpunkt, an dem das Kabel gezogen wurde. Steuert die Blockiergebühr. */
    private Date abgestecktUm;

    private double energieKwh;

    /** Höchste gemessene Ladeleistung während der Sitzung. */
    private double maxLeistungKw;

    public Ladesitzung() {
        // vom OCPP-Adapter per Setter befüllt
    }

    public Ladesitzung(String sitzungsId, Integer ladepunktId, Kunde kunde, Tarif tarif,
                       Date ladeStart, Date ladeEnde, Date abgestecktUm,
                       double energieKwh, double maxLeistungKw) {
        this.sitzungsId = sitzungsId;
        this.ladepunktId = ladepunktId;
        this.kunde = kunde;
        this.tarif = tarif;
        this.ladeStart = ladeStart;
        this.ladeEnde = ladeEnde;
        this.abgestecktUm = abgestecktUm;
        this.energieKwh = energieKwh;
        this.maxLeistungKw = maxLeistungKw;
    }

    public String getSitzungsId() {
        return sitzungsId;
    }

    public void setSitzungsId(String sitzungsId) {
        this.sitzungsId = sitzungsId;
    }

    public Integer getLadepunktId() {
        return ladepunktId;
    }

    public void setLadepunktId(Integer ladepunktId) {
        this.ladepunktId = ladepunktId;
    }

    public Kunde getKunde() {
        return kunde;
    }

    public void setKunde(Kunde kunde) {
        this.kunde = kunde;
    }

    public Tarif getTarif() {
        return tarif;
    }

    public void setTarif(Tarif tarif) {
        this.tarif = tarif;
    }

    public Date getLadeStart() {
        return ladeStart;
    }

    public void setLadeStart(Date ladeStart) {
        this.ladeStart = ladeStart;
    }

    public Date getLadeEnde() {
        return ladeEnde;
    }

    public void setLadeEnde(Date ladeEnde) {
        this.ladeEnde = ladeEnde;
    }

    public Date getAbgestecktUm() {
        return abgestecktUm;
    }

    public void setAbgestecktUm(Date abgestecktUm) {
        this.abgestecktUm = abgestecktUm;
    }

    public double getEnergieKwh() {
        return energieKwh;
    }

    public void setEnergieKwh(double energieKwh) {
        this.energieKwh = energieKwh;
    }

    public double getMaxLeistungKw() {
        return maxLeistungKw;
    }

    public void setMaxLeistungKw(double maxLeistungKw) {
        this.maxLeistungKw = maxLeistungKw;
    }
}
