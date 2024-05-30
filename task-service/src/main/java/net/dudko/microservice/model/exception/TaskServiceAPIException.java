package net.dudko.microservice.model.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class TaskServiceAPIException extends RuntimeException {

    public TaskServiceAPIException(String message) {
        super(message);
    }

}
