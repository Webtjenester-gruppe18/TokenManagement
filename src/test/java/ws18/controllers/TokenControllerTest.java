package ws18.controllers;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import ws18.model.Token;
import ws18.service.TokenManager;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class TokenControllerTest {

    private TokenController tokenController = new TokenController();
    String cpr = "112233-4455";
    ArrayList<Token> tokens;

    @After
    void tearDown() {
        tokenController = null;
    }

    @Test
    void getTokens() {
        tokens = tokenController.getTokensByCpr(cpr);
        Assert.assertEquals(0,tokens.size());
    }

    @Test
    void generateTokens() {
        tokenController.generateTokens(cpr, 6);
        tokens = tokenController.getTokensByCpr(cpr);
        Assert.assertEquals(6, tokens.size());
    }

    @Test
    void tooManyTokens() {
        tokenController.generateTokens(cpr, 7);
    }
}