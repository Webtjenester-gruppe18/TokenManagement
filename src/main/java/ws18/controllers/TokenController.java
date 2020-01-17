package ws18.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ws18.exceptions.TokenUsedException;
import ws18.exceptions.TokenValidationException;
import ws18.exceptions.TooManyTokensException;
import ws18.model.Token;
import ws18.service.TokenManager;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;

@RestController
public class TokenController  {

    private TokenManager tokenManager = new TokenManager();

    @RequestMapping("/token")
    public String index() {
        return "Greetings from tokencontroller";
    }

    @RequestMapping(path = "tokens/{cpr}", method = RequestMethod.GET)
    public ResponseEntity<Object> getTokensByCpr(@PathVariable @NotNull String cpr) {
        ArrayList<Token> tokens = tokenManager.getUnusedTokensByCpr(cpr);
        return ResponseEntity.status(HttpStatus.OK).body(tokens);
    }

    @RequestMapping(path = "tokens/{cpr}", method = RequestMethod.POST)
    public ResponseEntity<Object> generateToken(@PathVariable @NotNull String cpr) throws TooManyTokensException {
        ArrayList<Token> tokens = tokenManager.requestForNewTokens(cpr);
        return ResponseEntity.status(HttpStatus.OK).body(tokens);
    }

    @RequestMapping(path = "tokens/{cpr}/valid", method = RequestMethod.PUT)
    public ResponseEntity<Object> isTokenFake(@PathVariable @NotNull String cpr,
                                              @RequestBody @NotNull Token token) throws TokenValidationException {
        Token validToken = tokenManager.validateToken(cpr, token);
        return ResponseEntity.status(HttpStatus.OK).body(validToken);
    }

    @RequestMapping(path = "tokens", method = RequestMethod.PUT)
    public ResponseEntity<Object> useToken(@RequestBody @NotNull Token token) throws TokenUsedException {
        tokenManager.useToken(token);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
}
