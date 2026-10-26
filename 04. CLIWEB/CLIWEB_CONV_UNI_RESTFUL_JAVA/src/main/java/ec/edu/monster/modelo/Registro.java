package ec.edu.monster.modelo;

/**
 * Conversión registrada en el historial de la sesión.
 *
 * @author Grupo 1 - NRC 36841
 */
public record Registro(String fecha, String cliente, Resultado conversion) {
    // Getters para Expression Language (JSP)
    public String getFecha() {
        return fecha;
    }

    public String getCliente() {
        return cliente;
    }

    public Resultado getConversion() {
        return conversion;
    }

}
