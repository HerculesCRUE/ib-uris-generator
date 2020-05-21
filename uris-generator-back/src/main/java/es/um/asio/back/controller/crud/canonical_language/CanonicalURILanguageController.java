package es.um.asio.back.controller.crud.canonical_language;

import es.um.asio.back.controller.error.CustomNotFoundException;
import es.um.asio.service.model.*;
import es.um.asio.service.proxy.*;
import es.um.asio.service.service.SchemaService;
import es.um.asio.service.validation.group.Create;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(CanonicalURILanguageController.Mappings.BASE)
@Api(value = "CRUD Operations for Canonical Language URIs", tags = "CRUD Operations (GET, POST, DELETE) for Canonical Language URIs")
public class CanonicalURILanguageController {

    /**
     * Proxy service implementation for {@link CanonicalURILanguage}.
     */
    @Autowired
    private CanonicalURILanguageProxy proxy;

    /**
     * Proxy service implementation for {@link CanonicalURI}.
     */
    @Autowired
    private CanonicalURIProxy canonicalProxy;

    /**
     * Proxy service implementation for {@link Type}.
     */
    @Autowired
    private TypeProxy typeProxy;

    /**
     * Proxy service implementation for {@link Language}.
     */
    @Autowired
    private LanguageProxy languageProxy;

    /**
     * Proxy service implementation for {@link LanguageType}.
     */
    @Autowired
    private LanguageTypeProxy languageTypeProxy;

    /**
     * Schema service
     */
    @Autowired
    private SchemaService schemaService;


    /**
     * Save.
     *
     * @param entity
     *            the CanonicalURI in JSON format
     * @return the saved CanonicalURI
     */
    @PostMapping("/json")
    public CanonicalURILanguage save(@RequestBody @Validated(Create.class) final CanonicalURILanguage entity) {
        return this.proxy.save(entity);
    }

    /**
     * Save.
     *
     * @param domain
     *            the domain for insert
     * @param subDomain
     *            the subDomain for insert
     * @param typeCode
     *            the typeCode for insert
     * @param concept
     *            the concept for insert
     * @param reference
     *            the reference for insert
     * @param property
     *            the reference for insert
     * @return the saved CanonicalURI
     */
    @PostMapping
    public CanonicalURILanguage save(
            @ApiParam( name = "domain", value = "Domain Element", defaultValue = "hercules.org")
            @RequestParam(required = true) @Validated(Create.class) final String domain,
            @ApiParam(name = "subDomain", value = "Sub Domain Element", defaultValue = "um")
            @RequestParam(required = true) @Validated(Create.class) final String subDomain,
            @ApiParam(name = "language", value = "Sub Domain Element", defaultValue = "es-ES")
            @RequestParam(required = true) @Validated(Create.class) final String language,
            @ApiParam(name = "typeCode", value = "Type Code", defaultValue = "res")
            @RequestParam(required = true) @Validated(Create.class) final String typeCode,
            @ApiParam(name = "concept", value = "Concept (Entity) Element", defaultValue = "")
            @RequestParam(required = false) @Validated(Create.class) final String concept,
            @ApiParam(name = "reference", value = "Reference (Instance) Element", defaultValue = "")
            @RequestParam(required = false) @Validated(Create.class) final String reference,
            @ApiParam(name = "property", value = "Property Element", defaultValue = "")
            @RequestParam(required = false) @Validated(Create.class) final String property,
            @ApiParam(name = "parentEntity", value = "Property Element", defaultValue = "")
            @RequestParam(required = false) @Validated(Create.class) final String parentEntity,
            @ApiParam(name = "parentProperty", value = "Property Element", defaultValue = "")
            @RequestParam(required = false) @Validated(Create.class) final String parentProperty,
            @ApiParam(name = "createCanonicalIfNotExist", value = "Create canonical URI if not exist", allowableValues = "true, false", defaultValue = "false")
            @RequestParam(required = false) @Validated(Create.class) final boolean createCanonicalIfNotExist
    )  {
        Type t = typeProxy.findOrCreate(typeCode);
        Language l = languageProxy.findOrCreate(language);

        List<LanguageType> lts = languageTypeProxy.getByLanguageAndType(l.getIso().trim(),t.getCode().trim());
        LanguageType lt = null;
        if (!lts.isEmpty()) {
            lt = lts.get(lts.size()-1);
        }
        String schema = schemaService.getCanonicalLanguageSchema();
        CanonicalURILanguage entity = new CanonicalURILanguage(domain,subDomain,lt,concept,reference,property,schema);
        entity.setParentPropertyName(parentProperty);
        List<CanonicalURI> canonicalURIs = getCanonicalURIS(entity,parentEntity,parentProperty,reference);
        try {
            if (canonicalURIs.size()>1) {
                throw new CustomNotFoundException();
            } else {
                if (canonicalURIs.isEmpty()) {
                    if (createCanonicalIfNotExist) {
                        String canonicalSchema = schemaService.getCanonicalLanguageSchema();
                        CanonicalURI cu = new CanonicalURI(domain,subDomain,t,parentEntity,reference, parentProperty,canonicalSchema);
                        cu.setEntityName(cu.getConcept());
                        cu.updateState();
                        if (property!=null) {
                            cu.setPropertyName(property);
                            cu.updateState();
                        }
                        CanonicalURI canonicalURI = canonicalProxy.save(cu);
                        canonicalURIs.add(canonicalURI);
                    }
                    else
                        throw new CustomNotFoundException();
                }
                String schemaCanonical = schemaService.getCanonicalSchema();
                CanonicalURI canonicalURI = canonicalURIs.get(0);
                canonicalURIs.get(0).generateFullURL(schemaCanonical);
                entity.setCanonicalURI(canonicalURIs.get(0));
                entity.setParentEntityName(parentEntity);
                entity.setParentPropertyName(parentProperty);
                entity.generateFullURL();
                return this.proxy.save(entity);
            }
        } catch (Exception e) {
            throw new CustomNotFoundException();
        }

    }

    private List<CanonicalURI> getCanonicalURIS(CanonicalURILanguage entity, String parentEntity, String parentProperty, String reference){
        List<CanonicalURI> canonicalURIs = new ArrayList<>();
        if (entity.getIsEntity()) {
            canonicalURIs.addAll(getCanonicalURIEntities(parentEntity));
        } else if (entity.getIsInstance()) {
            canonicalURIs.addAll(getCanonicalURIInstances(parentEntity, reference));
        } else  {
            canonicalURIs.addAll(getCanonicalURIProperties(entity, parentEntity, parentProperty));
        }
        return canonicalURIs;
    }

    private List<CanonicalURI> getCanonicalURIEntities(String parentEntity) {
        List<CanonicalURI> canonicalURIs = new ArrayList<>();
        for (CanonicalURI cu : canonicalProxy.getAllByEntityNameAndPropertyName(parentEntity, null)) {
            if (cu.getIsEntity() && cu.getReference()==null) {
                canonicalURIs.add(cu);
            }
        }
        return canonicalURIs;
    }

    private List<CanonicalURI> getCanonicalURIInstances(String parentEntity, String reference) {
        List<CanonicalURI> canonicalURIs = new ArrayList<>();
        for (CanonicalURI cu : canonicalProxy.getAllByEntityNameAndReference(parentEntity,reference)) {
            if (cu.getIsInstance() && cu.getReference().trim().equals(reference)) {
                canonicalURIs.add(cu);
            }
        }
        return canonicalURIs;
    }

    private List<CanonicalURI> getCanonicalURIProperties(CanonicalURILanguage entity, String parentEntity, String parentProperty) {
        List<CanonicalURI> canonicalURIs = new ArrayList<>();
        for (CanonicalURI cu : canonicalProxy.getAllByEntityNameAndPropertyName(parentEntity,parentProperty)) {
            if (cu.getIsProperty() && cu.getReference().trim().equals(entity.getParentPropertyName().trim())) {
                canonicalURIs.add(cu);
            }
        }
        return canonicalURIs;
    }

    @GetMapping("all")
    public List<CanonicalURILanguage> getAll() {
        return this.proxy.findAll();
    }

    @GetMapping("entity/{entityName}/property/{propertyName}")
    public List<CanonicalURILanguage> get(
            @PathVariable(required = true,name = "entityName") @Validated(Create.class) final String entityName,
            @PathVariable(required = true,name = "propertyName") @Validated(Create.class) final String propertyName
    ) {
        return this.proxy.getAllByEntityNameAndPropertyName(entityName,propertyName);
    }

    @GetMapping("entity/{entityName}/reference/{referenceId}")
    public List<CanonicalURILanguage> getByReference(
            @PathVariable(required = true,name = "entityName") @Validated(Create.class) final String entityName,
            @PathVariable(required = true,name = "referenceId") @Validated(Create.class) final String referenceId
    ) {
        return this.proxy.getAllByEntityNameAndReference(entityName,referenceId);
    }

    @GetMapping("entity/{entityName}")
    public List<CanonicalURILanguage> get(
            @PathVariable(required = true,name = "entityName")  @Validated(Create.class) final String entityName
    ) {
        return this.proxy.getAllByEntityName(entityName);
    }

    @GetMapping("uri")
    public CanonicalURILanguage getFullURI(
            @RequestParam(required = true) @Validated(Create.class) final String fullURI
    ) {
        return this.proxy.getAllByFullURI(fullURI);
    }

    @GetMapping()
    public List<CanonicalURILanguage> get(
            @ApiParam( name = "domain", value = "Domain Element", defaultValue = "hercules.org")
            @RequestParam(required = false) @Validated(Create.class) final String domain,
            @ApiParam(value = "Sub Domain Element", defaultValue = "um")
            @RequestParam(required = false) @Validated(Create.class) final String subDomain,
            @ApiParam(name = "language", value = "Type Code")
            @RequestParam(required = false) @Validated(Create.class) final String language,
            @ApiParam(name = "typeCode", value = "Type Code")
            @RequestParam(required = false) @Validated(Create.class) final String typeCode,
            @ApiParam(name = "concept", value = "Concept (Entity) Element")
            @RequestParam(required = false) @Validated(Create.class) final String concept,
            @ApiParam(name = "reference", value = "Reference (Instance) Element")
            @RequestParam(required = false) @Validated(Create.class) final String reference
    ) {
        return this.proxy.getAllByElements(domain, subDomain, language, typeCode, concept, reference);
    }

    @DeleteMapping("entity/{entityName}/property/{propertyName}")
    public void delete(
            @PathVariable(required = true,name = "entityName") @Validated(Create.class) final String entityName,
            @PathVariable(required = true,name = "propertyName") @Validated(Create.class) final String propertyName
    ) {
        List<CanonicalURILanguage> culs = this.proxy.getAllByEntityNameAndPropertyName(entityName,propertyName);
        for (CanonicalURILanguage cul:culs) {
            if (cul != null)
                this.proxy.delete(cul);
        }
    }

    @DeleteMapping("entity/{entityName}/reference/{referenceId}")
    public void deleteByReference(
            @PathVariable(required = true,name = "entityName") @Validated(Create.class) final String entityName,
            @PathVariable(required = true,name = "referenceId") @Validated(Create.class) final String referenceId
    ) {
        List<CanonicalURILanguage> culs = this.proxy.getAllByEntityNameAndReference(entityName,referenceId);
        for (CanonicalURILanguage cul:culs) {
            if (cul != null)
                this.proxy.delete(cul);
        }
    }

    @DeleteMapping("entity/{entityName}")
    public void delete(
            @PathVariable(required = true,name = "entityName") @Validated(Create.class) final String entityName
    ) {
        List<CanonicalURILanguage> culs = this.proxy.getAllByEntityName(entityName);
        for (CanonicalURILanguage cul:culs) {
            if (cul != null)
                this.proxy.delete(cul);
        }
    }

    @DeleteMapping("/uri")
    public void deleteURI(
            @RequestParam(required = true) @Validated(Create.class) final String fullURI
    ) {
        CanonicalURILanguage cul = this.proxy.getAllByFullURI(fullURI);

        if (cul != null)
            this.proxy.delete(cul);
    }

    @DeleteMapping
    public void delete(
            @ApiParam( name = "domain", value = "Domain Element")
            @RequestParam(required = false) @Validated(Create.class) final String domain,
            @ApiParam(name = "subDomain", value = "Sub Domain Element")
            @RequestParam(required = false) @Validated(Create.class) final String subDomain,
            @ApiParam(name = "lang", value = "Language Code")
            @RequestParam(required = false) @Validated(Create.class) final String language,
            @ApiParam(name = "type", value = "Type Code")
            @RequestParam(required = false) @Validated(Create.class) final String typeCode,
            @ApiParam(name = "concept", value = "Concept (Entity) Element")
            @RequestParam(required = false) @Validated(Create.class) final String concept,
            @ApiParam(name = "reference", value = "Reference (Instance) Element")
            @RequestParam(required = false) @Validated(Create.class) final String reference
    ) {
        List<CanonicalURILanguage> culs = this.proxy.getAllByElements(domain, subDomain,language, typeCode, concept, reference);
        for (CanonicalURILanguage cul:culs) {
            if (cul != null)
                this.proxy.delete(cul);
        }
    }

    /**
     * Mappgins.
     */
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    static final class Mappings {
        /**
         * Controller request mapping.
         */
        protected static final String BASE = "/canonical-uri-language";
    }
}
