package ec.edu.monster.modelo;

import java.util.List;

/**
 * Magnitud con sus unidades, publicada en el catálogo.
 *
 * @author Grupo 1 - NRC 36841
 */
public class MagnitudCatalogo {

    private String codigo;
    private String nombre;
    private String unidadBase;
    private List<UnidadCatalogo> unidades;

    public MagnitudCatalogo() {
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

    public String getUnidadBase() {
        return unidadBase;
    }

    public void setUnidadBase(String unidadBase) {
        this.unidadBase = unidadBase;
    }

    public List<UnidadCatalogo> getUnidades() {
        return unidades;
    }

    public void setUnidades(List<UnidadCatalogo> unidades) {
        this.unidades = unidades;
    }

}
