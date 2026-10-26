package ec.edu.monster.controlador;

import ec.edu.monster.modelo.RespuestaApi;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Raíz de la API (/api): presenta el servicio y sus endpoints.
 *
 * @author Grupo 1 - NRC 36841
 */
@Path("")
@Produces(MediaType.APPLICATION_JSON)
public class InicioControlador {

    @GET
    public Response inicio() {
        Map<String, String> endpoints = new LinkedHashMap<>();
        endpoints.put("POST /api/auth/login", "Iniciar sesión {usuario, clave, cliente}");
        endpoints.put("POST /api/auth/logout", "Cerrar sesión (token)");
        endpoints.put("GET /api/conversiones/unidades", "Catálogo de unidades (token)");
        endpoints.put("POST /api/conversiones", "Convertir {magnitud, origen, destino, valor} (token)");
        endpoints.put("GET /api/conversiones/historial", "Historial de la sesión (token)");
        endpoints.put("GET /api/estado", "Estado del servidor");
        return Response.ok(RespuestaApi.ok("Servicio RESTful de Conversión de Unidades Monster - Grupo 1", endpoints)).build();
    }
}
