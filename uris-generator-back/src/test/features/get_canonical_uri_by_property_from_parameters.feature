#Author: druiz@izertis.com
#Keywords Summary : uris-factory
Feature: Check the insertion of a property and get Canonical URI from parameters

  Background:
    Given baseUri is http://localhost

  Scenario Outline: Post Property. The client invokes the end point /uri-factory/canonical/property to get the canonical URI passing the entity name in JSON format
    When When post property the client calls endpoint "/uri-factory/canonical/property" with domain "hercules.org" ,subDomain "um", language "es-ES" with JSON body
      """
      { "property": "idGrupoInvestigacion","canonicalProperty": "idGrupoInvestigacion"}
      """
    Then after post property the client receives status code of 200
    And after post property the client receives server valid response URI from Post Property Request
    Examples:
      | domain       | subDomain | language | jsonBody                                                                                                                                    |
      | hercules.org | um        | es-ES    | { "property": "idGrupoInvestigacion","canonicalProperty": "idGrupoInvestigacion"} |
