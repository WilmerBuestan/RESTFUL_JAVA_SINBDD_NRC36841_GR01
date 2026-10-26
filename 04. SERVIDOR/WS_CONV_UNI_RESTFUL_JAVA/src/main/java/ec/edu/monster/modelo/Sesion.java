package ec.edu.monster.modelo;

import java.time.LocalDateTime;

/**
 * Sesión activa de un usuario, identificada por su token.
 *
 * @author Grupo 1 - NRC 36841
 */
public class Sesion {

    private final String token;
    private final String usuario;
    private final String cliente;
    private final LocalDateTime inicio;
    private final LocalDateTime expira;

    public Sesion(String token, String usuario, String cliente, LocalDateTime inicio, LocalDateTime expira) {
        this.token = token;
        this.usuario = usuario;
        this.cliente = cliente;
        this.inicio = inicio;
        this.expira = expira;
    }

    public boolean estaVigente() {
        return LocalDateTime.now().isBefore(expira);
    }

    public String getToken() {
        return token;
    }

    public String getUsuario() {
        return usuario;
    }

    public String getCliente() {
        return cliente;
    }

    public LocalDateTime getInicio() {
        return inicio;
    }

    public LocalDateTime getExpira() {
        return expira;
    }
}
