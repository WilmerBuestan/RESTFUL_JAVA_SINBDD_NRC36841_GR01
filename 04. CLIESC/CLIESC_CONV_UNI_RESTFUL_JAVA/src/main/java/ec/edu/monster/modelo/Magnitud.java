package ec.edu.monster.modelo;

import java.util.List;

/**
 * Magnitud del catálogo con sus unidades.
 *
 * @author Grupo 1 - NRC 36841
 */
public record Magnitud(String codigo, String nombre, String unidadBase, List<Unidad> unidades) {

    @Override
    public String toString() {
        return nombre;
    }
}
