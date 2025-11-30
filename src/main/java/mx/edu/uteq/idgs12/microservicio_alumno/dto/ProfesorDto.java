package mx.edu.uteq.idgs12.microservicio_alumno.dto;


import lombok.Data;

@Data
public class ProfesorDto {
    private Long id;
    private String nombre;
    private String apellido;
    private String correo;
    private String telefono;
    private Long divisionId;
}
