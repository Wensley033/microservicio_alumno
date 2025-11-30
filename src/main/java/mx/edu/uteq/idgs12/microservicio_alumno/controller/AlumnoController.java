package mx.edu.uteq.idgs12.microservicio_alumno.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mx.edu.uteq.idgs12.microservicio_alumno.dto.AlumnoDto;
import mx.edu.uteq.idgs12.microservicio_alumno.dto.AlumnoViewDto;
import mx.edu.uteq.idgs12.microservicio_alumno.dto.CambiarGrupoDto;
import mx.edu.uteq.idgs12.microservicio_alumno.service.AlumnoService;

@RestController
@RequestMapping("/alumnos")
@RequiredArgsConstructor
public class AlumnoController {

    private final AlumnoService alumnoService;

    @GetMapping
    public ResponseEntity<List<AlumnoDto>> obtenerTodos() {
        try {
            List<AlumnoDto> alumnos = alumnoService.obtenerTodos();
            return ResponseEntity.ok(alumnos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/activos")
    public ResponseEntity<List<AlumnoDto>> obtenerActivos() {
        try {
            List<AlumnoDto> alumnos = alumnoService.obtenerActivos();
            return ResponseEntity.ok(alumnos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<AlumnoDto> obtenerPorId(@PathVariable Long id) {
        try {
            AlumnoDto alumno = alumnoService.obtenerPorId(id);
            return ResponseEntity.ok(alumno);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}/detalles")
    public ResponseEntity<AlumnoViewDto> obtenerAlumnoConDetalles(@PathVariable Long id) {
        try {
            AlumnoViewDto alumno = alumnoService.obtenerAlumnoConDetalles(id);
            return ResponseEntity.ok(alumno);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/matricula/{matricula}")
    public ResponseEntity<AlumnoDto> obtenerPorMatricula(@PathVariable String matricula) {
        try {
            AlumnoDto alumno = alumnoService.obtenerPorMatricula(matricula);
            return ResponseEntity.ok(alumno);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/grupo/{grupoId}")
    public ResponseEntity<List<AlumnoDto>> obtenerPorGrupo(@PathVariable Long grupoId) {
        try {
            List<AlumnoDto> alumnos = alumnoService.obtenerPorGrupo(grupoId);
            return ResponseEntity.ok(alumnos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/programa-educativo/{programaEducativoId}")
    public ResponseEntity<List<AlumnoDto>> obtenerPorProgramaEducativo(
            @PathVariable Long programaEducativoId) {
        try {
            List<AlumnoDto> alumnos = alumnoService.obtenerPorProgramaEducativo(programaEducativoId);
            return ResponseEntity.ok(alumnos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<AlumnoDto>> buscarPorNombreOApellido(
            @RequestParam String termino) {
        try {
            List<AlumnoDto> alumnos = alumnoService.buscarPorNombreOApellido(termino);
            return ResponseEntity.ok(alumnos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping
    public ResponseEntity<AlumnoDto> crear(@Valid @RequestBody AlumnoDto alumnoDto) {
        try {
            AlumnoDto nuevoAlumno = alumnoService.crear(alumnoDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoAlumno);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<AlumnoDto> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody AlumnoDto alumnoDto) {
        try {
            AlumnoDto alumnoActualizado = alumnoService.actualizar(id, alumnoDto);
            return ResponseEntity.ok(alumnoActualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PatchMapping("/{id}/cambiar-grupo")
    public ResponseEntity<AlumnoDto> cambiarGrupo(
            @PathVariable Long id,
            @Valid @RequestBody CambiarGrupoDto dto) {
        try {
            AlumnoDto alumnoActualizado = alumnoService.cambiarGrupo(id, dto.getNuevoGrupoId());
            return ResponseEntity.ok(alumnoActualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PatchMapping("/{id}/toggle-activo")
    public ResponseEntity<AlumnoDto> toggleActivo(@PathVariable Long id) {
        try {
            AlumnoDto alumnoActualizado = alumnoService.toggleActivo(id);
            return ResponseEntity.ok(alumnoActualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        try {
            alumnoService.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}