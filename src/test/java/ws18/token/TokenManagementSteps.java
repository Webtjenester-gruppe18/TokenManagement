package ws18.token;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import ws18.messagingutils.Listener;
import ws18.model.Event;
import ws18.model.EventType;
import ws18.model.Token;
import ws18.service.TokenManager;

import java.util.ArrayList;

import static org.mockito.Mockito.verify;

@EnableAutoConfiguration
@SpringBootTest
public class TokenManagementSteps {
    private ArrayList<Token> tokensReceived;

    private TokenManager tokenManager;
    private Listener listener;
    @Autowired
    public TokenManagementSteps(TokenManager tokenManager, Listener listener){
        this.tokenManager = tokenManager;
        this.listener = listener;
    }

    @When("the service receives a {string} event")
    public void the_service_receives_a_event(String string) {
        Event event = new Event();
        event.setType(EventType.valueOf(string));
        event.setObject("123");
        System.out.println(tokenManager);
        System.out.println(listener);

    }

    @Then("tokens are successfully generated")
    public void tokens_are_successfully_generated() {
        System.out.println(verify(tokenManager).generateToken("123"));
    }

    @Then("the event {string} is broadcast")
    public void the_event_is_broadcast(String string) {
        // Write code here that turns the phrase above into concrete actions
        throw new cucumber.api.PendingException();
    }

    @Then("tokens are not generated")
    public void tokens_are_not_generated() {
        // Write code here that turns the phrase above into concrete actions
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
