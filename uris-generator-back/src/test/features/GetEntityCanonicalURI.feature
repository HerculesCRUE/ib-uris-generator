#Author: druiz@izertis.com
#Keywords Summary : uris-factory
Feature: Get canonical URIS for entities
  Scenario: The client invokes the end point to get the canonical URI passing the entity name in JSON format
    When the client calls endpoint with JSON body
      """
        { "name": "Baeldung", "java": true }
      """
    Then the client receives status code of 200
    And the client receives server version 1.0
