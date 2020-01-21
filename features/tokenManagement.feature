Feature: Token Management Features

  # @author Emil Vinkel, s175107

  Scenario: Successful token generation
    When the service receives a "REQUEST_FOR_NEW_TOKENS" event
    Then tokens are successfully generated
    And the event "TOKEN_GENERATION_RESPONSE_SUCCESS" is broadcast

  Scenario: Failing token generation
    When the service receives a "REQUEST_FOR_NEW_TOKENS" event
    Then tokens are successfully generated
    When the service receives a "REQUEST_FOR_NEW_TOKENS" event again from the same customer
    Then the token generation failed
    And the event "TOKEN_GENERATION_RESPONSE_FAILED" is broadcast

  Scenario: Successful token retrieval
    When the service receives a "RETRIEVE_TOKENS" event
    Then tokens are successfully retrieved
    And the event "RETRIEVE_TOKENS_RESPONSE" is broadcast

  Scenario: Successful token validation
    When the service receives a "TOKEN_VALIDATION_REQUEST" event
    Then the token is successfully validated
    And the event "MONEY_TRANSFER_REQUEST" is broadcast

  Scenario: Failing token validation
    When the service receives a "TOKEN_VALIDATION_REQUEST" event with an invalid token
    Then tokens are not validated
    And the event "TOKEN_VALIDATION_FAILED" is broadcast



