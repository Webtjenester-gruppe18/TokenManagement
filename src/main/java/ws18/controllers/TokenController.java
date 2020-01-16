package ws18.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ws18.exceptions.TooManyTokensException;
import ws18.model.Token;
import ws18.service.TokenManager;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;

@RestController
public class TokenController {

    private TokenManager tokenManager = new TokenManager();

    @RequestMapping("/token")
    public String index() {
        return "Greetings from tokencontroller";
    }


    @RequestMapping(path = "/tokens/{cpr}", method = RequestMethod.GET)
    public ArrayList<Token> getTokensByCpr(@PathVariable @NotNull String cpr) {
        return tokenManager.getTokensByCpr(cpr);
    }

    @RequestMapping(path = "tokens/{cpr}/{amount}", method = RequestMethod.POST)
    public ResponseEntity<Object> generateTokens(@PathVariable @NotNull String cpr,
                                                 @PathVariable @DecimalMax("6") @DecimalMin("0") int amount) throws TooManyTokensException {
        if (cpr == null || amount < 0 || amount > 6) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        tokenManager.generateTokens(cpr, amount);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
