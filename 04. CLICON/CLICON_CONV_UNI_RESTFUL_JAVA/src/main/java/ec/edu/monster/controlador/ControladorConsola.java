package ec.edu.monster.controlador;

import ec.edu.monster.modelo.ExcepcionCliente;
import ec.edu.monster.modelo.Magnitud;
import ec.edu.monster.modelo.Resultado;
import ec.edu.monster.modelo.ServicioRest;
import ec.edu.monster.modelo.Unidad;
import ec.edu.monster.vista.VistaConsola;
import java.util.List;

/**
 * Controlador del cliente de consola: coordina la vista con el servicio REST.
 *
 * @author Grupo 1 - NRC 36841
 */
public class ControladorConsola {

    private final VistaConsola vista;
    private final ServicioRest servicio;
    private List<Magnitud> catalogo;

    public ControladorConsola(VistaConsola vista, ServicioRest servicio) {
        this.vista = vista;
        this.servicio = servicio;
    }

    public void iniciar() {
        vista.limpiar();
        vista.banner();
        servicio.setUrlBase(vista.leerTexto("URL del servidor", servicio.getUrlBase()));
        while (true) {
            if (!servicio.haySesion() && !login()) {
                vista.info("Hasta pronto 👋");
                return;
            }
            int op = vista.menuPrincipal(servicio.getUsuario());
            try {
                switch (op) {
                    case 1, 2, 3, 4 -> convertir(catalogo.get(op - 1));
                    case 5 -> {
                        vista.esperando("Consultando historial");
                        vista.historial(servicio.obtenerHistorial());
                        vista.pausaEnter();
                    }
                    case 6 -> {
                        servicio.cerrarSesion();
                        vista.exito("Sesión cerrada correctamente");
                    }
                    default -> {
                        servicio.cerrarSesion();
                        vista.info("Hasta pronto 👋");
                        return;
                    }
                }
            } catch (ExcepcionCliente e) {
                vista.error(e.getMessage());
                if (e.sesionExpirada()) {
                    vista.info("Debe iniciar sesión nuevamente");
                }
            }
        }
    }

    /** @return false si el usuario decide salir */
    private boolean login() {
        while (true) {
            vista.titulo("INICIO DE SESIÓN");
            String usuario = vista.leerTexto("Usuario (o 'salir')", null);
            if (usuario.equalsIgnoreCase("salir")) {
                return false;
            }
            String clave = vista.leerClave("Clave");
            try {
                vista.esperando("Validando credenciales");
                vista.exito(servicio.iniciarSesion(usuario, clave));
                catalogo = servicio.obtenerCatalogo();
                return true;
            } catch (ExcepcionCliente e) {
                vista.error(e.getMessage());
            }
        }
    }

    private void convertir(Magnitud m) throws ExcepcionCliente {
        vista.titulo("CONVERTIR " + m.nombre().toUpperCase());
        Unidad origen = m.unidades().get(vista.elegirUnidad("Unidad de ORIGEN", m));
        Unidad destino = m.unidades().get(vista.elegirUnidad("Unidad de DESTINO", m));
        double valor = vista.leerDecimal("Valor en " + origen.simbolo());
        vista.esperando("Convirtiendo");
        try {
            Resultado r = servicio.convertir(m.codigo(), origen.codigo(), destino.codigo(), valor);
            vista.resultado(r.detalle());
        } catch (ExcepcionCliente e) {
            if (e.sesionExpirada()) {
                throw e;
            }
            vista.error(e.getMessage());
        }
        vista.pausaEnter();
    }
}
