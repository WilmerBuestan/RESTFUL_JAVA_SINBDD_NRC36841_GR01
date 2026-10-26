package ec.edu.monster.modelo;

/**
 * Unidades de medida soportadas. El factor indica cuántas unidades base
 * equivalen a una unidad (la temperatura se convierte con fórmulas propias).
 *
 * @author Grupo 1 - NRC 36841
 */
public enum Unidad {
    // Longitud (base: metro)
    CENTIMETRO(Magnitud.LONGITUD, "Centímetros", "cm", 0.01),
    METRO(Magnitud.LONGITUD, "Metros", "m", 1.0),
    KILOMETRO(Magnitud.LONGITUD, "Kilómetros", "km", 1000.0),
    PIE(Magnitud.LONGITUD, "Pies", "ft", 0.3048),
    MILLA(Magnitud.LONGITUD, "Millas", "mi", 1609.344),
    // Masa (base: kilogramo)
    GRAMO(Magnitud.MASA, "Gramos", "g", 0.001),
    KILOGRAMO(Magnitud.MASA, "Kilogramos", "kg", 1.0),
    ONZA(Magnitud.MASA, "Onzas", "oz", 0.028349523125),
    LIBRA(Magnitud.MASA, "Libras", "lb", 0.45359237),
    QUINTAL(Magnitud.MASA, "Quintales", "qq", 45.359237),
    // Temperatura (base: grado Celsius)
    CELSIUS(Magnitud.TEMPERATURA, "Grados Celsius", "°C", 1.0),
    FAHRENHEIT(Magnitud.TEMPERATURA, "Grados Fahrenheit", "°F", 1.0),
    KELVIN(Magnitud.TEMPERATURA, "Kelvin", "K", 1.0),
    // Tiempo (base: segundo)
    SEGUNDO(Magnitud.TIEMPO, "Segundos", "s", 1.0),
    MINUTO(Magnitud.TIEMPO, "Minutos", "min", 60.0),
    HORA(Magnitud.TIEMPO, "Horas", "h", 3600.0),
    DIA(Magnitud.TIEMPO, "Días", "d", 86400.0),
    SEMANA(Magnitud.TIEMPO, "Semanas", "sem", 604800.0);

    private final Magnitud magnitud;
    private final String nombre;
    private final String simbolo;
    private final double factorBase;

    Unidad(Magnitud magnitud, String nombre, String simbolo, double factorBase) {
        this.magnitud = magnitud;
        this.nombre = nombre;
        this.simbolo = simbolo;
        this.factorBase = factorBase;
    }

    public Magnitud getMagnitud() {
        return magnitud;
    }

    public String getNombre() {
        return nombre;
    }

    public String getSimbolo() {
        return simbolo;
    }

    public double getFactorBase() {
        return factorBase;
    }
}
