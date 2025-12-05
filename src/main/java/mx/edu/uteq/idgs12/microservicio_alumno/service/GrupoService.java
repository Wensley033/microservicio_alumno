package mx.edu.uteq.idgs12.microservicio_alumno.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mx.edu.uteq.idgs12.microservicio_alumno.client.ProfesorClient;
import mx.edu.uteq.idgs12.microservicio_alumno.client.ProgramaEducativoClient;
import mx.edu.uteq.idgs12.microservicio_alumno.dto.GrupoDto;
import mx.edu.uteq.idgs12.microservicio_alumno.dto.GrupoViewDto;
import mx.edu.uteq.idgs12.microservicio_alumno.dto.ProfesorDto;
import mx.edu.uteq.idgs12.microservicio_alumno.dto.ProgramaEducativoDto;
import mx.edu.uteq.idgs12.microservicio_alumno.entity.GrupoEntity;
import mx.edu.uteq.idgs12.microservicio_alumno.exception.BusinessRuleException;
import mx.edu.uteq.idgs12.microservicio_alumno.exception.DuplicateResourceException;
import mx.edu.uteq.idgs12.microservicio_alumno.exception.ExternalServiceException;
import mx.edu.uteq.idgs12.microservicio_alumno.exception.ResourceNotFoundException;
import mx.edu.uteq.idgs12.microservicio_alumno.repository.AlumnoRepository;
import mx.edu.uteq.idgs12.microservicio_alumno.repository.GrupoRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class GrupoService {

    private final GrupoRepository grupoRepository;
    private final AlumnoRepository alumnoRepository;
    private final ProgramaEducativoClient programaEducativoClient;
    private final ProfesorClient profesorClient;

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
                .orElseThrow(() -> new ResourceNotFoundException("Grupo", "id", id));
        return convertirADto(grupo);
    }

    @Transactional(readOnly = true)
    public GrupoViewDto obtenerGrupoConDetalles(Long id) {
        GrupoEntity grupo = grupoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Grupo", "id", id));

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
            throw new DuplicateResourceException("Ya existe un grupo con el nombre '" +
                grupoDto.getNombre() + "' en este programa educativo");
        }

        validarProgramaEducativo(grupoDto.getProgramaEducativoId());

        if (grupoDto.getProfesorId() != null) {
            validarProfesor(grupoDto.getProfesorId());
        }

        GrupoEntity grupo = convertirAEntidad(grupoDto);
        grupo.setActivo(true);

        GrupoEntity guardado = grupoRepository.save(grupo);
        return convertirADto(guardado);
    }

    @Transactional
    public GrupoDto actualizar(Long id, GrupoDto grupoDto) {
        GrupoEntity grupo = grupoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Grupo", "id", id));

        if (!grupo.getNombre().equals(grupoDto.getNombre()) &&
            grupoRepository.existsByNombreAndProgramaEducativoId(
                grupoDto.getNombre(), grupo.getProgramaEducativoId())) {
            throw new DuplicateResourceException("Ya existe un grupo con el nombre '" +
                grupoDto.getNombre() + "' en este programa educativo");
        }

        if (grupoDto.getProfesorId() != null) {
            validarProfesor(grupoDto.getProfesorId());
        }

        grupo.setNombre(grupoDto.getNombre());
        grupo.setProfesorId(grupoDto.getProfesorId());

        GrupoEntity actualizado = grupoRepository.save(grupo);
        return convertirADto(actualizado);
    }

    @Transactional
    public GrupoDto asignarProfesor(Long grupoId, Long profesorId) {
        GrupoEntity grupo = grupoRepository.findById(grupoId)
                .orElseThrow(() -> new ResourceNotFoundException("Grupo", "id", grupoId));

        validarProfesor(profesorId);

        grupo.setProfesorId(profesorId);

        GrupoEntity actualizado = grupoRepository.save(grupo);
        return convertirADto(actualizado);
    }

    @Transactional
    public void eliminar(Long id) {
        GrupoEntity grupo = grupoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Grupo", "id", id));

        long alumnosActivos = alumnoRepository.findByGrupoIdAndActivoTrue(id).size();
        if (alumnosActivos > 0) {
            throw new BusinessRuleException("No se puede eliminar el grupo porque tiene " +
                alumnosActivos + " alumnos asignados");
        }

        grupo.setActivo(false);
        grupoRepository.save(grupo);
    }

    @Transactional
    public GrupoDto toggleActivo(Long id) {
        GrupoEntity grupo = grupoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Grupo", "id", id));

        grupo.setActivo(!grupo.isActivo());

        GrupoEntity actualizado = grupoRepository.save(grupo);
        return convertirADto(actualizado);
    }

    // Métodos auxiliares
    private void validarProgramaEducativo(Long programaEducativoId) {
        try {
            ProgramaEducativoDto programa = programaEducativoClient.obtenerProgramaPorId(programaEducativoId);
            if (programa == null || !programa.isActivo()) {
                throw new BusinessRuleException("El programa educativo no está disponible");
            }
        } catch (FeignException.NotFound e) {
            throw new ResourceNotFoundException("Programa Educativo", "id", programaEducativoId);
        } catch (FeignException e) {
            log.error("Error al validar programa educativo: {}", e.getMessage());
            throw new ExternalServiceException("microservicio-division", "No se pudo validar el programa educativo");
        }
    }

    private void validarProfesor(Long profesorId) {
        try {
            ProfesorDto profesor = profesorClient.obtenerProfesorPorId(profesorId);
            if (profesor == null) {
                throw new ResourceNotFoundException("Profesor", "id", profesorId);
            }
        } catch (FeignException.NotFound e) {
            throw new ResourceNotFoundException("Profesor", "id", profesorId);
        } catch (FeignException e) {
            log.error("Error al validar profesor: {}", e.getMessage());
            throw new ExternalServiceException("microservicio-profesor", "No se pudo validar el profesor");
        }
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
        viewDto.setTotalAlumnos((int) totalAlumnos);
        viewDto.setActivo(grupo.isActivo());

        // Obtener nombre real del programa educativo usando Feign
        try {
            ProgramaEducativoDto programa = programaEducativoClient.obtenerProgramaPorId(grupo.getProgramaEducativoId());
            viewDto.setProgramaEducativo(programa != null ? programa.getNombre() : "Programa no disponible");
        } catch (FeignException e) {
            log.warn("No se pudo obtener programa educativo con id {}: {}", grupo.getProgramaEducativoId(), e.getMessage());
            viewDto.setProgramaEducativo("Programa " + grupo.getProgramaEducativoId());
        }

        // Obtener datos del profesor usando Feign
        if (grupo.getProfesorId() != null) {
            try {
                ProfesorDto profesor = profesorClient.obtenerProfesorPorId(grupo.getProfesorId());
                if (profesor != null) {
                    viewDto.setProfesor(profesor.getNombre() + " " + profesor.getApellido());
                } else {
                    viewDto.setProfesor("Profesor no disponible");
                }
            } catch (FeignException e) {
                log.warn("No se pudo obtener profesor con id {}: {}", grupo.getProfesorId(), e.getMessage());
                viewDto.setProfesor("Profesor " + grupo.getProfesorId());
            }
        } else {
            viewDto.setProfesor("Sin profesor asignado");
        }

        return viewDto;
    }
}
