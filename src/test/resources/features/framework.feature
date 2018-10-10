Feature: frameworks can be created, retrieved, updated

  Scenario: client retrieves all frameworks
    Given test framework objects posted
    When the client calls /api/frameworks
    Then the client receives framework status code of 200
    And the client receives the frameworks

  Scenario: client retrieves requested framework
    Given single test framework object posted
    When the client makes a GET framework request
    Then the client receives framework status code of 200
    And the client receives the correct framework

  Scenario: client adds a framework
    When the client makes a POST framework request
    Then the client receives framework status code of 201
    And the framework is added to the database
    And the client receives the correct added framework

  Scenario: client updates a framework
    Given single test framework object posted
    When the client makes a PUT framework request with a new framework
    Then the client receives framework status code of 200
    And the framework is updated in the database
    And the client receives the correct framework




