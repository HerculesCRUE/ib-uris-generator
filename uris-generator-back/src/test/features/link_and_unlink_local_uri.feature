#Author: druiz@izertis.com
#Keywords Summary : uris-factory
Feature: Linking and unlinking local URIs to Canonical URIs (by language or not)

  Background:
    Given baseUri is http://localhost

  Scenario Outline: GET Local URI. The client invokes GET to endpoint /uri-factory/local to get response of API
    When  GET Local URI to endpoint "/uri-factory/local" with localUri "http://trellis/concept/Entity-1"
    Then after GET Local URI to endpoint /uri-factory/local the client receives health status code of 200
    And after GET Local URI to endpoint /uri-factory/local  the client receives server health valid response
    Examples:
      | endpoint                   | localUri                         |
      | /uri-factory/local         | http://trellis/concept/Entity-1" |

  Scenario Outline: POST Local URI. The client invokes POST to endpoint /uri-factory/local to get response of API
    When POST Local URI to endpoint "/uri-factory/local" to link Canonical URI Language "http://hercules.org/um/es-ES/rec/Entity-1" to localUri "http://trellis/concept/Entity-1" in storageName "trellis"
    Then after POST Local URI to endpoint /uri-factory/local the client receives health status code of 200
    And after POST Local URI to endpoint /uri-factory/local  the client receives server health valid response
    Examples:
      | endpoint                   | Canonical URI Language                     | localUri                         | storageName |
      | /uri-factory/local         | http://hercules.org/um/es-ES/rec/Entity-1  | http://trellis/concept/Entity-1" | trellis     |

  Scenario Outline: DELETE Local URI. The client invokes DELETE to endpoint /uri-factory/local to get response of API
    When DELETE Local URI to endpoint "/uri-factory/local" to unlink Canonical URI Language "http://hercules.org/um/es-ES/rec/Entity-1" to localUri "http://trellis/concept/Entity-1" in storageName "trellis"
    Then after DELETE Local URI to endpoint /uri-factory/local the client receives health status code of 200
    Examples:
      | endpoint                   | Canonical URI Language                     | localUri                        | storageName |
      | /uri-factory/local         | http://hercules.org/um/es-ES/rec/Entity-1  | http://trellis/concept/Entity-1"| trellis     |

  Scenario Outline: GET Local URI from Canonical URI. The client invokes GET to endpoint /uri-factory/local/canonical to get response of API
    When  GET Local URI from Canonical URI to endpoint "/uri-factory/local/canonical" with canonicalUri "http://hercules.org/um/res/Entity-1" and languageCode "es-ES" and storageName "trellis"
    Then after GET Local URI from Canonical URI to endpoint /uri-factory/local/canonical the client receives health status code of 200
    And after GET Local URI from Canonical URI to endpoint /uri-factory/local/canonical the client receives server health valid response
    Examples:
      | endpoint                             | Canonical URI Language                     | languageCode  | storageName |
      | /uri-factory/local/canonical         | http://hercules.org/um/es-ES/rec/Entity-1  | es-ES         | trellis     |

  Scenario Outline: GET Local URI from Canonical Language URI. The client invokes GET to endpoint /uri-factory/local/canonical/language to get response of API
    When  GET Local URI from Canonical Language URI to endpoint "/uri-factory/local/canonical/language" with canonicalLanguageUri "http://hercules.org/um/es-ES/rec/Entity-1" and languageCode "es-ES" and storageName "trellis"
    Then after GET Local URI from Canonical Language URI to endpoint /uri-factory/local/canonical/language the client receives health status code of 200
    And after GET Local URI from Canonical Language URI to endpoint /uri-factory/local/canonical/language the client receives server health valid response
    Examples:
      | endpoint                              | Canonical URI Language                     | languageCode  | storageName |
      | /uri-factory/local/canonical/language | http://hercules.org/um/es-ES/rec/Entity-1  | es-ES         | trellis     |