#Author: druiz@izertis.com
#Keywords Summary : uris-factory
Feature: Actions (associate and disassociate) for Local URI from Property

  Background:
    Given baseUri is http://localhost

  Scenario Outline: POST Local URI Property. The client invokes POST to endpoint /uri-factory/local/property to get response of API
    When  POST Local URI Property to endpoint "/uri-factory/local/property" with domain "hercules.org" and subDomain "um" and languageCode "es-ES" and typeCode "res" and property "property-1" and localUri "http://trellis/concept/Entity-1" and storageName "trellis"
    Then after POST Local URI Property to endpoint /uri-factory/local/property the client receives health status code of 200
    And after POST Local URI Property to endpoint /uri-factory/local/property  the client receives server health valid response
    Examples:
      | endpoint                   | domain       | subDomain | languageCode | typeCode | property   | localUri                        | storageName |
      | /uri-factory/local/entity  | hercules.org | um        | es-ES        | res      | property-1 | http://trellis/concept/Entity-1 | trellis     |

  Scenario Outline: DELETE Local URI Property. The client invokes DELETE to endpoint /uri-factory/local/property to get response of API
    When  DELETE Local URI Property to endpoint "/uri-factory/local/property" with domain "hercules.org" and subDomain "um" and languageCode "es-ES" and typeCode "res" and entity "property-1" and localUri "http://trellis/concept/Entity-1" and storageName "trellis"
    Then after DELETE Local URI Property to endpoint /uri-factory/local/property the client receives health status code of 200
    Examples:
      | endpoint                   | domain       | subDomain | languageCode | typeCode | property   | localUri                        | storageName |
      | /uri-factory/local/entity  | hercules.org | um        | es-ES        | res      | property-1 | http://trellis/concept/Entity-1 | trellis     |