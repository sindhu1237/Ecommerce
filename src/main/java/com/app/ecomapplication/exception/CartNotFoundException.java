package com.app.ecomapplication.exception;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class CartNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 17683256L;
    public CartNotFoundException(String message) {
        super(message);
    }
}