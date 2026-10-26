package ec.edu.monster.modelo;

/**
 * Unidad del catálogo publicado por el servidor.
 *
 * @author Grupo 1 - NRC 36841
 */
public record Unidad(String codigo, String nombre, String simbolo) {

    @Override
    public String toString() {
        return nombre + " (" + simbolo + ")";
    }
}
