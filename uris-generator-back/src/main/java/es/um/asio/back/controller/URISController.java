package es.um.asio.back.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
     * Retrieve URI.
     *
     * @param input the input
     * @return the string uri
     */
    @PostMapping(URISController.Mappings.RESOURCE_ID)
    public String createResourceID(@RequestBody final Object input) {
        logger.info(input.toString());
        
        // Example input: 
        /*
         * {
         * @class=es.um.asio.service.util.dummy.data.ConceptoGrupoDummy, 
         * entityId=null, 
         * version=0, 
         * idGrupoInvestigacion=E0A6-01, 
         * numero=5, 
         * codTipoConcepto=DESCRIPTORES, 
         * texto=LENGUAJES PLASTICOS}
         */
        // FIXME
        return "http://www.jan107.es/10886753";
    }
    
    /**
     * Creates the property URI.
     *
     * @param input the input
     * @return the string
     */
    @PostMapping(URISController.Mappings.PROPERTY_URI)
    public String createPropertyURI(@RequestBody final Object input) {
        logger.info(input.toString());
        
        // Example input: 
        /*
         * {
         * @class=es.um.asio.service.util.dummy.data.ConceptoGrupoDummy, 
         * entityId=null, 
         * version=0, 
         * idGrupoInvestigacion=E0A6-01, 
         * numero=5, 
         * codTipoConcepto=DESCRIPTORES, 
         * texto=LENGUAJES PLASTICOS}
         */
        // FIXME
        return "http://www.w3.org/2001/asio-rdf/3.0#";
    }
    
    /**
     * Creates the resource type URI.
     *
     * @param input the input
     * @return the string
     */
    @PostMapping(URISController.Mappings.RESOURCE_TYPE_URI)
    public String createResourceTypeURI(@RequestBody final String className) {
        logger.info("ClassName: " + className);
        // Example input:
        /*
         * ConceptoGrupoDummy
         */
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
