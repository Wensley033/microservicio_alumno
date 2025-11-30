package mx.edu.uteq.idgs12.microservicio_alumno.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import mx.edu.uteq.idgs12.microservicio_alumno.entity.AlumnoEntity;

@Repository
public interface AlumnoRepository extends JpaRepository<AlumnoEntity, Long> {
    
    Optional<AlumnoEntity> findByMatricula(String matricula);
    
    List<AlumnoEntity> findByGrupoId(Long grupoId);
    
    List<AlumnoEntity> findByProgramaEducativoId(Long programaEducativoId);
    
    List<AlumnoEntity> findByActivoTrue();
    
    List<AlumnoEntity> findByGrupoIdAndActivoTrue(Long grupoId);
    
    List<AlumnoEntity> findByNombreContainingIgnoreCaseOrApellidoContainingIgnoreCase(
        String nombre, String apellido);
    
    boolean existsByMatricula(String matricula);
    
    boolean existsByCorreo(String correo);
}