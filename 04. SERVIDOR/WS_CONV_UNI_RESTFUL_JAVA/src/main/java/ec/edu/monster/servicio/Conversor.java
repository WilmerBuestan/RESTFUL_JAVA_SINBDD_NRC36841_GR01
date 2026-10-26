package ec.edu.monster.servicio;

import ec.edu.monster.modelo.Unidad;

/**
 * Contrato común de los conversores de cada magnitud.
 *
 * @author Grupo 1 - NRC 36841
 */
public interface Conversor {

    double convertir(double valor, Unidad origen, Unidad destino);

    /** Conversión por factor: valor -> unidad base -> unidad destino. */
    static double porFactor(double valor, Unidad origen, Unidad destino) {
        double enBase = valor * origen.getFactorBase();
        return enBase / destino.getFactorBase();
    }
}
