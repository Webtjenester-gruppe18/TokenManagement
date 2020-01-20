package ws18.service;

import ws18.exceptions.TokenUsedException;
import ws18.exceptions.TokenValidationException;
import ws18.exceptions.TooManyTokensException;
import ws18.model.Token;

import java.util.ArrayList;

public interface ITokenManager{

    ArrayList<Token> getTokensByCpr(String cprNumber);
    ArrayList<Token> getUnusedTokensByCpr(String cprNumber);
    Token generateToken(String cprNumber);
    ArrayList<Token> generateTokens(String cprNumber, int amount) throws TooManyTokensException;
    ArrayList<Token> requestForNewTokens(String cprNumber) throws TooManyTokensException;
    void clearUserTokens(String cprNumber);
    Token validateToken(String userCprNumber, Token token) throws TokenValidationException;
    Token useToken(Token token) throws TokenUsedException;
}
