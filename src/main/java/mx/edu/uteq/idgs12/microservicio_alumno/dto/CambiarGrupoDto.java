package mx.edu.uteq.idgs12.microservicio_alumno.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Data
public class CambiarGrupoDto {
    @NotNull(message = "El nuevo grupo es obligatorio")
    private Long nuevoGrupoId;

}
