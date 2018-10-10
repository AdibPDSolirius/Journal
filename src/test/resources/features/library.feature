Feature: libraries can be created, retrieved, updated

  Scenario: client retrieves all libraries
    Given test library objects posted
    When the client calls /api/libraries
    Then the client receives library status code of 200
    And the client receives the libraries

  Scenario: client retrieves requested library
    Given single test library object posted
    When the client makes a GET library request
    Then the client receives library status code of 200
    And the client receives the correct library

  Scenario: client adds a library
    When the client makes a POST library request
    Then the client receives library status code of 201
    And the library is added to the database
    And the client receives the correct added library

  Scenario: client updates a library
    Given single test library object posted
    When the client makes a PUT library request with a new library
    Then the client receives library status code of 200
    And the library is updated in the database
    And the client receives the correct library