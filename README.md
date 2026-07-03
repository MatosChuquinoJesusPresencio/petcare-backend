# PetCare Backend

![Java 25](https://img.shields.io/badge/Java-25-ED8B00?logo=java)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.1-6DB33F?logo=springboot)
![Maven](https://img.shields.io/badge/Maven-3.9-C71A36?logo=apachemaven)
![License](https://img.shields.io/badge/License-MIT-green)

API RESTful para la gestión de una clínica veterinaria. Desarrollada con Java 25 y Spring Boot 4.1.

**Despliegue:** [https://petcare-backend-production-5445.up.railway.app/](https://petcare-backend-production-5445.up.railway.app/)

## Características

- Autenticación y autorización con JWT (access + refresh tokens)
- Gestión de usuarios con roles (ADMINISTRADOR, VETERINARIO, ASISTENTE, DUENO)
- CRUD completo de clientes (dueños) y contactos de emergencia
- CRUD completo de mascotas con vinculación a dueños
- Gestión de citas con reprogramación y control de disponibilidad horaria
- CRUD de servicios veterinarios con costo referencial
- Documentación interactiva con Swagger UI / OpenAPI 3
- Base de datos PostgreSQL en producción / H2 en desarrollo
- Arquitectura hexagonal para separación de responsabilidades

## Tecnologías

| Categoría | Tecnologías |
|---|---|
| **Lenguaje** | Java 25 |
| **Framework** | Spring Boot 4.1, Spring MVC, Spring Data JPA, Spring Security, Spring Validation, Spring Cloud 2025.1 |
| **Base de datos** | PostgreSQL (producción), H2 (desarrollo) |
| **Autenticación** | JWT (jjwt 0.11.5) — access + refresh tokens en cookies httpOnly |
| **Mapeo** | MapStruct 1.5.5 + MapStruct-Lombok binding 0.2.0 |
| **Documentación** | SpringDoc OpenAPI 2.8.5 (Swagger UI) |
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
└── application.properties   # Configuración principal (H2 por defecto)
```

## Requisitos

- Java 21 (JDK)
- Maven 3.9+ (o usar `mvnw`)
- Docker (opcional)

## Configuración

Crear archivo `.env` en la raíz:

```env
SPRING_DATASOURCE_URL=jdbc:postgresql://<host>:5432/<db>
SPRING_DATASOURCE_USERNAME=usuario
SPRING_DATASOURCE_PASSWORD=contraseña
JWT_SECRET=clave-secreta-jwt
JWT_EXPIRATION=900000
JWT_REFRESH_EXPIRATION_MS=604800000
APP_CORS_ALLOWED_ORIGINS=http://localhost:3000
```

Sin `.env` se usa H2 en memoria (modo PostgreSQL) automáticamente.

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
docker run -p 8080:8080 --env-file .env petcare-backend
```

## Scripts disponibles

| Comando | Descripción |
|---|---|
| `./mvnw` / `mvnw.cmd` `spring-boot:run` | Inicia el servidor en desarrollo |
| `./mvnw` / `mvnw.cmd` `clean package` | Empaqueta la aplicación en JAR |
| `./mvnw` / `mvnw.cmd` `test` | Ejecuta los tests |
| `./mvnw` / `mvnw.cmd` `clean package -DskipTests` | Empaqueta sin ejecutar tests |
| `mvnw.cmd clean compile` | Compila sin empaquetar (Windows) |

## Seed Data

El proyecto incluye datos de prueba en `db/seed.sql`. Para cargarlos en la base de datos H2 en memoria:

1. Inicia la aplicación con `mvnw.cmd spring-boot:run`
2. Abre la consola H2 en [`http://localhost:8080/h2-console`](http://localhost:8080/h2-console)
3. Conéctate con los datos por defecto (URL: `jdbc:h2:mem:petcaredb`, usuario: `sa`, contraseña: vacío)
4. Copia y pega el contenido de `db/seed.sql` y ejecútalo

**Usuarios de prueba** (contraseña `password` para todos):

| Email | Rol |
|---|---|
| `admin@petcare.com` | ADMINISTRADOR |
| `carlos@petcare.com` | VETERINARIO |
| `ana@petcare.com` | ASISTENTE |
| `juan.perez@gmail.com` | DUENO |

## Notas

- **MapStruct** genera código fuente en `target/generated-sources/`. Si tu IDE muestra warnings en los mappers, marca `target/` como carpeta excluida (`File > Project Structure > Modules` en IntelliJ, o agrega `target/` en `Files > Settings > Editor > File Types > Ignore files and folders`).
- **H2 Console** solo está disponible con la configuración por defecto (sin `SPRING_DATASOURCE_URL` personalizada).

## Documentación

- Swagger UI: [`http://localhost:8080/swagger-ui.html`](https://petcare-backend-production-5445.up.railway.app/swagger-ui.html)
- OpenAPI JSON: [`http://localhost:8080/v3/api-docs`](https://petcare-backend-production-5445.up.railway.app/v3/api-docs)
- H2 Console: [`http://localhost:8080/h2-console`](http://localhost:8080/h2-console) (solo en desarrollo)
