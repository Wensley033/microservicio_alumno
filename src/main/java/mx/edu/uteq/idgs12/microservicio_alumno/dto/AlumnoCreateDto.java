package mx.edu.uteq.idgs12.microservicio_alumno.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Data
public class AlumnoCreateDto {
    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;
    
    @NotBlank(message = "El apellido es obligatorio")
    private String apellido;
    
    @NotBlank(message = "La matrícula es obligatoria")
    private String matricula;
    
    @Email(message = "El correo debe ser válido")
    private String correo;
    
    private String telefono;
    
    @NotNull(message = "El programa educativo es obligatorio")
    private Long programaEducativoId;
    
    private Long grupoId;
}
