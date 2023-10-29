package andrei.assignment1.controllers.handlers.exceptions.model;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class UsernameAlreadyExistsException extends RuntimeException{
    private final String resource;
    private final HttpStatus status ;
    private final List<String> validationErrors;
    private final LocalDateTime timestamp;

    public UsernameAlreadyExistsException(String message, HttpStatus status,  String resource, List<String> errors) {
        super(message);
        this.resource = resource;
        this.validationErrors = errors;
        this.status = status;
        this.timestamp = LocalDateTime.now();
    }

}
