Feature: resources can be accessed
  Background:
    Given test resource objects posted

  Scenario: client makes call to GET api/resources
    When the client calls /api/resources
    Then the client receives status code of 200
    And the client receives the resources

#  Scenario: client makes call to GET /api/resources/{resourceId}
#    When the client calls /api/resources/:id
#    Then the client receives status code 200
#    And the client receives the correct resource
#
#
#   Scenario: client makes call to GET /api/resources/language/{languageId}
#     When the client calls /api/resources/language/{languageId}
#     Then the client receives status code of 200
#     And the client receives the correct resources filtered by language


