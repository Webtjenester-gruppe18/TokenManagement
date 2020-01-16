package ws18.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
    public ArrayList<Token> getTokensByCpr(@PathVariable @NotNull String cpr) {
        return tokenManager.getTokensByCpr(cpr);
    }

    @RequestMapping(path = "tokens/{cpr}/unused", method = RequestMethod.GET)
    public ResponseEntity<Object> getUnusedTokensByCpr(@PathVariable @NotNull String cpr) {
        ArrayList<Token> tokens = tokenManager.getUnusedTokensByCpr(cpr);
        return ResponseEntity.status(HttpStatus.OK).body(tokens);
    }

    @RequestMapping(path = "tokens/{cpr}", method = RequestMethod.POST)
    public ResponseEntity<Object> generateToken(@PathVariable @NotNull String cpr) throws TooManyTokensException {
        ArrayList<Token> tokens = tokenManager.requestForNewTokens(cpr);
        return ResponseEntity.status(HttpStatus.OK).body(tokens);
    }

    @RequestMapping(path = "tokens/{cpr}", method = RequestMethod.DELETE)
    public ResponseEntity<Object> clearUserTokens(@PathVariable @NotNull String cpr) {
        tokenManager.clearUserTokens(cpr);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @RequestMapping(path = "tokens/{cpr}/valid", method = RequestMethod.POST)
    public ResponseEntity<Object> isTokenFake(@PathVariable @NotNull String cpr,
                                              @RequestBody @NotNull Token token) {
        boolean isFake = tokenManager.isTokenFake(cpr, token);
        return ResponseEntity.status(HttpStatus.OK).body(isFake);
    }

    @RequestMapping(path = "tokens/", method = RequestMethod.POST)
    public ResponseEntity<Object> useToken(@RequestBody @NotNull Token token) {
        tokenManager.useToken(token);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
}
