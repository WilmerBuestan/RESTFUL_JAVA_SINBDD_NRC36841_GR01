package ec.edu.monster.controlador;

import ec.edu.monster.modelo.ServicioRest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

/**
 * Guarda en la sesión HTTP el ServicioRest de cada usuario del cliente web.
 *
 * @author Grupo 1 - NRC 36841
 */
final class Sesiones {

    static final String SERVICIO = "servicioRest";
    static final String CATALOGO = "catalogo";

    private Sesiones() {
    }

    static ServicioRest servicio(HttpServletRequest req) {
        HttpSession s = req.getSession();
        ServicioRest r = (ServicioRest) s.getAttribute(SERVICIO);
        if (r == null) {
            r = new ServicioRest(req.getServletContext().getInitParameter("urlServidor"), "WEB");
            s.setAttribute(SERVICIO, r);
        }
        return r;
    }

    static void vista(HttpServletRequest req, jakarta.servlet.http.HttpServletResponse res, String jsp)
            throws jakarta.servlet.ServletException, java.io.IOException {
        req.getRequestDispatcher("/WEB-INF/vistas/" + jsp).forward(req, res);
    }
}
