# PetCare Backend

![Java 21](https://img.shields.io/badge/Java-21-ED8B00?logo=java)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5-6DB33F?logo=spring)
![Maven](https://img.shields.io/badge/Maven-3.9-C71A36?logo=apachemaven)
![License](https://img.shields.io/badge/License-MIT-green)

API RESTful para la gestión de una clínica veterinaria. Desarrollada con Java 21 y Spring Boot 3.5.

**Despliegue:** [https://petcare-backend-xxuc.onrender.com/](https://petcare-backend-xxuc.onrender.com/)

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

- **Java 21** + **Spring Boot 3.5.14**
- **Spring MVC**, **Spring Data JPA**, **Spring Security**
- **PostgreSQL** (producción) / **H2** (desarrollo)
- **JWT** (jjwt) para autenticación
- **MapStruct** para mapeo DTO ↔ Entidad
- **Lombok**, **Spring Validation**, **SpringDoc OpenAPI 3**
- **Docker**

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
├── web/              # Capa de entrada (controladores REST)
│   ├── controller/   #   Endpoints de la API
│   ├── dto/          #   Objetos de transferencia de datos
│   └── security/     #   Filtros JWT, configuración de seguridad
├── domain/           # Capa de negocio
│   ├── model/        #   Modelos de dominio
│   ├── port/         #   Puertos (interfaces de entrada/salida)
│   └── service/      #   Implementación de servicios
├── persistence/      # Capa de persistencia
│   ├── entity/       #   Entidades JPA
│   ├── adapter/      #   Adaptadores de repositorio
│   └── repository/   #   Repositorios Spring Data JPA
└── config/           # Configuración global
    └── openapi/      #   Configuración de Swagger/OpenAPI
```

```
src/main/resources/
├── application.yml         # Configuración principal
├── application-dev.yml     # Perfil de desarrollo (H2)
└── application-prod.yml    # Perfil de producción (PostgreSQL)
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
# Con Maven wrapper
./mvnw spring-boot:run

# Build y ejecución
./mvnw clean package -DskipTests
java -jar target/backend-0.0.1-SNAPSHOT.jar

# Con Docker
docker build -t petcare-backend .
docker run -p 8080:8080 --env-file .env petcare-backend
```

## Scripts disponibles

| Comando | Descripción |
|---|---|
| `./mvnw spring-boot:run` | Inicia el servidor en desarrollo |
| `./mvnw clean package` | Empaqueta la aplicación en JAR |
| `./mvnw test` | Ejecuta los tests |
| `./mvnw clean package -DskipTests` | Empaqueta sin ejecutar tests |

## Documentación

- Swagger UI: [`http://localhost:8080/swagger-ui.html`](http://localhost:8080/swagger-ui.html)
- OpenAPI JSON: [`http://localhost:8080/v3/api-docs`](http://localhost:8080/v3/api-docs)
- H2 Console: [`http://localhost:8080/h2-console`](http://localhost:8080/h2-console) (solo en desarrollo)
