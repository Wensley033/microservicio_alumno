package mx.edu.uteq.idgs12.microservicio_alumno.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mx.edu.uteq.idgs12.microservicio_alumno.client.ProgramaEducativoClient;
import mx.edu.uteq.idgs12.microservicio_alumno.dto.AlumnoDto;
import mx.edu.uteq.idgs12.microservicio_alumno.dto.AlumnoViewDto;
import mx.edu.uteq.idgs12.microservicio_alumno.dto.ProgramaEducativoDto;
import mx.edu.uteq.idgs12.microservicio_alumno.entity.AlumnoEntity;
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
public class AlumnoService {

    private final AlumnoRepository alumnoRepository;
    private final GrupoRepository grupoRepository;
    private final ProgramaEducativoClient programaEducativoClient;

    @Transactional(readOnly = true)
    public List<AlumnoDto> obtenerTodos() {
        return alumnoRepository.findAll()
                .stream()
                .map(this::convertirADto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<AlumnoDto> obtenerActivos() {
        return alumnoRepository.findByActivoTrue()
                .stream()
                .map(this::convertirADto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public AlumnoDto obtenerPorId(Long id) {
        AlumnoEntity alumno = alumnoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Alumno", "id", id));
        return convertirADto(alumno);
    }

    @Transactional(readOnly = true)
    public AlumnoDto obtenerPorMatricula(String matricula) {
        AlumnoEntity alumno = alumnoRepository.findByMatricula(matricula)
                .orElseThrow(() -> new ResourceNotFoundException("Alumno", "matrícula", matricula));
        return convertirADto(alumno);
    }

    @Transactional(readOnly = true)
    public AlumnoViewDto obtenerAlumnoConDetalles(Long id) {
        AlumnoEntity alumno = alumnoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Alumno", "id", id));

        return convertirAViewDto(alumno);
    }

    @Transactional(readOnly = true)
    public List<AlumnoDto> obtenerPorGrupo(Long grupoId) {
        return alumnoRepository.findByGrupoIdAndActivoTrue(grupoId)
                .stream()
                .map(this::convertirADto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<AlumnoDto> obtenerPorProgramaEducativo(Long programaEducativoId) {
        return alumnoRepository.findByProgramaEducativoId(programaEducativoId)
                .stream()
                .map(this::convertirADto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<AlumnoDto> buscarPorNombreOApellido(String termino) {
        return alumnoRepository.findByNombreContainingIgnoreCaseOrApellidoContainingIgnoreCase(
                termino, termino)
                .stream()
                .map(this::convertirADto)
                .collect(Collectors.toList());
    }

    @Transactional
    public AlumnoDto crear(AlumnoDto alumnoDto) {
        if (alumnoRepository.existsByMatricula(alumnoDto.getMatricula())) {
            throw new DuplicateResourceException("alumno", "matrícula", alumnoDto.getMatricula());
        }

        if (alumnoDto.getCorreo() != null &&
            alumnoRepository.existsByCorreo(alumnoDto.getCorreo())) {
            throw new DuplicateResourceException("alumno", "correo", alumnoDto.getCorreo());
        }

        if (alumnoDto.getGrupoId() != null) {
            validarGrupo(alumnoDto.getGrupoId());
        }

        validarProgramaEducativo(alumnoDto.getProgramaEducativoId());

        AlumnoEntity alumno = convertirAEntidad(alumnoDto);
        alumno.setActivo(true);

        AlumnoEntity guardado = alumnoRepository.save(alumno);
        return convertirADto(guardado);
    }

    @Transactional
    public AlumnoDto actualizar(Long id, AlumnoDto alumnoDto) {
        AlumnoEntity alumno = alumnoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Alumno", "id", id));

        if (alumnoDto.getCorreo() != null &&
            !alumnoDto.getCorreo().equals(alumno.getCorreo()) &&
            alumnoRepository.existsByCorreo(alumnoDto.getCorreo())) {
            throw new DuplicateResourceException("alumno", "correo", alumnoDto.getCorreo());
        }

        if (alumnoDto.getGrupoId() != null &&
            !alumnoDto.getGrupoId().equals(alumno.getGrupoId())) {
            validarGrupo(alumnoDto.getGrupoId());
        }

        alumno.setNombre(alumnoDto.getNombre());
        alumno.setApellido(alumnoDto.getApellido());
        alumno.setCorreo(alumnoDto.getCorreo());
        alumno.setTelefono(alumnoDto.getTelefono());
        alumno.setGrupoId(alumnoDto.getGrupoId());

        AlumnoEntity actualizado = alumnoRepository.save(alumno);
        return convertirADto(actualizado);
    }

    @Transactional
    public AlumnoDto cambiarGrupo(Long alumnoId, Long nuevoGrupoId) {
        AlumnoEntity alumno = alumnoRepository.findById(alumnoId)
                .orElseThrow(() -> new ResourceNotFoundException("Alumno", "id", alumnoId));

        validarGrupo(nuevoGrupoId);

        alumno.setGrupoId(nuevoGrupoId);

        AlumnoEntity actualizado = alumnoRepository.save(alumno);
        return convertirADto(actualizado);
    }

    @Transactional
    public void eliminar(Long id) {
        AlumnoEntity alumno = alumnoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Alumno", "id", id));

        alumno.setActivo(false);
        alumnoRepository.save(alumno);
    }

    @Transactional
    public AlumnoDto toggleActivo(Long id) {
        AlumnoEntity alumno = alumnoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Alumno", "id", id));

        alumno.setActivo(!alumno.isActivo());

        AlumnoEntity actualizado = alumnoRepository.save(alumno);
        return convertirADto(actualizado);
    }

    // Métodos auxiliares
    private void validarGrupo(Long grupoId) {
        GrupoEntity grupo = grupoRepository.findById(grupoId)
                .orElseThrow(() -> new ResourceNotFoundException("Grupo", "id", grupoId));

        if (!grupo.isActivo()) {
            throw new BusinessRuleException("El grupo seleccionado no está activo");
        }
    }

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

    // Métodos de conversión
    private AlumnoDto convertirADto(AlumnoEntity entity) {
        AlumnoDto dto = new AlumnoDto();
        dto.setId(entity.getId());
        dto.setNombre(entity.getNombre());
        dto.setApellido(entity.getApellido());
        dto.setMatricula(entity.getMatricula());
        dto.setCorreo(entity.getCorreo());
        dto.setTelefono(entity.getTelefono());
        dto.setProgramaEducativoId(entity.getProgramaEducativoId());
        dto.setGrupoId(entity.getGrupoId());
        dto.setActivo(entity.isActivo());
        return dto;
    }

    private AlumnoEntity convertirAEntidad(AlumnoDto dto) {
        AlumnoEntity entity = new AlumnoEntity();
        entity.setNombre(dto.getNombre());
        entity.setApellido(dto.getApellido());
        entity.setMatricula(dto.getMatricula());
        entity.setCorreo(dto.getCorreo());
        entity.setTelefono(dto.getTelefono());
        entity.setProgramaEducativoId(dto.getProgramaEducativoId());
        entity.setGrupoId(dto.getGrupoId());
        entity.setActivo(dto.isActivo());
        return entity;
    }

    private AlumnoViewDto convertirAViewDto(AlumnoEntity alumno) {
        AlumnoViewDto viewDto = new AlumnoViewDto();
        viewDto.setId(alumno.getId());
        viewDto.setNombre(alumno.getNombre());
        viewDto.setApellido(alumno.getApellido());
        viewDto.setMatricula(alumno.getMatricula());
        viewDto.setCorreo(alumno.getCorreo());
        viewDto.setTelefono(alumno.getTelefono());

        // Obtener nombre real del programa educativo usando Feign
        try {
            ProgramaEducativoDto programa = programaEducativoClient.obtenerProgramaPorId(alumno.getProgramaEducativoId());
            viewDto.setProgramaEducativo(programa != null ? programa.getNombre() : "Programa no disponible");
        } catch (FeignException e) {
            log.warn("No se pudo obtener programa educativo con id {}: {}", alumno.getProgramaEducativoId(), e.getMessage());
            viewDto.setProgramaEducativo("Programa " + alumno.getProgramaEducativoId());
        }

        if (alumno.getGrupoId() != null) {
            GrupoEntity grupo = grupoRepository.findById(alumno.getGrupoId()).orElse(null);
            viewDto.setGrupo(grupo != null ? grupo.getNombre() : "Grupo no encontrado");
        } else {
            viewDto.setGrupo("Sin grupo asignado");
        }

        viewDto.setActivo(alumno.isActivo());
        return viewDto;
    }
}
