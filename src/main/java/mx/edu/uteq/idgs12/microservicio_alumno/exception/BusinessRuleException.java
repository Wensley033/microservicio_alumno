package mx.edu.uteq.idgs12.microservicio_alumno.exception;

public class BusinessRuleException extends RuntimeException {

    public BusinessRuleException(String message) {
        super(message);
    }
}
