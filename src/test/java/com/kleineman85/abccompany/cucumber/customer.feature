Feature: ABC Company Customer API
  The ABC Company Customer API consists of 2 endpoints:
  - /register let's a new customer register and returns credentials (username and password)
  - /logon let's a registered customer logon and returns a authorization token

  Scenario: happy flow - register new customer
    Given "a valid" request
    When a client calls the register customer endpoint with username "kleineman85"
    Then the client should receive "successful" response "200 OK"
    And the client should receive credentials
    And the customer api should create an entry in the database
    And the credentials should match the credentials in the database
    And the customer api should create an IBAN in the database

  Scenario: non happy flow - user already exists
    Given "an invalid" request
    And the username "usernameAlreadyTaken" already exists
    When a client calls the register customer endpoint with username "usernameAlreadyTaken"
    Then the client should receive "error" response "400 BAD_REQUEST"
    And the client should receive an AbcErrorResponse with error message "Invalid username: username already exists"
