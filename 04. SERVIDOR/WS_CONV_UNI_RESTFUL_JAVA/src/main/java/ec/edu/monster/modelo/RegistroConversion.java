package ec.edu.monster.modelo;

/**
 * Conversión registrada en el historial.
 *
 * @author Grupo 1 - NRC 36841
 */
public class RegistroConversion {

    private String fecha;
    private String usuario;
    private String cliente;
    private ResultadoConversion conversion;

    public RegistroConversion() {
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public ResultadoConversion getConversion() {
        return conversion;
    }

    public void setConversion(ResultadoConversion conversion) {
        this.conversion = conversion;
    }

}
