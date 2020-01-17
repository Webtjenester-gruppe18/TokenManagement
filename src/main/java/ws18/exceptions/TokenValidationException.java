package ws18.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "The token could not be validated")
public class TokenValidationException extends Exception {

    public TokenValidationException(String message) {
        super(message);
    }
}
