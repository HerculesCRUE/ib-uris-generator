package es.um.asio.back.controller;

import es.um.asio.abstractions.constants.Constants;
import es.um.asio.back.controller.error.CustomNotFoundException;
import es.um.asio.back.controller.uri.CanonicalURILanguageController;
import es.um.asio.back.controller.uri.LocalURIController;
import es.um.asio.back.controller.uri.StorageTypeController;
import es.um.asio.service.model.*;
import es.um.asio.service.proxy.LanguageProxy;
import es.um.asio.service.proxy.LanguageTypeProxy;
import es.um.asio.service.proxy.LocalURIProxy;
import es.um.asio.service.proxy.TypeProxy;
import es.um.asio.service.service.CanonicalURILanguageService;
import es.um.asio.service.service.SchemaService;
import es.um.asio.service.util.Utils;
import es.um.asio.service.validation.group.Create;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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
	 * Schema Service
	 */
	@Autowired
	private SchemaService schemaService;


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
			@ApiParam(name = "lang", value = "Language of data", defaultValue = Constants.SPANISH_LANGUAGE, required = false)
			@RequestParam(required = false, defaultValue = "es-ES") @Validated(Create.class) final String lang,
			@RequestBody final Object input) {
		this.logger.info("Creating Instance URI..." );
		try {
			final String type = Constants.TYPE_REST;

			final HashMap map = (HashMap) input;
			final String entity = Utils.getClassNameFromPath((String) map.get(Constants.CLASS));
			final String pEntity = Utils.getClassNameFromPath((String) (map.get(Constants.CANONICAL_CLASS_NAME) != null ? (map.get(Constants.CANONICAL_CLASS_NAME))	: (map.get(Constants.CANONICAL_CLASS))));
			final String ref = Utils.generateUUIDFromOject(input);
			String entityId = Utils.getClassNameFromPath((String) (map.get(Constants.ENTITY_ID) != null ? (map.get(Constants.ENTITY_ID)) : (map.get(Constants.ID))));
			

			CanonicalURILanguage canonicalURILanguage = canonicalURILanguageControllerController.save(domain, subDomain,
					lang, type, entity, (Utils.isValidString(entityId) ? entityId : ref), null,
					(pEntity != null) ? pEntity : entity, null, true);

			// response
			final Map<String, String> response = new HashMap<>();
			response.put(Constants.CANONICAL_URI, canonicalURILanguage.getFullParentURI());
			response.put(Constants.LANGUAGE, lang);
			response.put(Constants.CANONICAL_LANGUAGE_URI, canonicalURILanguage.getFullURI());
			this.logger.info(CREATED_INSTANCE, new JSONObject(response));
			return response;

		} catch (final NoSuchAlgorithmException e) {
			this.logger.error(String.format("Error creating resource ID cause: %s",e.getMessage()));
		}

		return null;
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
			@ApiParam(name = "lang", value = "Language of data", defaultValue = Constants.SPANISH_LANGUAGE, required = false) 
			@RequestParam(required = false, defaultValue = "es-ES") @Validated(Create.class) final String lang,
			@RequestBody final Object input) {
		this.logger.info("Creating property URI");

		final String type = Constants.TYPE_REST;

		final HashMap map = (HashMap) input;
		final String property = Utils.getClassNameFromPath((String) map.get(Constants.PROPERTY));
		final String cProperty = Utils.getClassNameFromPath((String) map.get(Constants.CANONICAL_PROPERTY));


		CanonicalURILanguage canonicalURILanguage = canonicalURILanguageControllerController.save(domain, subDomain,
				lang, type, null, null, property, null,
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
			@ApiParam(name = "lang", value = "Language of data", defaultValue = Constants.SPANISH_LANGUAGE, required = false) 
			@RequestParam(required = true) @Validated(Create.class) final String lang,
			@RequestBody final Object input) {
		this.logger.info("Creating resource");

		final String type = Constants.TYPE_REST;

		final HashMap map = (HashMap) input;
		final String entity = Utils.getClassNameFromPath((String) map.get(Constants.CLASS));
		final String pEntity = Utils.getClassNameFromPath(
				(String) (map.get(Constants.CANONICAL_CLASS_NAME) != null ? (map.get(Constants.CANONICAL_CLASS_NAME))
						: (map.get(Constants.CANONICAL_CLASS))));

		CanonicalURILanguage canonicalURILanguage = canonicalURILanguageControllerController.save(domain, subDomain,
				lang, type, entity, null, null, (pEntity != null) ? pEntity : entity, null, true);

		// response
		final Map<String, String> response = new HashMap<>();
		response.put(Constants.CANONICAL_URI, canonicalURILanguage.getFullParentURI());
		response.put(Constants.LANGUAGE, lang);
		response.put(Constants.CANONICAL_LANGUAGE_URI, canonicalURILanguage.getFullURI());
		this.logger.info(CREATED_INSTANCE, new JSONObject(response));
		return response;

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
	 * Associate a Entity with a canonical uri in a language, for a storage system by CanonicalURIInLanguage
	 *
	 * @param String canonicalLanguageURI Canonical URI In Language.
	 * @param String localURI in URI in storage system.
	 * @param String storageName Storae Name for Local Storage (ej wikibase, trellis, weso-wikibase). If not exists it´s will be created
	 * @return URI for the input property.
	 */
	@ApiOperation(value = "Associate a Entity with a canonical uri in a language, for a storage system by CanonicalURIInLanguage")
	@PostMapping(URISController.Mappings.LOCAL_ENTITY_URI)
	public LocalURI setEntityInLanguageToLocalURI(
			@ApiParam( name = "domain", value = "Domain Element", required = false, defaultValue = Constants.DOMAIN_VALUE)
			@RequestParam(required = false) @Validated(Create.class) final String domain,
			@ApiParam(name = "subDomain", value = "Sub Domain Element", required = false, defaultValue = Constants.SUBDOMAIN_VALUE)
			@RequestParam(required = false) @Validated(Create.class) final String subDomain,
			@ApiParam(name = "languageCode", value = "Sub Domain Element", required = false, defaultValue = Constants.SPANISH_LANGUAGE)
			@RequestParam(required = false) @Validated(Create.class) final String languageCode,
			@ApiParam(name = "typeCode", value = "Type Code", required = false, defaultValue = Constants.TYPE_REST)
			@RequestParam(required = false) @Validated(Create.class) final String typeCode,
			@ApiParam(name = "entity", value = "entity name in language", required = false)
			@RequestParam(required = true) @Validated(Create.class) final String entity,
			@ApiParam(name = "localURI", value = "local URI where resource is Storage", required = true)
			@RequestParam(required = true) @Validated(Create.class) final String localURI,
			@ApiParam(name = "storageName", value = "Storage type by name", required = true)
			@RequestParam(required = true) @Validated(Create.class) final String storageName) {
		logger.info("Creating Local Entity URI");

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
		if (typeCode == null) {
			Type type = typeProxy.find(Constants.TYPE_REST).orElse(null);
			if (defLang!=null && type!=null) {
				List<LanguageType> tls = languageTypeProxy.getByLanguageAndType(defLang,type.getCode());
				if (tls.size() == 1 && tls.get(0).getTypeLangCode()!=null) {
					lt = tls.get(0);
				}

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
	 * Associate a Entity with a canonical uri in a language, for a storage system by CanonicalURIInLanguage
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
			@RequestParam(required = false) @Validated(Create.class) final String typeCode,
			@ApiParam(name = "entity", value = "entity name in language", required = true)
			@RequestParam(required = true) @Validated(Create.class) final String entity,
			@ApiParam(name = "reference", value = "instance id", required = true)
			@RequestParam(required = true) @Validated(Create.class) final String reference,
			@ApiParam(name = "localURI", value = "local URI where resource is Storage", required = true)
			@RequestParam(required = true) @Validated(Create.class) final String localURI,
			@ApiParam(name = "storageName", value = "Storage type by name", required = true)
			@RequestParam(required = true) @Validated(Create.class) final String storageName) {
		logger.info("Creating Local Resource URI: ");
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
		if (typeCode != null) {
			Type type = typeProxy.find(Constants.TYPE_REST).orElse(null);
			if (defLang!=null && type!=null) {
				List<LanguageType> tls = languageTypeProxy.getByLanguageAndType(defLang,type.getCode());
				if (tls.size() == 1 && tls.get(0).getTypeLangCode()!=null) {
					lt = tls.get(0);
				}
			}
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
			@RequestParam(required = false) @Validated(Create.class) final String typeCode,
			@ApiParam(name = "property", value = "instance id", required = true)
			@RequestParam(required = true) @Validated(Create.class) final String property,
			@ApiParam(name = "localURI", value = "local URI where resource is Storage", required = true)
			@RequestParam(required = true) @Validated(Create.class) final String localURI,
			@ApiParam(name = "storageName", value = "Storage type by name", required = true)
			@RequestParam(required = true) @Validated(Create.class) final String storageName) {
		logger.info("Creating Local Property URI: ");
		if (!Utils.isValidURL(localURI))
			throw new CustomNotFoundException(NOT_VALID_URI_LOCAL_FORMAT + localURI);

		List<CanonicalURILanguage> cus = canonicalURILanguageControllerService.getAllByEntityNameAndPropertyName(null,property);


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
			localURIController.deleteURI(luAux);
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
			@RequestParam(required = false) @Validated(Create.class) final String typeCode,
			@ApiParam(name = "entity", value = "entity name in language", required = true)
			@RequestParam(required = true) @Validated(Create.class) final String entity,
			@ApiParam(name = "localURI", value = "local URI where resource is Storage", required = true)
			@RequestParam(required = true) @Validated(Create.class) final String localURI,
			@ApiParam(name = "storageName", value = "Storage type by name", required = true)
			@RequestParam(required = true) @Validated(Create.class) final String storageName) {

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
		if (typeCode == null) {
			Type type = typeProxy.find(Constants.TYPE_REST).orElse(null);
			if (type!=null) {
				List<LanguageType> tls = languageTypeProxy.getByLanguageAndType(defLang,type.getCode());
				if (tls.size() == 1 && tls.get(0).getTypeLangCode()!=null) {
					lt = tls.get(0);
				}

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
			@RequestParam(required = false) @Validated(Create.class) final String typeCode,
			@ApiParam(name = "entity", value = "entity name in language", required = true)
			@RequestParam(required = true) @Validated(Create.class) final String entity,
			@ApiParam(name = "reference", value = "instance id", required = true)
			@RequestParam(required = true) @Validated(Create.class) final String reference,
			@ApiParam(name = "localURI", value = "local URI where resource is Storage", required = true)
			@RequestParam(required = true) @Validated(Create.class) final String localURI,
			@ApiParam(name = "storageName", value = "Storage type by name", required = true)
			@RequestParam(required = true) @Validated(Create.class) final String storageName) {
		logger.info("Deleting instance To local URI");
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
		if (typeCode == null) {
			Type type = typeProxy.find(Constants.TYPE_REST).orElse(null);
			if (type!=null) {
				List<LanguageType> tls = languageTypeProxy.getByLanguageAndType(defLang,type.getCode());
				if (tls.size() == 1 && tls.get(0).getTypeLangCode()!=null) {
					lt = tls.get(0);
				}
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
			@RequestParam(required = false) @Validated(Create.class) final String typeCode,
			@ApiParam(name = "property", value = "instance id", required = true)
			@RequestParam(required = true) @Validated(Create.class) final String property,
			@ApiParam(name = "localURI", value = "local URI where resource is Storage", required = true)
			@RequestParam(required = true) @Validated(Create.class) final String localURI,
			@ApiParam(name = "storageName", value = "Storage type by name", required = true)
			@RequestParam(required = true) @Validated(Create.class) final String storageName) {
		logger.info("Deleting property in language");
		if (!Utils.isValidURL(localURI))
			throw new CustomNotFoundException(NOT_VALID_URI_LOCAL_FORMAT + localURI);


		List<CanonicalURILanguage> cus = canonicalURILanguageControllerService.getAllByEntityNameAndPropertyName(null,property);

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

		/** The Constant LOCAL_RESOURCE. */
		public static final String LOCAL_URI = "local";

		/** The Constant LOCAL_RESOURCE. */
		public static final String LOCAL_ENTITY_URI = "local/entity";

		/** The Constant LOCAL_RESOURCE. */
		public static final String LOCAL_RESOURCE_ID_URI = "local/resource";

		/** The Constant LOCAL_RESOURCE. */
		public static final String LOCAL_PROPERTY_URI = "local/property";

		/**
		 * Controller request mapping.
		 */
		protected static final String BASE = "/uri-factory";

	}
}
