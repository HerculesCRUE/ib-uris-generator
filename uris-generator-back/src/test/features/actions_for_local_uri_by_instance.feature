#Author: druiz@izertis.com
#Keywords Summary : uris-factory
Feature: Actions (associate and disassociate) for Local URI from Instance

  Background:
    Given baseUri is http://localhost

  Scenario Outline: POST Local URI Instance. The client invokes POST to endpoint /uri-factory/local/resource to get response of API
    When  POST Local URI Instance to endpoint "/uri-factory/local/resource" with domain "hercules.org" and subDomain "um" and languageCode "es-ES" and typeCode "res" and entity "Entity-2" and reference "instance-1" and localUri "http://trellis/concept/Entity-1" and storageName "trellis"
    Then after POST Local URI Instance to endpoint /uri-factory/local/resource the client receives health status code of 200
    And after POST Local URI Instance to endpoint /uri-factory/local/resource  the client receives server health valid response

    Examples:
      | endpoint                   | domain       | subDomain | languageCode | typeCode | entity   | reference    | localUri                        | storageName |
      | /uri-factory/local/entity  | hercules.org | um        | es-ES        | res      | Entity-1 | instance-1   | http://trellis/concept/Entity-1 | trellis     |

  Scenario Outline: DELETE Local URI Instance. The client invokes DELETE to endpoint /uri-factory/local/resource to get response of API
    When  DELETE Local URI Instance to endpoint "/uri-factory/local/resource" with domain "hercules.org" and subDomain "um" and languageCode "es-ES" and typeCode "res" and entity "Entity-2" and reference "instance-1" and localUri "http://trellis/concept/Entity-1" and storageName "trellis"
    Then after DELETE Local URI Instance to endpoint /uri-factory/local/resource the client receives health status code of 200

    Examples:
      | endpoint                   | domain       | subDomain | languageCode | typeCode | entity   | reference    | localUri                        | storageName |
      | /uri-factory/local/entity  | hercules.org | um        | es-ES        | res      | Entity-1 | instance-1   | http://trellis/concept/Entity-1 | trellis     |