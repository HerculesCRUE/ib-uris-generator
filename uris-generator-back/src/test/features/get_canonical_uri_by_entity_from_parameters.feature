#Author: druiz@izertis.com
#Keywords Summary : uris-factory
Feature: Check the insertion of a entity and get Canonical URI from parameters

  Background:
    Given baseUri is http://localhost


  Scenario Outline: Post Entity for get Canonical URI from parameters. The client invokes the end point /uri-factory/canonical/entity to get the canonical URI passing the entity name in JSON format
    When When post entity the client calls endpoint "/uri-factory/canonical/entity" with domain "hercules.org" ,subDomain "um", language "es-ES" with JSON body
      """
      { "@class": "ConceptoGrupo","canonicalClassName": "ConceptoGrupo"}
      """
    Then after post entity the client receives status code of 200
    And after post entity the client receives server valid response URI from Post Entity Request

    Examples:
      | domain       | subDomain | language | jsonBody                                                           |
      | hercules.org | um        | es-ES    | { "@class": "ConceptoGrupo","canonicalClassName": "ConceptoGrupo"} |


