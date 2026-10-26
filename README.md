<div align="center">

```
 __  __   ___   _   _  ____  _____  _____  ____
|  \/  | / _ \ | \ | |/ ___||_   _|| ____||  _ \
| |\/| || | | ||  \| |\___ \  | |  |  _|  | |_) |
| |  | || |_| || |\  | ___) | | |  | |___ |  _ <
|_|  |_| \___/ |_| \_||____/  |_|  |_____||_| \_\

   ╔═══════════════════════════════════════════════╗
   ║   C O N V E R S I Ó N   D E   U N I D A D E S  ║
   ║        SOA · RESTful · Java · sin BDD          ║
   ╚═══════════════════════════════════════════════╝
```

![Java](https://img.shields.io/badge/Java-21-orange?style=for-the-badge&logo=openjdk&logoColor=white)
![Jakarta EE](https://img.shields.io/badge/Jakarta%20EE-11-1F3864?style=for-the-badge)
![Payara](https://img.shields.io/badge/Payara-7-0E7EC2?style=for-the-badge)
![Android](https://img.shields.io/badge/Android-Java-3DDC84?style=for-the-badge&logo=android&logoColor=white)
![REST](https://img.shields.io/badge/API-REST%20%2B%20JSON-2DD4BF?style=for-the-badge)

**TI.1.3 · PAO202651 · NRC 36841 · Grupo 1**
Arquitectura de Software — Universidad de las Fuerzas Armadas ESPE

</div>

---

## 📌 Descripción

Servidor **RESTful** en Java (Jakarta EE sobre **Payara Server**) que centraliza la conversión de unidades de
**Longitud, Masa, Temperatura y Tiempo**, protegido con **token de sesión**, y consumido por **cuatro clientes**:

```
                         ┌──────────────────────────────────────────────┐
  ┌───────────┐  JSON    │        Payara Server 7 · Jakarta EE 11        │
  │  CONSOLA  │────────▶ │  ┌────────────────────────────────────────┐   │
  └───────────┘          │  │      WS_CONV_UNI_RESTFUL_JAVA  /api     │   │
  ┌───────────┐  JSON    │  │  config      → token · CORS · errores   │   │
  │ ESCRITORIO│────────▶ │  │  controlador → /auth /conversiones      │   │
  └───────────┘          │  │  servicio    → auth · conversión · log  │   │
  ┌───────────┐  JSON    │  │  modelo      → Magnitud · Unidad · DTO  │   │
  │    WEB    │────────▶ │  └────────────────────────────────────────┘   │
  └───────────┘          │        📡 Panel de monitoreo en vivo           │
  ┌───────────┐  JSON    └──────────────────────────────────────────────┘
  │  MÓVIL 📱 │────────▶        (sin base de datos: todo en memoria)
  └───────────┘
```

## 🗂️ Estructura

```
TI.1.3 PAO202651_RESTFUL_JAVA_SINBDD_NRC36841_GR01
├── 01. UML ............ ERS · ECUD · diagramas (casos de uso, actividad, secuencia, clases, arquitectura)
├── 02. MER ............ no aplica (sin base de datos)
├── 03. BDD ............ CREDENCIALES.txt
├── 04. SERVIDOR ....... WS_CONV_UNI_RESTFUL_JAVA      ⚙️  servicio REST + panel
├── 04. CLICON ......... CLICON_CONV_UNI_RESTFUL_JAVA  🖥️  consola
├── 04. CLIESC ......... CLIESC_CONV_UNI_RESTFUL_JAVA  🪟  escritorio (Swing + FlatLaf)
├── 04. CLIWEB ......... CLIWEB_CONV_UNI_RESTFUL_JAVA  🌐  web (Servlets + JSP)
├── 04. CLIMOV ......... app Android                    📱  móvil
├── 05. DOCUMENTACION .. manual técnico (Word) · presentación (PPT)
└── 06. EVIDENCIAS ..... capturas de funcionamiento
```

## 🔐 Credenciales

| Usuario | Clave |
|:-:|:-:|
| `monster` | `monster69` |

> La clave nunca se guarda en texto plano: el servidor compara su **hash SHA-256**. El token dura **30 minutos**.

## 🔌 API REST

Base: `http://<IP>:8080/WS_CONV_UNI_RESTFUL_JAVA/api`

| Método | Ruta | Token | Descripción |
|:-:|---|:-:|---|
| `POST` | `/auth/login` | ❌ | Inicia sesión `{usuario, clave, cliente}` |
| `POST` | `/auth/logout` | ✅ | Cierra la sesión |
| `GET` | `/conversiones/unidades` | ✅ | Catálogo de magnitudes y unidades |
| `POST` | `/conversiones` | ✅ | Convierte `{magnitud, origen, destino, valor}` |
| `GET` | `/conversiones/historial` | ✅ | Historial de la sesión |
| `GET` | `/estado` | ❌ | Estado del servidor (panel) |

Encabezados: `Authorization: Bearer <token>` · `X-Cliente: CONSOLA | ESCRITORIO | WEB | MOVIL`

```json
POST /api/conversiones
{ "magnitud": "MASA", "origen": "QUINTAL", "destino": "KILOGRAMO", "valor": 2 }

→ { "exito": true, "mensaje": "Conversión exitosa",
    "datos": { "resultado": 90.7185, "detalle": "2 qq = 90.7185 kg" } }
```

## 📏 Unidades

| 📏 Longitud | ⚖️ Masa | 🌡️ Temperatura | ⏱️ Tiempo |
|---|---|---|---|
| Centímetros | Gramos | Celsius | Segundos |
| Metros | Kilogramos | Fahrenheit | Minutos |
| Kilómetros | Onzas | Kelvin | Horas |
| Pies | Libras | | Días |
| Millas | Quintales | | Semanas |

## 🚀 Cómo ejecutar

```
1) Payara 7 con JDK 21  →  C:\payara\glassfish\config\asenv.bat
      set AS_JAVA=C:\Program Files\Eclipse Adoptium\jdk-21.0.12.101-hotspot

2) NetBeans  →  04. SERVIDOR\WS_CONV_UNI_RESTFUL_JAVA  →  Run
      Panel:  http://localhost:8080/WS_CONV_UNI_RESTFUL_JAVA/

3) Clientes
      Consola     →  Clean and Build  →  ejecutar_consola.bat
      Escritorio  →  Clean and Build  →  ejecutar_escritorio.bat
      Web         →  Run  →  http://localhost:8080/CLIWEB_CONV_UNI_RESTFUL_JAVA/
      Móvil       →  Android Studio  →  Run en el celular
```

📱 **Móvil:** usar la IP de la PC (`ipconfig`) y abrir el puerto 8080 en el firewall:

```powershell
New-NetFirewallRule -DisplayName "Payara 8080" -Direction Inbound -Protocol TCP -LocalPort 8080 -Action Allow
```

Sin WiFi común → cable USB y `adb reverse tcp:8080 tcp:8080`, luego `http://localhost:8080/...` en la app.

## 🧪 Pruebas

`WS_CONV_UNI_RESTFUL_JAVA` → clic derecho → **Test** → `PruebasConversion`: **6/6 ✅**
(conversiones de las 4 magnitudes, validaciones y ciclo de autenticación).

## 🎬 Demostración (roles rotativos)

| Rol | Muestra |
|---|---|
| 1 | Servidor + panel + consola |
| 2 | Web + escritorio |
| 3 | Móvil en el celular |

## 👥 Grupo 1

| Integrante | Rol |
|---|---|
| Capt. Jefferson Santiago Masapanta Guilcatoma | Líder |
| Moisés Sebastián Benalcázar Farinango | Integrante |
| Wilmer Omar Buestán Guaillas | Integrante |

**Tutor:** Ing. Eduardo Mauricio Campaña Ortega

<div align="center">

```
  ┌─────────────────────────────────────────┐
  │  Hecho con ☕ y 💙 por el Grupo 1 · ESPE  │
  └─────────────────────────────────────────┘
```

</div>
