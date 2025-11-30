package mx.edu.uteq.idgs12.microservicio_alumno.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import mx.edu.uteq.idgs12.microservicio_alumno.dto.AlumnoDto;
import mx.edu.uteq.idgs12.microservicio_alumno.dto.AlumnoViewDto;
import mx.edu.uteq.idgs12.microservicio_alumno.entity.AlumnoEntity;
import mx.edu.uteq.idgs12.microservicio_alumno.entity.GrupoEntity;
import mx.edu.uteq.idgs12.microservicio_alumno.repository.AlumnoRepository;
import mx.edu.uteq.idgs12.microservicio_alumno.repository.GrupoRepository;

@Service
@RequiredArgsConstructor
public class AlumnoService {

    private final AlumnoRepository alumnoRepository;
    private final GrupoRepository grupoRepository;

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
                .orElseThrow(() -> new RuntimeException("Alumno no encontrado con id: " + id));
        return convertirADto(alumno);
    }

    @Transactional(readOnly = true)
    public AlumnoDto obtenerPorMatricula(String matricula) {
        AlumnoEntity alumno = alumnoRepository.findByMatricula(matricula)
                .orElseThrow(() -> new RuntimeException("Alumno no encontrado con matrícula: " + matricula));
        return convertirADto(alumno);
    }

    @Transactional(readOnly = true)
    public AlumnoViewDto obtenerAlumnoConDetalles(Long id) {
        AlumnoEntity alumno = alumnoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Alumno no encontrado con id: " + id));
        
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
        // Validar matrícula única
        if (alumnoRepository.existsByMatricula(alumnoDto.getMatricula())) {
            throw new RuntimeException("Ya existe un alumno con la matrícula: " + alumnoDto.getMatricula());
        }
        
        // Validar correo único si se proporciona
        if (alumnoDto.getCorreo() != null && 
            alumnoRepository.existsByCorreo(alumnoDto.getCorreo())) {
            throw new RuntimeException("Ya existe un alumno con el correo: " + alumnoDto.getCorreo());
        }
        
        // Validar que el grupo existe si se proporciona
        if (alumnoDto.getGrupoId() != null) {
            validarGrupo(alumnoDto.getGrupoId());
        }
        
        AlumnoEntity alumno = convertirAEntidad(alumnoDto);
        alumno.setActivo(true);
        
        AlumnoEntity guardado = alumnoRepository.save(alumno);
        return convertirADto(guardado);
    }

    @Transactional
    public AlumnoDto actualizar(Long id, AlumnoDto alumnoDto) {
        AlumnoEntity alumno = alumnoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Alumno no encontrado con id: " + id));
        
        // Validar correo si cambió
        if (alumnoDto.getCorreo() != null &&
            !alumnoDto.getCorreo().equals(alumno.getCorreo()) && 
            alumnoRepository.existsByCorreo(alumnoDto.getCorreo())) {
            throw new RuntimeException("Ya existe un alumno con el correo: " + alumnoDto.getCorreo());
        }
        
        // Validar grupo si cambió
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
                .orElseThrow(() -> new RuntimeException("Alumno no encontrado con id: " + alumnoId));
        
        validarGrupo(nuevoGrupoId);
        
        alumno.setGrupoId(nuevoGrupoId);
        
        AlumnoEntity actualizado = alumnoRepository.save(alumno);
        return convertirADto(actualizado);
    }

    @Transactional
    public void eliminar(Long id) {
        AlumnoEntity alumno = alumnoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Alumno no encontrado con id: " + id));
        
        alumno.setActivo(false);
        alumnoRepository.save(alumno);
    }

    @Transactional
    public AlumnoDto toggleActivo(Long id) {
        AlumnoEntity alumno = alumnoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Alumno no encontrado con id: " + id));
        
        alumno.setActivo(!alumno.isActivo());
        
        AlumnoEntity actualizado = alumnoRepository.save(alumno);
        return convertirADto(actualizado);
    }

    // Métodos auxiliares
    private void validarGrupo(Long grupoId) {
        GrupoEntity grupo = grupoRepository.findById(grupoId)
                .orElseThrow(() -> new RuntimeException("Grupo no encontrado con id: " + grupoId));
        
        if (!grupo.isActivo()) {
            throw new RuntimeException("El grupo seleccionado no está activo");
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
        viewDto.setProgramaEducativo("Programa " + alumno.getProgramaEducativoId()); // Aquí llamar al client
        
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