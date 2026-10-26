package ec.edu.monster.modelo;

/**
 * Resultado de una conversión devuelto por el servidor.
 *
 * @author Grupo 1 - NRC 36841
 */
public record Resultado(String magnitud, String origen, String destino, double valor, double resultado,
        String simboloOrigen, String simboloDestino, String detalle) {
}
