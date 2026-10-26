package ec.edu.monster.controlador;

import ec.edu.monster.config.FiltroAutenticacion;
import ec.edu.monster.config.Seguro;
import ec.edu.monster.modelo.RespuestaApi;
import ec.edu.monster.modelo.ResultadoConversion;
import ec.edu.monster.modelo.Sesion;
import ec.edu.monster.modelo.SolicitudConversion;
import ec.edu.monster.servicio.ExcepcionConversion;
import ec.edu.monster.servicio.ServicioConversion;
import ec.edu.monster.servicio.ServicioHistorial;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.core.HttpHeaders;
import ec.edu.monster.servicio.ServicioAutenticacion;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * Recurso REST de conversiones: catálogo, conversión e historial.
 * Todos los métodos requieren token (@Seguro).
 *
 * @author Grupo 1 - NRC 36841
 */
@Path("conversiones")
@Seguro
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ConversionControlador {

    private final ServicioConversion conversion = ServicioConversion.getInstancia();
    private final ServicioHistorial historial = ServicioHistorial.getInstancia();

    /** Encabezado Authorization (ya validado por FiltroAutenticacion). */
    @HeaderParam(HttpHeaders.AUTHORIZATION)
    private String autorizacion;

    @GET
    @Path("unidades")
    public Response listarUnidades() {
        return Response.ok(RespuestaApi.ok("Catálogo de unidades", conversion.catalogo())).build();
    }

    @POST
    public Response convertir(SolicitudConversion s) {
        Sesion sesion = sesion();
        try {
            ResultadoConversion r = conversion.convertir(s);
            historial.registrar(sesion, r);
            historial.registrarEvento("CONVERSION", sesion.getCliente(), r.getMagnitud() + ": " + r.getDetalle(), true);
            return Response.ok(RespuestaApi.ok("Conversión exitosa", r)).build();
        } catch (ExcepcionConversion e) {
            historial.registrarEvento("CONVERSION", sesion.getCliente(), e.getMessage(), false);
            return Response.status(Response.Status.BAD_REQUEST).entity(RespuestaApi.error(e.getMessage())).build();
        }
    }

    @GET
    @Path("historial")
    public Response consultarHistorial() {
        Sesion sesion = sesion();
        var lista = historial.listar(sesion.getToken());
        historial.registrarEvento("HISTORIAL", sesion.getCliente(), lista.size() + " registro(s)", true);
        return Response.ok(RespuestaApi.ok(lista.isEmpty() ? "Aún no ha realizado conversiones" : "Historial de la sesión", lista)).build();
    }

    private Sesion sesion() {
        return ServicioAutenticacion.getInstancia().validarToken(FiltroAutenticacion.extraerToken(autorizacion));
    }
}
