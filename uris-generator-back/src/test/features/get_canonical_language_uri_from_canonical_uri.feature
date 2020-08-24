#Author: druiz@izertis.com
#Keywords Summary : uris-factory
Feature: Get canonical language URI for Canonical URI in language

  Background:
    Given baseUri is http://localhost

  Scenario Outline: Get Canonical URI Language. The client invokes the end point /uri-factory/canonical/languages to get the canonical Language URI passing the Canonical Language URI
    When When the client calls endpoint GET "/uri-factory/canonical/languages" with canonicalURI "http://hercules.org/um/res/Entity-1" and language "es-ES"
    Then after get request GET /uri-factory/canonical/languages the client receives status code of 200
    And after get request GET /uri-factory/canonical/languages the client receives a valid response URI from Post Property Request

    Examples:
      | canonicalURI                        | subDomain | language |
      | http://hercules.org/um/res/Entity-1 | um        | es-ES    |