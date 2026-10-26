package ec.edu.monster.climov.vista;

import android.graphics.Color;

/**
 * Colores e íconos por magnitud (iguales en todos los clientes).
 *
 * @author Grupo 1 - NRC 36841
 */
public final class Colores {

    private Colores() {
    }

    public static int de(String magnitud) {
        switch (magnitud) {
            case "LONGITUD":
                return Color.parseColor("#3D8BFD");
            case "MASA":
                return Color.parseColor("#22C55E");
            case "TEMPERATURA":
                return Color.parseColor("#F43F5E");
            default:
                return Color.parseColor("#FBBF24");
        }
    }

    public static String icono(String magnitud) {
        switch (magnitud) {
            case "LONGITUD":
                return "📏";
            case "MASA":
                return "⚖️";
            case "TEMPERATURA":
                return "🌡️";
            default:
                return "⏱️";
        }
    }
}
