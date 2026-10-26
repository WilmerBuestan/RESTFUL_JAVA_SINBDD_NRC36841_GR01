package ec.edu.monster.climov.modelo;

import java.util.List;

/**
 * Magnitud del catálogo con sus unidades.
 *
 * @author Grupo 1 - NRC 36841
 */
public class Magnitud {

    public final String codigo;
    public final String nombre;
    public final String unidadBase;
    public final List<Unidad> unidades;

    public Magnitud(String codigo, String nombre, String unidadBase, List<Unidad> unidades) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.unidadBase = unidadBase;
        this.unidades = unidades;
    }
}
