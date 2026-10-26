package ec.edu.monster.modelo;

/**
 * Datos que envía el cliente para convertir un valor (magnitud y unidades por su código, ej. LONGITUD, METRO).
 *
 * @author Grupo 1 - NRC 36841
 */
public class SolicitudConversion {

    private String magnitud;
    private String origen;
    private String destino;
    private Double valor;

    public SolicitudConversion() {
    }

    public String getMagnitud() {
        return magnitud;
    }

    public void setMagnitud(String magnitud) {
        this.magnitud = magnitud;
    }

    public String getOrigen() {
        return origen;
    }

    public void setOrigen(String origen) {
        this.origen = origen;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

}
