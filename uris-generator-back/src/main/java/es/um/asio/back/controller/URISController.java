package es.um.asio.back.controller;

import es.um.asio.abstractions.constants.Constants;
import es.um.asio.back.controller.crud.canonical_language.CanonicalURILanguageController;
import es.um.asio.back.controller.crud.local.LocalURIController;
import es.um.asio.back.controller.crud.storage_type.StorageTypeController;
import es.um.asio.back.controller.error.CustomNotFoundException;
import es.um.asio.service.model.*;
import es.um.asio.service.proxy.*;
import es.um.asio.service.service.*;
import es.um.asio.service.util.Utils;
import es.um.asio.service.validation.group.Create;
import io.cucumber.messages.internal.com.google.gson.Gson;
import io.cucumber.messages.internal.com.google.gson.internal.LinkedTreeMap;
import io.swagger.annotations.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.stream.Collectors;

import static es.um.asio.service.model.LocalURI_.canonicalURILanguage;


/**
 * URIS controller.
 */
@RestController
@RequestMapping(URISController.Mappings.BASE)
@Api(value = "API for URIs Factory", tags = "API for URIs Factory")
public class URISController {

	/** The logger. */
	private final Logger logger = LoggerFactory.getLogger(URISController.class);

	private static final String CANONICAL_LANGUAGE_URI_NOT_FOUND = "Canonical Language URI Not Found";
	private static final String STORAGE_NOT_FOUND = "Storage type %s Not Found";
	private static final String CREATED_INSTANCE = "Created Instance URI: {}";
	private static final String NOT_VALID_URI_LOCAL_FORMAT = "Not valid format URI Local";

	/**
	 * Controller implementation for {@link CanonicalURILanguage}.
	 */
	@Autowired
	private CanonicalURILanguageController canonicalURILanguageControllerController;

	/**
	 * Service implementation for {@link CanonicalURILanguage}.
	 */
	@Autowired
	private CanonicalURILanguageService canonicalURILanguageControllerService;

	/**
	 * Service implementation for {@link CanonicalURILanguage}.
	 */
	@Autowired
	private CanonicalURIService canonicalURIService;

	/**
	 * Controller implementation for {@link StorageType}.
	 */
	@Autowired
	private StorageTypeController storageTypeController;

	/**
	 * Controller implementation for {@link LocalURI}.
	 */
	@Autowired
	private LocalURIController localURIController;

	/**
	 * Proxy implementation for {@link LocalURI}.
	 */
	@Autowired
	private LocalURIProxy localURIProxy;

	/**
	 * Proxy implementation for {@link LocalURI}.
	 */
	@Autowired
	private LanguageProxy languageProxy;

	/**
	 * Proxy implementation for {@link LocalURI}.
	 */
	@Autowired
	private LanguageTypeProxy languageTypeProxy;

	/**
	 * Proxy implementation for {@link LocalURI}.
	 */
	@Autowired
	private TypeProxy typeProxy;

	/**
	 * Proxy implementation for {@link LocalURI}.
	 */
	@Autowired
	private StorageTypeProxy storageTypeProxy;

	/**
	 * Schema Service
	 */
	@Autowired
	private SchemaService schemaService;

	/**
	 * DiscoveryService Service
	 */
	@Autowired
	private DiscoveryService discoveryService;

	@ApiOperation(value = "Check if server is Alive", notes = "Check if server is Alive")
	@RequestMapping(method={RequestMethod.GET},value={Mappings.HEALTH})
	public String getHealth() {
		return "Alive";
	}

	/**
	 * Creates the resourceID URI
	 * <p>
	 * Example
	 * <p>
	 * 
	 * <pre>
	 * {
	 *    obj: 
	 *    {
	 *      @class:es.um.asio.service.util.data.ConceptoGrupo,
	 *      entityId:null,
	 *      version:0,
	 *      idGrupoInvestigacion:E0A6-01,
	 *      numero:5,
	 *      codTipoConcepto:DESCRIPTORES,
	 *      texto:LENGUAJES PLASTICOS
	 *    }
	 * }
	 * </pre>
	 * 
	 * @param domain
	 * @param subDomain
	 * @param lang
	 * @param input
	 * @return
	 */
	@ApiOperation(value = "Create or Get Canonical URI for Resource or Instance", notes = "Allow create canonical URI and canonical URI in language if not exist, if exist then return URIs")
	@ApiImplicitParams({
			@ApiImplicitParam(
					name = "input",
					value = "A JSON where @class attribute with the name of class in language is required, canonicalClassName or canonicalClass is the canonical name of the entity without language. Attribute entityId if is present is the id of entity, in other wise a hash id from attributes value will be generated." +
							" Ej: {\"@class\": \"es.um.asio.service.util.data.ConceptoGrupo\",\"entityId\": null,\"version\": 0,\"idGrupoInvestigacion\": \"E0A6-01\", \"numero\": 5, \"codTipoConcepto\": \"DESCRIPTORES\", \"codTipoConcepto\": \"DESCRIPTORES\" }",
					required = true,
					dataType = "Map",
					paramType = "body",
					examples = @Example(value = {@ExampleProperty(mediaType = "application/json", value = "{foo: whatever, bar: whatever2}")}))})
	@PostMapping(URISController.Mappings.RESOURCE_ID)
	public Map<String, String> createResourceID(
			@ApiParam(name = "domain", value = "Domain: hercules.org", defaultValue = Constants.DOMAIN_VALUE, required = false) 
			@RequestParam(required = false, defaultValue = "hercules.org") @Validated(Create.class) final String domain,
			@ApiParam(name = "subDomain", value = "Subdomain: um (universidad de murcia)", defaultValue = Constants.SUBDOMAIN_VALUE, required = false) 
			@RequestParam(required = false, defaultValue = "um") @Validated(Create.class) final String subDomain,
			@ApiParam(name = "type", value = "Type of URI", defaultValue = Constants.TYPE_REST, required = false)
			@RequestParam(required = false, defaultValue = Constants.TYPE_REST) @Validated(Create.class) final String type,
			@ApiParam(name = "lang", value = "Language of data", defaultValue = Constants.SPANISH_LANGUAGE, required = false)
			@RequestParam(required = false, defaultValue = "es-ES") @Validated(Create.class) final String lang,
			@ApiParam(name = "tripleStore", value = "Triple Store", defaultValue = Constants.TRELLIS, required = false)
			@RequestParam(required = false, defaultValue = Constants.TRELLIS) @Validated(Create.class) final String tripleStore,
			@ApiParam(name = "requestDiscovery", value = "Request in discovery library", defaultValue = "true", required = false)
			@RequestParam(required = false, defaultValue = "true") @Validated(Create.class) final boolean requestDiscovery,
			@RequestBody final Object input) {
		this.logger.info("Creating Instance URI..." );

		try {
			if (!Arrays.asList(new String[] {"cat","def","kos","res"}).contains(type)) {
				throw new CustomNotFoundException("Type: " +type +" wrong, the type must be one of this [ cat,def,kos,res ]" );
			}
/*			final String type = Constants.TYPE_REST;*/

			final HashMap map = (HashMap) input;
			final String entity = Utils.getClassNameFromPath(String.valueOf(map.get(Constants.CLASS) != null ? map.get(Constants.CLASS): map.get(Constants.CLASS)));
			if (!Utils.isValidString(entity)) {
				throw new CustomNotFoundException("Attribute @Class (required) is not present");
			}
			String entityNormalized = Utils.toConceptFormat(entity);
			final String pEntity = Utils.getClassNameFromPath((String) (map.get(Constants.CANONICAL_CLASS_NAME) != null ? (map.get(Constants.CANONICAL_CLASS_NAME))	: (map.get(Constants.CANONICAL_CLASS))));
			final String ref = Utils.generateUUIDFromOject(input);
			String entityId = map.containsKey(Constants.ENTITY_ID)?String.valueOf(map.get(Constants.ENTITY_ID)) :
					map.containsKey(Constants.ID)?String.valueOf(map.get(Constants.ID)):null;
			String localId = new String(entityId);
			if (Utils.isValidString(entityId) && !Utils.isValidUUID(entityId)) {
				entityId = Utils.getUUIDFromString(entityId);
			}
			
			boolean found = false;

			logger.info("requestDiscovery value:" + String.valueOf(requestDiscovery));
			if (requestDiscovery!=false) {
				logger.info("requesting similarities");
				LinkedTreeMap<String, Object> similarity = discoveryService.findSimilarEntity(subDomain, tripleStore, entityNormalized, entityId, map);
				logger.info("requesting similarities results:" + ((similarity==null)?"find":"no find"));
				if (similarity != null) {
					logger.info("Found similarities:" + new Gson().toJsonTree(similarity).getAsJsonObject().toString());
					if (similarity.containsKey("entityId")) {
						entityId = similarity.get("entityId").toString();
						found = true;
						logger.info("requestDiscovery similarity entityId:",new Gson().toJsonTree(similarity).getAsJsonObject().toString());
					}
				}
			}
			CanonicalURILanguage canonicalURILanguage = canonicalURILanguageControllerController.save(domain, subDomain,
					lang, type, entity, (Utils.isValidString(entityId) ? entityId : ref),localId, null,
					(pEntity != null) ? pEntity : entity, null, true);

			// response
			final Map<String, String> response = new HashMap<>();
			response.put(Constants.CANONICAL_URI, canonicalURILanguage.getFullParentURI());
			response.put(Constants.LANGUAGE, lang);
			response.put(Constants.CANONICAL_LANGUAGE_URI, canonicalURILanguage.getFullURI());
			if (found) {
				response.put("similarity", "");
			}
			
			this.logger.info(CREATED_INSTANCE, new JSONObject(response));
			return response;

		} catch (final NoSuchAlgorithmException e) {
			this.logger.error(String.format("Error creating resource ID cause: %s",e.getMessage()));
		}

		return null;
	}


	/**
	 * Search the resourceID URI
	 * @param domain
	 * @param subDomain
	 * @param type
	 * @param lang
	 * @param className
	 * @param entityId
	 * @return List of Canonical URIs by Language
	 */
	@ApiOperation(value = "Get Canonical URI for Resource or Instance", notes = "If exist then return URIs")
	@GetMapping(URISController.Mappings.RESOURCE_ID)
	public List<Map<String, String>> getResource(
			@ApiParam(name = "domain", value = "Domain: hercules.org", defaultValue = Constants.DOMAIN_VALUE, required = false)
			@RequestParam(required = false, defaultValue = "hercules.org") @Validated(Create.class) final String domain,
			@ApiParam(name = "subDomain", value = "Subdomain: um (universidad de murcia)", defaultValue = Constants.SUBDOMAIN_VALUE, required = false)
			@RequestParam(required = false, defaultValue = "um") @Validated(Create.class) final String subDomain,
			@ApiParam(name = "type", value = "Type of URI", defaultValue = Constants.TYPE_REST, required = false)
			@RequestParam(required = false) @Validated(Create.class) final String type,
			@ApiParam(name = "lang", value = "Language of data", defaultValue = Constants.SPANISH_LANGUAGE, required = false)
			@RequestParam(required = false) @Validated(Create.class) final String lang,
			@ApiParam(name = "className", required = true)
			@RequestParam(required = true) @Validated(Create.class) final String className,
			@ApiParam(name = "entityId", required = true)
			@RequestParam(required = true) @Validated(Create.class)  String entityId) {
		this.logger.info("Retrieving Instance URI..." );

		final String entity = className;
		if (!Utils.isValidString(entity)) {
			throw new CustomNotFoundException("Parameter className is required");
		}

		if (type!= null && !Arrays.asList(new String[] {"cat","def","kos","res"}).contains(type)) {
			throw new CustomNotFoundException("Type: " +type +" wrong, the type must be one of this [ cat,def,kos,res ]" );
		}

		List<Map<String, String>> canonicalURIsLanguage = canonicalURILanguageControllerController
				.getByReference(className,entityId).stream().filter(cul ->
					cul.getIsInstance() &&
					(!Utils.isValidString(domain) || cul.getDomain().equals(domain)) &&
					(!Utils.isValidString(subDomain) || cul.getSubDomain().equals(subDomain)) &&
					(!Utils.isValidString(type) || cul.getTypeCode().equals(type)) &&
					(!Utils.isValidString(lang) || cul.getLanguageID().equals(lang))
				).map(
						culf -> new HashMap<String, String>() {{
							put(Constants.CANONICAL_URI, culf.getFullParentURI());
							put(Constants.LANGUAGE, culf.getLanguageID());
							put(Constants.CANONICAL_LANGUAGE_URI, culf.getFullURI());
						}}
				).collect(Collectors.toList());

		return canonicalURIsLanguage;
	}

	/**
	 * 
	 * Creates the property URI
	 * <p>
	 * Example
	 * <p>
	 * 
	 * <pre>
	 * {
	 *   obj: {
	 *      Arrobaclass=es.um.asio.service.util.data.ConceptoGrupo, 
	 *      entityId:null, 
	 *      version:0, 
	 *      idGrupoInvestigacion:E0A6-01, 
	 *      numero:5, 
	 *      codTipoConcepto:DESCRIPTORES, 
	 *      texto:LENGUAJES PLASTICOS
	 *    },
	 *    property: idGrupoInvestigacion,
	 *    resourceID: http://hercules.org/um/es-ES/rec/ConceptoGrupo/8d606c3a-13c7-3cc7-ab78-fb8f8595771b,
	 *  }
	 * </pre>
	 * 
	 * @param domain
	 * @param subDomain
	 * @param lang
	 * @param input
	 * @return
	 */
	@ApiOperation(value = "Create or Get Canonical URI for Property", notes = "Allow create canonical URI and canonical URI in language if not exist, if exist then return URIs")
	@ApiImplicitParams({
			@ApiImplicitParam(
					name = "input",
					value = "A JSON where property attribute is required with the name of property in language, canonicalProperty is the canonical name of property without language" +
							" Ej: {\"property\": \"idGrupoInvestigacion\",\"canonicalProperty\": \"idGrupoInvestigacion\" }",
					required = true,
					dataType = "Map",
					paramType = "body",
					examples = @Example(value = {@ExampleProperty(mediaType = "application/json", value = "{foo: whatever, bar: whatever2}")}))})
	@PostMapping(URISController.Mappings.PROPERTY_URI)
	public Map<String, String> createPropertyURI(
			@ApiParam(name = "domain", value = "Domain: hercules.org", defaultValue = Constants.DOMAIN_VALUE, required = false) 
			@RequestParam(required = false, defaultValue = "hercules.org") @Validated(Create.class) final String domain,
			@ApiParam(name = "subDomain", value = "Subdomain: um (universidad de murcia)", defaultValue = Constants.SUBDOMAIN_VALUE, required = false) 
			@RequestParam(required = false, defaultValue = "um") @Validated(Create.class) final String subDomain,
			@ApiParam(name = "type", value = "Type of URI", defaultValue = "def", required = false)
			@RequestParam(required = false, defaultValue = "def") @Validated(Create.class) final String type,
			@ApiParam(name = "lang", value = "Language of data", defaultValue = Constants.SPANISH_LANGUAGE, required = false) 
			@RequestParam(required = false, defaultValue = "es-ES") @Validated(Create.class) final String lang,
			@RequestBody final Object input) {
		this.logger.info("Creating property URI");

		if (!Arrays.asList(new String[] {"cat","def","kos","res"}).contains(type)) {
			throw new CustomNotFoundException("Type: " +type +" wrong, the type must be one of this [cat,def,kos,res]" );
		}

		final HashMap map = (HashMap) input;
		final String property = Utils.getClassNameFromPath((String) map.get(Constants.PROPERTY));
		final String cProperty = Utils.getClassNameFromPath((String) map.get(Constants.CANONICAL_PROPERTY));


		CanonicalURILanguage canonicalURILanguage = canonicalURILanguageControllerController.save(domain, subDomain,
				lang, type, null, null,null, property, null,
				(cProperty != null) ? cProperty : property, true);

		// response
		final Map<String, String> response = new HashMap<>();
		response.put(Constants.CANONICAL_URI, canonicalURILanguage.getFullParentURI());
		response.put(Constants.LANGUAGE, lang);
		response.put(Constants.CANONICAL_LANGUAGE_URI, canonicalURILanguage.getFullURI());
		this.logger.info(CREATED_INSTANCE, new JSONObject(response));
		return response;
	}

	/**
	 * Search the Property Canonical Language URI
	 * @param domain
	 * @param subDomain
	 * @param type
	 * @param lang
	 * @param className
	 * @param entityId
	 * @return List of Canonical URIs by Language
	 */
	@ApiOperation(value = "Get Canonical URI for Resource or Instance", notes = "If exist then return URIs")
	@GetMapping(URISController.Mappings.PROPERTY_URI)
	public List<Map<String, String>> getPropertiesURIS(
			@ApiParam(name = "domain", value = "Domain: hercules.org", defaultValue = Constants.DOMAIN_VALUE, required = false)
			@RequestParam(required = false) @Validated(Create.class) final String domain,
			@ApiParam(name = "subDomain", value = "Subdomain: um (universidad de murcia)", defaultValue = Constants.SUBDOMAIN_VALUE, required = false)
			@RequestParam(required = false) @Validated(Create.class) final String subDomain,
			@ApiParam(name = "type", value = "Type of URI", defaultValue = "def", required = false)
			@RequestParam(required = false) @Validated(Create.class) final String type,
			@ApiParam(name = "lang", value = "Language of data", defaultValue = Constants.SPANISH_LANGUAGE, required = false)
			@RequestParam(required = false) @Validated(Create.class) final String lang,
			@ApiParam(name = "propertyName", required = true)
			@RequestParam(required = true, defaultValue = Constants.TRELLIS) @Validated(Create.class)  String propertyName) {
		this.logger.info("Retrieving Instance URI...." );

		if (type!= null && !Arrays.asList(new String[] {"cat","def","kos","res"}).contains(type)) {
			throw new CustomNotFoundException("Type: " +type +" wrong, the type must be one of this [ cat,def,kos,res ]" );
		}

		List<CanonicalURILanguage> xxx = canonicalURILanguageControllerController.getByProperty(propertyName);
		List<Map<String, String>> canonicalURIsLanguage = canonicalURILanguageControllerController.getByProperty(propertyName).stream().filter(cul ->
						cul.getIsProperty() &&
						(!Utils.isValidString(domain) || cul.getDomain().equals(domain)) &&
						(!Utils.isValidString(subDomain) || cul.getSubDomain().equals(subDomain)) &&
						(!Utils.isValidString(type) || cul.getTypeCode().equals(type)) &&
						(!Utils.isValidString(lang) || cul.getLanguageID().equals(lang))
				).map(
						culf -> new HashMap<String, String>() {{
							put(Constants.CANONICAL_URI, culf.getFullParentURI());
							put(Constants.LANGUAGE, culf.getLanguageID());
							put(Constants.CANONICAL_LANGUAGE_URI, culf.getFullURI());
						}}
				).collect(Collectors.toList());

		return canonicalURIsLanguage;
	}

	/**
	 * Creates the resource type URI
	 * <p>
	 * Example
	 * <p>
	 * 
	 * <pre>
	 * {
	 *   @class: es.um.asio.service.util.data.ConceptoGrupo,
     * }
	 * 	 * </pre>
	 * 	 *
	 * 	 * @param domain
	 * 	 * @param subDomain
	 * 	 * @param lang
	 * @param input
	 * @return
	 */
	@ApiOperation(value = "Create or Get Canonical URI for Class", notes = "Allow create canonical URI and canonical URI in language if not exist, if exist then return URIs")
	@ApiImplicitParams({
			@ApiImplicitParam(
					name = "input",
					value = "A JSON where @class attribute is required with the name of class in language, canonicalClassName or canonicalClass is the canonical name of class without language" +
							" Ej: {\"@class\": \"ConceptoGrupo\",\"canonicalClassName\": \"ConceptoGrupo\" }",
					required = true,
					dataType = "Object",
					paramType = "body",
					examples = @Example(value = {@ExampleProperty(mediaType = "application/json", value = "{foo: whatever, bar: whatever2}")}))})
	@PostMapping(URISController.Mappings.RESOURCE_TYPE_URI)
	public Map<String, String> createResourceTypeURI(
			@ApiParam(name = "domain", value = "Domain: hercules.org", defaultValue = Constants.DOMAIN_VALUE, required = false) 
			@RequestParam(required = true) @Validated(Create.class) final String domain,
			@ApiParam(name = "subDomain", value = "Subdomain: um (universidad de murcia)", defaultValue = Constants.SUBDOMAIN_VALUE, required = false) 
			@RequestParam(required = true) @Validated(Create.class) final String subDomain,
			@ApiParam(name = "type", value = "Type of URI", defaultValue = Constants.TYPE_REST, required = true)
			@RequestParam(required = false, defaultValue = Constants.TYPE_REST) @Validated(Create.class) final String type,
			@ApiParam(name = "lang", value = "Language of data", defaultValue = Constants.SPANISH_LANGUAGE, required = true)
			@RequestParam(required = true) @Validated(Create.class) final String lang,
			@RequestBody final Object input) {
		this.logger.info("Creating resource");

		if (!Arrays.asList(new String[] {"cat","def","kos","res"}).contains(type)) {
			throw new CustomNotFoundException("Type: " +type +" wrong, the type must be one of this [cat,def,kos,res]" );
		}

		final HashMap map = (HashMap) input;
		final String entity = Utils.getClassNameFromPath((String) map.get(Constants.CLASS));
		final String pEntity = Utils.getClassNameFromPath(
				(String) (map.get(Constants.CANONICAL_CLASS_NAME) != null ? (map.get(Constants.CANONICAL_CLASS_NAME))
						: (map.get(Constants.CANONICAL_CLASS))));

		CanonicalURILanguage canonicalURILanguage = canonicalURILanguageControllerController.save(domain, subDomain,
				lang, type, entity, null, null, null,((pEntity != null) ? pEntity : entity), null, true);

		// response
		final Map<String, String> response = new HashMap<>();
		response.put(Constants.CANONICAL_URI, canonicalURILanguage.getFullParentURI());
		response.put(Constants.LANGUAGE, lang);
		response.put(Constants.CANONICAL_LANGUAGE_URI, canonicalURILanguage.getFullURI());
		this.logger.info(CREATED_INSTANCE, new JSONObject(response));
		return response;

	}

	/**
	 * Search the Property Canonical Language URI
	 * @param domain
	 * @param subDomain
	 * @param type
	 * @param lang
	 * @param className
	 * @param entityId
	 * @return List of Canonical URIs by Language
	 */
	@ApiOperation(value = "Get Canonical URI for Resource or Instance", notes = "If exist then return URIs")
	@GetMapping(URISController.Mappings.RESOURCE_TYPE_URI)
	public List<Map<String, String>> getClassURIS(
			@ApiParam(name = "domain", value = "Domain: hercules.org", defaultValue = Constants.DOMAIN_VALUE, required = false)
			@RequestParam(required = false) @Validated(Create.class) final String domain,
			@ApiParam(name = "subDomain", value = "Subdomain: um (universidad de murcia)", defaultValue = Constants.SUBDOMAIN_VALUE, required = false)
			@RequestParam(required = false) @Validated(Create.class) final String subDomain,
			@ApiParam(name = "type", value = "Type of URI", defaultValue = Constants.TYPE_REST, required = false)
			@RequestParam(required = false) @Validated(Create.class) final String type,
			@ApiParam(name = "lang", value = "Language of data", defaultValue = Constants.SPANISH_LANGUAGE, required = false)
			@RequestParam(required = false) @Validated(Create.class) final String lang,
			@ApiParam(name = "className", required = true)
			@RequestParam(required = true) @Validated(Create.class)  String className) {
		this.logger.info("Retrieving Instance URI...." );

		if (type!= null && !Arrays.asList(new String[] {"cat","def","kos","res"}).contains(type)) {
			throw new CustomNotFoundException("Type: " +type +" wrong, the type must be one of this [ cat,def,kos,res ]" );
		}

		List<Map<String, String>> canonicalURIsLanguage = canonicalURILanguageControllerController.get(className).stream().filter(cul ->
				cul.getIsEntity() &&
				(!Utils.isValidString(domain) || cul.getDomain().equals(domain)) &&
				(!Utils.isValidString(subDomain) || cul.getSubDomain().equals(subDomain)) &&
				(!Utils.isValidString(type) || cul.getTypeCode().equals(type)) &&
				(!Utils.isValidString(lang) || cul.getLanguageID().equals(lang))
		).map(
				culf -> new HashMap<String, String>() {{
					put(Constants.CANONICAL_URI, culf.getFullParentURI());
					put(Constants.LANGUAGE, culf.getLanguageID());
					put(Constants.CANONICAL_LANGUAGE_URI, culf.getFullURI());
				}}
		).collect(Collectors.toList());

		return canonicalURIsLanguage;
	}

	/**
	 * Associate a local uri with a canonical uri in a language, for a storage system by CanonicalURIInLanguage
	 * 
	 * @param String canonicalLanguageURI Canonical URI In Language.
     * @param String localURI in URI in storage system.
	 * @param String storageName Storae Name for Local Storage (ej wikibase, trellis, weso-wikibase). If not exists it´s will be created
	 * @return URI for the input property.
	 */
	@ApiOperation(value = "Link a Canonical URI in Language with the local URI in Storage")
	@PostMapping(URISController.Mappings.LOCAL_URI)
	public List<LocalURI> linkCanonicalURIToLocalURI(
			@ApiParam(name = "canonicalLanguageURI", value = "Canonical Uri Language returned in the creation", required = true)
			@RequestParam(required = true) @Validated(Create.class) final String canonicalLanguageURI,
			@ApiParam(name = "localURI", value = "local URI where resource is Storage", required = true) 
			@RequestParam(required = true) @Validated(Create.class) final String localURI,
			@ApiParam(name = "storageName", value = "Storage type by name", required = true) 
			@RequestParam(required = true) @Validated(Create.class) final String storageName) {
		logger.info("Creating Link Canonical URI: {}	\tLocal URI: {}",canonicalLanguageURI,localURI);

		if (!Utils.isValidURL(localURI))
			throw new CustomNotFoundException(NOT_VALID_URI_LOCAL_FORMAT + localURI);
		if (!Utils.isValidURL(canonicalLanguageURI))
			throw new CustomNotFoundException("Not valid format in Canonical URI: " + canonicalLanguageURI);

		CanonicalURILanguage cu = canonicalURILanguageControllerController.getFullURI(canonicalLanguageURI);
		if (cu == null)
			throw new CustomNotFoundException(String.format(STORAGE_NOT_FOUND,storageName));
		StorageType storageType = storageTypeController.get(storageName);
		if (storageType == null)
			throw new CustomNotFoundException(String.format(STORAGE_NOT_FOUND,storageName));

		List<LocalURI> luAux = localURIProxy.getAllByLocalURIStr(localURI);
		if (luAux!=null) {
			for (LocalURI lu : luAux) {
				if (!lu.getCanonicalURILanguageStr().equals(canonicalLanguageURI)) {
					throw new CustomNotFoundException("Local URI : " + localURI + " is used yet in " + lu.getCanonicalURILanguageStr());
				}
			}
		}
		List<LocalURI> lrs = localURIController.getFullURI(canonicalLanguageURI,storageName);
		List<LocalURI> lrsAux = new ArrayList<>();
		LocalURI lr;
		if (lrs.isEmpty()) {
			lr = new LocalURI(localURI, cu, storageType);
			lrsAux.add(lr);
			localURIController.save(lr);
			return lrsAux;
		} else {
			for (LocalURI lu : lrs) {
				lu.setLocalUri(localURI);
				localURIController.save(lu);
				lrsAux.add(lu);
			}
		}
		logger.info("Link Created (Canonical URI: {}\tLocal URI: {})",canonicalLanguageURI,localURI);
		return lrs;
	}

	private void evaluateAndLunchException(boolean condition, String message) {
		if (condition)
			throw  new CustomNotFoundException(message);
	}

	/**
	 * Get a local URI from parameters
	 *
	 * @param String canonicalLanguageURI Canonical URI In Language.
	 * @param String localURI in URI in storage system.
	 * @param String storageName Storae Name for Local Storage (ej wikibase, trellis, weso-wikibase). If not exists it´s will be created
	 * @return URI for the input property.
	 */
	@ApiOperation(value = "GET a Local URI from parameters")
	@GetMapping(URISController.Mappings.LOCAL_ENTITY_URI)
	public Map<String,Object> getLocalStorageURIEntity(
			@ApiParam( name = "domain", value = "Domain Element", required = false, defaultValue = Constants.DOMAIN_VALUE)
			@RequestParam(required = false) @Validated(Create.class) final String domain,
			@ApiParam(name = "subDomain", value = "Sub Domain Element", required = false, defaultValue = Constants.SUBDOMAIN_VALUE)
			@RequestParam(required = false) @Validated(Create.class) final String subDomain,
			@ApiParam(name = "languageCode", value = "Sub Domain Element", required = false, defaultValue = Constants.SPANISH_LANGUAGE)
			@RequestParam(required = false) @Validated(Create.class) final String languageCode,
			@ApiParam(name = "typeCode", value = "Type Code", required = false, defaultValue = Constants.TYPE_REST)
			@RequestParam(required = false, defaultValue = Constants.TYPE_REST) @Validated(Create.class) final String typeCode,
			@ApiParam(name = "entity", value = "entity name in language", required = false)
			@RequestParam(required = true) @Validated(Create.class) final String entity,
			@ApiParam(name = "storageName", value = "Storage type by name", required = true)
			@RequestParam(required = true) @Validated(Create.class) final String storageName) {
		logger.info("Creating Local Entity URI");

		if (!Arrays.asList(new String[] {"cat","def","kos","res"}).contains(typeCode)) {
			throw new CustomNotFoundException("Type: " +typeCode +" wrong, the type must be one of this [cat,def,kos,res]" );
		}

		String defDomain = (domain!=null)?domain:Constants.DOMAIN_VALUE;
		String defSubdomain = (subDomain!=null)?subDomain:Constants.SUBDOMAIN_VALUE;
		String defLang = null;

		if (languageCode == null) {
			List<Language> l = languageProxy.getDefaultLanguages();
			defLang = (l.isEmpty() ? l.get(0).getIso() : Constants.SPANISH_LANGUAGE);
		} else {
			defLang = languageCode;
		}

		LanguageType lt = null;
		Type type = typeProxy.find((typeCode==null)?Constants.TYPE_REST:typeCode).orElse(null);
		if (defLang!=null && type!=null) {
			List<LanguageType> tls = languageTypeProxy.getByLanguageAndType(defLang,type.getCode());
			if (tls.size() == 1 && tls.get(0).getTypeLangCode()!=null) {
				lt = tls.get(0);
			}
		}


		String schema = schemaService.getCanonicalLanguageSchema();
		CanonicalURILanguage aux = new CanonicalURILanguage(defDomain,defSubdomain,lt,entity , null, null, schema);
		CanonicalURILanguage cu =  canonicalURILanguageControllerService.getAllByFullURI(aux.getFullURI());

		evaluateAndLunchException((cu==null),CANONICAL_LANGUAGE_URI_NOT_FOUND);
		StorageType storageType = storageTypeController.get(storageName);
		evaluateAndLunchException((storageType==null),String.format(STORAGE_NOT_FOUND,storageName));

		List<LocalURI> localURIs = localURIController.getFullURI(cu.getFullURI(),storageName);

		Map<String,Object> response = new HashMap<>();
		response.put(Constants.CANONICAL_URI,cu.getFullParentURI());
		response.put(Constants.CANONICAL_LANGUAGE_URI,cu.getFullURI());
		response.put("localURIS",localURIs);
		return response;
	}

	/**
	 * Associate a Entity with a canonical uri in a language, for a storage system by CanonicalURIInLanguage
	 *
	 * @param String canonicalLanguageURI Canonical URI In Language.
	 * @param String localURI in URI in storage system.
	 * @param String storageName Storae Name for Local Storage (ej wikibase, trellis, weso-wikibase). If not exists it´s will be created
	 * @return URI for the input property.
	 */
	@ApiOperation(value = "POST a Local URI from parameters")
	@PostMapping(URISController.Mappings.LOCAL_ENTITY_URI)
	public LocalURI setEntityInLanguageToLocalURI(
			@ApiParam( name = "domain", value = "Domain Element", required = false, defaultValue = Constants.DOMAIN_VALUE)
			@RequestParam(required = false) @Validated(Create.class) final String domain,
			@ApiParam(name = "subDomain", value = "Sub Domain Element", required = false, defaultValue = Constants.SUBDOMAIN_VALUE)
			@RequestParam(required = false) @Validated(Create.class) final String subDomain,
			@ApiParam(name = "languageCode", value = "Sub Domain Element", required = false, defaultValue = Constants.SPANISH_LANGUAGE)
			@RequestParam(required = false) @Validated(Create.class) final String languageCode,
			@ApiParam(name = "typeCode", value = "Type Code", required = false, defaultValue = Constants.TYPE_REST)
			@RequestParam(required = false, defaultValue = Constants.TYPE_REST) @Validated(Create.class) final String typeCode,
			@ApiParam(name = "entity", value = "entity name in language", required = false)
			@RequestParam(required = true) @Validated(Create.class) final String entity,
			@ApiParam(name = "localURI", value = "local URI where resource is Storage", required = true)
			@RequestParam(required = true) @Validated(Create.class) final String localURI,
			@ApiParam(name = "storageName", value = "Storage type by name", required = true)
			@RequestParam(required = true) @Validated(Create.class) final String storageName) {
		logger.info("Creating Local Entity URI");
		if (!Arrays.asList(new String[] {"cat","def","kos","res"}).contains(typeCode)) {
			throw new CustomNotFoundException("Type: " +typeCode +" wrong, the type must be one of this [cat,def,kos,res]" );
		}
		evaluateAndLunchException(!Utils.isValidURL(localURI),"Format of Local URI is wrong: " + localURI);

		String defDomain = (domain!=null)?domain:Constants.DOMAIN_VALUE;
		String defSubdomain = (subDomain!=null)?subDomain:Constants.SUBDOMAIN_VALUE;
		String defLang = null;

		if (languageCode == null) {
			List<Language> l = languageProxy.getDefaultLanguages();
			defLang = (l.isEmpty() ? l.get(0).getIso() : Constants.SPANISH_LANGUAGE);
		} else {
			defLang = languageCode;
		}

		LanguageType lt = null;
		Type type = typeProxy.find((typeCode==null)?Constants.TYPE_REST:typeCode).orElse(null);
		if (defLang!=null && type!=null) {
			List<LanguageType> tls = languageTypeProxy.getByLanguageAndType(defLang,type.getCode());
			if (tls.size() == 1 && tls.get(0).getTypeLangCode()!=null) {
				lt = tls.get(0);
			}
		}


		String schema = schemaService.getCanonicalLanguageSchema();
		CanonicalURILanguage aux = new CanonicalURILanguage(defDomain,defSubdomain,lt,entity , null, null, schema);
		CanonicalURILanguage cu =  canonicalURILanguageControllerService.getAllByFullURI(aux.getFullURI());

		evaluateAndLunchException((cu==null),CANONICAL_LANGUAGE_URI_NOT_FOUND);
		StorageType storageType = storageTypeController.get(storageName);
		evaluateAndLunchException((storageType==null),String.format(STORAGE_NOT_FOUND,storageName));
		LocalURI lr = new LocalURI(localURI, cu, storageType);
		localURIController.save(lr);
		return lr;
	}

	/**
	 * Get a local URI from Resource By ID
	 *
	 * @param String canonicalLanguageURI Canonical URI In Language.
	 * @param String localURI in URI in storage system.
	 * @param String storageName Storae Name for Local Storage (ej wikibase, trellis, weso-wikibase). If not exists it´s will be created
	 * @return URI for the input property.
	 */
	@GetMapping(URISController.Mappings.LOCAL_RESOURCE_ID_URI)
	public Map<String, Object> getLocalStorageURIResource(
			@ApiParam( name = "domain", value = "Domain Element", required = false, defaultValue = Constants.DOMAIN_VALUE)
			@RequestParam(required = false) @Validated(Create.class) final String domain,
			@ApiParam(name = "subDomain", value = "Sub Domain Element", required = false, defaultValue = Constants.SUBDOMAIN_VALUE)
			@RequestParam(required = false) @Validated(Create.class) final String subDomain,
			@ApiParam(name = "languageCode", value = "Sub Domain Element", required = false, defaultValue = Constants.SPANISH_LANGUAGE)
			@RequestParam(required = false) @Validated(Create.class) final String languageCode,
			@ApiParam(name = "typeCode", value = "Type Code", required = false, defaultValue = Constants.TYPE_REST)
			@RequestParam(required = false, defaultValue = Constants.TYPE_REST) @Validated(Create.class) final String typeCode,
			@ApiParam(name = "entity", value = "entity name in language", required = true)
			@RequestParam(required = true) @Validated(Create.class) final String entity,
			@ApiParam(name = "reference", value = "instance id", required = true)
			@RequestParam(required = true) @Validated(Create.class)  String reference,
			@ApiParam(name = "storageName", value = "Storage type by name", required = true)
			@RequestParam(required = true) @Validated(Create.class) final String storageName) {
		logger.info("Get Local Resource URI");
		if (!Arrays.asList(new String[] {"cat","def","kos","res"}).contains(typeCode)) {
			throw new CustomNotFoundException("Type: " +typeCode +" wrong, the type must be one of this [cat,def,kos,res]" );
		}
		String defDomain = (domain!=null)?domain:Constants.DOMAIN_VALUE;
		String defSubdomain = (subDomain!=null)?subDomain:Constants.SUBDOMAIN_VALUE;
		String defLang = null;
		if (languageCode == null) {
			List<Language> l = languageProxy.getDefaultLanguages();
			defLang = (l.isEmpty() ? l.get(0).getIso() : Constants.SPANISH_LANGUAGE);
		} else {
			defLang = languageCode;
		}

		LanguageType lt = null;
		Type type = typeProxy.find((typeCode==null)?Constants.TYPE_REST:typeCode).orElse(null);
		if (defLang!=null && type!=null) {
			List<LanguageType> tls = languageTypeProxy.getByLanguageAndType(defLang,type.getCode());
			if (tls.size() == 1 && tls.get(0).getTypeLangCode()!=null) {
				lt = tls.get(0);
			}
		}

		if (!Utils.isValidUUID(reference)) {
			reference = Utils.getUUIDFromString(reference);
		}

		String schema = schemaService.getCanonicalLanguageSchema();
		CanonicalURILanguage aux = new CanonicalURILanguage(defDomain,defSubdomain,lt,entity , reference, null, schema);


		CanonicalURILanguage cu = canonicalURILanguageControllerService.getAllByFullURI(aux.getFullURI());
		evaluateAndLunchException((cu==null),CANONICAL_LANGUAGE_URI_NOT_FOUND);

		StorageType storageType = storageTypeController.get(storageName);
		evaluateAndLunchException((storageType==null),String.format(STORAGE_NOT_FOUND,storageName));

		List<LocalURI> localURIs = localURIController.getFullURI(cu.getFullURI(),storageName);

		// response
		final Map<String, Object> response = new HashMap<>();
		response.put(Constants.CANONICAL_URI, cu.getFullParentURI());
		response.put(Constants.CANONICAL_LANGUAGE_URI, cu.getFullURI());
		response.put("localURIS",localURIs);
		return response;

	}
	/**
	 * Associate a Resource with a canonical uri in a language, for a storage system by CanonicalURIInLanguage
	 *
	 * @param String canonicalLanguageURI Canonical URI In Language.
	 * @param String localURI in URI in storage system.
	 * @param String storageName Storae Name for Local Storage (ej wikibase, trellis, weso-wikibase). If not exists it´s will be created
	 * @return URI for the input property.
	 */
	@PostMapping(URISController.Mappings.LOCAL_RESOURCE_ID_URI)
	public LocalURI setInstanceInLanguageToLocalURI(
			@ApiParam( name = "domain", value = "Domain Element", required = false, defaultValue = Constants.DOMAIN_VALUE)
			@RequestParam(required = false) @Validated(Create.class) final String domain,
			@ApiParam(name = "subDomain", value = "Sub Domain Element", required = false, defaultValue = Constants.SUBDOMAIN_VALUE)
			@RequestParam(required = false) @Validated(Create.class) final String subDomain,
			@ApiParam(name = "languageCode", value = "Sub Domain Element", required = false, defaultValue = Constants.SPANISH_LANGUAGE)
			@RequestParam(required = false) @Validated(Create.class) final String languageCode,
			@ApiParam(name = "typeCode", value = "Type Code", required = false, defaultValue = Constants.TYPE_REST)
			@RequestParam(required = false, defaultValue = Constants.TYPE_REST) @Validated(Create.class) final String typeCode,
			@ApiParam(name = "entity", value = "entity name in language", required = true)
			@RequestParam(required = true) @Validated(Create.class) final String entity,
			@ApiParam(name = "reference", value = "instance id", required = true)
			@RequestParam(required = true) @Validated(Create.class) String reference,
			@ApiParam(name = "localURI", value = "local URI where resource is Storage", required = true)
			@RequestParam(required = true) @Validated(Create.class) final String localURI,
			@ApiParam(name = "storageName", value = "Storage type by name", required = true)
			@RequestParam(required = true) @Validated(Create.class) final String storageName) {
		logger.info("Creating Local Resource URI: ");
		if (!Utils.isValidURL(localURI))
			throw new CustomNotFoundException(NOT_VALID_URI_LOCAL_FORMAT + localURI);
		if (!Arrays.asList(new String[] {"cat","def","kos","res"}).contains(typeCode)) {
			throw new CustomNotFoundException("Type: " +typeCode +" wrong, the type must be one of this [cat,def,kos,res]" );
		}
		String defDomain = (domain!=null)?domain:Constants.DOMAIN_VALUE;
		String defSubdomain = (subDomain!=null)?subDomain:Constants.SUBDOMAIN_VALUE;
		String defLang = null;
		if (languageCode == null) {
			List<Language> l = languageProxy.getDefaultLanguages();
			defLang = (l.isEmpty() ? l.get(0).getIso() : Constants.SPANISH_LANGUAGE);
		} else {
			defLang = languageCode;
		}

		LanguageType lt = null;
		Type type = typeProxy.find((typeCode==null)?Constants.TYPE_REST:typeCode).orElse(null);
		if (defLang!=null && type!=null) {
			List<LanguageType> tls = languageTypeProxy.getByLanguageAndType(defLang,type.getCode());
			if (tls.size() == 1 && tls.get(0).getTypeLangCode()!=null) {
				lt = tls.get(0);
			}
		}

		if (!Utils.isValidUUID(reference)) {
			reference = Utils.getUUIDFromString(reference);
		}

		String schema = schemaService.getCanonicalLanguageSchema();
		CanonicalURILanguage aux = new CanonicalURILanguage(defDomain,defSubdomain,lt,entity , reference, null, schema);


		CanonicalURILanguage cu = canonicalURILanguageControllerService.getAllByFullURI(aux.getFullURI());
		evaluateAndLunchException((cu==null),CANONICAL_LANGUAGE_URI_NOT_FOUND);

		StorageType storageType = storageTypeController.get(storageName);
		evaluateAndLunchException((storageType==null),String.format(STORAGE_NOT_FOUND,storageName));

		LocalURI lr = new LocalURI(localURI, cu, storageType);
		localURIController.save(lr);
		return lr;
	}
	/**
	 * Associate a Property with a canonical uri in a language, for a storage system by CanonicalURIInLanguage
	 *
	 * @param String canonicalLanguageURI Canonical URI In Language.
	 * @param String localURI in URI in storage system.
	 * @param String storageName Storaae Name for Local Storage (ej wikibase, trellis, weso-wikibase).
	 * @return URI for the input property.
	 */
	@GetMapping(Mappings.LOCAL_PROPERTY_URI)
	public Map<String,Object> getLocalStorageURIProperty(
			@ApiParam( name = "domain", value = "Domain Element", required = false, defaultValue = Constants.DOMAIN_VALUE)
			@RequestParam(required = false) @Validated(Create.class) final String domain,
			@ApiParam(name = "subDomain", value = "Sub Domain Element", required = false, defaultValue = Constants.SUBDOMAIN_VALUE)
			@RequestParam(required = false) @Validated(Create.class) final String subDomain,
			@ApiParam(name = "languageCode", value = "Language", required = false, defaultValue = Constants.SPANISH_LANGUAGE)
			@RequestParam(required = false) @Validated(Create.class) final String languageCode,
			@ApiParam(name = "typeCode", value = "Type Code", required = false, defaultValue = Constants.TYPE_REST)
			@RequestParam(required = false, defaultValue = Constants.TYPE_REST) @Validated(Create.class) final String typeCode,
			@ApiParam(name = "property", value = "property id", required = true)
			@RequestParam(required = true) @Validated(Create.class) final String property,
			@ApiParam(name = "storageName", value = "Storage type by name", required = true)
			@RequestParam(required = true) @Validated(Create.class) final String storageName) {
		logger.info("Get Local Property URI: ");
		if (!Arrays.asList(new String[] {"cat","def","kos","res"}).contains(typeCode)) {
			throw new CustomNotFoundException("Type: " +typeCode +" wrong, the type must be one of this [cat,def,kos,res]" );
		}
		List<CanonicalURILanguage> cus = canonicalURILanguageControllerService.getAllByPropertyNameAndIsProperty(Utils.toASIONormalization(property));


		CanonicalURILanguage cu = null;

		if (cus.isEmpty())
			throw new CustomNotFoundException(CANONICAL_LANGUAGE_URI_NOT_FOUND);
		else if (cus.size() > 1)
			throw new CustomNotFoundException("Ambiguous Canonical Language URI Founds: "+cus.size() +" results");
		else
			cu = cus.get(0);
		StorageType storageType = storageTypeController.get(storageName);
		if (storageType == null)
			throw new CustomNotFoundException(String.format(STORAGE_NOT_FOUND,storageName));
		/*LocalURI lr = new LocalURI(localURI, cu, storageType);
		localURIController.save(lr);
		return lr;*/

		List<LocalURI> localURIS = localURIController.getFullURI(cu.getFullURI(),storageName);

		Map<String,Object> response = new HashMap<>();
		response.put(Constants.CANONICAL_URI,cu.getFullParentURI());
		response.put(Constants.CANONICAL_LANGUAGE_URI,cu.getFullURI());
		response.put("localURIS",localURIS);
		return response;
	}

	/**
	 * Associate a Property with a canonical uri in a language, for a storage system by CanonicalURIInLanguage
	 *
	 * @param String canonicalLanguageURI Canonical URI In Language.
	 * @param String localURI in URI in storage system.
	 * @param String storageName Storae Name for Local Storage (ej wikibase, trellis, weso-wikibase). If not exists it´s will be created
	 * @return URI for the input property.
	 */
	@PostMapping(Mappings.LOCAL_PROPERTY_URI)
	public LocalURI setPropertyInLanguageToLocalURI(
			@ApiParam( name = "domain", value = "Domain Element", required = false, defaultValue = Constants.DOMAIN_VALUE)
			@RequestParam(required = false) @Validated(Create.class) final String domain,
			@ApiParam(name = "subDomain", value = "Sub Domain Element", required = false, defaultValue = Constants.SUBDOMAIN_VALUE)
			@RequestParam(required = false) @Validated(Create.class) final String subDomain,
			@ApiParam(name = "languageCode", value = "Language", required = false, defaultValue = Constants.SPANISH_LANGUAGE)
			@RequestParam(required = false) @Validated(Create.class) final String languageCode,
			@ApiParam(name = "typeCode", value = "Type Code", required = false, defaultValue = Constants.TYPE_REST)
			@RequestParam(required = false, defaultValue = Constants.TYPE_REST) @Validated(Create.class) final String typeCode,
			@ApiParam(name = "property", value = "property name", required = true)
			@RequestParam(required = true) @Validated(Create.class) final String property,
			@ApiParam(name = "localURI", value = "local URI where resource is Storage", required = true)
			@RequestParam(required = true) @Validated(Create.class) final String localURI,
			@ApiParam(name = "storageName", value = "Storage type by name", required = true)
			@RequestParam(required = true) @Validated(Create.class) final String storageName) {
		logger.info("Creating Local Property URI: ");
		if (!Utils.isValidURL(localURI))
			throw new CustomNotFoundException(NOT_VALID_URI_LOCAL_FORMAT + localURI);
		if (!Arrays.asList(new String[] {"cat","def","kos","res"}).contains(typeCode)) {
			throw new CustomNotFoundException("Type: " +typeCode +" wrong, the type must be one of this [cat,def,kos,res]" );
		}
		Utils.toASIONormalization(property);
		List<CanonicalURILanguage> cus = canonicalURILanguageControllerService.getAllByPropertyNameAndIsProperty(Utils.toASIONormalization(property));


		CanonicalURILanguage cu = null;

		if (cus.isEmpty())
			throw new CustomNotFoundException(CANONICAL_LANGUAGE_URI_NOT_FOUND);
		else if (cus.size() > 1)
			throw new CustomNotFoundException("Ambiguous Canonical Language URI Founds: "+cus.size() +" results");
		else
			cu = cus.get(0);
		StorageType storageType = storageTypeController.get(storageName);
		if (storageType == null)
			throw new CustomNotFoundException(String.format(STORAGE_NOT_FOUND,storageName));
		LocalURI lr = new LocalURI(localURI, cu, storageType);
		localURIController.save(lr);
		return lr;
	}

	/**
	 * Delete a local uri with a canonical uri in a language, for a storage system by CanonicalURIInLanguage
	 *
	 * @param String canonicalLanguageURI Canonical URI In Language.
	 * @param String localURI in URI in storage system.
	 * @param String storageName Storae Name for Local Storage (ej wikibase, trellis, weso-wikibase). If not exists it´s will be created
	 * @return URI for the input property.
	 */
	@ApiOperation(value = "Unlink a Canonical URI in Language with the local URI in Storage")
	@DeleteMapping(URISController.Mappings.LOCAL_URI)
	public void unlinkCanonicalURIToLocalURI(
			@ApiParam(name = "canonicalLanguageURI", value = "Canonical Uri Language retorned in the creation", required = true)
			@RequestParam(required = true) @Validated(Create.class) final String canonicalLanguageURI,
			@ApiParam(name = "localURI", value = "local URI where resource is Storage", required = true)
			@RequestParam(required = true) @Validated(Create.class) final String localURI,
			@ApiParam(name = "storageName", value = "Storage type by name", required = true)
			@RequestParam(required = true) @Validated(Create.class) final String storageName) {
		logger.info("UNLinking Canonical URI in Language : {} to Local URI: {} in Storage: {}",canonicalLanguageURI,localURI,storageName);
		if (!Utils.isValidURL(localURI))
			throw new CustomNotFoundException(NOT_VALID_URI_LOCAL_FORMAT + localURI);
		if (!Utils.isValidURL(canonicalLanguageURI))
			throw new CustomNotFoundException("Not valid format URI Canonical:  " + canonicalLanguageURI);
		CanonicalURILanguage cu = canonicalURILanguageControllerController.getFullURI(canonicalLanguageURI);
		if (cu == null)
			throw new CustomNotFoundException("Canonical Language URI: " + canonicalLanguageURI + " Not Found");
		StorageType storageType = storageTypeController.get(storageName);
		if (storageType == null)
			throw new CustomNotFoundException(String.format(STORAGE_NOT_FOUND,storageName));

		for (LocalURI luAux : localURIProxy.getAllByLocalURI(new LocalURI(localURI, cu, storageType)))
			localURIController.deleteURI(luAux.getLocalUri());
		logger.info("UNLinking complete");
	}

	/**
	 * Delete a Entity with a canonical uri in a language, for a storage system by CanonicalURIInLanguage
	 *
	 * @param String canonicalLanguageURI Canonical URI In Language.
	 * @param String localURI in URI in storage system.
	 * @param String storageName Storae Name for Local Storage (ej wikibase, trellis, weso-wikibase). If not exists it´s will be created
	 * @return URI for the input property.
	 */
	@DeleteMapping(URISController.Mappings.LOCAL_ENTITY_URI)
	public void deleteEntityInLanguageToLocalURI(
			@ApiParam( name = "domain", value = "Domain Element", required = false)
			@RequestParam(required = false) @Validated(Create.class) final String domain,
			@ApiParam(name = "subDomain", value = "Sub Domain Element", required = false)
			@RequestParam(required = false) @Validated(Create.class) final String subDomain,
			@ApiParam(name = "languageCode", value = "Sub Domain Element", required = false)
			@RequestParam(required = false) @Validated(Create.class) final String languageCode,
			@ApiParam(name = "typeCode", value = "Type Code", required = false)
			@RequestParam(required = false, defaultValue = Constants.TYPE_REST) @Validated(Create.class) final String typeCode,
			@ApiParam(name = "entity", value = "entity name in language", required = true)
			@RequestParam(required = true) @Validated(Create.class) final String entity,
			@ApiParam(name = "localURI", value = "local URI where resource is Storage", required = true)
			@RequestParam(required = true) @Validated(Create.class) final String localURI,
			@ApiParam(name = "storageName", value = "Storage type by name", required = true)
			@RequestParam(required = true) @Validated(Create.class) final String storageName) {

		if (!Utils.isValidURL(localURI))
			throw new CustomNotFoundException(NOT_VALID_URI_LOCAL_FORMAT + localURI);
		if (!Arrays.asList(new String[] {"cat","def","kos","res"}).contains(typeCode)) {
			throw new CustomNotFoundException("Type: " +typeCode +" wrong, the type must be one of this [cat,def,kos,res]" );
		}
		String defDomain = (domain!=null)?domain:Constants.DOMAIN_VALUE;
		String defSubdomain = (subDomain!=null)?subDomain:Constants.SUBDOMAIN_VALUE;
		String defLang = null;
		if (languageCode == null) {
			List<Language> l = languageProxy.getDefaultLanguages();
			defLang = (l.isEmpty() ? l.get(0).getIso() : Constants.SPANISH_LANGUAGE);
		} else {
			defLang = languageCode;
		}

		LanguageType lt = null;
		Type type = typeProxy.find((typeCode==null)?Constants.TYPE_REST:typeCode).orElse(null);
		if (defLang!=null && type!=null) {
			List<LanguageType> tls = languageTypeProxy.getByLanguageAndType(defLang,type.getCode());
			if (tls.size() == 1 && tls.get(0).getTypeLangCode()!=null) {
				lt = tls.get(0);
			}
		}

		String schema = schemaService.getCanonicalLanguageSchema();
		CanonicalURILanguage aux = new CanonicalURILanguage(defDomain,defSubdomain,lt,entity , null, null, schema);


		CanonicalURILanguage cu = canonicalURILanguageControllerService.getAllByFullURI(aux.getFullURI());
		evaluateAndLunchException((cu==null),CANONICAL_LANGUAGE_URI_NOT_FOUND);

		StorageType storageType = storageTypeController.get(storageName);
		evaluateAndLunchException((storageType==null),String.format(STORAGE_NOT_FOUND,storageName));

		for (LocalURI luAux : localURIProxy.getAllByLocalURI(new LocalURI(localURI, cu, storageType)))
			localURIProxy.delete(luAux);
	}

	/**
	 * Delete a Entity with a canonical uri in a language, for a storage system by CanonicalURIInLanguage
	 *
	 * @param String canonicalLanguageURI Canonical URI In Language.
	 * @param String localURI in URI in storage system.
	 * @param String storageName Storae Name for Local Storage (ej wikibase, trellis, weso-wikibase). If not exists it´s will be created
	 * @return URI for the input property.
	 */
	@DeleteMapping(URISController.Mappings.LOCAL_RESOURCE_ID_URI)
	public void deleteInstanceInLanguageToLocalURI(
			@ApiParam( name = "domain", value = "Domain Element", required = false)
			@RequestParam(required = false) @Validated(Create.class) final String domain,
			@ApiParam(name = "subDomain", value = "Sub Domain Element", required = false)
			@RequestParam(required = false) @Validated(Create.class) final String subDomain,
			@ApiParam(name = "languageCode", value = "Sub Domain Element", required = false)
			@RequestParam(required = false) @Validated(Create.class) final String languageCode,
			@ApiParam(name = "typeCode", value = "Type Code", required = false)
			@RequestParam(required = false, defaultValue = Constants.TYPE_REST) @Validated(Create.class) final String typeCode,
			@ApiParam(name = "entity", value = "entity name in language", required = true)
			@RequestParam(required = true) @Validated(Create.class) final String entity,
			@ApiParam(name = "reference", value = "instance id", required = true)
			@RequestParam(required = true) @Validated(Create.class) final String reference,
			@ApiParam(name = "localURI", value = "local URI where resource is Storage", required = true)
			@RequestParam(required = true) @Validated(Create.class) final String localURI,
			@ApiParam(name = "storageName", value = "Storage type by name", required = true)
			@RequestParam(required = true) @Validated(Create.class) final String storageName) {
		logger.info("Deleting instance To local URI");
		if (!Arrays.asList(new String[] {"cat","def","kos","res"}).contains(typeCode)) {
			throw new CustomNotFoundException("Type: " +typeCode +" wrong, the type must be one of this [cat,def,kos,res]" );
		}
		if (!Utils.isValidURL(localURI))
			throw new CustomNotFoundException(NOT_VALID_URI_LOCAL_FORMAT + localURI);
		String defDomain = (domain!=null)?domain:Constants.DOMAIN_VALUE;
		String defSubdomain = (subDomain!=null)?subDomain:Constants.SUBDOMAIN_VALUE;
		String defLang = null;
		if (languageCode == null) {
			List<Language> l = languageProxy.getDefaultLanguages();
			defLang = (l.isEmpty() ? l.get(0).getIso() : Constants.SPANISH_LANGUAGE);
		} else {
			defLang = languageCode;
		}

		LanguageType lt = null;
		Type type = typeProxy.find((typeCode==null)?Constants.TYPE_REST:typeCode).orElse(null);
		if (defLang!=null && type!=null) {
			List<LanguageType> tls = languageTypeProxy.getByLanguageAndType(defLang,type.getCode());
			if (tls.size() == 1 && tls.get(0).getTypeLangCode()!=null) {
				lt = tls.get(0);
			}
		}

		String schema = schemaService.getCanonicalLanguageSchema();
		CanonicalURILanguage aux = new CanonicalURILanguage(defDomain,defSubdomain,lt,entity , reference, null,schema);


		CanonicalURILanguage cu = canonicalURILanguageControllerService.getAllByFullURI(aux.getFullURI());
		evaluateAndLunchException((cu==null),CANONICAL_LANGUAGE_URI_NOT_FOUND);

		StorageType storageType = storageTypeController.get(storageName);
		evaluateAndLunchException((storageType==null),String.format(STORAGE_NOT_FOUND,storageName));
		for (LocalURI luAux : localURIProxy.getAllByLocalURI(new LocalURI(localURI, cu, storageType))) {
			localURIProxy.delete(luAux);
			logger.info("Deleting instance {}",luAux);
		}
	}



	/**
	 * Associate a Property with a canonical uri in a language, for a storage system by CanonicalURIInLanguage
	 *
	 * @param String canonicalLanguageURI Canonical URI In Language.
	 * @param String localURI in URI in storage system.
	 * @param String storageName Storae Name for Local Storage (ej wikibase, trellis, weso-wikibase). If not exists it´s will be created
	 * @return URI for the input property.
	 */
	@DeleteMapping(Mappings.LOCAL_PROPERTY_URI)
	public void deletePropertyInLanguageToLocalURI(
			@ApiParam( name = "domain", value = "Domain Element", required = false)
			@RequestParam(required = false) @Validated(Create.class) final String domain,
			@ApiParam(name = "subDomain", value = "Sub Domain Element", required = false)
			@RequestParam(required = false) @Validated(Create.class) final String subDomain,
			@ApiParam(name = "languageCode", value = "Sub Domain Element", required = false)
			@RequestParam(required = false) @Validated(Create.class) final String languageCode,
			@ApiParam(name = "typeCode", value = "Type Code", required = false)
			@RequestParam(required = false, defaultValue = Constants.TYPE_REST) @Validated(Create.class) final String typeCode,
			@ApiParam(name = "property", value = "instance id", required = true)
			@RequestParam(required = true) @Validated(Create.class) final String property,
			@ApiParam(name = "localURI", value = "local URI where resource is Storage", required = true)
			@RequestParam(required = true) @Validated(Create.class) final String localURI,
			@ApiParam(name = "storageName", value = "Storage type by name", required = true)
			@RequestParam(required = true) @Validated(Create.class) final String storageName) {
		logger.info("Deleting property in language");
		if (!Arrays.asList(new String[] {"cat","def","kos","res"}).contains(typeCode)) {
			throw new CustomNotFoundException("Type: " +typeCode +" wrong, the type must be one of this [cat,def,kos,res]" );
		}
		if (!Utils.isValidURL(localURI))
			throw new CustomNotFoundException(NOT_VALID_URI_LOCAL_FORMAT + localURI);


		List<CanonicalURILanguage> cus = canonicalURILanguageControllerService.getAllByPropertyNameAndIsProperty(Utils.toASIONormalization(property));

		CanonicalURILanguage cu = null;

		if (cus.isEmpty())
			throw new CustomNotFoundException(CANONICAL_LANGUAGE_URI_NOT_FOUND);
		else if (cus.size() > 1)
			throw new CustomNotFoundException("Ambiguous Canonical Language URI Founds: "+cus.size() +" results");
		else
			cu = cus.get(0);
		StorageType storageType = storageTypeController.get(storageName);
		if (storageType == null)
			throw new CustomNotFoundException(String.format(STORAGE_NOT_FOUND,storageName));
		for (LocalURI luAux : localURIProxy.getAllByLocalURI(new LocalURI(localURI, cu, storageType))) {
			localURIProxy.delete(luAux);
			logger.info("Deleting property {}",luAux);
		}
	}


	/**
	 * Get LocalURIS by canonicalUri, storageName and languageCode
	 *
	 * @param String canonicalUri Canonical URI.
	 * @param String storageName name of storage.
	 * @param String languageCode Language code in ISO 639-1.
	 */
	@GetMapping(Mappings.LOCAL_URI_CANONICAL)
	public List<LocalURI> getLocalStorageFromCanonicalURI(
			@ApiParam( name = "canonicalUri", value = "Canonical URI", required = true)
			@RequestParam(required = true) @Validated(Create.class) final String canonicalUri,
			@ApiParam(name = "storageName", value = "Storage Name", required = true)
			@RequestParam(required = true) @Validated(Create.class) final String storageName,
			@ApiParam(name = "languageCode", value = "Language Code", required = true)
			@RequestParam(required = false) @Validated(Create.class) final String languageCode) {
		logger.info("Deleting property in language");
		List<CanonicalURI> cus =  this.canonicalURIService.getAllByFullURI(canonicalUri);
		if (cus.isEmpty()) {
			throw new CustomNotFoundException("Canonical Uri not fond");
		} else if (cus.size() != 1) {
			throw new CustomNotFoundException("Ambiguous Canonical URI, "+cus.size() + " Canonical URIs found");
		}
		List<LocalURI> localURIS = new ArrayList<>();
		for (CanonicalURILanguage cul: cus.get(0).getCanonicalURILanguages()) {
			if (cul.getLanguageID().equals(languageCode)) {
				localURIS.addAll(this.localURIProxy.getAllByCanonicalURILanguageStrAndStorageTypeStr(cul.getFullURI(), storageName));
			}
		}
		return localURIS;
	}

	/**
	 * Get LocalURIS by canonicalUri, storageName and languageCode
	 *
	 * @param String canonicalUri Canonical URI.
	 * @param String storageName name of storage.
	 * @param String languageCode Language code in ISO 639-1.
	 */
	@GetMapping(Mappings.LOCAL_URI_CANONICAL_LANGUAGE)
	public List<LocalURI> getLocalStorageFromCanonicalLanguageURI(
			@ApiParam( name = "canonicalLanguageUri", value = "Canonical URI", required = true)
			@RequestParam(required = true) @Validated(Create.class) final String canonicalLanguageUri,
			@ApiParam(name = "storageName", value = "Storage Name", required = true)
			@RequestParam(required = true) @Validated(Create.class) final String storageName,
			@ApiParam(name = "languageCode", value = "Language Code", required = true)
			@RequestParam(required = false) @Validated(Create.class) final String languageCode) {
		logger.info("Deleting property in language");

		List<LocalURI> localURIS = new ArrayList<>();
		CanonicalURILanguage cul = this.canonicalURILanguageControllerService.getAllByFullURI(canonicalLanguageUri);

		if (cul.getLanguageID().equals(languageCode)) {
			localURIS.addAll(this.localURIProxy.getAllByCanonicalURILanguageStrAndStorageTypeStr(cul.getFullURI(), storageName));
		}


		return localURIS;
	}

	/**
	 * Get Canonical Language URI from localURI
	 *
	 * @param String localURI Local URI.
	 */
	@GetMapping(Mappings.LOCAL_URI)
	public List<CanonicalURILanguage> getCanonicalURILanguageFromLocalURI(
			@ApiParam( name = "localURI", value = "Local URI", required = true)
			@RequestParam(required = true) @Validated(Create.class) final String localURI)
	{
		logger.info("Deleting property in language");

		List<LocalURI> lus = this.localURIProxy.getAllByLocalURIStr(localURI);
		List<CanonicalURILanguage> canonicalURILanguages = new ArrayList<>();
		for (LocalURI lu : lus) {
			canonicalURILanguages.add(lu.getCanonicalURILanguage());
		}
		return  canonicalURILanguages;
	}


	/**
	 * Get Canonical Language URI from localURI
	 *
	 * @param String localURI Local URI.
	 */
	@GetMapping(Mappings.CANONICAL_LANGUAGES)
	public Map<String, List<Map<String,String>>>   getCanonicalURILanguagesFromCanonicalURI(
			@ApiParam( name = "canonicalURI", value = "Canonical URI", required = true)
			@RequestParam(required = true) @Validated(Create.class) final String canonicalURI,
			@ApiParam( name = "language", value = "Language", required = false)
			@RequestParam(required = false) @Validated(Create.class) final String language)
	{
		logger.info("Get Canonical URIs Language from Canonical URI, filtered by language");

		final Map<String, List<Map<String,String>>> response = new HashMap<>();

		List<CanonicalURI> cus = this.canonicalURIService.getAllByFullURI(canonicalURI);
		for (CanonicalURI cu : cus) {
			CanonicalURILanguage defaultLanguageCUL = null;
			List<CanonicalURILanguage> filteredCULs = new ArrayList<>();
			for (CanonicalURILanguage cul : cu.getCanonicalURILanguages()) {
				if (Utils.isValidString(language)) {
					if (cul.getLanguageID().equals(language))
						filteredCULs.add(cul);
				} else {
					filteredCULs.add(cul);
				}

				if (cul.getLanguage().getIsDefault())
					defaultLanguageCUL = cul;
			}
			if (filteredCULs.isEmpty() && defaultLanguageCUL !=null)
				filteredCULs.add(defaultLanguageCUL);
			if (!filteredCULs.isEmpty()) {
				response.put(cu.getFullURI(),new ArrayList<>());
				for (CanonicalURILanguage filerCul : filteredCULs) {
					Map<String, String> culRes = new HashMap<>();
					culRes.put("languageIso", filerCul.getLanguageID());
					culRes.put("isDefaultLanguage", String.valueOf(filerCul.getLanguage().getIsDefault()));
					culRes.put("canonicalURILanguage", filerCul.getFullURI());
					response.get(cu.getFullURI()).add(culRes);
				}

			}
		}

		return response;
	}

	@GetMapping(Mappings.CANONICAL_SCHEMA)
	public String getCanonicalSchema() {
		return schemaService.getCanonicalSchema();
	}

	@GetMapping(Mappings.CANONICAL_LOCAL_SCHEMA)
	public String getLocalCanonicalSchema() {
		return schemaService.getCanonicalLanguageSchema();
	}

	@GetMapping(Mappings.BUILD_CANONICAL_SCHEMA)
	public String getBuilderCanonicalSchema(
			@ApiParam(name = "domain", value = "Domain: hercules.org", defaultValue = Constants.DOMAIN_VALUE, required = true)
			@RequestParam(required = true, defaultValue = "hercules.org") @Validated(Create.class) final String domain,
			@ApiParam(name = "subDomain", value = "Subdomain: um (universidad de murcia)", defaultValue = Constants.SUBDOMAIN_VALUE, required = true)
			@RequestParam(required = true, defaultValue = Constants.SUBDOMAIN_VALUE) @Validated(Create.class) final String subDomain,
			@ApiParam(name = "type", value = "res", defaultValue = Constants.TYPE_REST, required = true)
			@RequestParam(required = true, defaultValue =  Constants.TYPE_REST) @Validated(Create.class) final String type,
			@ApiParam(name = "entity", value = "", required = false)
			@RequestParam(required = false) @Validated(Create.class) final String entity,
			@ApiParam(name = "reference", value = "", required = false)
			@RequestParam(required = false) @Validated(Create.class) final String reference
	) {
		if (!Arrays.asList(new String[] {"cat","def","kos","res"}).contains(type)) {
			throw new CustomNotFoundException("Type: " +type +" wrong, the type must be one of this [cat,def,kos,res]" );
		}
		return schemaService.buildCanonical(domain,subDomain,type,entity,reference);
	}

	@GetMapping(Mappings.BUILD_CANONICAL_LOCAL_SCHEMA)
	public String getBuilderCanonicalLanguageSchema(
			@ApiParam(name = "domain", value = "Domain: hercules.org", defaultValue = Constants.DOMAIN_VALUE, required = true)
			@RequestParam(required = true, defaultValue = "hercules.org") @Validated(Create.class) final String domain,
			@ApiParam(name = "subDomain", value = "Subdomain: um (universidad de murcia)", defaultValue = Constants.SUBDOMAIN_VALUE, required = true)
			@RequestParam(required = true, defaultValue = Constants.LANGUAGE) @Validated(Create.class) final String subDomain,
			@ApiParam(name = "language", value = "es-ES", defaultValue = Constants.LANGUAGE, required = true)
			@RequestParam(required = true, defaultValue = Constants.SUBDOMAIN_VALUE) @Validated(Create.class) final String language,
			@ApiParam(name = "type", value = "res", defaultValue = Constants.TYPE_REST, required = true)
			@RequestParam(required = true, defaultValue =  Constants.TYPE_REST) @Validated(Create.class) final String type,
			@ApiParam(name = "entity", value = "", required = false)
			@RequestParam(required = false) @Validated(Create.class) final String entity,
			@ApiParam(name = "reference", value = "", required = false)
			@RequestParam(required = false) @Validated(Create.class) final String reference
	) {
		return schemaService.buildCanonicalLanguage(domain,subDomain,language,type,entity,reference);
	}


	@GetMapping(Mappings.GET_ALL_LOCAL_STORAGES)
	public List<String> getAllLocalStorages(
	) {
		return storageTypeProxy.findAll().stream().map(st -> st.getName()).collect(Collectors.toList());
	}

	/**
	 * Mappings.
	 */

	/**
	 * Instantiates a new mappings.
	 */
	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	static final class Mappings {

		/** The Constant ROOT_URI. */
		public static final String ROOT_URI = "root/uri";

		/** The Constant RESOURCE_ID. */
		public static final String RESOURCE_ID = "canonical/resource";

		/** The Constant PROPERTY_URI. */
		public static final String PROPERTY_URI = "canonical/property";

		/** The Constant RESOURCE_TYPE_URI. */
		public static final String RESOURCE_TYPE_URI = "canonical/entity";

		/** The Constant RESOURCE_TYPE_URI. */
		public static final String CANONICAL_LANGUAGES = "canonical/languages";

		/** The Constant LOCAL_RESOURCE. */
		public static final String LOCAL_URI = "local";

		/** The Constant LOCAL_URI_CANONICAL. */
		public static final String LOCAL_URI_CANONICAL = "local/canonical";

		/** The Constant LOCAL_URI_CANONICAL. */
		public static final String LOCAL_URI_CANONICAL_LANGUAGE = "local/canonical/language";


		/** The Constant LOCAL_RESOURCE. */
		public static final String LOCAL_ENTITY_URI = "local/entity";

		/** The Constant LOCAL_RESOURCE. */
		public static final String LOCAL_RESOURCE_ID_URI = "local/resource";

		/** The Constant LOCAL_RESOURCE. */
		public static final String LOCAL_PROPERTY_URI = "local/property";

		/** The Constant LOCAL_RESOURCE. */
		public static final String HEALTH = "health";

		/**
		 * Controller request mapping.
		 */
		protected static final String BASE = "/uri-factory";

		/** The Constant CANONICAL_SCHEMA. */
		protected static final String CANONICAL_SCHEMA = "/schema";

		/** The Constant CANONICAL_LOCAL_SCHEMA. */
		protected static final String CANONICAL_LOCAL_SCHEMA = "/local-schema";

		/** The Constant CANONICAL_SCHEMA. */
		protected static final String BUILD_CANONICAL_SCHEMA = "/schema/build";

		/** The Constant CANONICAL_LOCAL_SCHEMA. */
		protected static final String BUILD_CANONICAL_LOCAL_SCHEMA = "/local-schema/build";

		/** The Constant CANONICAL_LOCAL_SCHEMA. */
		protected static final String GET_ALL_LOCAL_STORAGES = "/local-storages";

	}
}
