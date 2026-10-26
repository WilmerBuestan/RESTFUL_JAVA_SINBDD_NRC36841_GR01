package ec.edu.monster.prueba;

import ec.edu.monster.modelo.ResultadoConversion;
import ec.edu.monster.modelo.Sesion;
import ec.edu.monster.modelo.SolicitudConversion;
import ec.edu.monster.servicio.ExcepcionConversion;
import ec.edu.monster.servicio.ServicioAutenticacion;
import ec.edu.monster.servicio.ServicioConversion;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import org.junit.jupiter.api.Test;

/**
 * Pruebas unitarias del servicio de conversión y de autenticación.
 *
 * @author Grupo 1 - NRC 36841
 */
public class PruebasConversion {

    private final ServicioConversion servicio = ServicioConversion.getInstancia();

    private double convertir(String magnitud, String origen, String destino, double valor) {
        SolicitudConversion s = new SolicitudConversion();
        s.setMagnitud(magnitud);
        s.setOrigen(origen);
        s.setDestino(destino);
        s.setValor(valor);
        ResultadoConversion r = servicio.convertir(s);
        return r.getResultado();
    }

    @Test
    public void pruebaLongitud() {
        assertEquals(1.0, convertir("LONGITUD", "CENTIMETRO", "METRO", 100));
        assertEquals(1.6093, convertir("LONGITUD", "MILLA", "KILOMETRO", 1));
        assertEquals(3.2808, convertir("LONGITUD", "METRO", "PIE", 1));
    }

    @Test
    public void pruebaMasa() {
        assertEquals(2.2046, convertir("MASA", "KILOGRAMO", "LIBRA", 1));
        assertEquals(100.0, convertir("MASA", "QUINTAL", "LIBRA", 1));
        assertEquals(28.3495, convertir("MASA", "ONZA", "GRAMO", 1));
    }

    @Test
    public void pruebaTemperatura() {
        assertEquals(212.0, convertir("TEMPERATURA", "CELSIUS", "FAHRENHEIT", 100));
        assertEquals(273.15, convertir("TEMPERATURA", "CELSIUS", "KELVIN", 0));
        assertEquals(37.0, convertir("TEMPERATURA", "FAHRENHEIT", "CELSIUS", 98.6));
        assertThrows(ExcepcionConversion.class, () -> convertir("TEMPERATURA", "KELVIN", "CELSIUS", -5));
    }

    @Test
    public void pruebaTiempo() {
        assertEquals(3600.0, convertir("TIEMPO", "HORA", "SEGUNDO", 1));
        assertEquals(7.0, convertir("TIEMPO", "SEMANA", "DIA", 1));
        assertEquals(1.5, convertir("TIEMPO", "MINUTO", "HORA", 90));
    }

    @Test
    public void pruebaErrores() {
        assertThrows(ExcepcionConversion.class, () -> convertir("LONGITUD", "METRO", "KILOGRAMO", 1));
        assertThrows(ExcepcionConversion.class, () -> convertir("VOLUMEN", "METRO", "PIE", 1));
        assertThrows(ExcepcionConversion.class, () -> convertir("LONGITUD", "LEGUA", "PIE", 1));
    }

    @Test
    public void pruebaAutenticacion() {
        ServicioAutenticacion auth = ServicioAutenticacion.getInstancia();
        assertNull(auth.iniciarSesion("monster", "clave_mala", "PRUEBA"));
        Sesion s = auth.iniciarSesion("monster", "monster69", "PRUEBA");
        assertNotNull(s);
        assertTrue(s.estaVigente());
        assertNotNull(auth.validarToken(s.getToken()));
        assertTrue(auth.cerrarSesion(s.getToken()));
        assertNull(auth.validarToken(s.getToken()));
        assertFalse(auth.cerrarSesion(s.getToken()));
    }
}
