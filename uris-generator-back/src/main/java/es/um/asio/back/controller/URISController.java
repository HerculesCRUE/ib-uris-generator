package es.um.asio.back.controller;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.um.asio.abstractions.constants.Constants;
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
    * Creates the resourceID URI
    * <p>
    * Example
    * <p>
    * <pre>
    * {
    *    obj: 
    *    {
	*      class=es.um.asio.service.util.data.ConceptoGrupo, 
	*      entityId=null, 
	*      version=0, 
	*      idGrupoInvestigacion=E0A6-01, 
	*      numero=5, 
	*      codTipoConcepto=DESCRIPTORES, 
	*      texto=LENGUAJES PLASTICOS
	*    },
	*    language: es,
	*    university: https://www.um.es
    * }
    * </pre>
    * @param input HashMap.
    * @return URI for the input property.
    */
    @SuppressWarnings({ "rawtypes", "unused" })
	@PostMapping(URISController.Mappings.RESOURCE_ID)
    public String createResourceID(@RequestBody final Object input) {
        logger.info(input.toString());
        HashMap map = (HashMap) input;
        Object obj = map.get(Constants.OBJECT);
        String className = (String) map.get(Constants.CLASS_NAME);
        String language = (String) map.get(Constants.LANGUAGE);
    	String university = (String) map.get(Constants.UNIVERSITY);
        
        // FIXME
        return "http://www.jan107.es/10886753";
    }
    
    /**
    * Creates the property URI
    * <p>
    * Example
    * <p>
    * <pre>
    * {
    *   obj: {
    *      class=es.um.asio.service.util.data.ConceptoGrupo, 
    *      entityId=null, 
    *      version=0, 
    *      idGrupoInvestigacion=E0A6-01, 
    *      numero=5, 
    *      codTipoConcepto=DESCRIPTORES, 
    *      texto=LENGUAJES PLASTICOS
    *    },
    *    property: idGrupoInvestigacion,
    *    resourceID: http://example.org/1321/012/1/2012%2F05%2F,
    *    language: es,
    *    university: https://www.um.es
    *  }
    * </pre>
    * @param input HashMap.
    * @return URI for the input property.
    */
    @SuppressWarnings({ "rawtypes", "unused" })
    @PostMapping(URISController.Mappings.PROPERTY_URI)
    public String createPropertyURI(@RequestBody final Object input) {
        logger.info("Creating property URI");

        HashMap map = (HashMap) input;
        Object obj = map.get(Constants.OBJECT);
        String className = (String) map.get(Constants.CLASS_NAME);
        String property = (String) map.get(Constants.PROPERTY);
        String resourceID = (String) map.get(Constants.RESOURCE_ID);
        String language = (String) map.get(Constants.LANGUAGE);
    	String university = (String) map.get(Constants.UNIVERSITY);
        
        // FIXME
        return "http://www.w3.org/2001/asio-rdf/3.0#";
    }
    
    /**
     * Creates the resource type URI
     * <p>
     * Example
     * <p>
     * <pre>
     * {
 	 *   className: ConceptoGrupo,
 	 *   language: es, 
 	 *   university: https://www.um.es
     * }
     * </pre>
     * @param input HashMap.
     * @return URI for the input property.
     */
    @SuppressWarnings({ "rawtypes", "unused" })
    @PostMapping(URISController.Mappings.RESOURCE_TYPE_URI)
    public String createResourceTypeURI(@RequestBody final Object input) {
    	logger.info("Creating resource type uri: ");
    	
    	HashMap map = (HashMap) input;
    	String className = (String) map.get(Constants.CLASS_NAME);
    	String language = (String) map.get(Constants.LANGUAGE);
    	String university = (String) map.get(Constants.UNIVERSITY);
    	
    	// FIXME
        return "http://example.org/" + className;
    }
	
	 /**
     * Mappgins.
     */
    
    /**
     * Instantiates a new mappings.
     */
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    static final class Mappings {
        

    	/** The Constant RESOURCE_ID. */
    	public static final String RESOURCE_ID = "/resource-id";
    	
		/** The Constant PROPERTY_URI. */
		public static final String PROPERTY_URI = "/property";

		/** The Constant RESOURCE_TYPE_URI. */
		public static final String RESOURCE_TYPE_URI = "/resource-type";
		/**
         * Controller request mapping.
         */
        protected static final String BASE = "/uri";

    }
}
