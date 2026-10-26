package ec.edu.monster.modelo;

/**
 * Evento mostrado en el panel de monitoreo.
 *
 * @author Grupo 1 - NRC 36841
 */
public class EventoServidor {

    private String hora;
    private String tipo;
    private String cliente;
    private String detalle;
    private boolean exito;

    public EventoServidor() {
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public boolean isExito() {
        return exito;
    }

    public void setExito(boolean exito) {
        this.exito = exito;
    }

}
