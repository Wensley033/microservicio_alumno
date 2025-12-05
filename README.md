# Microservicio Alumno

Microservicio REST para gestión de alumnos y grupos dentro de un ecosistema de microservicios educativos.

## Características

- ✅ CRUD completo de alumnos y grupos
- ✅ Integración con microservicios via Feign Clients
- ✅ Manejo centralizado de excepciones
- ✅ Base de datos H2 persistente
- ✅ Validaciones robustas
- ✅ Eliminación lógica (soft delete)
- ✅ Registro automático en Eureka

## Tecnologías

- Java 21
- Spring Boot 3.5.7
- Spring Cloud 2025.0.0
- Spring Data JPA
- H2 Database
- Lombok
- OpenFeign
- Eureka Client

## Endpoints Principales

### Alumnos

```
GET    /alumnos                          - Listar todos los alumnos
GET    /alumnos/activos                  - Listar alumnos activos
GET    /alumnos/{id}                     - Obtener alumno por ID
GET    /alumnos/{id}/detalles            - Alumno con datos enriquecidos
GET    /alumnos/matricula/{matricula}    - Buscar por matrícula
GET    /alumnos/buscar?termino={texto}   - Buscar por nombre/apellido
POST   /alumnos                          - Crear alumno
PUT    /alumnos/{id}                     - Actualizar alumno
PATCH  /alumnos/{id}/cambiar-grupo       - Cambiar grupo del alumno
PATCH  /alumnos/{id}/toggle-activo       - Activar/desactivar alumno
DELETE /alumnos/{id}                     - Eliminar alumno (soft delete)
```

### Grupos

```
GET    /grupos                               - Listar todos los grupos
GET    /grupos/activos                       - Listar grupos activos
GET    /grupos/{id}                          - Obtener grupo por ID
GET    /grupos/{id}/detalles                 - Grupo con datos enriquecidos
GET    /grupos/{id}/alumnos                  - Alumnos del grupo
GET    /grupos/programa-educativo/{id}       - Grupos por programa
GET    /grupos/profesor/{id}                 - Grupos por profesor
POST   /grupos                               - Crear grupo
PUT    /grupos/{id}                          - Actualizar grupo
PATCH  /grupos/{id}/asignar-profesor/{idProf} - Asignar profesor
PATCH  /grupos/{id}/toggle-activo            - Activar/desactivar grupo
DELETE /grupos/{id}                          - Eliminar grupo (soft delete)
```

## Configuración

### Puertos

- **Aplicación**: 8083
- **Eureka**: 8761
- **API Gateway**: 8080

### Base de Datos H2

- **URL**: `jdbc:h2:file:./data/alumnodb`
- **Consola H2**: http://localhost:8083/h2-console
- **Usuario**: sa
- **Contraseña**: (vacío)

### Feign Clients

El microservicio se comunica con:

- **microservicio-division** (puerto 8081): Validar programas educativos
- **microservicio-profesor** (puerto 8082): Validar profesores

## Instalación y Ejecución

### Pre-requisitos

1. Java 21 o superior
2. Maven 3.6+
3. Servicios dependientes corriendo:
   - Eureka Server (puerto 8761)
   - API Gateway (puerto 8080)
   - Microservicio Division (puerto 8081)
   - Microservicio Profesor (puerto 8082)

### Compilar

```bash
./mvnw clean compile
```

### Ejecutar

```bash
./mvnw spring-boot:run
```

### Ejecutar Tests

```bash
./mvnw test
```

**Nota**: Los tests están deshabilitados temporalmente porque requieren servicios externos corriendo.

## Ejemplos de Uso

### Crear un Alumno

```bash
curl -X POST http://localhost:8080/alumnos \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "Juan",
    "apellido": "Pérez",
    "matricula": "2025010001",
    "correo": "juan.perez@uteq.edu.mx",
    "telefono": "4421234567",
    "programaEducativoId": 1,
    "grupoId": 1
  }'
```

### Obtener Alumno con Detalles

```bash
curl http://localhost:8080/alumnos/1/detalles
```

Respuesta enriquecida:
```json
{
  "id": 1,
  "nombre": "Juan",
  "apellido": "Pérez García",
  "matricula": "2012010001",
  "correo": "juan.perez@uteq.edu.mx",
  "telefono": "4421234567",
  "programaEducativo": "Ingeniería en Desarrollo de Software",
  "grupo": "IDGS-12A",
  "activo": true
}
```

## Arquitectura

### Paquetes

```
mx.edu.uteq.idgs12.microservicio_alumno
├── client/              # Feign Clients
├── controller/          # REST Controllers
├── dto/                 # Data Transfer Objects
├── entity/              # JPA Entities
├── exception/           # Excepciones personalizadas
├── repository/          # Repositorios JPA
└── service/             # Lógica de negocio
```

### Modelo de Datos

**AlumnoEntity**:
- id, nombre, apellido, matricula (unique)
- correo (unique), telefono
- programaEducativoId, grupoId
- activo (boolean)

**GrupoEntity**:
- id, nombre
- programaEducativoId, profesorId
- activo (boolean)

## Reglas de Negocio

- ✅ Matrícula única por alumno
- ✅ Correo único por alumno
- ✅ Grupo debe estar activo para asignar alumnos
- ✅ No se puede eliminar grupo con alumnos activos
- ✅ Programa educativo debe existir (validado vía Feign)
- ✅ Profesor debe existir (validado vía Feign)
- ✅ Eliminación lógica (no física)

## Manejo de Errores

Todas las excepciones retornan JSON estructurado:

```json
{
  "timestamp": "2025-12-04T18:00:00",
  "status": 404,
  "error": "Recurso no encontrado",
  "message": "Alumno no encontrado con id: '999'"
}
```

### Códigos HTTP

- `200` - OK
- `201` - Created
- `204` - No Content (delete exitoso)
- `400` - Bad Request (validación fallida)
- `404` - Not Found
- `409` - Conflict (recurso duplicado)
- `503` - Service Unavailable (servicio externo no disponible)

## Documentación Adicional

- [MEJORAS_IMPLEMENTADAS.md](MEJORAS_IMPLEMENTADAS.md) - Detalle de mejoras recientes

## Desarrollo

### Agregar Nuevos Endpoints

1. Crear DTO en `/dto`
2. Agregar método en Service con lógica de negocio
3. Crear endpoint en Controller
4. Las excepciones se manejan automáticamente

### Agregar Validaciones

Usar anotaciones de Jakarta Validation en DTOs:

```java
@NotBlank(message = "El nombre es obligatorio")
private String nombre;

@Email(message = "El correo debe ser válido")
private String correo;
```

## Licencia

Proyecto educativo - Universidad Tecnológica de Querétaro

---

**Versión**: 0.0.1-SNAPSHOT
**Última actualización**: Diciembre 2025
