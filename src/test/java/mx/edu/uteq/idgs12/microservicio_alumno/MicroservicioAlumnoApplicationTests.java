package mx.edu.uteq.idgs12.microservicio_alumno;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * Test de contexto de Spring Boot
 *
 * NOTA: Este test está deshabilitado temporalmente porque requiere que los servicios
 * externos (Eureka, microservicio-division, microservicio-profesor) estén corriendo.
 *
 * Para habilitar los tests:
 * 1. Implementar configuración de test con @MockBean para los Feign Clients
 * 2. O usar @AutoConfigureMockMvc para tests de integración sin Eureka
 * 3. O crear un perfil de test con configuración específica
 */
@Disabled("Test deshabilitado - requiere servicios externos o mocks configurados")
class MicroservicioAlumnoApplicationTests {

	@Test
	void contextLoads() {
		// Test básico para verificar que el contexto de Spring Boot se carga correctamente
	}

}
