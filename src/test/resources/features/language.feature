Feature: languages can be created, retrieved, updated

  Scenario: client retrieves all languages
    Given test language objects posted
    When the client calls /api/languages
    Then the client receives language status code of 200
    And the client receives the languages

  Scenario: client retrieves requested language
    Given single test language object posted
    When the client makes a GET language request
    Then the client receives language status code of 200
    And the client receives the correct language

  Scenario: client adds a language
    When the client makes a POST language request
    Then the client receives language status code of 201
    And the language is added to the database
    And the client receives the correct added language

  Scenario: client updates a language
    Given single test language object posted
    When the client makes a PUT language request with a new language
    Then the client receives language status code of 200
    And the language is updated in the database
    And the client receives the correct language