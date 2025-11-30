package mx.edu.uteq.idgs12.microservicio_alumno.dto;

import java.util.List;

import lombok.Data;

@Data
public class DivisionDto {
    private Long divisionId;
    private String nombre;
    private List<String> programaEducativa;
    private boolean activo;
    private int numeroProgramas;
}
