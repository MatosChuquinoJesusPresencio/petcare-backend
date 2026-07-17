# PetCare Backend

![Java 25](https://img.shields.io/badge/Java-25-ED8B00?logo=java)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.1-6DB33F?logo=springboot)
![Maven](https://img.shields.io/badge/Maven-3.9-C71A36?logo=apachemaven)
![License](https://img.shields.io/badge/License-MIT-green)

API RESTful para la gestión de una clínica veterinaria. Desarrollada con Java 25 y Spring Boot 4.1.

## Características

- Autenticación y autorización con JWT (access 30min + refresh 7 días en cookies httpOnly)
- **Multi-dispositivo**: sesiones simultáneas soportadas, cada login genera refresh token independiente
- **Refresh token rotation**: el token se renueva en cada uso con validación de expiración
- **Refresh resiliente**: se crea el nuevo refresh token antes de eliminar el viejo, evitando pérdida de sesión por errores transitorios
- **Limpieza automática**: tokens expirados se eliminan al crear uno nuevo
- **Access token 30 minutos**: reduce frecuencia de refreshes y oportunidades de fallo
- Gestión de usuarios con roles (ADMINISTRADOR, VETERINARIO, ASISTENTE, DUENO)
- CRUD completo de clientes (dueños) y contactos de emergencia
- CRUD completo de mascotas con vinculación a dueños
- Gestión de citas con reprogramación y control de disponibilidad horaria
- CRUD de servicios veterinarios con costo referencial
- Documentación interactiva con Swagger UI / OpenAPI 3
- Base de datos PostgreSQL en producción (Supabase) / H2 en desarrollo
- Arquitectura hexagonal para separación de responsabilidades
- **Todos los mensajes de error al español** (validación, reglas de negocio, recursos no encontrados)
- **Validación de DNI** (exactamente 8 dígitos) y **teléfono** (exactamente 9 dígitos) en DTOs

## Tecnologías

| Categoría | Tecnologías |
|---|---|
| **Lenguaje** | Java 25 |
| **Framework** | Spring Boot 4.1, Spring MVC, Spring Data JPA, Spring Security, Spring Validation, Spring Cloud 2025.1 |
| **Base de datos** | PostgreSQL (producción / Supabase), H2 (desarrollo) |
| **Autenticación** | JWT (jjwt 0.11.5) — access (30min) + refresh (7días) en cookies httpOnly, rotation resiliente, sesiones simultáneas |
| **Mapeo** | MapStruct 1.6.3 + MapStruct-Lombok binding 0.2.0 |
| **Documentación** | SpringDoc OpenAPI 2.8.5 (Swagger UI en `/docs` y `/swagger-ui.html`) |
| **Herramientas** | Lombok, Maven 3.9, Docker, Spring Boot DevTools |
| **Arquitectura** | Hexagonal (puertos y adaptadores) |

## Arquitectura

Arquitectura hexagonal (puertos y adaptadores):

```
web/          → Controladores REST, DTOs, seguridad
domain/       → Modelos, puertos, servicios de negocio
persistence/  → Entidades JPA, adaptadores, repositorios
config/       → Configuración (OpenAPI, etc.)
```

### Estructura del proyecto

```
src/main/java/com/petcare/
├── web/                  # Capa de entrada (controladores REST)
│   ├── controller/       #   Endpoints de la API
│   ├── dto/              #   Objetos de transferencia de datos
│   ├── security/         #   Filtros JWT, configuración de seguridad
│   ├── exception/        #   Manejador global de excepciones (ProblemDetail)
│   └── config/           #   Configuración web (CORS, OpenAPI)
├── domain/               # Capa de negocio
│   ├── model/            #   Modelos de dominio
│   ├── port/             #   Puertos (interfaces de entrada/salida)
│   ├── service/          #   Implementación de servicios
│   └── exception/        #   Excepciones de negocio
├── persistence/          # Capa de persistencia
│   ├── entity/           #   Entidades JPA
│   ├── mapper/           #   Mappers MapStruct (entity ↔ domain)
│   ├── adapter/          #   Adaptadores de repositorio
│   ├── repository/       #   Repositorios Spring Data JPA
│   └── specification/    #   Especificaciones para consultas dinámicas
└── config/               # Configuración global
    └── openapi/          #   Configuración de Swagger/OpenAPI
```

```
src/main/resources/
├── application.properties           # Configuración principal (H2 por defecto)
└── application-prod.properties      # Perfil producción (PostgreSQL/Supabase)
```

## Requisitos

- Java 25 (JDK)
- Maven 3.9+ (o usar `mvnw`)
- Docker (opcional)

## Configuración

Crear archivo `.env` en `petcare-backend/` (opcional, solo para PostgreSQL local):

```env
SPRING_DATASOURCE_URL=jdbc:postgresql://<host>:5432/<db>
SPRING_DATASOURCE_USERNAME=usuario
SPRING_DATASOURCE_PASSWORD=contraseña
SPRING_DATASOURCE_DRIVER_CLASS_NAME=org.postgresql.Driver
JWT_SECRET=clave-secreta-jwt
JWT_EXPIRATION=1800000
JWT_REFRESH_EXPIRATION_MS=604800000
APP_CORS_ALLOWED_ORIGINS=http://localhost:5173
```

Sin `.env` se usa H2 en memoria (modo PostgreSQL) automáticamente — funciona al clonar sin configuración adicional.

## Ejecución

```bash
# Con Maven wrapper (Linux/macOS)
./mvnw spring-boot:run

# Con Maven wrapper (Windows)
mvnw.cmd spring-boot:run

# Build y ejecución (Linux/macOS)
./mvnw clean package -DskipTests
java -jar target/backend-0.0.1-SNAPSHOT.jar

# Build y ejecución (Windows)
mvnw.cmd clean package -DskipTests
java -jar target\backend-0.0.1-SNAPSHOT.jar

# Con Docker
docker build -t petcare-backend .
docker run -p 8080:8080 --env-file ../.env petcare-backend
```

## Scripts disponibles

| Comando | Descripción |
|---|---|
| `./mvnw` / `mvnw.cmd` `spring-boot:run` | Inicia el servidor en desarrollo (perfil `dev`) |
| `./mvnw` / `mvnw.cmd` `clean package` | Empaqueta la aplicación en JAR |
| `./mvnw` / `mvnw.cmd` `test` | Ejecuta los tests |
| `./mvnw` / `mvnw.cmd` `clean package -DskipTests` | Empaqueta sin ejecutar tests |
| `mvnw.cmd clean compile` | Compila sin empaquetar (Windows) |

## Seed Data

### Desarrollo (H2)

El archivo `src/main/resources/data.sql` se ejecuta automáticamente al iniciar la aplicación con el perfil `dev` (por defecto). Contiene datos de prueba para H2 (modo PostgreSQL).

**Usuarios de prueba** (contraseña `password` para todos):

| Email | Rol |
|---|---|
| `admin@petcare.com` | ADMINISTRADOR |
| `carlos@petcare.com` | VETERINARIO |
| `ana@petcare.com` | ASISTENTE |
| `juan.perez@gmail.com` | DUENO |
| `maria.lopez@gmail.com` | DUENO |
| `roberto.garcia@gmail.com` | DUENO |
| `lucia@petcare.com` | VETERINARIO |
| `pedro@petcare.com` | ASISTENTE |

### Producción (PostgreSQL / Supabase)

Ejecutar `db/supabase.sql` una vez en el SQL Editor de Supabase. Contiene el esquema completo (CREATE TABLE) y los mismos datos de prueba.

```bash
# Abrir Supabase Dashboard → SQL Editor → pegar db/supabase.sql → ejecutar
```

## Perfiles

| Perfil | Base de datos | Seed | H2 Console | Uso |
|---|---|---|---|---|
| `dev` (default) | H2 en memoria | `data.sql` automático | Disponible | Desarrollo local |
| `prod` | PostgreSQL (env vars) | Manual (`supabase.sql`) | Deshabilitado | Render / Supabase |

Para usar el perfil `prod` localmente:
```bash
set SPRING_PROFILES_ACTIVE=prod
mvnw.cmd spring-boot:run
```

## Notas

- **Mensajes de error en español**: todos los mensajes de validación, reglas de negocio y excepciones están en español.
- **Validación de DNI**: exactamente 8 dígitos (frontend y backend).
- **Validación de teléfono**: exactamente 9 dígitos (frontend y backend).
- **MapStruct** genera código fuente en `target/generated-sources/`. Si tu IDE muestra warnings en los mappers, marca `target/` como carpeta excluida.
- **H2 Console** solo está disponible en el perfil `dev` (`http://localhost:8080/h2-console`).
- **Swagger UI** disponible en `/docs` y `/swagger-ui.html` (esta última redirige).
- **Migración DB**: si vienes de una versión anterior con `token_version`, ejecuta `ALTER TABLE usuarios DROP COLUMN IF EXISTS token_version;` en Supabase.

## Despliegue

### Render (Docker)

1. Crear Web Service → seleccionar **Docker** como tipo de despliegue
2. Conectar repositorio GitHub
3. Configurar:
   - **Dockerfile Path**: `petcare-backend/Dockerfile`
   - **Docker Context**: `petcare-backend`
4. Agregar variables de entorno en Dashboard:

| Variable | Descripción | Ejemplo |
|---|---|---|
| `SPRING_PROFILES_ACTIVE` | Perfil activo | `prod` |
| `SPRING_DATASOURCE_URL` | URL JDBC de PostgreSQL | `jdbc:postgresql://host:5432/db?sslmode=require` |
| `SPRING_DATASOURCE_USERNAME` | Usuario de PostgreSQL | `postgres.xxxx` |
| `SPRING_DATASOURCE_PASSWORD` | Contraseña de PostgreSQL | `tu-password` |
| `SPRING_DATASOURCE_DRIVER_CLASS_NAME` | Driver JDBC | `org.postgresql.Driver` |
| `JWT_SECRET` | Clave secreta JWT (Base64, >=32 bytes) | `openssl rand -base64 32` |
| `JWT_EXPIRATION` | TTL del access token (ms) | `1800000` (30 min) |
| `JWT_REFRESH_EXPIRATION_MS` | TTL del refresh token (ms) | `604800000` (7 días) |
| `APP_CORS_ALLOWED_ORIGINS` | Orígenes CORS permitidos (coma-separados) | `https://tu-app.vercel.app` |
| `TEXTBEE_API_KEY` | API key de TextBee (opcional, SMS/WhatsApp) | |
| `TEXTBEE_GATEWAY_ID` | Gateway ID de TextBee (opcional) | |

5. Render ejecuta `Dockerfile` automáticamente en cada push
6. Health check en `/` (público, sin autenticación)
7. Cron cada 14 min en `/` para mantener vivo el free tier

**Variables opcionales:**
- `TEXTBEE_API_KEY` / `TEXTBEE_GATEWAY_ID` — Sin estas claves, el backend funciona normalmente. Las notificaciones por SMS/WhatsApp simplemente no se envían (se registra warning en logs).

### Base de datos (Supabase)

1. Crear proyecto en Supabase
2. Abrir SQL Editor → pegar contenido de `supabase.sql` (raíz del proyecto) → ejecutar
3. Copiar la URL de conexión del dashboard (Settings → Database → Connection string → URI)
4. Usar esa URL en `SPRING_DATASOURCE_URL`

### Docker local

```bash
docker build -t petcare-backend .
docker run -p 8080:8080 --env-file ../.env petcare-backend
```

## Documentación

- Swagger UI: [`/docs`](http://localhost:8080/docs) o [`/swagger-ui.html`](http://localhost:8080/swagger-ui.html) (desarrollo)
- OpenAPI JSON: [`/v3/api-docs`](http://localhost:8080/v3/api-docs)
- H2 Console: [`/h2-console`](http://localhost:8080/h2-console) (solo perfil `dev`; JDBC URL: `jdbc:h2:mem:petcaredb`, User: `sa`, password: vacío)
