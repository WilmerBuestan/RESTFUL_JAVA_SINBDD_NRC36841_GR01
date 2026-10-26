<%-- Vista: conversión de unidades e historial del cliente web --%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Conversión de Unidades Monster - Cliente Web</title>
    <link rel="stylesheet" href="css/estilo.css">
</head>
<body class="m-${actual.codigo}">
<header>
    <img src="img/monster.png" class="mini" alt="Monster" onerror="this.outerHTML='<div class=&quot;mini ph&quot;>M</div>'">
    <h1>Conversión de Unidades Monster<small>Cliente WEB · RESTful Java · Grupo 1 · NRC 36841</small></h1>
    <div class="usuario">
        <span>👤 <c:out value="${usuarioSesion}"/></span>
        <form method="post" action="logout" onsubmit="return confirm('¿Desea cerrar la sesión?')">
            <button class="btn rojo" type="submit">Cerrar sesión</button>
        </form>
    </div>
</header>

<main>
    <section class="tarjeta conv">
        <nav class="pestanas">
            <c:forEach var="m" items="${catalogo}">
                <a class="pestana m-${m.codigo} ${m.codigo == actual.codigo ? 'activa' : ''}" href="conversor?magnitud=${m.codigo}">
                    <c:choose>
                        <c:when test="${m.codigo == 'LONGITUD'}">📏</c:when>
                        <c:when test="${m.codigo == 'MASA'}">⚖️</c:when>
                        <c:when test="${m.codigo == 'TEMPERATURA'}">🌡️</c:when>
                        <c:otherwise>⏱️</c:otherwise>
                    </c:choose>
                    <c:out value="${m.nombre}"/>
                </a>
            </c:forEach>
        </nav>

        <form method="post" action="conversor">
            <input type="hidden" name="magnitud" value="${actual.codigo}">
            <div class="fila">
                <div>
                    <label for="origen">De</label>
                    <select id="origen" name="origen">
                        <c:forEach var="u" items="${actual.unidades}" varStatus="st">
                            <option value="${u.codigo}" ${u.codigo == origenSel || (empty origenSel && st.index == 0) ? 'selected' : ''}><c:out value="${u.nombre}"/> (<c:out value="${u.simbolo}"/>)</option>
                        </c:forEach>
                    </select>
                </div>
                <button type="button" class="swap" title="Intercambiar unidades" onclick="intercambiar()">⇄</button>
                <div>
                    <label for="destino">A</label>
                    <select id="destino" name="destino">
                        <c:forEach var="u" items="${actual.unidades}" varStatus="st">
                            <option value="${u.codigo}" ${u.codigo == destinoSel || (empty destinoSel && st.index == 1) ? 'selected' : ''}><c:out value="${u.nombre}"/> (<c:out value="${u.simbolo}"/>)</option>
                        </c:forEach>
                    </select>
                </div>
            </div>
            <label for="valor">Valor</label>
            <input id="valor" name="valor" type="text" inputmode="decimal" value="<c:out value='${valor}'/>" placeholder="Ingrese el valor a convertir" required autofocus>
            <button class="btn" type="submit" style="margin-top:10px">Convertir</button>
        </form>

        <c:choose>
            <c:when test="${not empty resultado}">
                <div class="resultado m-${resultado.magnitud}">
                    <div class="num"><span id="numero" data-valor="${resultado.resultado}">0</span> <c:out value="${resultado.simboloDestino}"/></div>
                    <div class="det"><c:out value="${resultado.detalle}"/></div>
                </div>
            </c:when>
            <c:otherwise>
                <div class="resultado vacio">
                    <div class="num">—</div>
                    <div class="det">Unidad base de <c:out value="${actual.nombre}"/>: <c:out value="${actual.unidadBase}"/></div>
                </div>
            </c:otherwise>
        </c:choose>
        <c:if test="${not empty error}"><div class="msg error">✖ <c:out value="${error}"/></div></c:if>
        <c:if test="${not empty aviso}"><div class="msg ok">✔ <c:out value="${aviso}"/></div></c:if>
    </section>

    <section class="tarjeta">
        <h2>🕘 Historial de la sesión</h2>
        <c:choose>
            <c:when test="${empty historial}">
                <div class="vacio-hist">Aún no ha realizado conversiones</div>
            </c:when>
            <c:otherwise>
                <table>
                    <thead><tr><th>Hora</th><th>Magnitud</th><th>Conversión</th></tr></thead>
                    <tbody>
                    <c:forEach var="r" items="${historial}" varStatus="st">
                        <tr style="animation-delay:${st.index * 60}ms">
                            <td><c:out value="${r.fecha.length() > 11 ? r.fecha.substring(11) : r.fecha}"/></td>
                            <td><span class="chip m-${r.conversion.magnitud}"><c:out value="${r.conversion.magnitud}"/></span></td>
                            <td><c:out value="${r.conversion.detalle}"/></td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </c:otherwise>
        </c:choose>
    </section>
</main>
<footer>Servidor: <c:out value="${servidor}"/> · Payara Server 7 · Jakarta EE 11</footer>

<script>
    function intercambiar() {
        const o = document.getElementById("origen"), d = document.getElementById("destino");
        const t = o.value; o.value = d.value; d.value = t;
    }
    // Animación: el resultado sube contando hasta su valor final
    const n = document.getElementById("numero");
    if (n) {
        const fin = parseFloat(n.dataset.valor), t0 = performance.now();
        const fmt = v => String(Number(v.toFixed(4)));
        (function paso(t) {
            const k = Math.min((t - t0) / 800, 1), s = 1 - Math.pow(1 - k, 3);
            n.textContent = fmt(fin * s);
            if (k < 1) requestAnimationFrame(paso); else n.textContent = fmt(fin);
        })(t0);
    }
</script>
</body>
</html>
