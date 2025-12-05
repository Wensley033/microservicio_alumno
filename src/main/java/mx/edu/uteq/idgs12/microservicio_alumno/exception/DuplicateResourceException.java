package mx.edu.uteq.idgs12.microservicio_alumno.exception;

public class DuplicateResourceException extends RuntimeException {

    public DuplicateResourceException(String message) {
        super(message);
    }

    public DuplicateResourceException(String resourceName, String fieldName, Object fieldValue) {
        super(String.format("Ya existe un %s con %s: '%s'", resourceName, fieldName, fieldValue));
    }
}
