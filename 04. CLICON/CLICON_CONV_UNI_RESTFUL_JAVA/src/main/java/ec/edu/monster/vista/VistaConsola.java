package ec.edu.monster.vista;

import ec.edu.monster.modelo.Magnitud;
import ec.edu.monster.modelo.Registro;
import ec.edu.monster.modelo.Unidad;
import java.io.Console;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Scanner;

/**
 * Vista de texto del cliente de consola: menús, lectura de datos y mensajes
 * con colores ANSI y pequeñas animaciones.
 *
 * @author Grupo 1 - NRC 36841
 */
public class VistaConsola {

    private static final String R = "\u001B[0m", N = "\u001B[1m", AZUL = "\u001B[94m", CIAN = "\u001B[96m",
            VERDE = "\u001B[92m", ROJO = "\u001B[91m", AMARILLO = "\u001B[93m", GRIS = "\u001B[90m";

    private final Scanner entrada = new Scanner(System.in, StandardCharsets.UTF_8);
    private final PrintStream out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
    private final boolean color;

    public VistaConsola(boolean color) {
        this.color = color;
    }

    private String c(String codigo, String texto) {
        return color ? codigo + texto + R : texto;
    }

    public void limpiar() {
        if (color) {
            out.print("\u001B[H\u001B[2J");
        }
        out.println();
    }

    public void banner() {
        String[] arte = {
            " __  __  ___  _  _  ___  _____  ___  ___ ",
            "|  \\/  |/ _ \\| \\| |/ __||_   _|| __|| _ \\",
            "| |\\/| | (_) | .` |\\__ \\  | |  | _| |   /",
            "|_|  |_|\\___/|_|\\_||___/  |_|  |___||_|_\\"};
        String[] tonos = {AZUL, CIAN, AZUL, CIAN};
        for (int k = 0; k < arte.length; k++) {
            out.println(c(N + tonos[k], "   " + arte[k]));
            pausa(60);
        }
        out.println(c(GRIS, "   Conversión de Unidades · Cliente CONSOLA · RESTful Java"));
        out.println(c(GRIS, "   Grupo 1 · NRC 36841 · Arquitectura de Software ESPE"));
        linea();
    }

    public void linea() {
        out.println(c(GRIS, "   ─────────────────────────────────────────────────"));
    }

    public void titulo(String t) {
        out.println();
        out.println(c(N + CIAN, "   ▶ " + t));
        linea();
    }

    public String leerTexto(String etiqueta, String porDefecto) {
        out.print(c(AMARILLO, "   " + etiqueta) + (porDefecto != null ? c(GRIS, " [" + porDefecto + "]") : "") + ": ");
        String t = entrada.hasNextLine() ? entrada.nextLine().trim() : "";
        return t.isEmpty() && porDefecto != null ? porDefecto : t;
    }

    public String leerClave(String etiqueta) {
        Console consola = System.console();
        if (consola != null) {
            char[] c = consola.readPassword("   " + etiqueta + ": ");
            return c == null ? "" : new String(c);
        }
        return leerTexto(etiqueta, null);
    }

    public int leerOpcion(String etiqueta, int min, int max) {
        while (true) {
            String t = leerTexto(etiqueta, null);
            try {
                int n = Integer.parseInt(t);
                if (n >= min && n <= max) {
                    return n;
                }
            } catch (NumberFormatException e) {
                // se vuelve a pedir
            }
            error("Ingrese un número entre " + min + " y " + max);
        }
    }

    public double leerDecimal(String etiqueta) {
        while (true) {
            String t = leerTexto(etiqueta, null).replace(',', '.');
            try {
                double v = Double.parseDouble(t);
                if (!Double.isNaN(v) && !Double.isInfinite(v)) {
                    return v;
                }
            } catch (NumberFormatException e) {
                // se vuelve a pedir
            }
            error("Ingrese un valor numérico válido (ej. 12.5)");
        }
    }

    public int menuPrincipal(String usuario) {
        titulo("MENÚ PRINCIPAL  ·  usuario: " + usuario);
        out.println("   " + c(AZUL, "1.") + " Convertir longitud");
        out.println("   " + c(AZUL, "2.") + " Convertir masa");
        out.println("   " + c(AZUL, "3.") + " Convertir temperatura");
        out.println("   " + c(AZUL, "4.") + " Convertir tiempo");
        out.println("   " + c(AZUL, "5.") + " Ver historial de conversiones");
        out.println("   " + c(AZUL, "6.") + " Cerrar sesión");
        out.println("   " + c(AZUL, "0.") + " Salir");
        linea();
        return leerOpcion("Opción", 0, 6);
    }

    public int elegirUnidad(String etiqueta, Magnitud m) {
        out.println(c(N, "   " + etiqueta + " (" + m.nombre() + ")"));
        List<Unidad> u = m.unidades();
        for (int k = 0; k < u.size(); k++) {
            out.println("     " + c(AZUL, (k + 1) + ".") + " " + u.get(k));
        }
        return leerOpcion("Unidad", 1, u.size()) - 1;
    }

    public void resultado(String detalle) {
        out.println();
        String marco = "═".repeat(detalle.length() + 4);
        out.println(c(VERDE, "   ╔" + marco + "╗"));
        out.print(c(VERDE, "   ║  "));
        for (char ch : detalle.toCharArray()) {
            out.print(c(N + VERDE, String.valueOf(ch)));
            pausa(18);
        }
        out.println(c(VERDE, "  ║"));
        out.println(c(VERDE, "   ╚" + marco + "╝"));
    }

    public void historial(List<Registro> lista) {
        titulo("HISTORIAL DE LA SESIÓN (" + lista.size() + ")");
        if (lista.isEmpty()) {
            info("Aún no ha realizado conversiones");
            return;
        }
        out.println(c(GRIS, String.format("   %-20s %-12s %-12s %s", "FECHA", "MAGNITUD", "CLIENTE", "CONVERSIÓN")));
        for (Registro r : lista) {
            out.println(String.format("   %-20s %-12s %-12s %s", r.fecha(), r.conversion().magnitud(), r.cliente(),
                    c(CIAN, r.conversion().detalle())));
        }
    }

    /** Muestra una animación corta mientras se espera al servidor. */
    public void esperando(String texto) {
        String[] f = {"⠋", "⠙", "⠹", "⠸", "⠼", "⠴", "⠦", "⠧", "⠇", "⠏"};
        for (int k = 0; k < 10; k++) {
            out.print("\r   " + c(CIAN, f[k]) + " " + texto + "…");
            pausa(35);
        }
        out.print("\r" + " ".repeat(texto.length() + 8) + "\r");
    }

    public void exito(String t) {
        out.println(c(VERDE, "   ✔ " + t));
    }

    public void error(String t) {
        out.println(c(ROJO, "   ✖ " + t));
    }

    public void info(String t) {
        out.println(c(GRIS, "   • " + t));
    }

    public void pausaEnter() {
        leerTexto("Presione ENTER para continuar", null);
    }

    private static void pausa(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
