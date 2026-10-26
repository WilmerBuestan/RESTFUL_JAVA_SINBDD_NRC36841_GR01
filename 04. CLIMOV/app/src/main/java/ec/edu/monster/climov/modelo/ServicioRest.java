package ec.edu.monster.climov.modelo;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Consume la API REST del servidor WS_CONV_UNI_RESTFUL_JAVA (cliente MOVIL).
 * Los métodos son bloqueantes: se llaman desde un hilo secundario.
 *
 * @author Grupo 1 - NRC 36841
 */
public class ServicioRest {

    public static final String URL_POR_DEFECTO = "http://10.40.49.158:8080/WS_CONV_UNI_RESTFUL_JAVA/api";
    private static final String CLIENTE = "MOVIL";

    private String urlBase = URL_POR_DEFECTO;
    private String token;
    private String usuario;

    public void setUrlBase(String url) {
        String u = url == null || url.trim().isEmpty() ? URL_POR_DEFECTO : url.trim();
        if (!u.startsWith("http")) {
            u = "http://" + u;
        }
        urlBase = u.endsWith("/") ? u.substring(0, u.length() - 1) : u;
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
        try {
            JSONObject cuerpo = new JSONObject();
            cuerpo.put("usuario", usuario);
            cuerpo.put("clave", clave);
            cuerpo.put("cliente", CLIENTE);
            JSONObject r = enviar("POST", "/auth/login", cuerpo.toString());
            JSONObject datos = r.getJSONObject("datos");
            token = datos.getString("token");
            this.usuario = datos.getString("usuario");
            return r.getString("mensaje");
        } catch (JSONException e) {
            throw new ExcepcionCliente("Respuesta inválida del servidor", 0);
        }
    }

    public void cerrarSesion() {
        try {
            if (token != null) {
                enviar("POST", "/auth/logout", "{}");
            }
        } catch (ExcepcionCliente e) {
            // la sesión ya no existía en el servidor
        } finally {
            token = null;
            usuario = null;
        }
    }

    public List<Magnitud> obtenerCatalogo() throws ExcepcionCliente {
        try {
            JSONArray datos = enviar("GET", "/conversiones/unidades", null).getJSONArray("datos");
            List<Magnitud> lista = new ArrayList<>();
            for (int i = 0; i < datos.length(); i++) {
                JSONObject m = datos.getJSONObject(i);
                JSONArray us = m.getJSONArray("unidades");
                List<Unidad> unidades = new ArrayList<>();
                for (int k = 0; k < us.length(); k++) {
                    JSONObject u = us.getJSONObject(k);
                    unidades.add(new Unidad(u.getString("codigo"), u.getString("nombre"), u.getString("simbolo")));
                }
                lista.add(new Magnitud(m.getString("codigo"), m.getString("nombre"), m.getString("unidadBase"), unidades));
            }
            return lista;
        } catch (JSONException e) {
            throw new ExcepcionCliente("Respuesta inválida del servidor", 0);
        }
    }

    public Resultado convertir(String magnitud, String origen, String destino, double valor) throws ExcepcionCliente {
        try {
            JSONObject cuerpo = new JSONObject();
            cuerpo.put("magnitud", magnitud);
            cuerpo.put("origen", origen);
            cuerpo.put("destino", destino);
            cuerpo.put("valor", valor);
            return aResultado(enviar("POST", "/conversiones", cuerpo.toString()).getJSONObject("datos"));
        } catch (JSONException e) {
            throw new ExcepcionCliente("Respuesta inválida del servidor", 0);
        }
    }

    public List<Registro> obtenerHistorial() throws ExcepcionCliente {
        try {
            JSONArray datos = enviar("GET", "/conversiones/historial", null).getJSONArray("datos");
            List<Registro> lista = new ArrayList<>();
            for (int i = 0; i < datos.length(); i++) {
                JSONObject o = datos.getJSONObject(i);
                Registro r = new Registro();
                r.fecha = o.getString("fecha");
                r.cliente = o.getString("cliente");
                r.conversion = aResultado(o.getJSONObject("conversion"));
                lista.add(r);
            }
            return lista;
        } catch (JSONException e) {
            throw new ExcepcionCliente("Respuesta inválida del servidor", 0);
        }
    }

    private static Resultado aResultado(JSONObject d) throws JSONException {
        Resultado r = new Resultado();
        r.magnitud = d.getString("magnitud");
        r.simboloDestino = d.getString("simboloDestino");
        r.valor = d.getDouble("valor");
        r.resultado = d.getDouble("resultado");
        r.detalle = d.getString("detalle");
        return r;
    }

    private JSONObject enviar(String metodo, String ruta, String json) throws ExcepcionCliente {
        HttpURLConnection con = null;
        try {
            con = (HttpURLConnection) new URL(urlBase + ruta).openConnection();
            con.setRequestMethod(metodo);
            con.setConnectTimeout(6000);
            con.setReadTimeout(10000);
            con.setRequestProperty("Accept", "application/json");
            con.setRequestProperty("X-Cliente", CLIENTE);
            if (token != null) {
                con.setRequestProperty("Authorization", "Bearer " + token);
            }
            if (json != null) {
                con.setDoOutput(true);
                con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                try (OutputStream os = con.getOutputStream()) {
                    os.write(json.getBytes(StandardCharsets.UTF_8));
                }
            }
            int codigo = con.getResponseCode();
            InputStream is = codigo >= 400 ? con.getErrorStream() : con.getInputStream();
            String cuerpo = leer(is);
            JSONObject r;
            try {
                r = new JSONObject(cuerpo);
            } catch (JSONException e) {
                throw new ExcepcionCliente("Respuesta inesperada del servidor (HTTP " + codigo + ")", codigo);
            }
            if (codigo == 401) {
                token = null;
            }
            if (!r.optBoolean("exito", false)) {
                throw new ExcepcionCliente(r.optString("mensaje", "Error en la solicitud"), codigo);
            }
            return r;
        } catch (IOException e) {
            throw new ExcepcionCliente("No se pudo conectar con el servidor. Verifique la URL y que el celular esté en la misma red.", 0);
        } finally {
            if (con != null) {
                con.disconnect();
            }
        }
    }

    private static String leer(InputStream is) throws IOException {
        if (is == null) {
            return "";
        }
        try (InputStream in = is; ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            byte[] b = new byte[4096];
            int n;
            while ((n = in.read(b)) > 0) {
                out.write(b, 0, n);
            }
            return new String(out.toByteArray(), StandardCharsets.UTF_8);
        }
    }
}
