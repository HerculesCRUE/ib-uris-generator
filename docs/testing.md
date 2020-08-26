![](../images/logos_feder.png)

# Testing BDD con Cucumber

## Configuración del entorno

Ver documento [README](https://github.com/HerculesCRUE/ib-asio-docs-/blob/master/common/testing/testing.md) para la configuración de los tests.

## Escenarios

A continuación se describen los escenarios probados, utilizando el framework [Cucumber](https://cucumber.io/docs/cucumber/)

| Feature                                                     | Descripción                                                                                                                                          |
| ----------------------------------------------------------- | ---------------------------------------------------------------------------------------------------------------------------------------------------- |
| [`actions_for_local_uri_by_entity.feature`](../src/test/features/multilanguage.feature) | Pruebas sobre la generación de RDF en distintos lenguajes  
| [`actions_for_local_uri_by_instance.feature`](../src/test/features/pojo-etl.feature)           | Pruebas sobre la generación de RDF procedentes de **ETL**                                                                                            
| [`actions_for_local_uri_by_property.feature`](../src/test/features/pojo-xml.feature)           | Pruebas sobre la generación de RDF procedentes de ficheros importados **xml**                                                                        |
| [`get_canonical_language_uri_from_canonical_uri.feature`](../src/test/features/pojo-xml.feature)           | Pruebas sobre la generación de RDF procedentes de ficheros importados **xml**                                                                        |
| [`get_canonical_uri_by_entity_from_parameters.feature`](../src/test/features/pojo-xml.feature)           | Pruebas sobre la generación de RDF procedentes de ficheros importados **xml**                                                                        |
| [`get_canonical_uri_by_instance_from_parameters.feature`](../src/test/features/pojo-xml.feature)           | Pruebas sobre la generación de RDF procedentes de ficheros importados **xml**                                                                        |
| [`get_canonical_uri_by_property_from_parameters.feature`](../src/test/features/pojo-xml.feature)           | Pruebas sobre la generación de RDF procedentes de ficheros importados **xml**                                                                        |
| [`link_and_unlink_local_uri.feature`](../src/test/features/pojo-xml.feature)           | Pruebas sobre la generación de RDF procedentes de ficheros importados **xml**                                                                        |

