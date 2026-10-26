package ec.edu.monster.climov.controlador;

import ec.edu.monster.climov.modelo.Magnitud;
import ec.edu.monster.climov.modelo.Registro;
import ec.edu.monster.climov.modelo.Resultado;
import ec.edu.monster.climov.modelo.ServicioRest;
import ec.edu.monster.climov.modelo.SesionApp;
import java.util.List;

/**
 * Controlador: casos de uso de la app (iniciar sesión, convertir, historial,
 * cerrar sesión) que las pantallas invocan.
 *
 * @author Grupo 1 - NRC 36841
 */
public class ControladorConversion {

    private final ServicioRest servicio = SesionApp.get().getServicio();

    public void iniciarSesion(String url, String usuario, String clave, Tareas.Exito<String> ok, Tareas.Fallo error) {
        servicio.setUrlBase(url);
        Tareas.ejecutar(() -> {
            String mensaje = servicio.iniciarSesion(usuario, clave);
            SesionApp.get().setCatalogo(servicio.obtenerCatalogo());
            return mensaje;
        }, ok, error);
    }

    public void convertir(Magnitud m, String origen, String destino, double valor, Tareas.Exito<Resultado> ok, Tareas.Fallo error) {
        Tareas.ejecutar(() -> servicio.convertir(m.codigo, origen, destino, valor), ok, error);
    }

    public void historial(Tareas.Exito<List<Registro>> ok, Tareas.Fallo error) {
        Tareas.ejecutar(servicio::obtenerHistorial, ok, error);
    }

    public void cerrarSesion(Tareas.Exito<Boolean> ok) {
        Tareas.ejecutar(() -> {
            servicio.cerrarSesion();
            return true;
        }, ok, e -> ok.listo(true));
    }

    public List<Magnitud> catalogo() {
        return SesionApp.get().getCatalogo();
    }

    public String usuario() {
        return servicio.getUsuario();
    }

    public String urlServidor() {
        return servicio.getUrlBase();
    }
}
