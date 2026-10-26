package ec.edu.monster.modelo;

/**
 * Magnitudes físicas soportadas por el servicio de conversión.
 *
 * @author Grupo 1 - NRC 36841
 */
public enum Magnitud {
    LONGITUD("Longitud", "metro"),
    MASA("Masa", "kilogramo"),
    TEMPERATURA("Temperatura", "grado Celsius"),
    TIEMPO("Tiempo", "segundo");

    private final String nombre;
    private final String unidadBase;

    Magnitud(String nombre, String unidadBase) {
        this.nombre = nombre;
        this.unidadBase = unidadBase;
    }

    public String getNombre() {
        return nombre;
    }

    public String getUnidadBase() {
        return unidadBase;
    }
}
