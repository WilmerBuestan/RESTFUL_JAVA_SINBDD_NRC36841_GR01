# CLIWEB_CONV_UNI_RESTFUL_JAVA — Cliente Web (JSP/Servlet)

Aplicación Java web en Payara que consume el servidor `WS_CONV_UNI_RESTFUL_JAVA`.
MVC: `modelo` (ServicioRest, Json, DTOs) · `controlador` (LoginServlet, ConversorServlet, LogoutServlet) · vista (`WEB-INF/vistas/*.jsp` + `css/estilo.css`).

## Ejecutar
1. El servidor WS debe estar desplegado.
2. Clic derecho → Run (Payara). Abre `http://localhost:8080/CLIWEB_CONV_UNI_RESTFUL_JAVA/`
3. Desde otro equipo/celular: `http://IP_DEL_PC:8080/CLIWEB_CONV_UNI_RESTFUL_JAVA/`

La URL del servidor REST está en `WEB-INF/web.xml` (urlServidor) y también se puede cambiar en el login (Configuración del servidor).

Imagen del Monster: `src/main/webapp/img/monster.png`. Credenciales: monster / monster69
