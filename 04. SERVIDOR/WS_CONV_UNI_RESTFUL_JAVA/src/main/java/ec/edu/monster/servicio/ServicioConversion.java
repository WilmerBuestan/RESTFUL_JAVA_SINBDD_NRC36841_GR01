package ec.edu.monster.servicio;

import ec.edu.monster.modelo.Magnitud;
import ec.edu.monster.modelo.MagnitudCatalogo;
import ec.edu.monster.modelo.ResultadoConversion;
import ec.edu.monster.modelo.SolicitudConversion;
import ec.edu.monster.modelo.Unidad;
import ec.edu.monster.modelo.UnidadCatalogo;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Lógica de negocio de las conversiones: valida la solicitud, delega al
 * conversor de la magnitud y redondea el resultado a 4 decimales.
 *
 * @author Grupo 1 - NRC 36841
 */
public class ServicioConversion {

    private static final ServicioConversion INSTANCIA = new ServicioConversion();
    private final Map<Magnitud, Conversor> conversores = new EnumMap<>(Magnitud.class);

    private ServicioConversion() {
        conversores.put(Magnitud.LONGITUD, new ConversorLongitud());
        conversores.put(Magnitud.MASA, new ConversorMasa());
        conversores.put(Magnitud.TEMPERATURA, new ConversorTemperatura());
        conversores.put(Magnitud.TIEMPO, new ConversorTiempo());
    }

    public static ServicioConversion getInstancia() {
        return INSTANCIA;
    }

    public ResultadoConversion convertir(SolicitudConversion s) {
        if (s == null) {
            throw new ExcepcionConversion("La solicitud está vacía");
        }
        Magnitud magnitud = leerMagnitud(s.getMagnitud());
        Unidad origen = leerUnidad(s.getOrigen(), "origen");
        Unidad destino = leerUnidad(s.getDestino(), "destino");
        if (origen.getMagnitud() != magnitud || destino.getMagnitud() != magnitud) {
            throw new ExcepcionConversion("Las unidades deben pertenecer a la magnitud " + magnitud.getNombre());
        }
        Double valor = s.getValor();
        if (valor == null || valor.isNaN() || valor.isInfinite()) {
            throw new ExcepcionConversion("Ingrese un valor numérico válido");
        }
        double crudo = conversores.get(magnitud).convertir(valor, origen, destino);
        double resultado = redondear(crudo);

        ResultadoConversion r = new ResultadoConversion();
        r.setMagnitud(magnitud.name());
        r.setOrigen(origen.name());
        r.setDestino(destino.name());
        r.setSimboloOrigen(origen.getSimbolo());
        r.setSimboloDestino(destino.getSimbolo());
        r.setValor(valor);
        r.setResultado(resultado);
        r.setDetalle(formatear(valor) + " " + origen.getSimbolo() + " = " + formatear(resultado) + " " + destino.getSimbolo());
        return r;
    }

    public List<MagnitudCatalogo> catalogo() {
        List<MagnitudCatalogo> lista = new ArrayList<>();
        for (Magnitud m : Magnitud.values()) {
            MagnitudCatalogo mc = new MagnitudCatalogo();
            mc.setCodigo(m.name());
            mc.setNombre(m.getNombre());
            mc.setUnidadBase(m.getUnidadBase());
            List<UnidadCatalogo> unidades = new ArrayList<>();
            Arrays.stream(Unidad.values()).filter(u -> u.getMagnitud() == m).forEach(u -> {
                UnidadCatalogo uc = new UnidadCatalogo();
                uc.setCodigo(u.name());
                uc.setNombre(u.getNombre());
                uc.setSimbolo(u.getSimbolo());
                unidades.add(uc);
            });
            mc.setUnidades(unidades);
            lista.add(mc);
        }
        return lista;
    }

    private Magnitud leerMagnitud(String texto) {
        if (texto == null || texto.isBlank()) {
            throw new ExcepcionConversion("Seleccione una magnitud");
        }
        try {
            return Magnitud.valueOf(texto.trim().toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException e) {
            throw new ExcepcionConversion("Magnitud no soportada: " + texto);
        }
    }

    private Unidad leerUnidad(String texto, String campo) {
        if (texto == null || texto.isBlank()) {
            throw new ExcepcionConversion("Seleccione la unidad de " + campo);
        }
        try {
            return Unidad.valueOf(texto.trim().toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException e) {
            throw new ExcepcionConversion("Unidad no soportada: " + texto);
        }
    }

    static double redondear(double v) {
        return BigDecimal.valueOf(v).setScale(4, RoundingMode.HALF_UP).doubleValue();
    }

    private static String formatear(double v) {
        return BigDecimal.valueOf(v).setScale(4, RoundingMode.HALF_UP).stripTrailingZeros().toPlainString();
    }
}
