Feature: ABC Company Customer API
  The ABC Company Customer API consists of 2 endpoints:
  - /register let's a new customer register and returns credentials (username and password)
  - /logon let's a registered customer logon and returns a authorization token

  Scenario:
    Given a valid request
    When a client calls the register customer endpoint
    Then the client should receive credentials
    And the customer api should create an entry in the database
    And the credentials should match the credentials in the database
    And the customer api should create an IBAN in the database