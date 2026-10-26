package ec.edu.monster.modelo;

/**
 * Unidad publicada en el catálogo.
 *
 * @author Grupo 1 - NRC 36841
 */
public class UnidadCatalogo {

    private String codigo;
    private String nombre;
    private String simbolo;

    public UnidadCatalogo() {
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getSimbolo() {
        return simbolo;
    }

    public void setSimbolo(String simbolo) {
        this.simbolo = simbolo;
    }

}
