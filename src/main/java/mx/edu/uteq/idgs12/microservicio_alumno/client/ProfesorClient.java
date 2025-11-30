package mx.edu.uteq.idgs12.microservicio_alumno.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import mx.edu.uteq.idgs12.microservicio_alumno.dto.ProfesorDto;

@FeignClient(name = "microservicio-profesor", path = "/profesores")
public interface ProfesorClient {

    @GetMapping("/{id}")
    ProfesorDto obtenerProfesorPorId(@PathVariable("id") Long id);
    
    @GetMapping
    List<ProfesorDto> obtenerTodosProfesores();
}