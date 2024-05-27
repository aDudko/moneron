package net.dudko.microservice.model.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class DepartmentServiceAPIException extends RuntimeException {

    public DepartmentServiceAPIException(String message) {
        super(message);
    }

}
