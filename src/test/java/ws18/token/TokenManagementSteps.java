package ws18.token;

import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.After;
import ws18.exceptions.ExceptionContainer;
import ws18.exceptions.TooManyTokensException;
import ws18.model.Token;
import ws18.service.ITokenManager;
import ws18.service.TokenManager;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class TokenManagementSteps {

    private User currentCustomer;
    private ITokenManager tokenManager;
    private ArrayList<Token> tokensReceived;
    private ExceptionContainer exceptionContainer = new ExceptionContainer();

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
}
