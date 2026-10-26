package ec.edu.monster.controlador;

import ec.edu.monster.modelo.ExcepcionCliente;
import ec.edu.monster.modelo.Magnitud;
import ec.edu.monster.modelo.Registro;
import ec.edu.monster.modelo.Resultado;
import ec.edu.monster.modelo.ServicioRest;
import ec.edu.monster.modelo.Unidad;
import ec.edu.monster.vista.Estilo;
import ec.edu.monster.vista.VentanaLogin;
import ec.edu.monster.vista.VentanaPrincipal;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.function.Consumer;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

/**
 * Controlador del cliente de escritorio. Las llamadas al servidor se hacen en
 * segundo plano (SwingWorker) para no congelar la interfaz.
 *
 * @author Grupo 1 - NRC 36841
 */
public class ControladorEscritorio {

    private final ServicioRest servicio;
    private final VentanaLogin login;
    private final VentanaPrincipal principal = new VentanaPrincipal();
    private Magnitud magnitudActual;

    public ControladorEscritorio(ServicioRest servicio) {
        this.servicio = servicio;
        this.login = new VentanaLogin(servicio.getUrlBase());
        login.getBtnIngresar().addActionListener(e -> iniciarSesion());
        principal.getBtnConvertir().addActionListener(e -> convertir());
        principal.getBtnIntercambiar().addActionListener(e -> principal.intercambiar());
        principal.getBtnHistorial().addActionListener(e -> cargarHistorial());
        principal.getBtnCerrarSesion().addActionListener(e -> cerrarSesion());
        principal.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                servicio.cerrarSesion();
                System.exit(0);
            }
        });
    }

    public void iniciar() {
        login.setVisible(true);
    }

    private void iniciarSesion() {
        if (login.getUsuario().isEmpty() || login.getClave().isEmpty()) {
            login.error("Ingrese usuario y clave");
            return;
        }
        servicio.setUrlBase(login.getServidor());
        login.cargando(true);
        enSegundoPlano(() -> {
            String msg = servicio.iniciarSesion(login.getUsuario(), login.getClave());
            List<Magnitud> cat = servicio.obtenerCatalogo();
            return new Object[]{msg, cat};
        }, r -> {
            login.cargando(false);
            @SuppressWarnings("unchecked")
            List<Magnitud> cat = (List<Magnitud>) r[1];
            principal.setUsuario(servicio.getUsuario());
            principal.setEstado("Conectado: " + r[0] + "   ·   Servidor: " + servicio.getUrlBase() + "   ·   Sesión válida por 30 minutos");
            principal.setCatalogo(cat, this::seleccionarMagnitud);
            principal.mostrarHistorial(List.of());
            login.setVisible(false);
            login.limpiar();
            principal.setVisible(true);
        }, e -> {
            login.cargando(false);
            login.error(e.getMessage());
        });
    }

    private void seleccionarMagnitud(Magnitud m) {
        magnitudActual = m;
        principal.mostrarMagnitud(m);
    }

    private void convertir() {
        Unidad o = principal.getOrigen();
        Unidad d = principal.getDestino();
        double valor;
        try {
            valor = Double.parseDouble(principal.getValorTexto());
        } catch (NumberFormatException ex) {
            principal.mostrarError("Ingrese un valor numérico válido (ej. 12.5)");
            return;
        }
        if (magnitudActual == null || o == null || d == null) {
            principal.mostrarError("Seleccione la magnitud y las unidades");
            return;
        }
        principal.cargando(true);
        enSegundoPlano(() -> servicio.convertir(magnitudActual.codigo(), o.codigo(), d.codigo(), valor), (Resultado r) -> {
            principal.cargando(false);
            principal.mostrarResultado(r.resultado(), r.simboloDestino(), r.detalle(), Estilo.colorMagnitud(r.magnitud()));
            cargarHistorial();
        }, e -> {
            principal.cargando(false);
            principal.mostrarError(e.getMessage());
        });
    }

    private void cargarHistorial() {
        enSegundoPlano(servicio::obtenerHistorial, (List<Registro> l) -> principal.mostrarHistorial(l),
                e -> principal.mostrarError(e.getMessage()));
    }

    private void cerrarSesion() {
        int r = JOptionPane.showConfirmDialog(principal, "¿Desea cerrar la sesión?", "Cerrar sesión",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, Estilo.monster(48));
        if (r != JOptionPane.YES_OPTION) {
            return;
        }
        enSegundoPlano(() -> {
            servicio.cerrarSesion();
            return true;
        }, x -> volverAlLogin("Sesión cerrada correctamente"), e -> volverAlLogin(null));
    }

    private void volverAlLogin(String mensaje) {
        principal.setVisible(false);
        login.setVisible(true);
        if (mensaje != null) {
            login.mensaje(mensaje, Estilo.VERDE);
        }
    }

    /** Ejecuta una llamada al servidor sin bloquear la interfaz. */
    private <T> void enSegundoPlano(Callable<T> tarea, Consumer<T> alTerminar, Consumer<ExcepcionCliente> alFallar) {
        new SwingWorker<T, Void>() {
            @Override
            protected T doInBackground() throws Exception {
                return tarea.call();
            }

            @Override
            protected void done() {
                try {
                    alTerminar.accept(get());
                } catch (java.util.concurrent.ExecutionException ex) {
                    Throwable c = ex.getCause();
                    ExcepcionCliente e = c instanceof ExcepcionCliente ec ? ec : new ExcepcionCliente("Error: " + c.getMessage(), 0);
                    if (e.sesionExpirada() && principal.isVisible()) {
                        JOptionPane.showMessageDialog(principal, e.getMessage(), "Sesión expirada", JOptionPane.WARNING_MESSAGE);
                        volverAlLogin(null);
                        login.error("Su sesión expiró. Inicie sesión nuevamente.");
                        return;
                    }
                    alFallar.accept(e);
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
            }
        }.execute();
    }
}
