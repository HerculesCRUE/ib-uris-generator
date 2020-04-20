package es.um.asio.back.controller;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import es.um.asio.abstractions.constants.Constants;
import es.um.asio.back.controller.error.CustomNotFoundException;
import es.um.asio.back.controller.uri.CanonicalURILanguageController;
import es.um.asio.back.controller.uri.LocalURIController;
import es.um.asio.back.controller.uri.StorageTypeController;
import es.um.asio.service.model.CanonicalURILanguage;
import es.um.asio.service.model.LocalURI;
import es.um.asio.service.model.StorageType;
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
					(pEntity != null) ? pEntity : entity, (pEntity != null) ? pEntity : entity, true);

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
		final String entity = Utils.getClassNameFromPath((String) map.get(Constants.CLASS));

		HashMap objMap = (HashMap) map.get(Constants.OBJECT);
		String pEntity = Utils.getClassNameFromPath((String) (objMap.get(Constants.CANONICAL_CLASS_NAME) != null
				? (objMap.get(Constants.CANONICAL_CLASS_NAME))
				: (objMap.get(Constants.CANONICAL_CLASS))));

		CanonicalURILanguage canonicalURILanguage = canonicalURILanguageControllerController.save(domain, subDomain,
				lang, type, entity, null, property, (pEntity != null) ? pEntity : entity,
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
	 *   Arrobaclass: es.um.asio.service.util.data.ConceptoGrupo,
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
	 * Associate a local uri with a canonical uri in a language, for a storage
	 * system
	 * <p>
	 * Example
	 * <p>
	 * 
	 * <pre>
	 * {
	 *   className: ConceptoGrupo,
	 *   language: es,
	 *   university: https://www.um.es
	 * }
	 * </pre>
	 * 
	 * @param input HashMap.
	 * @return URI for the input property.
	 */
	@PostMapping(URISController.Mappings.LOCAL_URI)
	public LocalURI createResourceTypeURI(
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

		/**
		 * Controller request mapping.
		 */
		protected static final String BASE = "/uri-factory";

	}
}
