package ec.edu.monster.servicio;

import ec.edu.monster.modelo.Unidad;

/**
 * Conversor de unidades de tiempo.
 *
 * @author Grupo 1 - NRC 36841
 */
public class ConversorTiempo implements Conversor {

    @Override
    public double convertir(double valor, Unidad origen, Unidad destino) {
        return Conversor.porFactor(valor, origen, destino);
    }
}
