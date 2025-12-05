package mx.edu.uteq.idgs12.microservicio_alumno.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import mx.edu.uteq.idgs12.microservicio_alumno.dto.ProgramaEducativoDto;

@FeignClient(name = "microservicio-division", path = "/programas-educativos")
public interface ProgramaEducativoClient {

    @GetMapping("/{id}")
    ProgramaEducativoDto obtenerProgramaPorId(@PathVariable("id") Long id);

    @GetMapping
    List<ProgramaEducativoDto> obtenerTodosProgramas();

    @GetMapping("/activos")
    List<ProgramaEducativoDto> obtenerProgramasActivos();
}
