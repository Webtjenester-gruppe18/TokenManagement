Feature: Token Management Features

  Scenario: Successful token generation
    When the service receives a "REQUEST_FOR_NEW_TOKENS" event
    Then tokens are successfully generated
    And the event "TOKEN_GENERATION_SUCCEED" is broadcast

  Scenario: Failing token generation
    When the service receives a "REQUEST_FOR_NEW_TOKENS" event
    Then tokens are successfully generated
    When the service receives a "REQUEST_FOR_NEW_TOKENS" event again from the same customer
    Then the event "TOKEN_GENERATION_FAILED" is broadcast

  Scenario: Successful token retrieval
    When the service receives a "RETRIEVE_TOKENS" event
    Then tokens are successfully retrieved
    And the event "RETRIEVE_TOKENS_SUCCEED" is broadcast

  Scenario: Failing token retrieval
    When the service receives a "RETRIEVE_TOKENS" event
    Then tokens are not retrieved
    And the event "RETRIEVE_TOKENS_FAILED" is broadcast

  Scenario: Successful token validation
    When the service receives a "TOKEN_VALIDATION_REQUEST" event
    Then tokens are successfully validated
    And the event "MONEY_TRANSFER_REQUEST" is broadcast

  Scenario: Failing token validation
    When the service receives a "TOKEN_VALIDATION_REQUEST" event
    Then tokens are not validated
    And the event "TOKEN_VALIDATION_FAILED" is broadcast



