package org.fortech.navigation.security.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class CarNotFoundException extends RuntimeException{

    @Serial
    private static final long serialVersionUID = 1L;

    public CarNotFoundException(String vin, String message) {
        super(String.format("Failed for [%s]: %s", vin, message));
    }
}
