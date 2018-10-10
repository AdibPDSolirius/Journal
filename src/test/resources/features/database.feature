Feature: databases can be created, retrieved, updated

  Scenario: client retrieves all databases
    Given test database objects posted
    When the client calls /api/databases
    Then the client receives database status code of 200
    And the client receives the databases

  Scenario: client retrieves requested database
    Given single test database object posted
    When the client makes a GET database request
    Then the client receives database status code of 200
    And the client receives the correct database

  Scenario: client adds a database
    When the client makes a POST database request
    Then the client receives database status code of 201
    And the database is added to the database
    And the client receives the correct added database

  Scenario: client updates a database
    Given single test database object posted
    When the client makes a PUT database request with a new database
    Then the client receives database status code of 200
    And the database is updated in the database
    And the client receives the correct database




