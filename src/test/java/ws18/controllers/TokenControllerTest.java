package ws18.controllers;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import ws18.exceptions.TooManyTokensException;
import ws18.model.Token;
import ws18.service.TokenManager;

import java.util.ArrayList;

import static org.hamcrest.core.StringContains.containsString;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TokenControllerTest {

    private TokenController tokenController = new TokenController();
    private String cpr = "112233-4455";
    private int tokenAmount;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void tooManyTokens() throws TooManyTokensException {
        tokenController.generateTokens(cpr, 7);
    }

    @Test
    void getTokensByCpr() throws Exception {
        mockMvc.perform(get("/tokens/" + cpr))
                .andExpect(status().isOk());
    }

    @Test
    void generateTokens() throws Exception {
        tokenAmount = 5;
        mockMvc.perform(post("/tokens/" + cpr + "/" + tokenAmount))
                .andExpect(status().isCreated());
    }

    @Test
    void generateNegativeTokens() throws Exception {
        tokenAmount = -1;
        mockMvc.perform(post("/tokens/" + cpr + "/" + tokenAmount))
                .andExpect(status().isBadRequest());
    }

    @Test
    void generateTooManyTokens() throws Exception {
        tokenAmount = 7;
        mockMvc.perform(post("/tokens/" + cpr + "/" + tokenAmount))
                .andExpect(status().isBadRequest());
    }

}