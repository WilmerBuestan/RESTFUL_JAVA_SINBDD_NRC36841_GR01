package ec.edu.monster.modelo;

/**
 * Resultado de una conversión.
 *
 * @author Grupo 1 - NRC 36841
 */
public class ResultadoConversion {

    private String magnitud;
    private String origen;
    private String destino;
    private String simboloOrigen;
    private String simboloDestino;
    private double valor;
    private double resultado;
    private String detalle;

    public ResultadoConversion() {
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

    public String getSimboloOrigen() {
        return simboloOrigen;
    }

    public void setSimboloOrigen(String simboloOrigen) {
        this.simboloOrigen = simboloOrigen;
    }

    public String getSimboloDestino() {
        return simboloDestino;
    }

    public void setSimboloDestino(String simboloDestino) {
        this.simboloDestino = simboloDestino;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public double getResultado() {
        return resultado;
    }

    public void setResultado(double resultado) {
        this.resultado = resultado;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

}
