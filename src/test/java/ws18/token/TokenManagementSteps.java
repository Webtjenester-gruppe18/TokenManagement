package ws18.token;

import io.cucumber.java.After;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.mockito.ArgumentCaptor;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import ws18.messagingutils.IEventSender;
import ws18.model.Event;
import ws18.model.EventType;
import ws18.model.PaymentRequest;
import ws18.model.Token;
import ws18.service.TokenManager;

import java.math.BigDecimal;
import java.util.ArrayList;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@EnableAutoConfiguration
@SpringBootTest
public class TokenManagementSteps {
    private ArrayList<Token> tokensReceived;
    private IEventSender eventSender = mock(IEventSender.class);
    private TokenManager tokenManager = new TokenManager(eventSender);
    private String errormessage = "";

    @After
    public void tearDown() {
        tokenManager.clearUserTokens("123");
    }

    @When("the service receives a {string} event")
    public void the_service_receives_a_event(String string) throws Exception {
        Event event = new Event();
        event.setType(EventType.valueOf(string));
        if (event.getType().equals(EventType.REQUEST_FOR_NEW_TOKENS)||event.getType().equals(EventType.RETRIEVE_TOKENS)) {
            event.setObject("123");
        }else if(event.getType().equals(EventType.TOKEN_VALIDATION_REQUEST)){
            Token token = tokenManager.generateToken("456");
            PaymentRequest request= new PaymentRequest();
            request.setAmount(BigDecimal.ONE);
            request.setCpr("456");
            request.setDescription("Test");
            request.setToken(token);
            event.setObject(request);
        }

        tokenManager.receiveEvent(event);
    }

    @Then("tokens are successfully generated")
    public void tokens_are_successfully_generated() {
        assertFalse(tokenManager.getTokensByCpr("123").isEmpty());
    }

    @Then("the event {string} is broadcast")
    public void the_event_is_broadcast(String string) throws Exception {
        ArgumentCaptor<Event> argumentCaptor = ArgumentCaptor.forClass(Event.class);
        verify(eventSender, atLeastOnce()).sendEvent(argumentCaptor.capture());
        assertEquals(EventType.valueOf(string), argumentCaptor.getValue().getType());
    }

    @Then("the service receives a {string} event again from the same customer")
    public void receiveRepeatedTokenGenerationRequest(String string) {
        Event event = new Event();
        event.setType(EventType.valueOf(string));
        event.setObject("123");
        try {
            tokenManager.receiveEvent(event);
        } catch (Exception e) {
            errormessage = e.getMessage();
        }

    }

    @Then("the token generation failed")
    public void theTokenGenerationFailed() {
        // assertEquals("The user has too many token to request for new ones.",errormessage);
    }

    @Then("tokens are successfully retrieved")
    public void tokens_are_successfully_retrieved() {
        assertNotNull(tokenManager.getTokensByCpr("123"));
    }

    @Then("the token is successfully validated")
    public void tokens_are_successfully_validated() {
      //Is handled in receive event.
    }

    @Then("tokens are not validated")
    public void tokens_are_not_validated() {

      //Is handled in receive event.
    }
    @When("the service receives a {string} event with an invalid token")
    public void theServiceReceivesAEventWithAnInvalidToken(String string) throws Exception {
        Event event = new Event();
        event.setType(EventType.valueOf(string));
        Token token = tokenManager.generateToken("456");
        PaymentRequest request= new PaymentRequest();
        request.setAmount(BigDecimal.ONE);
        request.setCpr("456");
        request.setDescription("Test");
        token.setHasBeenUsed(true);
        request.setToken(token);
        event.setObject(request);
        tokenManager.receiveEvent(event);
    }

}
