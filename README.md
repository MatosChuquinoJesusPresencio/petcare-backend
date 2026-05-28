# PetCare Backend

API RESTful para la gestión de una clínica veterinaria. Desarrollada con Java 21 y Spring Boot 3.5.

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

## Documentación

- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`
- H2 Console: `http://localhost:8080/h2-console` (solo en desarrollo)
