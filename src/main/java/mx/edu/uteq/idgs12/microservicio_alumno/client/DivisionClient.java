package mx.edu.uteq.idgs12.microservicio_alumno.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import mx.edu.uteq.idgs12.microservicio_alumno.dto.DivisionDto;

@FeignClient(name = "microservicio-division", path = "/divisiones")
public interface DivisionClient {

    @GetMapping("/{id}")
    DivisionDto obtenerDivisionPorId(@PathVariable("id") Long id);
    
    @GetMapping
    List<DivisionDto> obtenerTodasLasDivisiones();
    
    @GetMapping("/activas")
    List<DivisionDto> obtenerDivisionesActivas();
}