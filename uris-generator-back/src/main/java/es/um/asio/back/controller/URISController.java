package es.um.asio.back.controller;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.um.asio.service.model.*;
import es.um.asio.service.proxy.LanguageProxy;
import es.um.asio.service.proxy.LanguageTypeProxy;
import es.um.asio.service.proxy.LocalURIProxy;
import es.um.asio.service.proxy.TypeProxy;
import es.um.asio.service.service.CanonicalURILanguageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import es.um.asio.abstractions.constants.Constants;
import es.um.asio.back.controller.error.CustomNotFoundException;
import es.um.asio.back.controller.uri.CanonicalURILanguageController;
import es.um.asio.back.controller.uri.LocalURIController;
import es.um.asio.back.controller.uri.StorageTypeController;
import es.um.asio.service.util.Utils;
import es.um.asio.service.validation.group.Create;
import io.swagger.annotations.ApiParam;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * URIS controller.
 */
@RestController
@RequestMapping(URISController.Mappings.BASE)
public class URISController {

	/** The logger. */
	private final Logger logger = LoggerFactory.getLogger(URISController.class);

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
	 * Returns the root URI
	 * 
	 * @return
	 */
	@GetMapping(URISController.Mappings.ROOT_URI)
	public String rootURI() {
		return Constants.ROOT_URI;
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
	 *      Arrobaclass:es.um.asio.service.util.data.ConceptoGrupo,
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
	@SuppressWarnings({ "rawtypes" })
	@PostMapping(URISController.Mappings.RESOURCE_ID)
	public Map<String, String> createResourceID(
			@ApiParam(name = "domain", value = "Domain: hercules.org", defaultValue = Constants.DOMAIN_VALUE, required = false) 
			@RequestParam(required = true) @Validated(Create.class) final String domain,
			@ApiParam(name = "subDomain", value = "Subdomain: um (universidad de murcia)", defaultValue = Constants.SUBDOMAIN_VALUE, required = false) 
			@RequestParam(required = true) @Validated(Create.class) final String subDomain,
			@ApiParam(name = "lang", value = "Language of data", defaultValue = Constants.SPANISH_LANGUAGE, required = false) 
			@RequestParam(required = true) @Validated(Create.class) final String lang,
			@RequestBody final Object input) {
		this.logger.info("Creating resource ID");
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
			return response;

		} catch (final NoSuchAlgorithmException e) {
			logger.error("Error creating resource ID cause: " + e.getMessage());
			e.printStackTrace();
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
	@SuppressWarnings({ "rawtypes" })
	@PostMapping(URISController.Mappings.PROPERTY_URI)
	public Map<String, String> createPropertyURI(
			@ApiParam(name = "domain", value = "Domain: hercules.org", defaultValue = Constants.DOMAIN_VALUE, required = false) 
			@RequestParam(required = true) @Validated(Create.class) final String domain,
			@ApiParam(name = "subDomain", value = "Subdomain: um (universidad de murcia)", defaultValue = Constants.SUBDOMAIN_VALUE, required = false) 
			@RequestParam(required = true) @Validated(Create.class) final String subDomain,
			@ApiParam(name = "lang", value = "Language of data", defaultValue = Constants.SPANISH_LANGUAGE, required = false) 
			@RequestParam(required = true) @Validated(Create.class) final String lang,
			@RequestBody final Object input) {
		this.logger.info("Creating property URI");

		final String type = Constants.TYPE_REST;

		final HashMap map = (HashMap) input;
		final String property = Utils.getClassNameFromPath((String) map.get(Constants.PROPERTY));
		final String cProperty = Utils.getClassNameFromPath((String) map.get(Constants.CANONICAL_PROPERTY));
		// final String entity = Utils.getClassNameFromPath((String) map.get(Constants.CLASS));

		HashMap objMap = (HashMap) map.get(Constants.OBJECT);
		/*
		String pEntity = Utils.getClassNameFromPath((String) (objMap.get(Constants.CANONICAL_CLASS_NAME) != null
				? (objMap.get(Constants.CANONICAL_CLASS_NAME))
				: (objMap.get(Constants.CANONICAL_CLASS))));
		 */

		CanonicalURILanguage canonicalURILanguage = canonicalURILanguageControllerController.save(domain, subDomain,
				lang, type, null, null, property, null,
				(cProperty != null) ? cProperty : property, true);

		// response
		final Map<String, String> response = new HashMap<>();
		response.put(Constants.CANONICAL_URI, canonicalURILanguage.getFullParentURI());
		response.put(Constants.LANGUAGE, lang);
		response.put(Constants.CANONICAL_LANGUAGE_URI, canonicalURILanguage.getFullURI());

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
	 * </pre>
	 * 
	 * @param domain
	 * @param subDomain
	 * @param lang
	 * @param input
	 * @return
	 */
	@SuppressWarnings({ "rawtypes" })
	@PostMapping(URISController.Mappings.RESOURCE_TYPE_URI)
	public Map<String, String> createResourceTypeURI(
			@ApiParam(name = "domain", value = "Domain: hercules.org", defaultValue = Constants.DOMAIN_VALUE, required = false) 
			@RequestParam(required = true) @Validated(Create.class) final String domain,
			@ApiParam(name = "subDomain", value = "Subdomain: um (universidad de murcia)", defaultValue = Constants.SUBDOMAIN_VALUE, required = false) 
			@RequestParam(required = true) @Validated(Create.class) final String subDomain,
			@ApiParam(name = "lang", value = "Language of data", defaultValue = Constants.SPANISH_LANGUAGE, required = false) 
			@RequestParam(required = true) @Validated(Create.class) final String lang,
			@RequestBody final Object input) {
		this.logger.info("Creating resource type uri: ");

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
	@PostMapping(URISController.Mappings.LOCAL_URI)
	public LocalURI linkCanonicalURIToLocalURI(
			@ApiParam(name = "canonicalLanguageURI", value = "Canonical Uri Language retorned in the creation", required = true) 
			@RequestParam(required = true) @Validated(Create.class) final String canonicalLanguageURI,
			@ApiParam(name = "localURI", value = "local URI where resource is Storage", required = true) 
			@RequestParam(required = true) @Validated(Create.class) final String localURI,
			@ApiParam(name = "storageName", value = "Storage type by name", required = true) 
			@RequestParam(required = true) @Validated(Create.class) final String storageName) {
		logger.info("Creating resource type uri: ");

		CanonicalURILanguage cu = canonicalURILanguageControllerController.getFullURI(canonicalLanguageURI);
		if (cu == null)
			throw new CustomNotFoundException("Canonical Language URI: " + canonicalLanguageURI + " Not Found");
		StorageType storageType = storageTypeController.get(storageName);
		if (storageType == null)
			throw new CustomNotFoundException("Storage type " + storageName + " Not Found");
		if (Utils.isValidURL(localURI))
			throw new CustomNotFoundException("Not valid format URI Local:  " + localURI);
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
	@PostMapping(URISController.Mappings.LOCAL_ENTITY_URI)
	public LocalURI setEntityInLanguageToLocalURI(
			@ApiParam( name = "domain", value = "Domain Element", defaultValue = "hercules.org", required = false)
			@RequestParam(required = false) @Validated(Create.class) final String domain,
			@ApiParam(name = "subDomain", value = "Sub Domain Element", defaultValue = "um", required = false)
			@RequestParam(required = false) @Validated(Create.class) final String subDomain,
			@ApiParam(name = "languageCode", value = "Sub Domain Element", defaultValue = "es-ES", required = false)
			@RequestParam(required = false) @Validated(Create.class) final String languageCode,
			@ApiParam(name = "typeCode", value = "Type Code", defaultValue = "res", required = false)
			@RequestParam(required = false) @Validated(Create.class) final String typeCode,
			@ApiParam(name = "entity", value = "entity name in language", required = true)
			@RequestParam(required = true) @Validated(Create.class) final String entity,
			@ApiParam(name = "localURI", value = "local URI where resource is Storage", required = true)
			@RequestParam(required = true) @Validated(Create.class) final String localURI,
			@ApiParam(name = "storageName", value = "Storage type by name", required = true)
			@RequestParam(required = true) @Validated(Create.class) final String storageName) {
		logger.info("Creating resource type uri: ");

		String defDomain = (domain!=null)?domain:Constants.DOMAIN_VALUE;
		String defSubdomain = (subDomain!=null)?subDomain:Constants.SUBDOMAIN_VALUE;
		String defLang = null;
		String defType = Constants.TYPE_REST;
		if (languageCode == null) {
			List<Language> l = languageProxy.getDefaultLanguages();
			defLang = (l.size() > 0 ? l.get(0).getISO() : Constants.SPANISH_LANGUAGE);
		} else
			defLang = languageCode;

		if (typeCode == null) {
			Type type = typeProxy.find(Constants.TYPE_REST).orElse(null);
			if (defLang!=null && type!=null) {
				List<LanguageType> tls = languageTypeProxy.getByLanguageAndType(defLang,type.getCode());
				if (tls.size() == 1 && tls.get(0).getTypeLangCode()!=null)
					defType = tls.get(0).getTypeLangCode();
			}
		}

		List<CanonicalURILanguage> cus = canonicalURILanguageControllerService.getAllByElements(
				defDomain,
				defSubdomain,
				defLang,
				defType,
				entity,
				null
		);

		CanonicalURILanguage cu = null;

		if (cus.size() == 0)
			throw new CustomNotFoundException("Canonical Language URI  Not Found");
		else if (cus.size() > 1)
			throw new CustomNotFoundException("Ambiguous Canonical Language URI Founds: "+cus.size() +" results");
		else
			cu = cus.get(0);
		StorageType storageType = storageTypeController.get(storageName);
		if (storageType == null)
			throw new CustomNotFoundException("Storage type " + storageName + " Not Found");
		if (Utils.isValidURL(localURI))
			throw new CustomNotFoundException("Not valid format URI Local:  " + localURI);
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
			@ApiParam( name = "domain", value = "Domain Element", defaultValue = "hercules.org", required = false)
			@RequestParam(required = false) @Validated(Create.class) final String domain,
			@ApiParam(name = "subDomain", value = "Sub Domain Element", defaultValue = "um", required = false)
			@RequestParam(required = false) @Validated(Create.class) final String subDomain,
			@ApiParam(name = "languageCode", value = "Sub Domain Element", defaultValue = "es-ES", required = false)
			@RequestParam(required = false) @Validated(Create.class) final String languageCode,
			@ApiParam(name = "typeCode", value = "Type Code", defaultValue = "res", required = false)
			@RequestParam(required = false) @Validated(Create.class) final String typeCode,
			@ApiParam(name = "entity", value = "entity name in language", required = true)
			@RequestParam(required = true) @Validated(Create.class) final String entity,
			@ApiParam(name = "reference", value = "instance id", required = true)
			@RequestParam(required = true) @Validated(Create.class) final String reference,
			@ApiParam(name = "localURI", value = "local URI where resource is Storage", required = true)
			@RequestParam(required = true) @Validated(Create.class) final String localURI,
			@ApiParam(name = "storageName", value = "Storage type by name", required = true)
			@RequestParam(required = true) @Validated(Create.class) final String storageName) {
		logger.info("Creating resource type uri: ");

		String defDomain = (domain!=null)?domain:Constants.DOMAIN_VALUE;
		String defSubdomain = (subDomain!=null)?subDomain:Constants.SUBDOMAIN_VALUE;
		String defLang = null;
		String defType = Constants.TYPE_REST;
		if (languageCode == null) {
			List<Language> l = languageProxy.getDefaultLanguages();
			defLang = (l.size() > 0 ? l.get(0).getISO() : Constants.SPANISH_LANGUAGE);
		} else
			defLang = languageCode;

		if (typeCode == null) {
			Type type = typeProxy.find(Constants.TYPE_REST).orElse(null);
			if (defLang!=null && type!=null) {
				List<LanguageType> tls = languageTypeProxy.getByLanguageAndType(defLang,type.getCode());
				if (tls.size() == 1 && tls.get(0).getTypeLangCode()!=null)
					defType = tls.get(0).getTypeLangCode();
			}
		}

		List<CanonicalURILanguage> cus = canonicalURILanguageControllerService.getAllByElements(
				defDomain,
				defSubdomain,
				defLang,
				defType,
				entity,
				reference
		);

		CanonicalURILanguage cu = null;

		if (cus.size() == 0)
			throw new CustomNotFoundException("Canonical Language URI  Not Found");
		else if (cus.size() > 1)
			throw new CustomNotFoundException("Ambiguous Canonical Language URI Founds: "+cus.size() +" results");
		else
			cu = cus.get(0);
		StorageType storageType = storageTypeController.get(storageName);
		if (storageType == null)
			throw new CustomNotFoundException("Storage type " + storageName + " Not Found");
		if (Utils.isValidURL(localURI))
			throw new CustomNotFoundException("Not valid format URI Local:  " + localURI);
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
			@ApiParam( name = "domain", value = "Domain Element", defaultValue = "hercules.org", required = false)
			@RequestParam(required = false) @Validated(Create.class) final String domain,
			@ApiParam(name = "subDomain", value = "Sub Domain Element", defaultValue = "um", required = false)
			@RequestParam(required = false) @Validated(Create.class) final String subDomain,
			@ApiParam(name = "languageCode", value = "Sub Domain Element", defaultValue = "es-ES", required = false)
			@RequestParam(required = false) @Validated(Create.class) final String languageCode,
			@ApiParam(name = "typeCode", value = "Type Code", defaultValue = "res", required = false)
			@RequestParam(required = false) @Validated(Create.class) final String typeCode,
			@ApiParam(name = "property", value = "instance id", required = true)
			@RequestParam(required = true) @Validated(Create.class) final String property,
			@ApiParam(name = "localURI", value = "local URI where resource is Storage", required = true)
			@RequestParam(required = true) @Validated(Create.class) final String localURI,
			@ApiParam(name = "storageName", value = "Storage type by name", required = true)
			@RequestParam(required = true) @Validated(Create.class) final String storageName) {
		logger.info("Creating resource type uri: ");

		String defDomain = (domain!=null)?domain:Constants.DOMAIN_VALUE;
		String defSubdomain = (subDomain!=null)?subDomain:Constants.SUBDOMAIN_VALUE;
		String defLang = null;
		String defType = Constants.TYPE_REST;
		if (languageCode == null) {
			List<Language> l = languageProxy.getDefaultLanguages();
			defLang = (l.size() > 0 ? l.get(0).getISO() : Constants.SPANISH_LANGUAGE);
		} else
			defLang = languageCode;

		if (typeCode == null) {
			Type type = typeProxy.find(Constants.TYPE_REST).orElse(null);
			if (defLang!=null && type!=null) {
				List<LanguageType> tls = languageTypeProxy.getByLanguageAndType(defLang,type.getCode());
				if (tls.size() == 1 && tls.get(0).getTypeLangCode()!=null)
					defType = tls.get(0).getTypeLangCode();
			}
		}

		List<CanonicalURILanguage> cus = canonicalURILanguageControllerService.getAllByPropertyName(property);


		CanonicalURILanguage cu = null;

		if (cus.size() == 0)
			throw new CustomNotFoundException("Canonical Language URI  Not Found");
		else if (cus.size() > 1)
			throw new CustomNotFoundException("Ambiguous Canonical Language URI Founds: "+cus.size() +" results");
		else
			cu = cus.get(0);
		StorageType storageType = storageTypeController.get(storageName);
		if (storageType == null)
			throw new CustomNotFoundException("Storage type " + storageName + " Not Found");
		if (Utils.isValidURL(localURI))
			throw new CustomNotFoundException("Not valid format URI Local:  " + localURI);
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
	@DeleteMapping(URISController.Mappings.LOCAL_URI)
	public void unlinkCanonicalURIToLocalURI(
			@ApiParam(name = "canonicalLanguageURI", value = "Canonical Uri Language retorned in the creation", required = true)
			@RequestParam(required = true) @Validated(Create.class) final String canonicalLanguageURI,
			@ApiParam(name = "localURI", value = "local URI where resource is Storage", required = true)
			@RequestParam(required = true) @Validated(Create.class) final String localURI,
			@ApiParam(name = "storageName", value = "Storage type by name", required = true)
			@RequestParam(required = true) @Validated(Create.class) final String storageName) {
		logger.info("Creating resource type uri: ");

		CanonicalURILanguage cu = canonicalURILanguageControllerController.getFullURI(canonicalLanguageURI);
		if (cu == null)
			throw new CustomNotFoundException("Canonical Language URI: " + canonicalLanguageURI + " Not Found");
		StorageType storageType = storageTypeController.get(storageName);
		if (storageType == null)
			throw new CustomNotFoundException("Storage type " + storageName + " Not Found");
		if (Utils.isValidURL(localURI))
			throw new CustomNotFoundException("Not valid format URI Local:  " + localURI);
		for (LocalURI luAux : localURIProxy.getAllByLocalURI(new LocalURI(localURI, cu, storageType)))
			localURIController.deleteURI(luAux);
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
			@ApiParam( name = "domain", value = "Domain Element", defaultValue = "hercules.org", required = false)
			@RequestParam(required = false) @Validated(Create.class) final String domain,
			@ApiParam(name = "subDomain", value = "Sub Domain Element", defaultValue = "um", required = false)
			@RequestParam(required = false) @Validated(Create.class) final String subDomain,
			@ApiParam(name = "languageCode", value = "Sub Domain Element", defaultValue = "es-ES", required = false)
			@RequestParam(required = false) @Validated(Create.class) final String languageCode,
			@ApiParam(name = "typeCode", value = "Type Code", defaultValue = "res", required = false)
			@RequestParam(required = false) @Validated(Create.class) final String typeCode,
			@ApiParam(name = "entity", value = "entity name in language", required = true)
			@RequestParam(required = true) @Validated(Create.class) final String entity,
			@ApiParam(name = "localURI", value = "local URI where resource is Storage", required = true)
			@RequestParam(required = true) @Validated(Create.class) final String localURI,
			@ApiParam(name = "storageName", value = "Storage type by name", required = true)
			@RequestParam(required = true) @Validated(Create.class) final String storageName) {

		String defDomain = (domain!=null)?domain:Constants.DOMAIN_VALUE;
		String defSubdomain = (subDomain!=null)?subDomain:Constants.SUBDOMAIN_VALUE;
		String defLang = null;
		String defType = Constants.TYPE_REST;
		if (languageCode == null) {
			List<Language> l = languageProxy.getDefaultLanguages();
			defLang = (l.size() > 0 ? l.get(0).getISO() : Constants.SPANISH_LANGUAGE);
		} else
			defLang = languageCode;

		if (typeCode == null) {
			Type type = typeProxy.find(Constants.TYPE_REST).orElse(null);
			if (defLang!=null && type!=null) {
				List<LanguageType> tls = languageTypeProxy.getByLanguageAndType(defLang,type.getCode());
				if (tls.size() == 1 && tls.get(0).getTypeLangCode()!=null)
					defType = tls.get(0).getTypeLangCode();
			}
		}

		List<CanonicalURILanguage> cus = canonicalURILanguageControllerService.getAllByElements(
				defDomain,
				defSubdomain,
				defLang,
				defType,
				entity,
				null
		);

		CanonicalURILanguage cu = null;

		if (cus.size() == 0)
			throw new CustomNotFoundException("Canonical Language URI  Not Found");
		else if (cus.size() > 1)
			throw new CustomNotFoundException("Ambiguous Canonical Language URI Founds: "+cus.size() +" results");
		else
			cu = cus.get(0);
		StorageType storageType = storageTypeController.get(storageName);
		if (storageType == null)
			throw new CustomNotFoundException("Storage type " + storageName + " Not Found");
		if (Utils.isValidURL(localURI))
			throw new CustomNotFoundException("Not valid format URI Local:  " + localURI);
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
			@ApiParam( name = "domain", value = "Domain Element", defaultValue = "hercules.org", required = false)
			@RequestParam(required = false) @Validated(Create.class) final String domain,
			@ApiParam(name = "subDomain", value = "Sub Domain Element", defaultValue = "um", required = false)
			@RequestParam(required = false) @Validated(Create.class) final String subDomain,
			@ApiParam(name = "languageCode", value = "Sub Domain Element", defaultValue = "es-ES", required = false)
			@RequestParam(required = false) @Validated(Create.class) final String languageCode,
			@ApiParam(name = "typeCode", value = "Type Code", defaultValue = "res", required = false)
			@RequestParam(required = false) @Validated(Create.class) final String typeCode,
			@ApiParam(name = "entity", value = "entity name in language", required = true)
			@RequestParam(required = true) @Validated(Create.class) final String entity,
			@ApiParam(name = "reference", value = "instance id", required = true)
			@RequestParam(required = true) @Validated(Create.class) final String reference,
			@ApiParam(name = "localURI", value = "local URI where resource is Storage", required = true)
			@RequestParam(required = true) @Validated(Create.class) final String localURI,
			@ApiParam(name = "storageName", value = "Storage type by name", required = true)
			@RequestParam(required = true) @Validated(Create.class) final String storageName) {
		logger.info("Creating resource type uri: ");

		String defDomain = (domain!=null)?domain:Constants.DOMAIN_VALUE;
		String defSubdomain = (subDomain!=null)?subDomain:Constants.SUBDOMAIN_VALUE;
		String defLang = null;
		String defType = Constants.TYPE_REST;
		if (languageCode == null) {
			List<Language> l = languageProxy.getDefaultLanguages();
			defLang = (l.size() > 0 ? l.get(0).getISO() : Constants.SPANISH_LANGUAGE);
		} else
			defLang = languageCode;

		if (typeCode == null) {
			Type type = typeProxy.find(Constants.TYPE_REST).orElse(null);
			if (defLang!=null && type!=null) {
				List<LanguageType> tls = languageTypeProxy.getByLanguageAndType(defLang,type.getCode());
				if (tls.size() == 1 && tls.get(0).getTypeLangCode()!=null)
					defType = tls.get(0).getTypeLangCode();
			}
		}

		List<CanonicalURILanguage> cus = canonicalURILanguageControllerService.getAllByElements(
				defDomain,
				defSubdomain,
				defLang,
				defType,
				entity,
				reference
		);

		CanonicalURILanguage cu = null;

		if (cus.size() == 0)
			throw new CustomNotFoundException("Canonical Language URI  Not Found");
		else if (cus.size() > 1)
			throw new CustomNotFoundException("Ambiguous Canonical Language URI Founds: "+cus.size() +" results");
		else
			cu = cus.get(0);
		StorageType storageType = storageTypeController.get(storageName);
		if (storageType == null)
			throw new CustomNotFoundException("Storage type " + storageName + " Not Found");
		if (Utils.isValidURL(localURI))
			throw new CustomNotFoundException("Not valid format URI Local:  " + localURI);
		for (LocalURI luAux : localURIProxy.getAllByLocalURI(new LocalURI(localURI, cu, storageType)))
			localURIProxy.delete(luAux);
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
			@ApiParam( name = "domain", value = "Domain Element", defaultValue = "hercules.org", required = false)
			@RequestParam(required = false) @Validated(Create.class) final String domain,
			@ApiParam(name = "subDomain", value = "Sub Domain Element", defaultValue = "um", required = false)
			@RequestParam(required = false) @Validated(Create.class) final String subDomain,
			@ApiParam(name = "languageCode", value = "Sub Domain Element", defaultValue = "es-ES", required = false)
			@RequestParam(required = false) @Validated(Create.class) final String languageCode,
			@ApiParam(name = "typeCode", value = "Type Code", defaultValue = "res", required = false)
			@RequestParam(required = false) @Validated(Create.class) final String typeCode,
			@ApiParam(name = "property", value = "instance id", required = true)
			@RequestParam(required = true) @Validated(Create.class) final String property,
			@ApiParam(name = "localURI", value = "local URI where resource is Storage", required = true)
			@RequestParam(required = true) @Validated(Create.class) final String localURI,
			@ApiParam(name = "storageName", value = "Storage type by name", required = true)
			@RequestParam(required = true) @Validated(Create.class) final String storageName) {
		logger.info("Creating resource type uri: ");

		String defDomain = (domain!=null)?domain:Constants.DOMAIN_VALUE;
		String defSubdomain = (subDomain!=null)?subDomain:Constants.SUBDOMAIN_VALUE;
		String defLang = null;
		String defType = Constants.TYPE_REST;
		if (languageCode == null) {
			List<Language> l = languageProxy.getDefaultLanguages();
			defLang = (l.size() > 0 ? l.get(0).getISO() : Constants.SPANISH_LANGUAGE);
		} else
			defLang = languageCode;

		if (typeCode == null) {
			Type type = typeProxy.find(Constants.TYPE_REST).orElse(null);
			if (defLang!=null && type!=null) {
				List<LanguageType> tls = languageTypeProxy.getByLanguageAndType(defLang,type.getCode());
				if (tls.size() == 1 && tls.get(0).getTypeLangCode()!=null)
					defType = tls.get(0).getTypeLangCode();
			}
		}

		List<CanonicalURILanguage> cus = canonicalURILanguageControllerService.getAllByPropertyName(property);


		CanonicalURILanguage cu = null;

		if (cus.size() == 0)
			throw new CustomNotFoundException("Canonical Language URI  Not Found");
		else if (cus.size() > 1)
			throw new CustomNotFoundException("Ambiguous Canonical Language URI Founds: "+cus.size() +" results");
		else
			cu = cus.get(0);
		StorageType storageType = storageTypeController.get(storageName);
		if (storageType == null)
			throw new CustomNotFoundException("Storage type " + storageName + " Not Found");
		if (Utils.isValidURL(localURI))
			throw new CustomNotFoundException("Not valid format URI Local:  " + localURI);
		for (LocalURI luAux : localURIProxy.getAllByLocalURI(new LocalURI(localURI, cu, storageType)))
			localURIProxy.delete(luAux);
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
