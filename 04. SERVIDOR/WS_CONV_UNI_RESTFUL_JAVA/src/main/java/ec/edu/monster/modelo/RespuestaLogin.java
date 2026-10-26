package ec.edu.monster.modelo;

/**
 * Respuesta de un inicio de sesión exitoso.
 *
 * @author Grupo 1 - NRC 36841
 */
public class RespuestaLogin {

    private String token;
    private String usuario;
    private String expira;
    private int minutosVigencia;

    public RespuestaLogin() {
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getExpira() {
        return expira;
    }

    public void setExpira(String expira) {
        this.expira = expira;
    }

    public int getMinutosVigencia() {
        return minutosVigencia;
    }

    public void setMinutosVigencia(int minutosVigencia) {
        this.minutosVigencia = minutosVigencia;
    }

}
