package ec.edu.monster.modelo;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Lector/escritor JSON mínimo (sin librerías externas).
 * Objetos -> Map, arreglos -> List, números -> Double.
 *
 * @author Grupo 1 - NRC 36841
 */
public final class Json {

    private final String s;
    private int i;

    private Json(String s) {
        this.s = s;
    }

    public static Object leer(String texto) {
        Json j = new Json(texto);
        j.espacios();
        Object v = j.valor();
        j.espacios();
        if (j.i < j.s.length()) {
            throw new IllegalArgumentException("JSON inválido en la posición " + j.i);
        }
        return v;
    }

    @SuppressWarnings("unchecked")
    public static Map<String, Object> objeto(String texto) {
        return (Map<String, Object>) leer(texto);
    }

    public static String escribir(Map<String, ?> mapa) {
        StringBuilder sb = new StringBuilder("{");
        boolean primero = true;
        for (Map.Entry<String, ?> e : mapa.entrySet()) {
            if (!primero) {
                sb.append(',');
            }
            primero = false;
            sb.append(cadena(e.getKey())).append(':');
            Object v = e.getValue();
            if (v == null) {
                sb.append("null");
            } else if (v instanceof Number || v instanceof Boolean) {
                sb.append(v);
            } else {
                sb.append(cadena(v.toString()));
            }
        }
        return sb.append('}').toString();
    }

    private static String cadena(String t) {
        StringBuilder sb = new StringBuilder("\"");
        for (char c : t.toCharArray()) {
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
        return sb.append('"').toString();
    }

    private void espacios() {
        while (i < s.length() && Character.isWhitespace(s.charAt(i))) {
            i++;
        }
    }

    private Object valor() {
        if (i >= s.length()) {
            throw new IllegalArgumentException("JSON incompleto");
        }
        char c = s.charAt(i);
        return switch (c) {
            case '{' -> obj();
            case '[' -> arr();
            case '"' -> str();
            case 't' -> lit("true", Boolean.TRUE);
            case 'f' -> lit("false", Boolean.FALSE);
            case 'n' -> lit("null", null);
            default -> num();
        };
    }

    private Map<String, Object> obj() {
        Map<String, Object> m = new LinkedHashMap<>();
        i++;
        espacios();
        if (s.charAt(i) == '}') {
            i++;
            return m;
        }
        while (true) {
            espacios();
            String k = str();
            espacios();
            esperar(':');
            espacios();
            m.put(k, valor());
            espacios();
            if (s.charAt(i) == ',') {
                i++;
            } else {
                esperar('}');
                return m;
            }
        }
    }

    private List<Object> arr() {
        List<Object> l = new ArrayList<>();
        i++;
        espacios();
        if (s.charAt(i) == ']') {
            i++;
            return l;
        }
        while (true) {
            espacios();
            l.add(valor());
            espacios();
            if (s.charAt(i) == ',') {
                i++;
            } else {
                esperar(']');
                return l;
            }
        }
    }

    private String str() {
        esperar('"');
        StringBuilder sb = new StringBuilder();
        while (true) {
            char c = s.charAt(i++);
            if (c == '"') {
                return sb.toString();
            }
            if (c == '\\') {
                char e = s.charAt(i++);
                switch (e) {
                    case 'n' -> sb.append('\n');
                    case 't' -> sb.append('\t');
                    case 'r' -> sb.append('\r');
                    case 'b' -> sb.append('\b');
                    case 'f' -> sb.append('\f');
                    case 'u' -> {
                        sb.append((char) Integer.parseInt(s.substring(i, i + 4), 16));
                        i += 4;
                    }
                    default -> sb.append(e);
                }
            } else {
                sb.append(c);
            }
        }
    }

    private Double num() {
        int ini = i;
        while (i < s.length() && "+-0123456789.eE".indexOf(s.charAt(i)) >= 0) {
            i++;
        }
        if (ini == i) {
            throw new IllegalArgumentException("Carácter inesperado en JSON: " + s.charAt(i));
        }
        return Double.valueOf(s.substring(ini, i));
    }

    private Object lit(String palabra, Object v) {
        if (!s.startsWith(palabra, i)) {
            throw new IllegalArgumentException("JSON inválido en la posición " + i);
        }
        i += palabra.length();
        return v;
    }

    private void esperar(char c) {
        if (i >= s.length() || s.charAt(i) != c) {
            throw new IllegalArgumentException("Se esperaba '" + c + "' en la posición " + i);
        }
        i++;
    }
}
