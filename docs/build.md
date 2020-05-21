# Compilación

Se indicará a continuación los pasos que hay que seguir para llevar a cabo la generación del artefacto.

## Prerrequisitos

Se precisa disponer los siguientes elementos configurados:

* OpenJDK 11
* Maven 3.6.x

## Compilación

Para realizar la compilación se ejecutará el siguiente comando:

```bash
mvn clean package
```

En caso de querer generar al mismo tiempo Sources el comando siguiente: 

```bash
mvn clean package javadoc:jar source:jar
```

También sería posible instalar o desplegar los artefactos sustituyendo `package` por `install` o `deploy` respectivamente.

Los artefactos se generarán dentro del directorio `target` de cada uno de los módulos:

### Back

Los artefactos se encuentran dentro de uris-generator-back/target

* Artefacto: uris-generator-back-{version}.jar
* Sources: uris-generator-back-{version}-sources.jar

### Service

Los artefactos se encuentran dentro de uris-generator-service/target

* Artefacto: uris-generator-service-{version}.jar
* Sources: uris-generator-service-{version}-sources.jar

### Service Abstractions

Los artefactos se encuentran dentro de triples-storage-adapter-service-abstractions/target

* Artefacto: triples-storage-adapter-service-abstractions-{version}.jar
* Sources: triples-storage-adapter-service-abstractions-{version}-sources.jar

### JPA Abstractions

Los artefactos se encuentran dentro de triples-storage-adapter-jpa-abstractions/target

* Artefacto: triples-storage-adapter-jpa-abstractions-{version}.jar
* Sources: triples-storage-adapter-jpa-abstractions-{version}-sources.jar

### Swagger

Los artefactos se encuentran dentro de triples-storage-adapter-swagger/target

* Artefacto: triples-storage-adapter-swagger-{version}.jar
* Sources: triples-storage-adapter-swagger-{version}-sources.jar
