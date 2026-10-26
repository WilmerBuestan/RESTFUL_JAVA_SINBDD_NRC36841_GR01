package ec.edu.monster.config;

import ec.edu.monster.modelo.RespuestaApi;
import ec.edu.monster.modelo.Sesion;
import ec.edu.monster.servicio.ServicioAutenticacion;
import ec.edu.monster.servicio.ServicioHistorial;
import jakarta.annotation.Priority;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;

/**
 * Valida el encabezado "Authorization: Bearer token" en los recursos @Seguro.
 *
 * @author Grupo 1 - NRC 36841
 */
@Seguro
@Provider
@Priority(Priorities.AUTHENTICATION)
public class FiltroAutenticacion implements ContainerRequestFilter {

    public static final String ATRIBUTO_SESION = "sesion";

    @Override
    public void filter(ContainerRequestContext ctx) {
        String token = extraerToken(ctx.getHeaderString(HttpHeaders.AUTHORIZATION));
        Sesion sesion = ServicioAutenticacion.getInstancia().validarToken(token);
        if (sesion == null) {
            ServicioHistorial.getInstancia().registrarEvento("RECHAZO", ctx.getHeaderString("X-Cliente"),
                    ctx.getMethod() + " /" + ctx.getUriInfo().getPath() + " sin token válido", false);
            ctx.abortWith(Response.status(Response.Status.UNAUTHORIZED)
                    .type(MediaType.APPLICATION_JSON)
                    .entity(RespuestaApi.error("Sesión no válida o expirada. Inicie sesión nuevamente."))
                    .build());
            return;
        }
        ctx.setProperty(ATRIBUTO_SESION, sesion);
    }

    public static String extraerToken(String encabezado) {
        if (encabezado == null) {
            return null;
        }
        String e = encabezado.trim();
        return e.regionMatches(true, 0, "Bearer ", 0, 7) ? e.substring(7).trim() : e;
    }
}
