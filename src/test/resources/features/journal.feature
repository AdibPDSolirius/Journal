Feature: resources can be retrieved
  Scenario: client makes call to GET /resources
    When the client calls /resources
    Then the client receives status code of 200
    And the client receives the resources
