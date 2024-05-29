package net.dudko.microservice.model.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class OfficeServiceAPIException extends RuntimeException {

    public OfficeServiceAPIException(String message) {
        super(message);
    }

}
