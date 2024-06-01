package net.dudko.microservice.model.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class ExpenseCategoryServiceAPIException extends RuntimeException {

    public ExpenseCategoryServiceAPIException(String message) {
        super(message);
    }

}
