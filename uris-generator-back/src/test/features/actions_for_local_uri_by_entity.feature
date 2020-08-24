#Author: druiz@izertis.com
#Keywords Summary : uris-factory
Feature: Actions (associate and disassociate) for Local URI from Entity

  Background:
    Given baseUri is http://localhost

  Scenario Outline: POST Local URI Entity. The client invokes POST to endpoint /uri-factory/local/entity to get response of API
    When  POST Local URI Entity to endpoint "/uri-factory/local/entity" with domain "hercules.org" and subDomain "um" and languageCode "es-ES" and typeCode "res" and entity "Entity-1" and localUri "http://trellis/concept/Entity-1" and storageName "trellis"
    Then after POST Local URI Entity to endpoint /uri-factory/local/entity the client receives health status code of 200
    And after POST Local URI Entity to endpoint /uri-factory/local/entity  the client receives server health valid response

    Examples:
      | endpoint                   | domain       | subDomain | languageCode | typeCode | entity   | localUri                        | storageName |
      | /uri-factory/local/entity  | hercules.org | um        | es-ES        | res      | Entity-1 | http://trellis/concept/Entity-1 | trellis     |

  Scenario Outline: DELETE Local URI Entity. The client invokes DELETE to endpoint /uri-factory/local/entity to get response of API
    When  DELETE Local URI Entity to endpoint "/uri-factory/local/entity" with domain "hercules.org" and subDomain "um" and languageCode "es-ES" and typeCode "res" and entity "Entity-1" and localUri "http://trellis/concept/Entity-1" and storageName "trellis"
    Then after DELETE Local URI Entity to endpoint /uri-factory/local/entity the client receives health status code of 200

    Examples:
      | endpoint                   | domain       | subDomain | languageCode | typeCode | entity   | localUri                        | storageName |
      | /uri-factory/local/entity  | hercules.org | um        | es-ES        | res      | Entity-1 | http://trellis/concept/Entity-1 | trellis     |