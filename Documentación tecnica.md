![cabecera](C:\Users\druiz\repositorios\UM\uris-generator\images\logos_feder.png)

# Documentación técnica para la Factoría de URIs 

| Entregable     | Documentación de la librería factoría de URIs                |
| -------------- | ------------------------------------------------------------ |
| Fecha          | 25/05/2020                                                   |
| Proyecto       | [ASIO](https://www.um.es/web/hercules/proyectos/asio) (Arquitectura Semántica e Infraestructura Ontológica) en el marco de la iniciativa [Hércules](https://www.um.es/web/hercules/) para la Semántica de Datos de Investigación de Universidades que forma parte de [CRUE-TIC](http://www.crue.org/SitePages/ProyectoHercules.aspx) |
| Módulo         | Arquitectura Semántica                                       |
| Tipo           | Documento                                                    |
| Objetivo       | El presente documento pretende ser la documentación técnica relativa a el entregable Factoría de URIs. Para ello, se documentaran exhaustivamente tanto los aspectos relativos a su despliegue, como todos los End Point que esta ofrece a otros procesos o usuarios, para interactuar con la misma. |
| Estado         | Implementada al **100%**, según la funcionalidad prevista para cubrir lo expresado en los documentos de [esquema de URIs](https://github.com/HerculesCRUE/ib-asio-docs-/blob/master/entregables_hito_1/08-Esquema_de_URIs_Hércules/ASIO_Izertis_ArquitecturaDeURIs.md)  , y  [Buenas practicas para URIs Hércules](https://github.com/HerculesCRUE/ib-asio-docs-/blob/master/entregables_hito_1/09-Buenas_prácticas_para_URIs_Hércules/ASIO_Izertis_BuenasPracticasParaURIsHercules.md).  Por otro lado la exposición de los EndPoint relativos al [CRUD](https://github.com/HerculesCRUE/ib-asio-docs-/blob/master/entregables_hito_1/09-Buenas_prácticas_para_URIs_Hércules/ASIO_Izertis_BuenasPracticasParaURIsHercules.md) sobre [modelo de datos](#Modelo de datos) completo, hace posible realizar cualquier operación, aunque esta en principio no estuviese prevista. |
| Próximos pasos | La integración con componentes desarrollados en una fase de madurez no final, o otros por desarrollar (tales como el servicio de publicación web), quizás requieran la modificación o creación de algún EndPoint adicional, aunque según lo descrito en el apartado anterior, dado que existe un CRUD completo sobre todas las entidades, la implementación, debería de ser trivial. |
| Documentación  | [Esquema de URIs](https://github.com/HerculesCRUE/ib-asio-docs-/blob/master/entregables_hito_1/08-Esquema_de_URIs_Hércules/ASIO_Izertis_ArquitecturaDeURIs.md)<br/>[Buenas practicas para URIs Hércules](https://github.com/HerculesCRUE/ib-asio-docs-/blob/master/entregables_hito_1/09-Buenas_prácticas_para_URIs_Hércules/ASIO_Izertis_BuenasPracticasParaURIsHercules.md)<br/>[Manual de usuario](./Manual de usuario.md) (documentación de alto nivel)<br />[Documentación técnica](./Documentación tecnica.md) (documentación de bajo nivel)<br/> |



## Despliegue

### Requisitos

* OpenJDK 11
* Maven 3.6.x
* Docker

### Compilación

Para realizar la compilación se ejecutará el siguiente comando:

```bash
mvn clean package
```

También sería posible instalar o desplegar los artefactos sustituyendo `package` por `install` o `deploy` respectivamente.

Los artefactos se generarán dentro del directorio `target` de cada uno de los módulos:

#### Módulo Back

Módulo que añade una capa de servicios REST a la funcionalidad de la aplicación.

Los artefactos se encuentran dentro de uris-generator-back/target

* Artefacto: uris-generator-back-{version}.jar

#### Módulo Service

Módulo que contiene la lógica de la aplicación. Puede ser utilizado como librería independiente para ser integrado en otras aplicaciones

* Los artefactos se encuentran dentro de triples-storage-adapter-service-abstractions/target

  * Artefacto: triples-storage-adapter-service-abstractions-{version}.jar

#### Módulo jpa-abstractions

Módulo con utilidades para el acceso a datos mediante JPA.

#### Módulo service-abstractions

Módulo con utilidades para la generación de servicios.

#### Módulo swagger

Módulo que contiene la funcionalidad necesaria para añadir [Swagger](https://swagger.io/) para la interacción con el API Rest.

#### Módulo audit

Módulo que contiene la funcionalidad necesaria para la generación de datos de auditoría para las tablas de base de datos.

### Metodología de desarrollo

La metodología de desarrollo es [Git Flow](https://www.atlassian.com/es/git/tutorials/comparing-workflows/gitflow-workflow).

### Entorno de desarrollo Docker

La generación de la imagen Docker y su despliegue están descritas en el documento [Generación Docker](docs/docker.md)

También es necesario desplegar el entorno de servicios necesarios, por ejemplo MariaDB.

Para ello existe en el directorio **docker-devenv** el dichero docker-compose.yml que despliega dicho entorno. Para ello es suficiente ejecutar los siguientes comandos:

 Para arrancar el entorno:

```bash
docker-compose up -d
```

Para pararlo:

```bash
docker-compose down
```

### Variables de entorno

La configuración se encuentra en el fichero application.yml

Esta puede ser sustituida por las siguientes variables de entorno disponibles en la maquina donde se realiza el despliegue

| Nombre                                         |                            Valor                             |
| ---------------------------------------------- | :----------------------------------------------------------: |
| `APP_PERSISTENCE_DATASOURCE_DRIVER-CLASS-NAME` |                   org.mariadb.jdbc.Driver                    |
| `APP_PERSISTENCE_DATASOURCE_USERNAME`          |                             app                              |
| `APP_PERSISTENCE_DATASOURCE_PASSWORD`          |                           sqlpass                            |
| `APP_PERSISTENCE_DATASOURCE_URL`               |         jdbc:mariadb://127.0.0.1:3307/app?ssl=false          |
| `APP_URI_CANONICALURISCHEMA`                   |  http://$domain$/$sub-domain$/$type$/$concept$/$reference$   |
| `APP_URI_CANONICALURILANGUAGESCHEMA`           | http://$domain$/$sub-domain$/$language$/$type$/$concept$/$reference$ |



### Swagger

Se desplegara un API Swagger automáticamente al desplegar el proyecto.

El API esta disponible en

[http://{HOST_FACTORIA_URIS}:[SWAGGER_PORT]/swagger-ui.html](http://localhost:9326/swagger-ui.html)

Para activar Swagger se utilizará la variable `app.swagger.enabled`

### Modelo de datos

Es conveniente para mejorar la comprensión, ver la representación del modelo de datos que soporta la librería de URIs y se encuentra desplegado en MariaDB

![modelo datos](C:\Users\druiz\repositorios\UM\uris-generator\images\model_data.png)

## API REST de Factoría de URIS

La documentación de esta sección hará referencia a cada uno de los EndPoints desplegados por la librería de URIs, apoyándose en la documentación proporcionada por Swagger, por lo tanto los enlaces que se facilitan para los EndPoint descritos en Swagger, solo estarán disponibles se ha realizado el despliegue, y dicho despliegue se ha realizado en la misma máquina donde se encuentra la presente documentación. En otro caso es necesario cambiar el host y el puerto por aquellos donde la librería de URIs ha sido desplegada.

La librería de URIs despliega dos grandes módulos de EndPoints uno para todas aquellas operaciones [CRUD](#CRUD API REST) "Atómicas", para cada una de las entidades representadas en el modelo de datos y otra, para las operaciones implementadas por la factoría de URIs. En esta sección se documentaran ambas:

### CRUD API REST

#### Operaciones sobre la entidad TYPE

La entidad **Type** representa el tipo ($type$) disponible en el esquema de URIS

```bash
# Canonical Schema
Esquema canonico -> http://$domain$/$sub-domain$/$type$/$concept$/$reference$ 
# Canonical Langauge Schema
Esquema canonico por idioma -> http://$domain$/$sub-domain$/$type$/$concept$/$reference$
```

Es necesaria para el multilingüismo ya que la combinación de un tipo con un idioma, permitirá acceder a la traducción de dicho tipo.

![language](C:\Users\druiz\repositorios\UM\uris-generator\images\type_entity.png)

##### Implementación

Es implementado por el controlador [TypeController](.\uris-generator-back\src\main\java\es\um\asio\back\controller\crud\type\TypeController.java)

##### Test

Test de integración disponibles en [TypeControllerTest](.\uris-generator-back\src\test\java\es\um\asio\back\test\controller\crud\type\TypeControllerTest.java)

##### EndPoints

![TypeController](C:\Users\druiz\repositorios\UM\uris-generator\images\TypeController.png)

###### GET /type

Disponible en Swagger el siguiente [enlace](http://localhost:9326/swagger-ui.html#/CRUD Operations (GET, POST, DELETE) for Type/getUsingGET_5)



**Semántica**

Obtención de un recurso por su código

**Parámetros**

- **code:** (Requerido) Código del tipo a recuperar

**Petición**

```bash
curl -X GET "http://localhost:9326/type?code=res" -H "accept: */*"
```

**Respuesta**

```bash
{
  "code": "res",
  "name": "resources"
}
```



###### POST /type

Disponible en Swagger el siguiente [enlace](http://localhost:9326/swagger-ui.html#/CRUD Operations (GET, POST, DELETE) for Type/getUsingGET_5)

**Parámetros**

- **code:** (*Requerido) Código del tipo a insertar
- **name:** (*Opcional) Nombre del codigo de tipo a insertar. Si no se indica, por defecto se crea con el mismo nombre que el pasado en el parámetro code.

**Semántica**

Inserción de un recurso por su código y nombre

**Petición**

```bash
curl -X POST "http://localhost:9326/type?code=res&name=res" -H "accept: */*"
```

**Respuesta**

```bash
{
  "code": "res",
  "name": "res"
}
```

###### DELETE /type

Disponible en Swagger el siguiente [enlace](http://localhost:9326/swagger-ui.html#/CRUD Operations (GET, POST, DELETE) for Type/getUsingGET_5)

**Parámetros**

- **code:** (*Requerido) Código del tipo a borrar

**Semántica**

Borrado de un recurso por su código y nombre

**Petición**

```bash
curl -X DELETE "http://localhost:9326/type?code=res" -H "accept: */*"
```

**Respuesta**: 

```bash
Status 200 OK
```



###### GET /type/all

Disponible en Swagger el siguiente [enlace](http://localhost:9326/swagger-ui.html#/CRUD Operations (GET, POST, DELETE) for Type/getUsingGET_5)

**Semántica**

Obtención de todos los recursos.

**Parámetros**

Ninguno

**Petición**

```bash
curl -X GET "http://localhost:9326/type/all" -H "accept: */*"
```

**Respuesta**

```bash
[
  {
    "code": "cat",
    "name": "catalog"
  },
  {
    "code": "def",
    "name": "definitions"
  },
  {
    "code": "kos",
    "name": "skos"
  },
  {
    "code": "res",
    "name": "res"
  }
]
```

###### POST /type/json

Disponible en Swagger el siguiente [enlace](http://localhost:9326/swagger-ui.html#/CRUD Operations (GET, POST, DELETE) for Type/getUsingGET_5)

**Semántica**

Inserción de un tipo en formato json.

**Parámetros**

- (Requerido) JSON del tipo a insertar enviado el body de la petición

```bash
curl -X POST "http://localhost:9326/type/json" -H "accept: */*" -H "Content-Type: application/json" -d "{ \"code\": \"cat\", \"name\": \"category\"}"
```

**Respuesta**

```bash
{
  "code": "cat",
  "name": "category"
}
```

#### Operaciones sobre la entidad LANGUAGE

La entidad **LANGUAGE** representa un determinado idioma y la traducción de los componentes que aparecen en el  [Esquema de URIs](https://github.com/HerculesCRUE/ib-asio-docs-/blob/master/entregables_hito_1/08-Esquema_de_URIs_Hércules/ASIO_Izertis_ArquitecturaDeURIs.md)

Es necesaria para el multilingüismo ya que la combinación de una URI Canónica con un lenguaje, dará como resultado una URI Canonical en un determinado idioma.

![language](C:\Users\druiz\repositorios\UM\uris-generator\images\language_entity.png)

Otro atributo de importancia en la entidad Language es IS_DEFAULT, que establece si es el idioma por defecto, que será el retornado en caso de hacerse una solicitud a una URI Canoníca, indicando un recurso en un idioma no disponible para dicho recurso.

##### Implementación

Es implementado por el controlador [LanguageController](.\uris-generator-back\src\main\java\es\um\asio\back\controller\crud\language\LanguageController.java)

##### Test

Test de integración disponibles en [LanguageControllerTest](.\uris-generator-back\src\test\java\es\um\asio\back\test\controller\crud\language\LanguageControllerTest.java)

##### EndPoints

![TypeController](C:\Users\druiz\repositorios\UM\uris-generator\images\LanguageController.png)

###### GET /language

Disponible en Swagger el siguiente [enlace](http://localhost:9326/swagger-ui.html#/CRUD Operations (GET, POST, DELETE) for Type/getUsingGET_5)

**Semántica**

Obtención de un idioma por su código ISO 639-1

**Parámetros**

- **ISO:** (Requerido) Código del lenguaje (ISO 639-1) a recuperar

**Petición**

```bash
curl -X GET "http://localhost:9326/language?ISO=es-ES" -H "accept: */*"
```

**Respuesta**

```bash
{
  "iso": "es-ES",
  "languageStr": "español",
  "region": "España",
  "variant": null,
  "script": null,
  "name": "Español",
  "domain": "dominio",
  "subDomain": "sub-dominio",
  "type": "tipo",
  "concept": "concepto",
  "reference": "referencia",
  "isDefault": true
}
```



###### POST /language

Disponible en Swagger el siguiente [enlace](http://localhost:9326/swagger-ui.html#/CRUD Operations (GET, POST, DELETE) for Type/getUsingGET_5)

**Parámetros**

- **ISO:** (Requerido) Código del lenguaje (ISO 639-1) a recuperar
- **domain:** (*Opcional) Nombre del componente de la URI **dominio** en idioma indicado, si no se indica ninguno, se asume el mismo que el del idioma por defecto.
- **subDomain:** (*Opcional) Nombre del componente de la URI **sub-domain** en idioma indicado, si no se indica ninguno, se asume el mismo que el del idioma por defecto.
- **type:** (*Opcional) Nombre del componente de la URI **type** en idioma indicado, si no se indica ninguno, se asume el mismo que el del idioma por defecto. Este aparecerá traducido en las URIS Canónicas por Idioma, según el contenido de este atributo.
- **concept:** (*Opcional) Nombre del componente de la URI **concepto** en idioma indicado, si no se indica ninguno, se asume el mismo que el del idioma por defecto.
- **reference:** (*Opcional) Nombre del componente de la URI **referencia** en idioma indicado, si no se indica ninguno, se asume el mismo que el del idioma por defecto.
- **isDefault:** (*Opcional) Indica si el idioma indicado es el idioma por defecto. Solo puede haber 1 idioma por defecto, asi que si se indica, cualquier otro que lo fuese hasta ese momento, dejara de serlo. Si no se indica ninguno, se asume que no lo es.
- **name:** (*Opcional) Nombre del idioma, si no se indica ninguno, se aplicara el codigo ISO.

**Semántica**

Inserción de un idioma, ofreciendo configuración y traducción de los componentes de la URI.

**Petición**

```bash
curl -X POST "http://localhost:9326/language?ISO=pt-PT&concept=conceito&domain=dom%C3%ADnio&isDefault=false&name=nome&reference=refer%C3%AAncia&subDomain=%20sub-dom%C3%ADnio&type=tipo" -H "accept: */*"
```

**Respuesta**

```bash
{
  "iso": "pt-PT",
  "languageStr": "portugués",
  "region": "Portugal",
  "variant": null,
  "script": null,
  "name": "nome",
  "domain": "domínio",
  "subDomain": " sub-domínio",
  "type": "tipo",
  "concept": "conceito",
  "reference": "referência",
  "isDefault": false
}
```

###### DELETE /language

Disponible en Swagger el siguiente [enlace](http://localhost:9326/swagger-ui.html#/CRUD Operations (GET, POST, DELETE) for Type/getUsingGET_5)

**Parámetros**

- **ISO:** (*Requerido) Código del idioma a borrar

**Semántica**

Borrado de un recurso por su código y nombre

**Petición**

```bash
curl -X DELETE "http://localhost:9326/language?ISO=pt-PT" -H "accept: */*"
```

**Respuesta**: 

```bash
Status 200 OK
```



###### GET /language/all

Disponible en Swagger el siguiente [enlace](http://localhost:9326/swagger-ui.html#/CRUD Operations (GET, POST, DELETE) for Type/getUsingGET_5)

**Semántica**

Obtención de todos los idiomas.

**Parámetros**

Ninguno

**Petición**

```bash
curl -X GET "http://localhost:9326/type/all" -H "accept: */*"
```

**Respuesta**

```bash
[
  {
    "iso": "en-EN",
    "languageStr": "inglés",
    "region": "EN",
    "variant": null,
    "script": null,
    "name": "English",
    "domain": "domain",
    "subDomain": "sub-domain",
    "type": "type",
    "concept": "concept",
    "reference": "reference",
    "isDefault": false
  },
  {
    "iso": "es-ES",
    "languageStr": "español",
    "region": "España",
    "variant": null,
    "script": null,
    "name": "Español",
    "domain": "dominio",
    "subDomain": "sub-dominio",
    "type": "tipo",
    "concept": "concepto",
    "reference": "referencia",
    "isDefault": true
  }
]
```

###### POST /language/json

Disponible en Swagger el siguiente [enlace](http://localhost:9326/swagger-ui.html#/CRUD Operations (GET, POST, DELETE) for Type/getUsingGET_5)

**Semántica**

Inserción de un idioma en formato json.

**Parámetros**

- (Requerido) JSON del tipo a insertar enviado el body de la petición

```bash
curl -X POST "http://localhost:9326/language/json" -H "accept: */*" -H "Content-Type: application/json" -d "{ \"iso\": \"ES\", \"languageStr\": \"ES\", \"name\": \"Spain\", \"region\": \"ES\", \"script\": \"ES\", \"variant\": \"ES\", \"domain\": \"dominio\", \"subDomain\": \"sub-dominio\", \"type\": \"tipo\", \"concept\": \"class\", \"reference\": \"item\", \"isDefault\": true}"
```

**Respuesta**

```bash
{
  "iso": "ES",
  "languageStr": "ES",
  "region": "ES",
  "variant": "ES",
  "script": "ES",
  "name": "Spain",
  "domain": "dominio",
  "subDomain": "sub-dominio",
  "type": "tipo",
  "concept": "class",
  "reference": "item",
  "isDefault": true
}
```



#### Operaciones sobre la entidad LANGUAGE_TYPE

La entidad **LANGUAGE_TYPE** representa la unión de un determinado tipo, con un determinado idioma, y por lo tanto otorga la capacidad de traducir el atributo type de las URIs Canónicas por Idioma

![language](C:\Users\druiz\repositorios\UM\uris-generator\images\language_type_entity.png)

##### Implementación

Es implementado por el controlador [LanguageTypeController](.\uris-generator-back\src\main\java\es\um\asio\back\controller\crud\language_type\LanguageTypeController.java)

##### Test

Test de integración disponibles en [LanguageTypeControllerTest](.\uris-generator-back\src\test\java\es\um\asio\back\test\controller\crud\language_type\language\LanguageTypeControllerTest.java)

##### EndPoints

![TypeController](C:\Users\druiz\repositorios\UM\uris-generator\images\LanguageTypeController.png)

###### GET /language-type

Disponible en Swagger el siguiente [enlace](http://localhost:9326/swagger-ui.html#/CRUD Operations (GET, POST, DELETE) for Type/getUsingGET_5)

**Semántica**

Ofrece la traducción del tipo para el idioma indicado. Esto es necesario para la construcción de las URIs canónicas por idioma.

**Parámetros**

- **ISO:** (Opcional) Código del lenguaje (ISO 639-1) a recuperar
- **type:** (Opcional) Código del tipo a recuperar

**Petición**

```bash
curl -X GET "http://localhost:9326/language-type?ISO=es-ES&type=res" -H "accept: */*"
```

**Respuesta**

```bash
[
  {
    "id": 3,
    "languageId": "es-ES",
    "typeId": "res",
    "typeLangCode": "rec",
    "description": "recurso"
  }
]
```



###### POST /language-type

Disponible en Swagger el siguiente [enlace](http://localhost:9326/swagger-ui.html#/CRUD Operations (GET, POST, DELETE) for Type/getUsingGET_5)

**Parámetros**

- **ISOCode:** (Requerido) Código del lenguaje (ISO 639-1) a enlazar.
- **typeCode:** (Requerido) Código del tipo a enlazar.
- **languageTypeCode:** (*Opcional) Código corto (e caracteres) del tipo traducido.
- **description:** (*Opcional) Nombre largo de el tipo traducido.

**Semántica**

Inserción de la traducción del tipo para el idioma indicado. Esto es necesario para la construcción de las URIs canónicas por idioma.

**Petición**

```bash
curl -X POST "http://localhost:9326/language-type?ISOCode=es-ES&description=recurso&languageTypeCode=rec&typeCode=res" -H "accept: */*"
```

**Respuesta**

```bash
{
  "id": 3,
  "languageId": "es-ES",
  "typeId": "res",
  "typeLangCode": "rec",
  "description": "recurso"
}
```

###### DELETE /language-type

Disponible en Swagger el siguiente [enlace](http://localhost:9326/swagger-ui.html#/CRUD Operations (GET, POST, DELETE) for Type/getUsingGET_5)

**Parámetros**

- **ISO:** (*Requerido) Código del idioma a borrar
- **type:** (*Requerido) Código del tipo (canonico) a borrar 

**Semántica**

Borrado de la tradición de un tipo a un idioma por su código ISO y su tipo

**Petición**

```bash
curl -X DELETE "http://localhost:9326/language?ISO=es-ES&type=res" -H "accept: */*"
```

**Respuesta**: 

```bash
Status 200 OK
```



###### GET /language-type/all

Disponible en Swagger el siguiente [enlace](http://localhost:9326/swagger-ui.html#/CRUD Operations (GET, POST, DELETE) for Type/getUsingGET_5)

**Semántica**

Obtención de todas las traducciones de tipos por idioma los idiomas.

**Parámetros**

Ninguno

**Petición**

```bash
curl -X GET "http://localhost:9326/language-type/all" -H "accept: */*"
```

**Respuesta**

```bash
[
  {
    "id": 1,
    "languageId": "es-ES",
    "typeId": "def",
    "typeLangCode": "def",
    "description": "definiciones"
  },
  {
    "id": 2,
    "languageId": "es-ES",
    "typeId": "kos",
    "typeLangCode": "kos",
    "description": "skos"
  },
  {
    "id": 3,
    "languageId": "es-ES",
    "typeId": "res",
    "typeLangCode": "rec",
    "description": "recurso"
  },
  {
    "id": 4,
    "languageId": "es-ES",
    "typeId": "cat",
    "typeLangCode": "cat",
    "description": "catalogo"
  },
  {
    "id": 5,
    "languageId": "en-EN",
    "typeId": "def",
    "typeLangCode": "def",
    "description": "definitions"
  },
  {
    "id": 6,
    "languageId": "en-EN",
    "typeId": "kos",
    "typeLangCode": "kos",
    "description": "skos"
  },
  {
    "id": 7,
    "languageId": "en-EN",
    "typeId": "res",
    "typeLangCode": "res",
    "description": "resource"
  },
  {
    "id": 8,
    "languageId": "en-EN",
    "typeId": "cat",
    "typeLangCode": "cat",
    "description": "catalog"
  }
]

```

###### POST /language-type/json

Disponible en Swagger el siguiente [enlace](http://localhost:9326/swagger-ui.html#/CRUD Operations (GET, POST, DELETE) for Type/getUsingGET_5)

**Semántica**

Inserción de un idioma en formato json.

**Parámetros**

- (Requerido) JSON del tipo a insertar enviado el body de la petición

```bash
curl -X POST "http://localhost:9326/language-type/json" -H "accept: */*" -H "Content-Type: application/json" -d "{ \"languageId\": \"es-ES\", \"typeId\": \"res\", \"typeLangCode\": \"rec\", \"description\": \"recurso\"}"
```

**Respuesta**

```bash
{
  "id": 3,
  "languageId": "es-ES",
  "typeId": "res",
  "typeLangCode": "rec",
  "description": "recurso"
}
```



#### Operaciones sobre la entidad STORAGE_TYPE

La entidad **STORAGE_TYPE** almacena información relativa al un determinado tipo de almacenamiento, por ejemplo en el estado actual del proyecto, Trellis y Wikibase. Mantiene asimismo la capacidad de guardar metadatos relativos a dichos sistemas tales como la URL del EndPoint SPARQL o de su API

![language](C:\Users\druiz\repositorios\UM\uris-generator\images\storage_type_entity.png)

##### Implementación

Es implementado por el controlador [StorageTypeController](.\uris-generator-back\src\main\java\es\um\asio\back\controller\crud\storage_type\StorageTypeController.java)

##### Test

Test de integración disponibles en [StorageTypeControllerTest](.\uris-generator-back\src\test\java\es\um\asio\back\test\controller\crud\storage_type\type\StorageTypeControllerTest.java)

##### EndPoints

![TypeController](C:\Users\druiz\repositorios\UM\uris-generator\images\StorageTypeController.png)

###### GET /storage-type

Disponible en Swagger el siguiente [enlace](http://localhost:9326/swagger-ui.html#/CRUD Operations (GET, POST, DELETE) for Type/getUsingGET_5)

**Semántica**

Ofrece los datos relativos a un determinado sistema de almacenamiento.

**Parámetros**

- **name:** (Requerido) Nombre del sistema de almacenamiento.

**Petición**

```bash
curl -X GET "http://localhost:9326/storage-type?name=trellis" -H "accept: */*"
```

**Respuesta**

```bash
{
  "id": 9,
  "name": "trellis",
  "apiURL": null,
  "endPointURL": null,
  "schemaURI": null
}
```



###### POST /storage-type

Disponible en Swagger el siguiente [enlace](http://localhost:9326/swagger-ui.html#/CRUD Operations (GET, POST, DELETE) for Type/getUsingGET_5)

**Parámetros**

- **name:** (Requerido) Nombre del sistema de almacenamiento (ha de ser único).
- **apiURL:** (Opcional) URL Base para el API en el Sistema de almacenamiento establecido.
- **endPointURL:** (Opcional) URL Base el EndPoint SPARQL en el Sistema de almacenamiento establecido.
- **schemaURI:** (*Opcional) Esquema de URIs aplicable a dicho sistema de almacenamiento. Actualmente sin uso.

**Semántica**

Inserción de los datos relativos a un determinado sistema de almacenamiento.

**Petición**

```bash
curl -X POST "http://localhost:9326/storage-type?apiURL=http%3A%2F%2Ftrellis-otro%2Fapi&endPointURL=http%3A%2F%2Ftrellis-otro%2FendPoint&name=trellis-otro" -H "accept: */*"
```

**Respuesta**

```bash
{
  "id": 22,
  "name": "trellis-otro",
  "apiURL": "http://trellis-otro/api",
  "endPointURL": "http://trellis-otro/endPoint",
  "schemaURI": null
}
```

###### DELETE /storage-type

Disponible en Swagger el siguiente [enlace](http://localhost:9326/swagger-ui.html#/CRUD Operations (GET, POST, DELETE) for Type/getUsingGET_5)

**Parámetros**

- **name:** (Requerido) Nombre del sistema de almacenamiento (ha de ser único) a  borrar.

**Semántica**

Borrado de un sistema de almacenamiento determinado por su nombre.

**Petición**

```bash
curl -X DELETE "http://localhost:9326/language?name=trellis-otro" -H "accept: */*"
```

**Respuesta**: 

```bash
Status 200 OK
```



###### GET /storage-type/all

Disponible en Swagger el siguiente [enlace](http://localhost:9326/swagger-ui.html#/CRUD Operations (GET, POST, DELETE) for Type/getUsingGET_5)

**Semántica**

Obtención de todos los sistemas de almacenamiento.

**Parámetros**

Ninguno

**Petición**

```bash
curl -X GET "http://localhost:9326/storage-type/all" -H "accept: */*"
```

**Respuesta**

```bash
[
  {
    "id": 9,
    "name": "trellis",
    "apiURL": null,
    "endPointURL": null,
    "schemaURI": null
  },
  {
    "id": 10,
    "name": "wikibase",
    "apiURL": null,
    "endPointURL": null,
    "schemaURI": null
  },
  {
    "id": 11,
    "name": "weso-wikibase",
    "apiURL": null,
    "endPointURL": null,
    "schemaURI": null
  },
  {
    "id": 22,
    "name": "trellis-otro",
    "apiURL": "http://trellis-otro/api",
    "endPointURL": "http://trellis-otro/endPoint",
    "schemaURI": null
  }
]

```

###### POST /storage-type/json

Disponible en Swagger el siguiente [enlace](http://localhost:9326/swagger-ui.html#/CRUD Operations (GET, POST, DELETE) for Type/getUsingGET_5)

**Semántica**

Inserción de un sistema de almacenamiento en formato json.

**Parámetros**

- (Requerido) JSON del tipo a insertar enviado el body de la petición

```bash
curl -X POST "http://localhost:9326/storage-type/json" -H "accept: */*" -H "Content-Type: application/json" -d "{ \"name\": \"WIKIBASE-NO\"}"
```

**Respuesta**

```bash
{
  "id": 23,
  "name": "WIKIBASE-NO",
  "apiURL": null,
  "endPointURL": null,
  "schemaURI": null
}
```

#### Operaciones sobre la entidad CANONICAL_URI

La entidad **CANONICAL_URI** representa un URI canónica para un determinado recurso, ya sea una clase, una propiedad o una instancia. 

![language](C:\Users\druiz\repositorios\UM\uris-generator\images\canonical_uri_entity.png)

##### Implementación

Es implementado por el controlador [StorageTypeController](.\uris-generator-back\src\main\java\es\um\asio\back\controller\crud\canonical\CanonicalURIController.java)

##### Test

Test de integración disponibles en [StorageTypeControllerTest](.\uris-generator-back\src\test\java\es\um\asio\back\test\controller\crud\canonical\CanonicalURIControllerTest.java)

##### EndPoints

![TypeController](C:\Users\druiz\repositorios\UM\uris-generator\images\CanonicalURIController.png)

###### GET /canonical-uri

Disponible en Swagger el siguiente [enlace](http://localhost:9326/swagger-ui.html#/CRUD Operations (GET, POST, DELETE) for Type/getUsingGET_5)

**Semántica**

Permite recuperar una URI Canónica por sus componentes definidos en el esquema de URIs.

**Parámetros**

- **domain:** (Opcional) Nombre del dominio para filtrar.
- **subDomain:** (Opcional) Nombre del sub-dominio para filtrar.
- **typeCode:** (Opcional) Nombre del tipo canónico para filtrar.
- **concept:** (Opcional) Nombre del concepto para filtrar (clase o propiedad).
- **reference:** (Opcional) Nombre el identificador de la referencia para filtrar (clase o propiedad).

**Petición**

```bash
curl -X GET "http://localhost:9326/canonical-uri?concept=CvnRootBean&domain=hercules.org&subDomain=um&typeCode=res" -H "accept: */*"
```

**Respuesta**

```bash
[
  {
    "id": 15,
    "domain": "hercules.org",
    "subDomain": "um",
    "typeIdCode": "res",
    "concept": "CvnRootBean",
    "reference": null,
    "fullURI": "http://hercules.org/um/res/CvnRootBean/",
    "isEntity": true,
    "isProperty": false,
    "isInstance": false,
    "entityName": "CvnRootBean",
    "propertyName": null,
    "canonicalURILanguages": [
      {
        "id": 16,
        "languageID": "es-ES",
        "domain": "hercules.org",
        "subDomain": "um",
        "typeCode": "res",
        "typeLangCode": "rec",
        "concept": "CvnRootBean",
        "reference": null,
        "fullURI": "http://hercules.org/um/es-ES/rec/CvnRootBean/",
        "fullParentURI": "http://hercules.org/um/res/CvnRootBean/",
        "isEntity": true,
        "isProperty": false,
        "isInstance": false,
        "entityName": "CvnRootBean",
        "propertyName": null,
        "parentEntityName": "CvnRootBean",
        "parentPropertyName": null,
        "localURIs": [
          {
            "id": 16,
            "canonicalURILanguageStr": "http://hercules.org/um/es-ES/rec/CvnRootBean/",
            "storageTypeStr": "wikibase",
            "localUri": "http://wikibase/Q1"
          }
        ]
      }
    ]
  }
]
```



###### POST /canonical-uri

Disponible en Swagger el siguiente [enlace](http://localhost:9326/swagger-ui.html#/CRUD Operations (GET, POST, DELETE) for Type/getUsingGET_5)

**Parámetros**

- **domain:** (Opcional) Nombre del dominio para insertar.
- **subDomain:** (Opcional) Nombre del sub-dominio para insertar.
- **typeCode:** (Opcional) Nombre del tipo canónico para inserta.
- **concept:** (Opcional) Nombre del concepto para insertar (esto determina que es una clase).
- **property:** (Opcional) Nombre de la propiedad para insertar (esto determina que es una clase).
- **reference:** (Opcional) Nombre el identificador de la referencia para filtrar (clase o propiedad).

**Semántica**

Inserción de los datos relativos a una URI Canónica por sus componentes definidos en el esquema de URIs.

**Petición**

```bash
curl -X POST "http://localhost:9326/canonical-uri?domain=hercules.org&property=propiedad1&subDomain=um&typeCode=res" -H "accept: */*"
```

**Respuesta**

```bash
{
  "id": 12,
  "domain": "hercules.org",
  "subDomain": "um",
  "typeIdCode": "res",
  "concept": null,
  "reference": "propiedad1",
  "fullURI": "http://hercules.org/um/res/propiedad1",
  "isEntity": false,
  "isProperty": true,
  "isInstance": false,
  "entityName": null,
  "propertyName": "propiedad1",
  "canonicalURILanguages": [
    {
      "id": 13,
      "languageID": "es-ES",
      "domain": "hercules.org",
      "subDomain": "um",
      "typeCode": "res",
      "typeLangCode": "rec",
      "concept": null,
      "reference": "propiedad1",
      "fullURI": "http://hercules.org/um/es-ES/rec/propiedad1",
      "fullParentURI": "http://hercules.org/um/res/propiedad1",
      "isEntity": false,
      "isProperty": true,
      "isInstance": false,
      "entityName": null,
      "propertyName": "propiedad1",
      "parentEntityName": null,
      "parentPropertyName": "propiedad1",
      "localURIs": []
    }
  ]
}
```

###### DELETE /canonical-uri

Disponible en Swagger el siguiente [enlace](http://localhost:9326/swagger-ui.html#/CRUD Operations (GET, POST, DELETE) for Type/getUsingGET_5)

**Parámetros**

- **domain:** (Opcional) Nombre del dominio para borrar.
- **subDomain:** (Opcional) Nombre del sub-dominio para borrar.
- **type:** (Opcional) Nombre del tipo canónico para borrar.
- **concept:** (Opcional) Nombre del concepto para borrar (para clase o propiedad).
- **reference:** (Opcional) Nombre el identificador de la referencia para borrar(clase o propiedad).

**Semántica**

Borrado de un sistema de almacenamiento determinado por su nombre.

**Petición**

```bash
curl -X DELETE "http://localhost:9326/canonical-uri?concept=propiedad1&domain=hercules.org&subDomain=um&type=res" -H "accept: */*"
```

**Respuesta**: 

```bash
Status 200 OK
```



###### GET /canonical-uri/all

Disponible en Swagger el siguiente [enlace](http://localhost:9326/swagger-ui.html#/CRUD Operations (GET, POST, DELETE) for Type/getUsingGET_5)

**Semántica**

Obtención de todos las URIS Canonicas.

**Parámetros**

Ninguno

**Petición**

```bash
curl -X GET "http://localhost:9326/storage-type/all" -H "accept: */*"
```

**Respuesta**

```bash
[
  {
    "id": 9,
    "name": "trellis",
    "apiURL": null,
    "endPointURL": null,
    "schemaURI": null
  },
  {
    "id": 10,
    "name": "wikibase",
    "apiURL": null,
    "endPointURL": null,
    "schemaURI": null
  },
  {
    "id": 11,
    "name": "weso-wikibase",
    "apiURL": null,
    "endPointURL": null,
    "schemaURI": null
  },
  {
    "id": 22,
    "name": "trellis-otro",
    "apiURL": "http://trellis-otro/api",
    "endPointURL": "http://trellis-otro/endPoint",
    "schemaURI": null
  }
]

```

###### POST /type/json

Disponible en Swagger el siguiente [enlace](http://localhost:9326/swagger-ui.html#/CRUD Operations (GET, POST, DELETE) for Type/getUsingGET_5)

**Semántica**

Inserción de un sistema de almacenamiento en formato json.

**Parámetros**

- (Requerido) JSON del tipo a insertar enviado el body de la petición

```bash
curl -X POST "http://localhost:9326/storage-type/json" -H "accept: */*" -H "Content-Type: application/json" -d "{ \"name\": \"WIKIBASE-NO\"}"
```

**Respuesta**

```bash
{
  "id": 23,
  "name": "WIKIBASE-NO",
  "apiURL": null,
  "endPointURL": null,
  "schemaURI": null
}
```



### API REST Factoría de URIs

