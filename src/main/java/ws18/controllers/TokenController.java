package ws18.controllers;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
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

    TokenManager tokenManager = new TokenManager();

    @RequestMapping("/token")
    public String index() {
        return "Greetings from tokencontroller";
    }

    @RequestMapping(value = "/tokens/{cpr}", method = RequestMethod.GET)
    public ArrayList<Token> getTokensByCpr(@PathVariable @NotNull String cpr) {
        return tokenManager.getTokensByCpr(cpr);
    }

    @RequestMapping(value = "tokens/{cpr}/{amount}", method = RequestMethod.PUT)
    public void generateTokens(@PathVariable @NotNull String cpr,
                               @PathVariable @DecimalMax("6") @DecimalMin("0") int amount) {
        try {
            tokenManager.generateTokens(cpr, amount);
        } catch (TooManyTokensException e) {
            System.out.println(e);
        }
    }
}
