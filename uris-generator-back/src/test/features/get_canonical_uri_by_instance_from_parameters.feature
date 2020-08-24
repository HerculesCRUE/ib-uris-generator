#Author: druiz@izertis.com
#Keywords Summary : uris-factory
Feature: Check the insertion of a instance and get Canonical URI from parameters

  Background:
    Given baseUri is http://localhost

  Scenario Outline: Post Instance. The client invokes the end point /uri-factory/canonical/resource to get the canonical URI passing the entity name in JSON format
    When When post instance the client calls endpoint "/uri-factory/canonical/resource" with domain "hercules.org" ,subDomain "um", language "es-ES" with JSON body
      """
      { "@class": "ConceptoGrupo",
      "entityId": null,
      "version":0,
      "idGrupoInvestigacion":"E0A6-01",
      "numero":5,
      "codTipoConcepto":"DESCRIPTORES"}
      """
    Then after post instance the client receives status code of 200
    And after post instance the client receives server valid response URI from Post Property Request

    Examples:
      | domain       | subDomain | language | jsonBody                                                                                                                                    |
      | hercules.org | um        | es-ES    | { "@class": "ConceptoGrupo", "entityId": null, "version":0, "idGrupoInvestigacion":"E0A6-01", "numero":5, "codTipoConcepto":"DESCRIPTORES"} |