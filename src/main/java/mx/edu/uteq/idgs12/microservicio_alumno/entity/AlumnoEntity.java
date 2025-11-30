package mx.edu.uteq.idgs12.microservicio_alumno.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "alumnos")
public class AlumnoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String nombre;
    private String apellido;
    
    @Column(unique = true, nullable = false)
    private String matricula;
    
    private String correo;
    private String telefono;
    private Long programaEducativoId;
    private Long grupoId;
    private boolean activo;
}