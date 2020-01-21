package ws18.database;

import ws18.model.Token;

import java.util.ArrayList;

/**
 * @author Oliver Køppen, s175108
 */

public interface ITokenDatabase {

    void saveToken(Token token);
    void saveSetOfTokens(ArrayList<Token> tokens);
    Token getTokenByTokenValue(String tokenValue);
    ArrayList<Token> getTokensByCpr(String cpr);
    ArrayList<Token> getAllTokens();
}
