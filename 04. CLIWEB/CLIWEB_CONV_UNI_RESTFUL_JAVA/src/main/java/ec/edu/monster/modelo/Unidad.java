package ec.edu.monster.modelo;

/**
 * Unidad del catálogo publicado por el servidor.
 *
 * @author Grupo 1 - NRC 36841
 */
public record Unidad(String codigo, String nombre, String simbolo) {
    // Getters para Expression Language (JSP)
    public String getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public String getSimbolo() {
        return simbolo;
    }


    @Override
    public String toString() {
        return nombre + " (" + simbolo + ")";
    }
}
