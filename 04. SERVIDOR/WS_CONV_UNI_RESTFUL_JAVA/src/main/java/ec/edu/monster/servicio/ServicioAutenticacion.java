package ec.edu.monster.servicio;

import ec.edu.monster.modelo.Sesion;
import ec.edu.monster.util.UtilHash;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.HexFormat;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Seguridad del servicio: valida credenciales (hash SHA-256), emite tokens con
 * expiración y administra las sesiones en memoria. Patrón Singleton.
 *
 * @author Grupo 1 - NRC 36841
 */
public final class ServicioAutenticacion {

    public static final int MINUTOS_SESION = 30;
    private static final ServicioAutenticacion INSTANCIA = new ServicioAutenticacion();

    /** Usuario -> hash SHA-256 de su clave (la clave nunca se guarda en texto plano). */
    private final Map<String, String> usuarios = Map.of(
            "monster", "a52d2d5627127efd0cea32c6ff04ae46b9c2b5e1df5cf78f7218b1817aa8a59f");
    private final Map<String, Sesion> sesiones = new ConcurrentHashMap<>();
    private final SecureRandom aleatorio = new SecureRandom();

    private ServicioAutenticacion() {
    }

    public static ServicioAutenticacion getInstancia() {
        return INSTANCIA;
    }

    /** @return la sesión creada o null si las credenciales son incorrectas */
    public Sesion iniciarSesion(String usuario, String clave, String cliente) {
        String u = usuario == null ? "" : usuario.trim().toLowerCase();
        String hashGuardado = usuarios.get(u);
        if (hashGuardado == null || clave == null || !UtilHash.iguales(hashGuardado, UtilHash.sha256(clave))) {
            return null;
        }
        byte[] bytes = new byte[32];
        aleatorio.nextBytes(bytes);
        String token = HexFormat.of().formatHex(bytes);
        LocalDateTime ahora = LocalDateTime.now();
        Sesion s = new Sesion(token, u, normalizarCliente(cliente), ahora, ahora.plusMinutes(MINUTOS_SESION));
        sesiones.put(token, s);
        return s;
    }

    /** @return la sesión vigente del token o null si no existe o expiró */
    public Sesion validarToken(String token) {
        if (token == null || token.isBlank()) {
            return null;
        }
        Sesion s = sesiones.get(token.trim());
        if (s == null) {
            return null;
        }
        if (!s.estaVigente()) {
            sesiones.remove(s.getToken());
            return null;
        }
        return s;
    }

    public boolean cerrarSesion(String token) {
        return token != null && sesiones.remove(token.trim()) != null;
    }

    public int contarSesionesActivas() {
        sesiones.values().removeIf(s -> !s.estaVigente());
        return sesiones.size();
    }

    public static String normalizarCliente(String cliente) {
        if (cliente == null || cliente.isBlank()) {
            return "DESCONOCIDO";
        }
        return cliente.trim().toUpperCase();
    }
}
