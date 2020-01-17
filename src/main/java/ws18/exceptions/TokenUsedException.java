package ws18.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "The token has already been used")
public class TokenUsedException extends Exception {
    public TokenUsedException(String message) {
        super(message);
    }
}
