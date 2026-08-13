package de.voltwerk.mcp;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Lesezugriff auf den (simulierten) Artifactory-Index.
 *
 * <p>Quelle ist standardmäßig die mitgelieferte {@code artifactory-index.json} im
 * Classpath. Über die Umgebungsvariable {@code WERKSTATT_INDEX} lässt sich eine eigene
 * Datei einhängen — praktisch, wenn ihr den Index für eure eigenen Pakete erweitern wollt.</p>
 */
public class PaketIndex {

    /** Eine Version eines Pakets im Proxy. */
    public record Version(String version, String freigabe, String lizenz,
                          List<String> cves, String hinweis) {

        public boolean istFreigegeben() {
            return "freigegeben".equals(freigabe);
        }
    }

    /** Ein Paket mit allen im Proxy bekannten Versionen. */
    public record Paket(String koordinate, List<Version> versionen) {
    }

    private final String stand;
    private final String proxy;
    private final List<Paket> pakete;

    private PaketIndex(String stand, String proxy, List<Paket> pakete) {
        this.stand = stand;
        this.proxy = proxy;
        this.pakete = pakete;
    }

    public String stand() {
        return stand;
    }

    public String proxy() {
        return proxy;
    }

    public List<Paket> alle() {
        return pakete;
    }

    /** Findet ein Paket über seine Maven-Koordinate, Groß-/Kleinschreibung egal. */
    public Optional<Paket> finde(String koordinate) {
        if (koordinate == null) {
            return Optional.empty();
        }
        String gesucht = koordinate.trim();
        return pakete.stream()
                .filter(p -> p.koordinate().equalsIgnoreCase(gesucht))
                .findFirst();
    }

    /** Sucht Pakete, deren Koordinate den Teilstring enthält. */
    public List<Paket> suche(String teil) {
        String gesucht = teil == null ? "" : teil.trim().toLowerCase();
        return pakete.stream()
                .filter(p -> p.koordinate().toLowerCase().contains(gesucht))
                .toList();
    }

    // ------------------------------------------------------------------ Laden

    public static PaketIndex laden() {
        String pfad = System.getenv("WERKSTATT_INDEX");
        try {
            String json = (pfad != null && !pfad.isBlank())
                    ? Files.readString(Path.of(pfad), StandardCharsets.UTF_8)
                    : vonClasspath();
            return ausJson(json);
        } catch (IOException e) {
            throw new IllegalStateException("Index nicht lesbar: " + e.getMessage(), e);
        }
    }

    private static String vonClasspath() throws IOException {
        try (InputStream in = PaketIndex.class.getResourceAsStream("/artifactory-index.json")) {
            if (in == null) {
                throw new IOException("artifactory-index.json fehlt im Classpath");
            }
            return new String(in.readAllBytes(), StandardCharsets.UTF_8);
        }
    }

    static PaketIndex ausJson(String json) {
        Map<String, Object> wurzel = Json.alsObjekt(Json.parse(json));
        List<Paket> pakete = new ArrayList<>();

        for (Object rohesPaket : Json.alsListe(wurzel.get("pakete"))) {
            Map<String, Object> p = Json.alsObjekt(rohesPaket);
            List<Version> versionen = new ArrayList<>();

            for (Object rohesVersion : Json.alsListe(p.get("versionen"))) {
                Map<String, Object> v = Json.alsObjekt(rohesVersion);
                List<String> cves = Json.alsListe(v.get("cves")).stream()
                        .map(String::valueOf)
                        .toList();
                versionen.add(new Version(
                        Json.alsText(v.get("version"), "?"),
                        Json.alsText(v.get("freigabe"), "unbekannt"),
                        Json.alsText(v.get("lizenz"), "unbekannt"),
                        cves,
                        Json.alsText(v.get("hinweis"), "")));
            }
            pakete.add(new Paket(Json.alsText(p.get("koordinate"), "?"), versionen));
        }

        return new PaketIndex(
                Json.alsText(wurzel.get("stand"), "unbekannt"),
                Json.alsText(wurzel.get("proxy"), "unbekannt"),
                pakete);
    }
}
