package mx.edu.uteq.idgs12.microservicio_alumno.dto;


import lombok.Data;

@Data
public class AlumnoViewDto {
     private Long id;
    private String nombre;
    private String apellido;
    private String matricula;
    private String correo;
    private String telefono;
    private String programaEducativo;
    private String grupo;
    private boolean activo;

}
