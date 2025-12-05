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
        List<GrupoDto> grupos = grupoService.obtenerTodos();
        return ResponseEntity.ok(grupos);
    }

    @GetMapping("/activos")
    public ResponseEntity<List<GrupoDto>> obtenerActivos() {
        List<GrupoDto> grupos = grupoService.obtenerActivos();
        return ResponseEntity.ok(grupos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GrupoDto> obtenerPorId(@PathVariable Long id) {
        GrupoDto grupo = grupoService.obtenerPorId(id);
        return ResponseEntity.ok(grupo);
    }

    @GetMapping("/{id}/detalles")
    public ResponseEntity<GrupoViewDto> obtenerGrupoConDetalles(@PathVariable Long id) {
        GrupoViewDto grupo = grupoService.obtenerGrupoConDetalles(id);
        return ResponseEntity.ok(grupo);
    }

    @GetMapping("/{id}/alumnos")
    public ResponseEntity<List<AlumnoDto>> obtenerAlumnosDelGrupo(@PathVariable Long id) {
        List<AlumnoDto> alumnos = alumnoService.obtenerPorGrupo(id);
        return ResponseEntity.ok(alumnos);
    }

    @GetMapping("/programa-educativo/{programaEducativoId}")
    public ResponseEntity<List<GrupoDto>> obtenerPorProgramaEducativo(
            @PathVariable Long programaEducativoId) {
        List<GrupoDto> grupos = grupoService.obtenerPorProgramaEducativo(programaEducativoId);
        return ResponseEntity.ok(grupos);
    }

    @GetMapping("/profesor/{profesorId}")
    public ResponseEntity<List<GrupoDto>> obtenerPorProfesor(@PathVariable Long profesorId) {
        List<GrupoDto> grupos = grupoService.obtenerPorProfesor(profesorId);
        return ResponseEntity.ok(grupos);
    }

    @PostMapping
    public ResponseEntity<GrupoDto> crear(@Valid @RequestBody GrupoDto grupoDto) {
        GrupoDto nuevoGrupo = grupoService.crear(grupoDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoGrupo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GrupoDto> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody GrupoDto grupoDto) {
        GrupoDto grupoActualizado = grupoService.actualizar(id, grupoDto);
        return ResponseEntity.ok(grupoActualizado);
    }

    @PatchMapping("/{id}/asignar-profesor/{profesorId}")
    public ResponseEntity<GrupoDto> asignarProfesor(
            @PathVariable Long id,
            @PathVariable Long profesorId) {
        GrupoDto grupoActualizado = grupoService.asignarProfesor(id, profesorId);
        return ResponseEntity.ok(grupoActualizado);
    }

    @PatchMapping("/{id}/toggle-activo")
    public ResponseEntity<GrupoDto> toggleActivo(@PathVariable Long id) {
        GrupoDto grupoActualizado = grupoService.toggleActivo(id);
        return ResponseEntity.ok(grupoActualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        grupoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
