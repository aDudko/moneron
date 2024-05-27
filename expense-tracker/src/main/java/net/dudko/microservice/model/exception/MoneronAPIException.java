package net.dudko.microservice.model.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class MoneronAPIException extends RuntimeException {

    public MoneronAPIException(String message) {
        super(message);
    }

}
