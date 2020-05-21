

![](./images/logos_feder.png)

| Entregable     | Librería factoria de URIs                                    |
| -------------- | ------------------------------------------------------------ |
| Fecha          | 25/05/2020                                                   |
| Proyecto       | [ASIO](https://www.um.es/web/hercules/proyectos/asio) (Arquitectura Semántica e Infraestructura Ontológica) en el marco de la iniciativa [Hércules](https://www.um.es/web/hercules/) para la Semántica de Datos de Investigación de Universidades que forma parte de [CRUE-TIC](http://www.crue.org/SitePages/ProyectoHercules.aspx) |
| Módulo         | Arquitectura Semántica                                       |
| Tipo           | Software                                                     |
| Objetivo       | Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed lobortis  lacinia volutpat. Nullam at posuere velit, vitae feugiat purus. Vivamus a eros molestie, hendrerit mauris sit amet, varius odio. Suspendisse  metus neque, ultricies et neque et, sollicitudin condimentum nibh.  Curabitur erat mauris, ultricies nec consectetur nec, feugiat nec nisi.  In tincidunt, enim ut rutrum pharetra, metus dui commodo ex, in  venenatis erat diam ut massa. |
| Estado         | **XX%** Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed lobortis  lacinia volutpat. Nullam at posuere velit, vitae feugiat purus. Vivamus a eros molestie, hendrerit mauris sit amet, varius odio. Suspendisse  metus neque, ultricies et neque et, sollicitudin condimentum nibh. |
| Próximos pasos | Curabitur erat mauris, ultricies nec consectetur nec, feugiat nec nisi.  In tincidunt, enim ut rutrum pharetra, metus dui commodo ex, in  venenatis erat diam ut massa. Suspendisse tempus, sapien eu blandit  volutpat, massa mauris rutrum justo, scelerisque tincidunt nisi risus id dui. |
| Documentación  | [Manual de usuario]()<br />[Manual de despliegue]()<br />[Documentación técnica]() |

# ASIO - URIs generator service

EndPoint API Rest para operaciones CRUD sobre entidades y EndPoint de factoría de URIs, con base de datos relacional.

## OnBoarding

Para iniciar el entorno de desarrollo se necesita cumplir los siguientes requisitos:

* OpenJDK 11
* Eclipse JEE 2019-09 con plugins:
  * Spring Tools 4
  * m2e-apt
  * Lombok
* Docker

## Módulos disponibles

* **Módulo back**: módulo que añade una capa de servicios REST a la funcionalidad de la aplicación. Genera un artefacto JAR bootable
* **Módulo service**: módulo que contiene la lógica de la aplicación. Puede ser utilizado como librería independiente para ser integrado en otras aplicaciones
* **Módulo jpa-abstractions**: módulo con utilidades para el acceso a datos mediante JPA
* **Módulo service-abstractions**: módulo con utilidades para la generación de servicios
* **Módulo swagger**: módulo que contine la funcionalidad necesaria para añadir Swagger para la interacción con el API Rest
* **Módulo audit**: módulo que contiena la funcionalidad necesaria para la generación de datos de auditoría para las tablas de base de datos

## Metodología de desarrollo

La metodología de desarrollo es Git Flow.

## Entorno de desarrollo Docker

La inicialización de los elementos adicionales al entorno de desarrollo se realiza con docker. 

En el directorio docker-devenv se ha configurado un fichero docker-compose.yml para poder arrancar el entorno de desarrollo.

Para arrancar el entorno:

```bash
docker-compose up -d
```

Para pararlo:

```bash
docker-compose down
```

## Variables de entorno

La configuración se encuentra en el fichero application.yml

Esta puede ser sustituida por las siguentes variables de entorno

| Nombre | Valor |
|--------|:-----:|
| `APP_PERSISTENCE_DATASOURCE_DRIVER-CLASS-NAME` | org.mariadb.jdbc.Driver  |
| `APP_PERSISTENCE_DATASOURCE_USERNAME` | app  |
| `APP_PERSISTENCE_DATASOURCE_PASSWORD` | sqlpass  |
| `APP_PERSISTENCE_DATASOURCE_URL` | jdbc:mariadb://127.0.0.1:3307/app?ssl=false  |

## Ejecución

Al generarse un JAR bootable la ejecución se realizará mediante el siguiente comando:

```bash
java -jar {jar-name}.jar
```

Sustituyendo `{jar-name}` por el nombre del fichero JAR generado.

No es necesario especificar la clase de inicio de la aplicación, ya que el fichero MANIFEST.MF generado ya contiene la información necesaria. Solamente se especificarán los parametros necesarios.

## Swagger

Se ha añadido la posibilidad de utilizar Swagger. Para acceder a Swagger, se utilizará la siguiente URL:

* http://localhost:8080/swagger-ui.html

Para activar swagger se utilizará la variable `app.swagger.enabled`

## Modelo de datos

 ![model](./images/model_data.png)

 ## Mapeo de URIs

El mapeo de URIS sigue el siguiente esquema, donde cada URI canónica se mapea a n URIS en distintos idiomas y cada URI canónica en un determinado idioma a las URIs locales (1 por almacenamiento)

 ![mapper_url](./images/multi_languege_map_language.png)

##  Documentación adicional

* [Compilación](docs/build.md)
* [Generación Docker](docs/docker.md)
