package ws18.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Oliver Køppen, s175108
 */

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Too many tokens (max 6)")
public class TooManyTokensException extends Exception {

    public TooManyTokensException(String message) {
        super(message);
    }
}
