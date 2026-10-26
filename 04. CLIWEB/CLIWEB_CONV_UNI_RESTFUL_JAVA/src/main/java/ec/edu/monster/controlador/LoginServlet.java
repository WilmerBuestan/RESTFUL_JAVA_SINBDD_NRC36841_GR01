package ec.edu.monster.controlador;

import ec.edu.monster.modelo.ExcepcionCliente;
import ec.edu.monster.modelo.ServicioRest;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Controlador del inicio de sesión del cliente web.
 *
 * @author Grupo 1 - NRC 36841
 */
@WebServlet(name = "LoginServlet", urlPatterns = {"/login"})
public class LoginServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        if (Sesiones.servicio(req).haySesion()) {
            res.sendRedirect("conversor");
            return;
        }
        req.setAttribute("servidor", Sesiones.servicio(req).getUrlBase());
        Object aviso = req.getSession().getAttribute("aviso");
        if (aviso != null) {
            req.setAttribute("aviso", aviso);
            req.getSession().removeAttribute("aviso");
        }
        Sesiones.vista(req, res, "login.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String usuario = valor(req.getParameter("usuario"));
        String clave = valor(req.getParameter("clave"));
        ServicioRest servicio = Sesiones.servicio(req);
        String servidor = valor(req.getParameter("servidor"));
        if (!servidor.isEmpty()) {
            servicio.setUrlBase(servidor);
        }
        req.setAttribute("servidor", servicio.getUrlBase());
        req.setAttribute("usuario", usuario);
        if (usuario.isEmpty() || clave.isEmpty()) {
            req.setAttribute("error", "Ingrese usuario y clave");
            Sesiones.vista(req, res, "login.jsp");
            return;
        }
        try {
            String mensaje = servicio.iniciarSesion(usuario, clave);
            req.getSession().setAttribute(Sesiones.CATALOGO, servicio.obtenerCatalogo());
            req.getSession().setAttribute("aviso", mensaje);
            res.sendRedirect("conversor");
        } catch (ExcepcionCliente e) {
            req.setAttribute("error", e.getMessage());
            Sesiones.vista(req, res, "login.jsp");
        }
    }

    private static String valor(String p) {
        return p == null ? "" : p.trim();
    }
}
