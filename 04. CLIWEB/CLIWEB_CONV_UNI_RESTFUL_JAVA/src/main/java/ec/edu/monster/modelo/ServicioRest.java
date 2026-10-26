package ec.edu.monster.modelo;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Consume la API REST del servidor WS_CONV_UNI_RESTFUL_JAVA.
 *
 * @author Grupo 1 - NRC 36841
 */
public class ServicioRest {

    public static final String URL_POR_DEFECTO = "http://localhost:8080/WS_CONV_UNI_RESTFUL_JAVA/api";

    private final HttpClient http = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(5)).build();
    private final String cliente;
    private String urlBase;
    private String token;
    private String usuario;

    public ServicioRest(String urlBase, String cliente) {
        setUrlBase(urlBase);
        this.cliente = cliente;
    }

    public final void setUrlBase(String url) {
        String u = url == null || url.isBlank() ? URL_POR_DEFECTO : url.trim();
        if (!u.startsWith("http")) {
            u = "http://" + u;
        }
        this.urlBase = u.endsWith("/") ? u.substring(0, u.length() - 1) : u;
    }

    public String getUrlBase() {
        return urlBase;
    }

    public String getUsuario() {
        return usuario;
    }

    public boolean haySesion() {
        return token != null;
    }

    public String iniciarSesion(String usuario, String clave) throws ExcepcionCliente {
        Map<String, Object> cuerpo = new LinkedHashMap<>();
        cuerpo.put("usuario", usuario);
        cuerpo.put("clave", clave);
        cuerpo.put("cliente", cliente);
        Map<String, Object> r = enviar("POST", "/auth/login", Json.escribir(cuerpo));
        Map<?, ?> datos = (Map<?, ?>) r.get("datos");
        this.token = (String) datos.get("token");
        this.usuario = (String) datos.get("usuario");
        return (String) r.get("mensaje");
    }

    public void cerrarSesion() {
        try {
            if (token != null) {
                enviar("POST", "/auth/logout", "{}");
            }
        } catch (ExcepcionCliente e) {
            // la sesión ya no existía en el servidor; igual se limpia localmente
        } finally {
            token = null;
            usuario = null;
        }
    }

    public List<Magnitud> obtenerCatalogo() throws ExcepcionCliente {
        List<Magnitud> lista = new ArrayList<>();
        for (Object o : (List<?>) enviar("GET", "/conversiones/unidades", null).get("datos")) {
            Map<?, ?> m = (Map<?, ?>) o;
            List<Unidad> unidades = new ArrayList<>();
            for (Object u : (List<?>) m.get("unidades")) {
                Map<?, ?> um = (Map<?, ?>) u;
                unidades.add(new Unidad((String) um.get("codigo"), (String) um.get("nombre"), (String) um.get("simbolo")));
            }
            lista.add(new Magnitud((String) m.get("codigo"), (String) m.get("nombre"), (String) m.get("unidadBase"), unidades));
        }
        return lista;
    }

    public Resultado convertir(String magnitud, String origen, String destino, double valor) throws ExcepcionCliente {
        Map<String, Object> cuerpo = new LinkedHashMap<>();
        cuerpo.put("magnitud", magnitud);
        cuerpo.put("origen", origen);
        cuerpo.put("destino", destino);
        cuerpo.put("valor", valor);
        return aResultado((Map<?, ?>) enviar("POST", "/conversiones", Json.escribir(cuerpo)).get("datos"));
    }

    public List<Registro> obtenerHistorial() throws ExcepcionCliente {
        List<Registro> lista = new ArrayList<>();
        for (Object o : (List<?>) enviar("GET", "/conversiones/historial", null).get("datos")) {
            Map<?, ?> m = (Map<?, ?>) o;
            lista.add(new Registro((String) m.get("fecha"), (String) m.get("cliente"), aResultado((Map<?, ?>) m.get("conversion"))));
        }
        return lista;
    }

    private static Resultado aResultado(Map<?, ?> d) {
        return new Resultado((String) d.get("magnitud"), (String) d.get("origen"), (String) d.get("destino"),
                ((Number) d.get("valor")).doubleValue(), ((Number) d.get("resultado")).doubleValue(),
                (String) d.get("simboloOrigen"), (String) d.get("simboloDestino"), (String) d.get("detalle"));
    }

    private Map<String, Object> enviar(String metodo, String ruta, String json) throws ExcepcionCliente {
        HttpRequest.Builder b = HttpRequest.newBuilder(URI.create(urlBase + ruta))
                .timeout(Duration.ofSeconds(10))
                .header("Accept", "application/json")
                .header("X-Cliente", cliente);
        if (token != null) {
            b.header("Authorization", "Bearer " + token);
        }
        if (json != null) {
            b.header("Content-Type", "application/json; charset=UTF-8");
            b.method(metodo, HttpRequest.BodyPublishers.ofString(json, StandardCharsets.UTF_8));
        } else {
            b.method(metodo, metodo.equals("POST") ? HttpRequest.BodyPublishers.ofString("") : HttpRequest.BodyPublishers.noBody());
        }
        HttpResponse<String> res;
        try {
            res = http.send(b.build(), HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new ExcepcionCliente("No se pudo conectar con el servidor (" + urlBase + ")", 0);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new ExcepcionCliente("Operación interrumpida", 0);
        }
        Map<String, Object> r;
        try {
            r = Json.objeto(res.body());
        } catch (RuntimeException e) {
            throw new ExcepcionCliente("Respuesta inesperada del servidor (HTTP " + res.statusCode() + ")", res.statusCode());
        }
        if (res.statusCode() == 401) {
            token = null;
        }
        if (!Boolean.TRUE.equals(r.get("exito"))) {
            throw new ExcepcionCliente(String.valueOf(r.get("mensaje")), res.statusCode());
        }
        return r;
    }
}
