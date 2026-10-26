package ec.edu.monster.modelo;

import java.util.List;

/**
 * Magnitud del catálogo con sus unidades.
 *
 * @author Grupo 1 - NRC 36841
 */
public record Magnitud(String codigo, String nombre, String unidadBase, List<Unidad> unidades) {
    // Getters para Expression Language (JSP)
    public String getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public String getUnidadBase() {
        return unidadBase;
    }

    public List<Unidad> getUnidades() {
        return unidades;
    }


    @Override
    public String toString() {
        return nombre;
    }
}
