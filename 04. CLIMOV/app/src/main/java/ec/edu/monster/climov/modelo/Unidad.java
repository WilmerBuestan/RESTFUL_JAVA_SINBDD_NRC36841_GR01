package ec.edu.monster.climov.modelo;

/**
 * Unidad del catálogo publicado por el servidor.
 *
 * @author Grupo 1 - NRC 36841
 */
public class Unidad {

    public final String codigo;
    public final String nombre;
    public final String simbolo;

    public Unidad(String codigo, String nombre, String simbolo) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.simbolo = simbolo;
    }

    @Override
    public String toString() {
        return nombre + " (" + simbolo + ")";
    }
}
