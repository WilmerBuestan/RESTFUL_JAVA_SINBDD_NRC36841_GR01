package ec.edu.monster.servicio;

import ec.edu.monster.modelo.Unidad;

/**
 * Conversor de temperatura: pasa por grados Celsius como unidad base.
 *
 * @author Grupo 1 - NRC 36841
 */
public class ConversorTemperatura implements Conversor {

    public static final double CERO_ABSOLUTO_C = -273.15;

    @Override
    public double convertir(double valor, Unidad origen, Unidad destino) {
        double celsius = switch (origen) {
            case CELSIUS -> valor;
            case FAHRENHEIT -> (valor - 32) * 5.0 / 9.0;
            case KELVIN -> valor - 273.15;
            default -> throw new ExcepcionConversion("Unidad de temperatura no soportada: " + origen);
        };
        if (celsius < CERO_ABSOLUTO_C - 1e-9) {
            throw new ExcepcionConversion("La temperatura no puede ser menor al cero absoluto (-273,15 °C / 0 K)");
        }
        return switch (destino) {
            case CELSIUS -> celsius;
            case FAHRENHEIT -> celsius * 9.0 / 5.0 + 32;
            case KELVIN -> celsius + 273.15;
            default -> throw new ExcepcionConversion("Unidad de temperatura no soportada: " + destino);
        };
    }
}
