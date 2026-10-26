package ec.edu.monster.controlador;

import ec.edu.monster.modelo.EstadoServidor;
import ec.edu.monster.modelo.RespuestaApi;
import ec.edu.monster.servicio.ServicioAutenticacion;
import ec.edu.monster.servicio.ServicioHistorial;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Estado del servidor para el panel de monitoreo (no requiere token).
 *
 * @author Grupo 1 - NRC 36841
 */
@Path("estado")
@Produces(MediaType.APPLICATION_JSON)
public class EstadoControlador {

    @GET
    public Response consultarEstado() {
        ServicioHistorial h = ServicioHistorial.getInstancia();
        EstadoServidor e = new EstadoServidor();
        e.setEstado("ACTIVO");
        e.setInicio(h.getInicio().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        e.setSegundosActivo(Duration.between(h.getInicio(), LocalDateTime.now()).getSeconds());
        e.setSesionesActivas(ServicioAutenticacion.getInstancia().contarSesionesActivas());
        e.setTotalConversiones(h.getTotalConversiones());
        e.setTotalPeticiones(h.getTotalPeticiones());
        e.setConversionesPorMagnitud(h.conversionesPorMagnitud());
        e.setPeticionesPorCliente(h.peticionesPorCliente());
        e.setUltimosEventos(h.ultimosEventos(15));
        return Response.ok(RespuestaApi.ok("Servidor activo", e)).build();
    }
}
