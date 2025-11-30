package mx.edu.uteq.idgs12.microservicio_alumno.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "grupos")
public class GrupoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String nombre; // Ej: "2°A", "4°B"
    private Long programaEducativoId;
    private Long profesorId; // Profesor asignado (opcional)
    private boolean activo;
}