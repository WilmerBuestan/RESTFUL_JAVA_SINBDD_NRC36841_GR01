package ec.edu.monster.servicio;

/**
 * Error de negocio en una conversión (datos inválidos o unidad no soportada).
 *
 * @author Grupo 1 - NRC 36841
 */
public class ExcepcionConversion extends RuntimeException {

    public ExcepcionConversion(String mensaje) {
        super(mensaje);
    }
}
