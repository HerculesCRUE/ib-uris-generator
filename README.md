# URIs generator service
EndPoint API Rest para operaciones CRUD sobre entidades y EndPoint de factoría de URIs, con base de datos relacional.

## OnBoarding

Para iniciar el entorno de desarrollo se necesita cumplir los siguientes requisitos:

* OpenJDK 11 (en caso de querer JDK8: Oracle JDK 8)
* Eclipse JEE 2019-09 con plugins:
** Spring Tools 4
** m2e-apt
** Lombok
* Docker

### Instalar Lombok

Para la instalación de Lombok, es preciso descargar la última versión desde [https://projectlombok.org/download](https://projectlombok.org/download). Se descargará un jar que precisa ser ejecutado:

	java -jar lombok.jar

Se seleccionará la ubicación en la que se encuentra instalado Eclipse.

En caso que de problemas a la hora de generar las clases de Mapstruct, es preciso utilizar una versión parcheada de lombok. Para ello, se ha dejado en \\rackstation\Desarrollo\fuentes\Entorno de desarrollo\Eclipses el fichero lombok-patched-1.18.6.jar. Se deberá configurar en el fichero eclipse.ini, sustituyendo el jar que tiene configurado actualmente por el parcheado

```
-javaagent:C:\desarrollo\java\install\eclipse-jee-2018-12-R-win32-x86_64\lombok-patched-1.18.6.jar
```

## Inicialización de la base de datos

La inicialización de la base de datos y solr se realiza con docker. En primer lugar es preciso modificar el fichero ```docker-devenv\.env``` y actualizar la variable de entorno ```COMPOSE_PROJECT_NAME```

En el directorio docker-devenv se ha configurado un fichero docker-compose.yml para poder arrancar el entorno de desarrollo. Actualmente contiene los siguientes elementos:

* Postgre 11.1

En caso de querer cambiar una versión o añadir un nuevo elemento al docker-compose, se puede buscar la imagen apropiada en https://hub.docker.com/

Se pueden eliminar los elementos que no se vayan a utilizar.

Para arrancar el entorno:

	docker-compose up -d

Para pararlo:

	docker-compose down

## Metodología de desarrollo

La metodología de desarrollo es Git Flow.

## Swagger

Todos los endPoint se encuentran desplegados en Swagger:

http://localhost:8080/swagger-ui.html#/

## Modelo de datos

 ![model](./images/model_data.png)

 ## Mapeo de URIs

El mapeo de URIS sigue el siguiente esquema, donde cada URI canónica se mapea a n URIS en distintos idiomas y cada URI canónica en un determinado idioma a las URIs locales (1 por almacenamiento)



 ![mapper_url](./images/multi_languege_map_language.png)

  

 
