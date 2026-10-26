# WS_CONV_UNI_RESTFUL_JAVA — Servidor RESTful de Conversión de Unidades

Grupo 1 · NRC 36841 · Arquitectura de Software (ESPE) · Payara Server 7 · Jakarta EE 11 · JDK 21

## Ejecutar
1. Abrir en NetBeans (File → Open Project → esta carpeta).
2. Clic derecho → Run (elegir Payara Server). Se despliega en:
   `http://localhost:8080/WS_CONV_UNI_RESTFUL_JAVA/`  ← panel de monitoreo
3. Pruebas unitarias: clic derecho → Test.

## Credenciales
usuario `monster` · clave `monster69` (se guarda solo su hash SHA-256)

## Endpoints (base `/WS_CONV_UNI_RESTFUL_JAVA/api`)
| Método | Ruta | Token | Cuerpo |
|---|---|---|---|
| POST | /auth/login | No | `{"usuario":"monster","clave":"monster69","cliente":"CONSOLA"}` |
| POST | /auth/logout | Sí | — |
| GET | /conversiones/unidades | Sí | — |
| POST | /conversiones | Sí | `{"magnitud":"LONGITUD","origen":"METRO","destino":"PIE","valor":10}` |
| GET | /conversiones/historial | Sí | — |
| GET | /estado | No | — |

Encabezados en peticiones con token: `Authorization: Bearer <token>` y `X-Cliente: CONSOLA|ESCRITORIO|WEB|MOVIL`.

Respuesta estándar: `{"exito":true,"mensaje":"...","datos":{...}}` (HTTP 400 datos inválidos, 401 sin sesión).

## Prueba con curl (PowerShell: usar curl.exe)
```
curl.exe -X POST http://localhost:8080/WS_CONV_UNI_RESTFUL_JAVA/api/auth/login -H "Content-Type: application/json" -d "{\"usuario\":\"monster\",\"clave\":\"monster69\",\"cliente\":\"CONSOLA\"}"
curl.exe -X POST http://localhost:8080/WS_CONV_UNI_RESTFUL_JAVA/api/conversiones -H "Content-Type: application/json" -H "Authorization: Bearer TOKEN" -d "{\"magnitud\":\"MASA\",\"origen\":\"QUINTAL\",\"destino\":\"KILOGRAMO\",\"valor\":2}"
```

## Unidades
- LONGITUD: CENTIMETRO, METRO, KILOMETRO, PIE, MILLA
- MASA: GRAMO, KILOGRAMO, ONZA, LIBRA, QUINTAL
- TEMPERATURA: CELSIUS, FAHRENHEIT, KELVIN
- TIEMPO: SEGUNDO, MINUTO, HORA, DIA, SEMANA
