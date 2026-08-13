package de.voltwerk.mcp;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Winziger JSON-Parser und -Schreiber.
 *
 * <p>Bewusst ohne externe Bibliothek, damit der Server auch hinter einem strengen
 * Artifactory-Proxy ohne zusätzliche Freigaben baut. Das ist <b>Infrastruktur</b> —
 * für Lab B müsst ihr hier nichts ändern.</p>
 *
 * <p>Abbildung: Objekt → {@code Map<String,Object>}, Array → {@code List<Object>},
 * Zahl → {@code Double}, Wahrheitswert → {@code Boolean}, {@code null} → {@code null}.</p>
 */
public final class Json {

    private final String quelle;
    private int pos;

    private Json(String quelle) {
        this.quelle = quelle;
    }

    /** Parst einen JSON-Text. */
    public static Object parse(String text) {
        Json p = new Json(text);
        p.leerraum();
        Object wert = p.wert();
        p.leerraum();
        if (p.pos < p.quelle.length()) {
            throw new IllegalArgumentException("Unerwartete Zeichen ab Position " + p.pos);
        }
        return wert;
    }

    /** Serialisiert ein Objekt nach JSON. */
    public static String schreibe(Object wert) {
        StringBuilder sb = new StringBuilder();
        schreibe(wert, sb);
        return sb.toString();
    }

    // ------------------------------------------------------------------ Komfort

    @SuppressWarnings("unchecked")
    public static Map<String, Object> alsObjekt(Object wert) {
        return wert instanceof Map ? (Map<String, Object>) wert : Map.of();
    }

    @SuppressWarnings("unchecked")
    public static List<Object> alsListe(Object wert) {
        return wert instanceof List ? (List<Object>) wert : List.of();
    }

    public static String alsText(Object wert, String ersatz) {
        return wert instanceof String s ? s : ersatz;
    }

    /** Baut eine Map mit garantierter Reihenfolge aus Schlüssel-Wert-Paaren. */
    public static Map<String, Object> objekt(Object... paare) {
        Map<String, Object> map = new LinkedHashMap<>();
        for (int i = 0; i + 1 < paare.length; i += 2) {
            map.put(String.valueOf(paare[i]), paare[i + 1]);
        }
        return map;
    }

    // ------------------------------------------------------------------ Schreiben

    private static void schreibe(Object wert, StringBuilder sb) {
        switch (wert) {
            case null -> sb.append("null");
            case String s -> text(s, sb);
            case Boolean b -> sb.append(b);
            case Integer i -> sb.append(i);
            case Long l -> sb.append(l);
            case Double d -> sb.append(d == Math.rint(d) && !d.isInfinite()
                    ? String.valueOf(d.longValue()) : String.valueOf(d));
            case Number n -> sb.append(n);
            case Map<?, ?> m -> {
                sb.append('{');
                boolean erstes = true;
                for (Map.Entry<?, ?> e : m.entrySet()) {
                    if (!erstes) {
                        sb.append(',');
                    }
                    erstes = false;
                    text(String.valueOf(e.getKey()), sb);
                    sb.append(':');
                    schreibe(e.getValue(), sb);
                }
                sb.append('}');
            }
            case List<?> l -> {
                sb.append('[');
                for (int i = 0; i < l.size(); i++) {
                    if (i > 0) {
                        sb.append(',');
                    }
                    schreibe(l.get(i), sb);
                }
                sb.append(']');
            }
            default -> text(String.valueOf(wert), sb);
        }
    }

    private static void text(String s, StringBuilder sb) {
        sb.append('"');
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            switch (c) {
                case '"' -> sb.append("\\\"");
                case '\\' -> sb.append("\\\\");
                case '\n' -> sb.append("\\n");
                case '\r' -> sb.append("\\r");
                case '\t' -> sb.append("\\t");
                default -> {
                    if (c < 0x20) {
                        sb.append(String.format("\\u%04x", (int) c));
                    } else {
                        sb.append(c);
                    }
                }
            }
        }
        sb.append('"');
    }

    // ------------------------------------------------------------------ Parsen

    private Object wert() {
        leerraum();
        if (pos >= quelle.length()) {
            throw new IllegalArgumentException("Unerwartetes Ende");
        }
        char c = quelle.charAt(pos);
        return switch (c) {
            case '{' -> objektWert();
            case '[' -> listenWert();
            case '"' -> textWert();
            case 't' -> literal("true", Boolean.TRUE);
            case 'f' -> literal("false", Boolean.FALSE);
            case 'n' -> literal("null", null);
            default -> zahlWert();
        };
    }

    private Map<String, Object> objektWert() {
        Map<String, Object> map = new LinkedHashMap<>();
        pos++; // {
        leerraum();
        if (zeichen() == '}') {
            pos++;
            return map;
        }
        while (true) {
            leerraum();
            String schluessel = textWert();
            leerraum();
            erwarte(':');
            map.put(schluessel, wert());
            leerraum();
            char c = zeichen();
            pos++;
            if (c == '}') {
                return map;
            }
            if (c != ',') {
                throw new IllegalArgumentException("',' oder '}' erwartet an Position " + pos);
            }
        }
    }

    private List<Object> listenWert() {
        List<Object> liste = new ArrayList<>();
        pos++; // [
        leerraum();
        if (zeichen() == ']') {
            pos++;
            return liste;
        }
        while (true) {
            liste.add(wert());
            leerraum();
            char c = zeichen();
            pos++;
            if (c == ']') {
                return liste;
            }
            if (c != ',') {
                throw new IllegalArgumentException("',' oder ']' erwartet an Position " + pos);
            }
        }
    }

    private String textWert() {
        erwarte('"');
        StringBuilder sb = new StringBuilder();
        while (true) {
            char c = zeichen();
            pos++;
            if (c == '"') {
                return sb.toString();
            }
            if (c != '\\') {
                sb.append(c);
                continue;
            }
            char e = zeichen();
            pos++;
            switch (e) {
                case '"' -> sb.append('"');
                case '\\' -> sb.append('\\');
                case '/' -> sb.append('/');
                case 'b' -> sb.append('\b');
                case 'f' -> sb.append('\f');
                case 'n' -> sb.append('\n');
                case 'r' -> sb.append('\r');
                case 't' -> sb.append('\t');
                case 'u' -> {
                    sb.append((char) Integer.parseInt(quelle.substring(pos, pos + 4), 16));
                    pos += 4;
                }
                default -> throw new IllegalArgumentException("Unbekannte Escape-Sequenz \\" + e);
            }
        }
    }

    private Double zahlWert() {
        int start = pos;
        while (pos < quelle.length() && "+-0123456789.eE".indexOf(quelle.charAt(pos)) >= 0) {
            pos++;
        }
        if (start == pos) {
            throw new IllegalArgumentException("Zahl erwartet an Position " + pos);
        }
        return Double.valueOf(quelle.substring(start, pos));
    }

    private Object literal(String wort, Object wert) {
        if (!quelle.startsWith(wort, pos)) {
            throw new IllegalArgumentException("'" + wort + "' erwartet an Position " + pos);
        }
        pos += wort.length();
        return wert;
    }

    private void leerraum() {
        while (pos < quelle.length() && Character.isWhitespace(quelle.charAt(pos))) {
            pos++;
        }
    }

    private char zeichen() {
        if (pos >= quelle.length()) {
            throw new IllegalArgumentException("Unerwartetes Ende");
        }
        return quelle.charAt(pos);
    }

    private void erwarte(char c) {
        if (zeichen() != c) {
            throw new IllegalArgumentException("'" + c + "' erwartet an Position " + pos);
        }
        pos++;
    }
}
