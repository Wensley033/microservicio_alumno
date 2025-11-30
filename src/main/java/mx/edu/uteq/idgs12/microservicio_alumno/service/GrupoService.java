package mx.edu.uteq.idgs12.microservicio_alumno.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import mx.edu.uteq.idgs12.microservicio_alumno.dto.GrupoDto;
import mx.edu.uteq.idgs12.microservicio_alumno.dto.GrupoViewDto;
import mx.edu.uteq.idgs12.microservicio_alumno.entity.GrupoEntity;
import mx.edu.uteq.idgs12.microservicio_alumno.repository.AlumnoRepository;
import mx.edu.uteq.idgs12.microservicio_alumno.repository.GrupoRepository;

@Service
@RequiredArgsConstructor
public class GrupoService {

    private final GrupoRepository grupoRepository;
    private final AlumnoRepository alumnoRepository;

    @Transactional(readOnly = true)
    public List<GrupoDto> obtenerTodos() {
        return grupoRepository.findAll()
                .stream()
                .map(this::convertirADto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<GrupoDto> obtenerActivos() {
        return grupoRepository.findByActivoTrue()
                .stream()
                .map(this::convertirADto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public GrupoDto obtenerPorId(Long id) {
        GrupoEntity grupo = grupoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Grupo no encontrado con id: " + id));
        return convertirADto(grupo);
    }

    @Transactional(readOnly = true)
    public GrupoViewDto obtenerGrupoConDetalles(Long id) {
        GrupoEntity grupo = grupoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Grupo no encontrado con id: " + id));
        
        long totalAlumnos = alumnoRepository.findByGrupoIdAndActivoTrue(id).size();
        
        return convertirAViewDto(grupo, totalAlumnos);
    }

    @Transactional(readOnly = true)
    public List<GrupoDto> obtenerPorProgramaEducativo(Long programaEducativoId) {
        return grupoRepository.findByProgramaEducativoIdAndActivoTrue(programaEducativoId)
                .stream()
                .map(this::convertirADto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<GrupoDto> obtenerPorProfesor(Long profesorId) {
        return grupoRepository.findByProfesorId(profesorId)
                .stream()
                .map(this::convertirADto)
                .collect(Collectors.toList());
    }

    @Transactional
    public GrupoDto crear(GrupoDto grupoDto) {
        if (grupoRepository.existsByNombreAndProgramaEducativoId(
                grupoDto.getNombre(), grupoDto.getProgramaEducativoId())) {
            throw new RuntimeException("Ya existe un grupo con el nombre '" + 
                grupoDto.getNombre() + "' en este programa educativo");
        }
        
        GrupoEntity grupo = convertirAEntidad(grupoDto);
        grupo.setActivo(true);
        
        GrupoEntity guardado = grupoRepository.save(grupo);
        return convertirADto(guardado);
    }

    @Transactional
    public GrupoDto actualizar(Long id, GrupoDto grupoDto) {
        GrupoEntity grupo = grupoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Grupo no encontrado con id: " + id));
        
        // Validar nombre si cambió
        if (!grupo.getNombre().equals(grupoDto.getNombre()) &&
            grupoRepository.existsByNombreAndProgramaEducativoId(
                grupoDto.getNombre(), grupo.getProgramaEducativoId())) {
            throw new RuntimeException("Ya existe un grupo con el nombre '" + 
                grupoDto.getNombre() + "' en este programa educativo");
        }
        
        grupo.setNombre(grupoDto.getNombre());
        grupo.setProfesorId(grupoDto.getProfesorId());
        
        GrupoEntity actualizado = grupoRepository.save(grupo);
        return convertirADto(actualizado);
    }

    @Transactional
    public GrupoDto asignarProfesor(Long grupoId, Long profesorId) {
        GrupoEntity grupo = grupoRepository.findById(grupoId)
                .orElseThrow(() -> new RuntimeException("Grupo no encontrado con id: " + grupoId));
        
        grupo.setProfesorId(profesorId);
        
        GrupoEntity actualizado = grupoRepository.save(grupo);
        return convertirADto(actualizado);
    }

    @Transactional
    public void eliminar(Long id) {
        GrupoEntity grupo = grupoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Grupo no encontrado con id: " + id));
        
        // Verificar si tiene alumnos asignados
        long alumnosActivos = alumnoRepository.findByGrupoIdAndActivoTrue(id).size();
        if (alumnosActivos > 0) {
            throw new RuntimeException("No se puede eliminar el grupo porque tiene " + 
                alumnosActivos + " alumnos asignados");
        }
        
        grupo.setActivo(false);
        grupoRepository.save(grupo);
    }

    @Transactional
    public GrupoDto toggleActivo(Long id) {
        GrupoEntity grupo = grupoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Grupo no encontrado con id: " + id));
        
        grupo.setActivo(!grupo.isActivo());
        
        GrupoEntity actualizado = grupoRepository.save(grupo);
        return convertirADto(actualizado);
    }

    // Métodos de conversión
    private GrupoDto convertirADto(GrupoEntity entity) {
        GrupoDto dto = new GrupoDto();
        dto.setId(entity.getId());
        dto.setNombre(entity.getNombre());
        dto.setProgramaEducativoId(entity.getProgramaEducativoId());
        dto.setProfesorId(entity.getProfesorId());
        dto.setActivo(entity.isActivo());
        return dto;
    }

    private GrupoEntity convertirAEntidad(GrupoDto dto) {
        GrupoEntity entity = new GrupoEntity();
        entity.setNombre(dto.getNombre());
        entity.setProgramaEducativoId(dto.getProgramaEducativoId());
        entity.setProfesorId(dto.getProfesorId());
        entity.setActivo(dto.isActivo());
        return entity;
    }

    private GrupoViewDto convertirAViewDto(GrupoEntity grupo, long totalAlumnos) {
        GrupoViewDto viewDto = new GrupoViewDto();
        viewDto.setId(grupo.getId());
        viewDto.setNombre(grupo.getNombre());
        viewDto.setProgramaEducativo("Programa " + grupo.getProgramaEducativoId()); // Aquí llamarías al client
        viewDto.setProfesor(grupo.getProfesorId() != null ? 
            "Profesor " + grupo.getProfesorId() : "Sin profesor asignado"); // Aquí llamarías al client
        viewDto.setTotalAlumnos((int) totalAlumnos);
        viewDto.setActivo(grupo.isActivo());
        return viewDto;
    }
}