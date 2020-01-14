package ws18.controllers;

import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TokenControllerTest {

    TokenController tokenController;

    @Before
    void setUp() {
        tokenController = new TokenController();
    }

    @After
    void tearDown() {
        tokenController = null;
    }

    @Test
    void index() {

    }
}