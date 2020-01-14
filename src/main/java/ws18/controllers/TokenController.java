package ws18.controllers;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TokenController {

    @RequestMapping("/token")
    public String index() {
        return "Greetings from tokencontroller";
    }
}
