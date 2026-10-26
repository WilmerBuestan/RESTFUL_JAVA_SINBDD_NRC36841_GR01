<%-- Vista: inicio de sesión del cliente web --%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Conversión de Unidades Monster - Iniciar sesión</title>
    <link rel="stylesheet" href="css/estilo.css">
</head>
<body>
<div class="login">
    <div class="lado">
        <img src="img/monster.png" class="monster" alt="Monster"
             onerror="this.outerHTML='<div class=&quot;monster ph&quot;>M</div>'">
        <h1>Conversión de Unidades</h1>
        <p>Cliente WEB · RESTful Java</p>
        <small>Grupo 1 · NRC 36841 · Arquitectura de Software ESPE</small>
    </div>
    <div class="formulario">
        <div class="tarjeta ${not empty error ? 'error' : ''}">
            <form method="post" action="login" autocomplete="off">
                <h2>🔐 Iniciar sesión</h2>
                <label for="usuario">Usuario</label>
                <input id="usuario" name="usuario" value="<c:out value='${usuario}'/>" placeholder="Usuario" autofocus>
                <label for="clave">Clave</label>
                <input id="clave" name="clave" type="password" placeholder="Clave">
                <button class="btn" style="margin-top:14px" type="submit">Ingresar</button>
                <c:if test="${not empty error}"><div class="msg error">✖ <c:out value="${error}"/></div></c:if>
                <c:if test="${not empty aviso}"><div class="msg ok">✔ <c:out value="${aviso}"/></div></c:if>
                <details>
                    <summary>Configuración del servidor</summary>
                    <input name="servidor" value="<c:out value='${servidor}'/>">
                </details>
            </form>
        </div>
    </div>
</div>
</body>
</html>
