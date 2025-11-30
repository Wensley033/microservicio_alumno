package mx.edu.uteq.idgs12.microservicio_alumno.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
class GrupoCreateDto {
    @NotBlank(message = "El nombre del grupo es obligatorio")
    private String nombre;
    
    @NotNull(message = "El programa educativo es obligatorio")
    private Long programaEducativoId;
    
    private Long profesorId;
}

