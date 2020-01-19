package ws18.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TokenControllerTest {

    private String cpr = "112233-4455";
    private int tokenAmount;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getTokensByCpr() throws Exception {
        mockMvc.perform(get("/tokens/" + cpr))
                .andExpect(status().isOk());
    }

    @Test
    void generateTokens() throws Exception {
        mockMvc.perform(post("/tokens/" + cpr))
                .andExpect(status().isOk());
    }

}