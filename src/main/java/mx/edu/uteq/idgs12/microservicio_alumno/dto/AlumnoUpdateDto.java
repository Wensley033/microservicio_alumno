package mx.edu.uteq.idgs12.microservicio_alumno.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
class AlumnoUpdateDto {
    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;
    
    @NotBlank(message = "El apellido es obligatorio")
    private String apellido;
    
    @Email(message = "El correo debe ser válido")
    private String correo;
    
    private String telefono;
    private Long grupoId;
}

