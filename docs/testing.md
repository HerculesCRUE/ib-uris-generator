![](../images/logos_feder.png)

# Testing BDD con Cucumber

## Configuración del entorno

Ver documento [README](https://github.com/HerculesCRUE/ib-asio-docs-/blob/master/hito_2/testing/testing.md) para la configuración de los tests.

## Escenarios

A continuación se describen los escenarios probados, utilizando el framework [Cucumber](https://cucumber.io/docs/cucumber/)

| Feature                                                                                                                                                   | Descripción                                                                                               |
| --------------------------------------------------------------------------------------------------------------------------------------------------------- | --------------------------------------------------------------------------------------------------------- |
| [`actions_for_local_uri_by_entity.feature`](../uris-generator-back/src/test/features/actions_for_local_uri_by_entity.feature)                             | Pruebas sobre asociación / desasociación para uris locales de entidades                                   |
| [`actions_for_local_uri_by_instance.feature`](../uris-generator-back/src/test/features/actions_for_local_uri_by_instance.feature)                         | Pruebas sobre asociación / desasociación para uris locales de instancias                                  |
| [`actions_for_local_uri_by_property.feature`](../uris-generator-back/src/test/features/actions_for_local_uri_by_property.feature)                         | Pruebas sobre asociación / desasociación para uris locales de propiedades                                 |
| [`get_canonical_language_uri_from_canonical_uri.feature`](../uris-generator-back/src/test/features/get_canonical_language_uri_from_canonical_uri.feature) | Pruebas sobre uris canónicas                                                                              |
| [`get_canonical_uri_by_entity_from_parameters.feature`](../uris-generator-back/src/test/features/get_canonical_uri_by_entity_from_parameters.feature)     | Pruebas sobre la inserción de entidades y obtención de uris canónicas a partir de parámetros de entrada   |
| [`get_canonical_uri_by_instance_from_parameters.feature`](../uris-generator-back/src/test/features/get_canonical_uri_by_instance_from_parameters.feature) | Pruebas sobre la inserción de instancias y obtención de uris canónicas a partir de parámetros de entrada  |
| [`get_canonical_uri_by_property_from_parameters.feature`](../uris-generator-back/src/test/features/get_canonical_uri_by_property_from_parameters.feature) | Pruebas sobre la inserción de propiedades y obtención de uris canónicas a partir de parámetros de entrada |
| [`link_and_unlink_local_uri.feature`](../uris-generator-back/src/test/features/link_and_unlink_local_uri.feature)                                         | Pruebas sobre enlazar o desenlazar uris canónicas por o sin lenguaje                                      |
