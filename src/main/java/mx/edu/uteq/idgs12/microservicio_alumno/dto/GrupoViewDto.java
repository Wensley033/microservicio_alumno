package mx.edu.uteq.idgs12.microservicio_alumno.dto;

import lombok.Data;

@Data
public class GrupoViewDto {
    private Long id;
    private String nombre;
    private String programaEducativo;
    private String profesor;
    private int totalAlumnos;
    private boolean activo;
}
