package ec.edu.monster.modelo;

/**
 * Resultado de una conversión devuelto por el servidor.
 *
 * @author Grupo 1 - NRC 36841
 */
public record Resultado(String magnitud, String origen, String destino, double valor, double resultado,
        String simboloOrigen, String simboloDestino, String detalle) {
    // Getters para Expression Language (JSP)
    public String getMagnitud() {
        return magnitud;
    }

    public String getOrigen() {
        return origen;
    }

    public String getDestino() {
        return destino;
    }

    public double getValor() {
        return valor;
    }

    public double getResultado() {
        return resultado;
    }

    public String getSimboloOrigen() {
        return simboloOrigen;
    }

    public String getSimboloDestino() {
        return simboloDestino;
    }

    public String getDetalle() {
        return detalle;
    }

}
