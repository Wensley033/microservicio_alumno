package mx.edu.uteq.idgs12.microservicio_alumno.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import mx.edu.uteq.idgs12.microservicio_alumno.entity.GrupoEntity;

@Repository
public interface GrupoRepository extends JpaRepository<GrupoEntity, Long> {
    
    List<GrupoEntity> findByActivoTrue();
    
    List<GrupoEntity> findByProgramaEducativoId(Long programaEducativoId);
    
    List<GrupoEntity> findByProgramaEducativoIdAndActivoTrue(Long programaEducativoId);
    
    List<GrupoEntity> findByProfesorId(Long profesorId);
    
    boolean existsByNombreAndProgramaEducativoId(String nombre, Long programaEducativoId);
}