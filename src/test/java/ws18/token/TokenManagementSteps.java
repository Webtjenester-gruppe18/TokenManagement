package ws18.token;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import ws18.messagingutils.EventReceiverImpl;
import ws18.messagingutils.IEventSender;
import ws18.messagingutils.RabbitMQValues;
import ws18.model.Event;
import ws18.model.EventType;
import ws18.model.Token;
import ws18.service.ITokenManager;
import ws18.service.TokenManager;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

@EnableAutoConfiguration
@SpringBootTest
public class TokenManagementSteps {
    private ArrayList<Token> tokensReceived;
    private IEventSender eventSender = mock(IEventSender.class);
    private TokenManager tokenManager = new TokenManager(eventSender);



    @When("the service receives a {string} event")
    public void the_service_receives_a_event(String string) throws Exception {
        Event event = new Event();
        event.setType(EventType.valueOf(string));
        event.setObject("123");
        tokenManager.receiveEvent(event);
    }

    @Then("tokens are successfully generated")
    public void tokens_are_successfully_generated() {
       tokenManager.generateToken("123");
       assertTrue(!tokenManager.getTokensByCpr("123").isEmpty());
    }

    @Then("the event {string} is broadcast")
    public void the_event_is_broadcast(String string) throws Exception {
        ArgumentCaptor<Event> argumentCaptor = ArgumentCaptor.forClass(Event.class);
        verify(eventSender,atLeastOnce()).sendEvent(argumentCaptor.capture());
        assertEquals(EventType.valueOf(string),argumentCaptor.getValue().getType());
    }

    @Then("tokens are not generated")
    public void tokens_are_not_generated() {
        tokenManager.generateToken("123");
        tokenManager.generateToken("123");
        throw new cucumber.api.PendingException();
    }

    @Then("tokens are successfully retrieved")
    public void tokens_are_successfully_retrieved() {
        // Write code here that turns the phrase above into concrete actions
        throw new cucumber.api.PendingException();
    }

    @Then("tokens are not retrieved")
    public void tokens_are_not_retrieved() {
        // Write code here that turns the phrase above into concrete actions
        throw new cucumber.api.PendingException();
    }

    @Then("tokens are successfully validated")
    public void tokens_are_successfully_validated() {
        // Write code here that turns the phrase above into concrete actions
        throw new cucumber.api.PendingException();
    }

    @Then("tokens are not validated")
    public void tokens_are_not_validated() {
        // Write code here that turns the phrase above into concrete actions
        throw new cucumber.api.PendingException();
    }


}
