package ws18.token;

import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.After;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ws18.exceptions.ExceptionContainer;
import ws18.exceptions.TokenUsedException;
import ws18.exceptions.TooManyTokensException;
import ws18.messagingutils.Listener;
import ws18.model.Event;
import ws18.model.Token;
import ws18.service.ITokenManager;
import ws18.service.TokenManager;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

@SpringBootTest
public class TokenManagementSteps {

    private User currentCustomer;
    private ITokenManager tokenManager;
    private ArrayList<Token> tokensReceived;
    private ExceptionContainer exceptionContainer = new ExceptionContainer();
    private Token token;
    private Listener listener;
    @Autowired
    TokenManagementSteps(Listener listener) {
        this.listener = listener;
}

    @Before
    public void setUp() {
        this.tokenManager = new TokenManager();
    }

    @Given("the customer is registered")
    public void theCustomerIsRegistered() {



        User customer = new User();
        customer.setCprNumber("991199-0000");
        customer.setFirstName("Jane");
        customer.setLastName("Doe");

        this.currentCustomer = customer;
    }

    @Given("the customer has no more than {int} unused token left")
    public void theCustomerHasNotMoreThanUnusedTokenLeft(Integer tokensLeft) {
        try {
            this.tokenManager.generateTokens(this.currentCustomer.getCprNumber(), tokensLeft);
        } catch (TooManyTokensException e) {
            this.exceptionContainer.setErrorMessage(e.getMessage());
        }
    }

    @When("the customer requests more tokens")
    public void theCustomerRequestsMoreTokens() {
        try {
            this.tokensReceived = this.tokenManager.requestForNewTokens(this.currentCustomer.getCprNumber());
        } catch (TooManyTokensException e) {
            this.exceptionContainer.setErrorMessage(e.getMessage());
        }
    }

    @Then("the customer receives {int} new unused tokens")
    public void theCustomerReceivesNewUnusedTokens(Integer amountOfReceivedTokens) {
        assertEquals(Integer.valueOf(this.tokensReceived.size()), amountOfReceivedTokens);
    }

    @Then("then has {int} unused tokens")
    public void thenHasUnusedTokens(Integer amountOfTokensAttachedToTheUserAccount) {
        assertEquals(Integer.valueOf(this.tokenManager.getTokensByCpr(this.currentCustomer.getCprNumber()).size()), amountOfTokensAttachedToTheUserAccount);
    }

    @Given("the customer has atleast {int} unused token left")
    public void theCustomerHasAtleastUnusedTokenLeft(Integer amountOfTokens) {
        try {
            this.tokenManager.requestForNewTokens(this.currentCustomer.getCprNumber());
        } catch (TooManyTokensException e) {
            this.exceptionContainer.setErrorMessage(e.getMessage());
        }
    }

    @Then("the customer gets a error message saying {string}")
    public void theCustomerGetsAErrorMessageSaying(String errorMessage) {
        assertEquals(this.exceptionContainer.getErrorMessage(), errorMessage);
    }

    @After
    public void tearDown() {
        this.tokenManager.clearUserTokens(this.currentCustomer.getCprNumber());
    }

    @When("the customer uses a token")
    public void theCustomerUsesAToken() {
        String errorMsg = null;
        this.token = this.tokenManager.getUnusedTokensByCpr(this.currentCustomer.getCprNumber()).get(0);
        try {
            this.tokenManager.useToken(token);
        } catch (TokenUsedException e) {
            errorMsg = e.getMessage();
        }
        Assert.assertNull(errorMsg);
    }

    @And("the customer uses the same token again")
    public void theCustomerUsesTheSameTokenAgain() {
        try {
            this.tokenManager.useToken(token);
        } catch (TokenUsedException e) {
            this.exceptionContainer.setErrorMessage(e.getMessage());
        }
    }
}
