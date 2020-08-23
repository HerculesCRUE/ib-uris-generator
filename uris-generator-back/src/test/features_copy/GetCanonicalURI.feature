#Author: druiz@izertis.com
#Keywords Summary : uris-factory
Feature: Get canonical URI

  Background:
    Given When making a request to the api to get the cannonical URI

  Scenario: We want to obtain a Canonical URI for a entity, with class name defined JSON format
    When A request is made to the end point /uri-factory/canonical/entity with the JSON body:
      """
        {
          "@class": "Person",
          "canonicalClassName" : "Person"
        }
      """
    Then A Canonical URI is returned that complies with what is established in the URIS schema
    And The returned URI is normalized