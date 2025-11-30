package mx.edu.uteq.idgs12.microservicio_alumno.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
class GrupoUpdateDto {
    @NotBlank(message = "El nombre del grupo es obligatorio")
    private String nombre;
    
    private Long profesorId;
}
