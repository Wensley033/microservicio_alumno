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
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mx.edu.uteq.idgs12.microservicio_alumno.dto.AlumnoDto;
import mx.edu.uteq.idgs12.microservicio_alumno.dto.GrupoDto;
import mx.edu.uteq.idgs12.microservicio_alumno.dto.GrupoViewDto;
import mx.edu.uteq.idgs12.microservicio_alumno.service.AlumnoService;
import mx.edu.uteq.idgs12.microservicio_alumno.service.GrupoService;

@RestController
@RequestMapping("/grupos")
@RequiredArgsConstructor
public class GrupoController {

    private final GrupoService grupoService;
    private final AlumnoService alumnoService;

    @GetMapping
    public ResponseEntity<List<GrupoDto>> obtenerTodos() {
        try {
            List<GrupoDto> grupos = grupoService.obtenerTodos();
            return ResponseEntity.ok(grupos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/activos")
    public ResponseEntity<List<GrupoDto>> obtenerActivos() {
        try {
            List<GrupoDto> grupos = grupoService.obtenerActivos();
            return ResponseEntity.ok(grupos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<GrupoDto> obtenerPorId(@PathVariable Long id) {
        try {
            GrupoDto grupo = grupoService.obtenerPorId(id);
            return ResponseEntity.ok(grupo);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}/detalles")
    public ResponseEntity<GrupoViewDto> obtenerGrupoConDetalles(@PathVariable Long id) {
        try {
            GrupoViewDto grupo = grupoService.obtenerGrupoConDetalles(id);
            return ResponseEntity.ok(grupo);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}/alumnos")
    public ResponseEntity<List<AlumnoDto>> obtenerAlumnosDelGrupo(@PathVariable Long id) {
        try {
            List<AlumnoDto> alumnos = alumnoService.obtenerPorGrupo(id);
            return ResponseEntity.ok(alumnos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/programa-educativo/{programaEducativoId}")
    public ResponseEntity<List<GrupoDto>> obtenerPorProgramaEducativo(
            @PathVariable Long programaEducativoId) {
        try {
            List<GrupoDto> grupos = grupoService.obtenerPorProgramaEducativo(programaEducativoId);
            return ResponseEntity.ok(grupos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/profesor/{profesorId}")
    public ResponseEntity<List<GrupoDto>> obtenerPorProfesor(@PathVariable Long profesorId) {
        try {
            List<GrupoDto> grupos = grupoService.obtenerPorProfesor(profesorId);
            return ResponseEntity.ok(grupos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping
    public ResponseEntity<GrupoDto> crear(@Valid @RequestBody GrupoDto grupoDto) {
        try {
            GrupoDto nuevoGrupo = grupoService.crear(grupoDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoGrupo);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<GrupoDto> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody GrupoDto grupoDto) {
        try {
            GrupoDto grupoActualizado = grupoService.actualizar(id, grupoDto);
            return ResponseEntity.ok(grupoActualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PatchMapping("/{id}/asignar-profesor/{profesorId}")
    public ResponseEntity<GrupoDto> asignarProfesor(
            @PathVariable Long id,
            @PathVariable Long profesorId) {
        try {
            GrupoDto grupoActualizado = grupoService.asignarProfesor(id, profesorId);
            return ResponseEntity.ok(grupoActualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PatchMapping("/{id}/toggle-activo")
    public ResponseEntity<GrupoDto> toggleActivo(@PathVariable Long id) {
        try {
            GrupoDto grupoActualizado = grupoService.toggleActivo(id);
            return ResponseEntity.ok(grupoActualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        try {
            grupoService.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}