package ws18.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import ws18.database.ITokenDatabase;
import ws18.database.InMemoryTokenDatabase;
import ws18.exceptions.TokenUsedException;
import ws18.exceptions.TokenValidationException;
import ws18.exceptions.TooManyTokensException;
import ws18.messagingutils.IEventReceiver;
import ws18.messagingutils.IEventSender;
import ws18.messagingutils.RabbitMQValues;
import ws18.model.Event;
import ws18.model.EventType;
import ws18.model.PaymentRequest;
import ws18.model.Token;

import java.util.ArrayList;

/**
 * @author Oliver Køppen, s175108
 */

public class TokenManager implements ITokenManager, IEventReceiver {

    private final ObjectMapper objectMapper;
    private ITokenDatabase tokenDatabase = new InMemoryTokenDatabase();
    private IEventSender eventSender;

    public TokenManager(IEventSender eventSender) {
        this.objectMapper = new ObjectMapper();
        this.eventSender = eventSender;
    }

    @Override
    public ArrayList<Token> getTokensByCpr(String cprNumber) {
        return this.tokenDatabase.getTokensByCpr(cprNumber);
    }

    @Override
    public ArrayList<Token> getUnusedTokensByCpr(String cprNumber) {
        ArrayList<Token> result = new ArrayList<>();

        for (Token token : this.getTokensByCpr(cprNumber)) {
            if (!token.isHasBeenUsed()) {
                result.add(token);
            }
        }

        return result;
    }

    @Override
    public Token generateToken(String cprNumber) {

        Token token = new Token(cprNumber);

        this.tokenDatabase.saveToken(token);

        return token;
    }

    @Override
    public ArrayList<Token> generateTokens(String cprNumber, int amount) throws TooManyTokensException {
        ArrayList<Token> result = new ArrayList<>();

        if (getTokensByCpr(cprNumber).size() > 1) {
            throw new TooManyTokensException("The user has too many token to request for new ones.");
        }

        for (int i = 0; i < amount; i++) {
            result.add(generateToken(cprNumber));
        }

        return result;
    }

    @Override
    public ArrayList<Token> requestForNewTokens(String cprNumber) throws TooManyTokensException {

        ArrayList<Token> userTokens = getTokensByCpr(cprNumber);

        int amountOfTokensToRequestForNewOnes = 1;
        if (userTokens.size() > amountOfTokensToRequestForNewOnes) {
            throw new TooManyTokensException("The user has too many token to request for new ones.");
        }

        int maxAmountOfTokens = 6;
        return generateTokens(cprNumber, maxAmountOfTokens - userTokens.size());
    }

    @Override
    public void clearUserTokens(String cprNumber) {
        for (Token token : this.tokenDatabase.getTokensByCpr(cprNumber)) {
            this.tokenDatabase.getAllTokens().remove(token);
        }
    }

    @Override
    public Token validateToken(String userCprNumber, Token token) throws TokenValidationException {
        if (isTokenFake(userCprNumber, token) || token.isHasBeenUsed()) {
            throw new TokenValidationException("The token is not valid.");
        }

        return token;
    }

    @Override
    public Token useToken(Token token) throws TokenUsedException {
        for (Token tokenInc : this.tokenDatabase.getAllTokens()) {
            if (tokenInc.equals(token) && !tokenInc.isHasBeenUsed()) {
                tokenInc.setHasBeenUsed(true);
                return tokenInc;
            }
        }
        throw new TokenUsedException("The token has already been used.");
    }

    private boolean isTokenFake(String userCprNumber, Token token) {

        ArrayList<Token> tokens = this.getTokensByCpr(userCprNumber);

        for (Token t : tokens) {
            if (t.getValue().equals(token.getValue())) {
                return false;
            }
        }

        return true;
    }

    @Override
    public void receiveEvent(Event event) throws Exception {

        if (event.getType().equals(EventType.TOKEN_VALIDATION_REQUEST)) {
            PaymentRequest paymentRequest = objectMapper.convertValue(event.getObject(), PaymentRequest.class);

            try {
                validateToken(paymentRequest.getCpr(), paymentRequest.getToken());
            } catch (TokenValidationException e) {
                Event response = new Event(EventType.TOKEN_VALIDATION_FAILED, e.getMessage(), RabbitMQValues.DTU_SERVICE_ROUTING_KEY);
                eventSender.sendEvent(response);
                return;
            }

            event.setType(EventType.MONEY_TRANSFER_REQUEST);
            event.setRoutingKey(RabbitMQValues.PAYMENT_SERVICE_ROUTING_KEY);
            eventSender.sendEvent(event);
        } else if (event.getType().equals(EventType.REQUEST_FOR_NEW_TOKENS)) {
            String cpr = objectMapper.convertValue(event.getObject(), String.class);

            try {
                requestForNewTokens(cpr);
            } catch (TooManyTokensException e) {
                Event response = new Event(EventType.TOKEN_GENERATION_RESPONSE_FAILED, e.getMessage(), RabbitMQValues.DTU_SERVICE_ROUTING_KEY);
                eventSender.sendEvent(response);
                return;
            }

            Event successResponse = new Event(EventType.TOKEN_GENERATION_RESPONSE_SUCCESS, EventType.TOKEN_GENERATION_RESPONSE_SUCCESS, RabbitMQValues.DTU_SERVICE_ROUTING_KEY);
            eventSender.sendEvent(successResponse);

        } else if (event.getType().equals(EventType.RETRIEVE_TOKENS)) {
            String cpr = objectMapper.convertValue(event.getObject(), String.class);
            ArrayList<Token> tokens = getUnusedTokensByCpr(cpr);
            Event successResponse = new Event(EventType.RETRIEVE_TOKENS_RESPONSE, tokens, RabbitMQValues.DTU_SERVICE_ROUTING_KEY);
            eventSender.sendEvent(successResponse);
        }
    }
}
