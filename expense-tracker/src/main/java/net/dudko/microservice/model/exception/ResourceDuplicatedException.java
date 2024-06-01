package net.dudko.microservice.model.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class ResourceDuplicatedException extends RuntimeException {

    public ResourceDuplicatedException(String message) {
        super(message);
    }

}
