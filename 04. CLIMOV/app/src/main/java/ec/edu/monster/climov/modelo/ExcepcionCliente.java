package ec.edu.monster.climov.modelo;

/**
 * Error al consumir el servicio (conexión, sesión expirada o datos inválidos).
 *
 * @author Grupo 1 - NRC 36841
 */
public class ExcepcionCliente extends Exception {

    private final int codigoHttp;

    public ExcepcionCliente(String mensaje, int codigoHttp) {
        super(mensaje);
        this.codigoHttp = codigoHttp;
    }

    public boolean sesionExpirada() {
        return codigoHttp == 401;
    }
}
