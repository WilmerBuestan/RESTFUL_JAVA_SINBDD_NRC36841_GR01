package ec.edu.monster.modelo;

/**
 * Datos que envía el cliente para iniciar sesión.
 *
 * @author Grupo 1 - NRC 36841
 */
public class SolicitudLogin {

    private String usuario;
    private String clave;
    private String cliente;

    public SolicitudLogin() {
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

}
