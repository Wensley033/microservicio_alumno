package mx.edu.uteq.idgs12.microservicio_alumno;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class MicroservicioAlumnoApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicroservicioAlumnoApplication.class, args);
	}

}
