package ec.edu.monster.controlador;

import ec.edu.monster.modelo.ExcepcionCliente;
import ec.edu.monster.modelo.Magnitud;
import ec.edu.monster.modelo.Resultado;
import ec.edu.monster.modelo.ServicioRest;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

/**
 * Controlador de la pantalla de conversión del cliente web.
 *
 * @author Grupo 1 - NRC 36841
 */
@WebServlet(name = "ConversorServlet", urlPatterns = {"/conversor"})
public class ConversorServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        if (!Sesiones.servicio(req).haySesion()) {
            res.sendRedirect("login");
            return;
        }
        mostrar(req, res, req.getParameter("magnitud"));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        ServicioRest servicio = Sesiones.servicio(req);
        if (!servicio.haySesion()) {
            res.sendRedirect("login");
            return;
        }
        String magnitud = req.getParameter("magnitud");
        String origen = req.getParameter("origen");
        String destino = req.getParameter("destino");
        String valorTxt = req.getParameter("valor") == null ? "" : req.getParameter("valor").trim().replace(',', '.');
        req.setAttribute("origenSel", origen);
        req.setAttribute("destinoSel", destino);
        req.setAttribute("valor", valorTxt);
        try {
            double valor = Double.parseDouble(valorTxt);
            Resultado r = servicio.convertir(magnitud, origen, destino, valor);
            req.setAttribute("resultado", r);
        } catch (NumberFormatException e) {
            req.setAttribute("error", "Ingrese un valor numérico válido (ej. 12.5)");
        } catch (ExcepcionCliente e) {
            if (e.sesionExpirada()) {
                req.getSession().invalidate();
                req.getSession().setAttribute("aviso", "Su sesión expiró. Inicie sesión nuevamente.");
                res.sendRedirect("login");
                return;
            }
            req.setAttribute("error", e.getMessage());
        }
        mostrar(req, res, magnitud);
    }

    @SuppressWarnings("unchecked")
    private void mostrar(HttpServletRequest req, HttpServletResponse res, String codigo) throws ServletException, IOException {
        HttpSession s = req.getSession();
        ServicioRest servicio = Sesiones.servicio(req);
        List<Magnitud> catalogo = (List<Magnitud>) s.getAttribute(Sesiones.CATALOGO);
        try {
            if (catalogo == null) {
                catalogo = servicio.obtenerCatalogo();
                s.setAttribute(Sesiones.CATALOGO, catalogo);
            }
            req.setAttribute("historial", servicio.obtenerHistorial());
        } catch (ExcepcionCliente e) {
            if (e.sesionExpirada()) {
                s.invalidate();
                req.getSession().setAttribute("aviso", "Su sesión expiró. Inicie sesión nuevamente.");
                res.sendRedirect("login");
                return;
            }
            req.setAttribute("error", e.getMessage());
        }
        Magnitud actual = catalogo == null || catalogo.isEmpty() ? null : catalogo.get(0);
        if (catalogo != null && codigo != null) {
            for (Magnitud m : catalogo) {
                if (m.codigo().equals(codigo)) {
                    actual = m;
                }
            }
        }
        req.setAttribute("catalogo", catalogo);
        req.setAttribute("actual", actual);
        req.setAttribute("usuarioSesion", servicio.getUsuario());
        req.setAttribute("servidor", servicio.getUrlBase());
        Object aviso = s.getAttribute("aviso");
        if (aviso != null) {
            req.setAttribute("aviso", aviso);
            s.removeAttribute("aviso");
        }
        Sesiones.vista(req, res, "conversor.jsp");
    }
}
