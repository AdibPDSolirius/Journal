Feature: resources can be created, retrieved, updated and filtered

  Scenario: client retrieves all resources
    Given test resource objects posted
    When the client calls /api/resources
    Then the client receives resource status code of 200
    And the client receives the resources

  Scenario: client retrieves requested resource
    Given single test resource object posted
    When the client makes a GET resource request
    Then the client receives resource status code of 200
    And the client receives the correct resource

  Scenario: client adds a resource
    When the client makes a POST resource request
    Then the client receives resource status code of 201
    And the resource is added to the database
    And the client receives the correct added resource

  Scenario: client updates a resource
    Given single test resource object posted
    When the client makes a PUT resource request with a new resource
    Then the client receives resource status code of 200
    And the resource is updated in the database
    And the client receives the correct resource

  Scenario: client filters a resource by database
    Given test resource objects with databases posted
    When the client makes a GET resource request with a database ID
    Then the client receives resource status code of 200
    And the client receives the correctly database filtered resources

  Scenario: client filters a resource by framework
    Given test resource objects with frameworks posted
    When the client makes a GET resource request with a framework ID
    Then the client receives resource status code of 200
    And the client receives the correctly framework filtered resources

  Scenario: client filters a resource by language
    Given test resource objects with languages posted
    When the client makes a GET resource request with a language ID
    Then the client receives resource status code of 200
    And the client receives the correctly language filtered resources

  Scenario: client filters a resource by library
    Given test resource objects with libraries posted
    When the client makes a GET resource request with a library ID
    Then the client receives resource status code of 200
    And the client receives the correctly library filtered resources



