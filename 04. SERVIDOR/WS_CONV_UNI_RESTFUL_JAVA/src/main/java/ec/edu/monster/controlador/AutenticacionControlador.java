package ec.edu.monster.controlador;

import ec.edu.monster.config.FiltroAutenticacion;
import ec.edu.monster.config.Seguro;
import ec.edu.monster.modelo.RespuestaApi;
import ec.edu.monster.modelo.RespuestaLogin;
import ec.edu.monster.modelo.Sesion;
import ec.edu.monster.modelo.SolicitudLogin;
import ec.edu.monster.servicio.ServicioAutenticacion;
import ec.edu.monster.servicio.ServicioHistorial;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.time.format.DateTimeFormatter;

/**
 * Recurso REST de seguridad: inicio y cierre de sesión.
 *
 * @author Grupo 1 - NRC 36841
 */
@Path("auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AutenticacionControlador {

    private final ServicioAutenticacion autenticacion = ServicioAutenticacion.getInstancia();
    private final ServicioHistorial historial = ServicioHistorial.getInstancia();

    @POST
    @Path("login")
    public Response iniciarSesion(SolicitudLogin s) {
        if (s == null || vacio(s.getUsuario()) || vacio(s.getClave())) {
            historial.registrarEvento("LOGIN", s == null ? null : s.getCliente(), "Campos vacíos", false);
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(RespuestaApi.error("Ingrese usuario y clave")).build();
        }
        Sesion sesion = autenticacion.iniciarSesion(s.getUsuario(), s.getClave(), s.getCliente());
        if (sesion == null) {
            historial.registrarEvento("LOGIN", s.getCliente(), "Credenciales incorrectas para '" + s.getUsuario() + "'", false);
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity(RespuestaApi.error("Usuario o clave incorrectos")).build();
        }
        RespuestaLogin r = new RespuestaLogin();
        r.setToken(sesion.getToken());
        r.setUsuario(sesion.getUsuario());
        r.setExpira(sesion.getExpira().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        r.setMinutosVigencia(ServicioAutenticacion.MINUTOS_SESION);
        historial.registrarEvento("LOGIN", sesion.getCliente(), "Inicio de sesión de " + sesion.getUsuario(), true);
        return Response.ok(RespuestaApi.ok("Bienvenido al sistema, " + sesion.getUsuario(), r)).build();
    }

    @POST
    @Path("logout")
    @Seguro
    @Consumes(MediaType.WILDCARD)
    public Response cerrarSesion(@HeaderParam(HttpHeaders.AUTHORIZATION) String encabezado) {
        String token = FiltroAutenticacion.extraerToken(encabezado);
        Sesion sesion = autenticacion.validarToken(token);
        autenticacion.cerrarSesion(token);
        historial.eliminarSesion(token);
        historial.registrarEvento("LOGOUT", sesion == null ? null : sesion.getCliente(), "Cierre de sesión", true);
        return Response.ok(RespuestaApi.ok("Sesión cerrada correctamente", null)).build();
    }

    private static boolean vacio(String t) {
        return t == null || t.isBlank();
    }
}
