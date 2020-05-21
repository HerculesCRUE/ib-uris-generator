package es.um.asio.back.controller.crud.canonical;

import es.um.asio.service.model.CanonicalURI;
import es.um.asio.service.model.Type;
import es.um.asio.service.proxy.CanonicalURIProxy;
import es.um.asio.service.proxy.TypeProxy;
import es.um.asio.service.service.SchemaService;
import es.um.asio.service.validation.group.Create;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(CanonicalURIController.Mappings.BASE)
@Api(value = "CRUD Operations for Canonical URIs", tags = "CRUD Operations (GET, POST, DELETE) for Canonical URIs")
public class CanonicalURIController {

    /**
     * Proxy service implementation for {@link CanonicalURI}.
     */
    @Autowired
    private CanonicalURIProxy proxy;

    /**
     * Proxy service implementation for {@link Type}.
     */
    @Autowired
    private TypeProxy typeProxy;

    /**
     * Schema service
     */
    @Autowired
    private SchemaService schemaService;


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
     * @return the saved CanonicalURI
     */
    @PostMapping
    public CanonicalURI save(
            @ApiParam( name = "domain", value = "Domain Element", defaultValue = "hercules.org")
            @RequestParam(required = true) @Validated(Create.class) final String domain,
            @ApiParam(name = "subDomain", value = "Sub Domain Element", defaultValue = "um")
            @RequestParam(required = true) @Validated(Create.class) final String subDomain,
            @ApiParam(name = "typeCode", value = "Type Code", defaultValue = "res")
            @RequestParam(required = true) @Validated(Create.class) final String typeCode,
            @ApiParam(name = "concept", value = "Concept (Entity) Element", defaultValue = "")
            @RequestParam(required = false) @Validated(Create.class) final String concept,
            @ApiParam(name = "reference", value = "Reference (Instance) Element", defaultValue = "")
            @RequestParam(required = false) @Validated(Create.class) final String reference,
            @ApiParam(name = "property", value = "Property Element", defaultValue = "")
            @RequestParam(required = false) @Validated(Create.class) final String property
    ) {
        Type t = typeProxy.findOrCreate(typeCode);
        CanonicalURI entity = new CanonicalURI(domain,subDomain,t,concept,reference,property,schemaService.getCanonicalSchema());
        if (property!=null) {
            entity.setPropertyName(property);
            entity.setIsProperty(true);
        } else if (reference!=null) {
            entity.setIsInstance(true);
        } else {
            entity.setIsEntity(true);
        }
        entity.setEntityName(concept);
        return this.proxy.save(entity);
    }

    @GetMapping("all")
    public List<CanonicalURI> getAll() {
        return this.proxy.findAll();
    }

    @GetMapping("property/{propertyName}")
    public List<CanonicalURI> getByProperty(
            @PathVariable(required = true,name = "propertyName") @Validated(Create.class) final String propertyName
    ) {
        return this.proxy.getAllByPropertyFromProperties(propertyName);
    }

    @GetMapping("entity/{entityName}/reference/{referenceId}")
    public List<CanonicalURI> getByReference(
            @PathVariable(required = true,name = "entityName") @Validated(Create.class) final String entityName,
            @PathVariable(required = true,name = "referenceId") @Validated(Create.class) final String referenceId
    ) {
        return this.proxy.getAllByEntityNameAndReference(entityName,referenceId);
    }

    @GetMapping("entity/{entityName}")
    public List<CanonicalURI> getByEntity(
            @PathVariable(required = true,name = "entityName")  @Validated(Create.class) final String entityName
    ) {
        return this.proxy.getAllByEntityNameFromEntities(entityName);
    }

    @GetMapping("uri")
    public List<CanonicalURI> getFullURI(
            @RequestParam(required = true) @Validated(Create.class) final String fullURI
    ) {
        return this.proxy.getAllByFullURI(fullURI);
    }

    @GetMapping()
    public List<CanonicalURI> get(
            @ApiParam( name = "domain", value = "Domain Element", defaultValue = "hercules.org")
            @RequestParam(required = false) @Validated(Create.class) final String domain,
            @ApiParam(value = "Sub Domain Element", defaultValue = "um")
            @RequestParam(required = false) @Validated(Create.class) final String subDomain,
            @ApiParam(name = "typeCode", value = "Type Code")
            @RequestParam(required = false) @Validated(Create.class) final String typeCode,
            @ApiParam(name = "concept", value = "Concept (Entity) Element")
            @RequestParam(required = false) @Validated(Create.class) final String concept,
            @ApiParam(name = "reference", value = "Reference (Instance) Element")
            @RequestParam(required = false) @Validated(Create.class) final String reference
    ) {
        return this.proxy.getAllByElements(domain, subDomain, typeCode, concept, reference);
    }

    @DeleteMapping("property/{propertyName}")
    public void deleteByProperty(
            @PathVariable(required = true,name = "propertyName") @Validated(Create.class) final String propertyName
    ) {
        List<CanonicalURI> cus = this.proxy.getAllByPropertyName(propertyName);
        for (CanonicalURI cu:cus) {
            if (cu != null)
                this.proxy.delete(cu);
        }
    }

    @DeleteMapping("entity/{entityName}/reference/{referenceId}")
    public void deleteByReference(
            @PathVariable(required = true,name = "entityName") @Validated(Create.class) final String entityName,
            @PathVariable(required = true,name = "referenceId") @Validated(Create.class) final String referenceId
    ) {
        List<CanonicalURI> cus = this.proxy.getAllByEntityNameAndReference(entityName,referenceId);
        for (CanonicalURI cu:cus) {
            if (cu != null)
                this.proxy.delete(cu);
        }
    }

    @DeleteMapping("entity/{entityName}")
    public void delete(
            @PathVariable(required = true,name = "entityName") @Validated(Create.class) final String entityName
    ) {
        List<CanonicalURI> cus = this.proxy.getAllByEntityNameFromEntities(entityName);
        for (CanonicalURI cu:cus) {
            if (cu != null)
                this.proxy.delete(cu);
        }
    }

    @DeleteMapping("/uri")
    public void deleteURI(
            @RequestParam(required = true) @Validated(Create.class) final String fullURI
    ) {
        List<CanonicalURI> cus = this.proxy.getAllByFullURI(fullURI);
        for (CanonicalURI cu:cus) {
            if (cu != null)
                this.proxy.delete(cu);
        }
    }

    @DeleteMapping
    public void delete(
            @ApiParam( name = "domain", value = "Domain Element")
            @RequestParam(required = false) @Validated(Create.class) final String domain,
            @ApiParam(name = "subDomain", value = "Sub Domain Element")
            @RequestParam(required = false) @Validated(Create.class) final String subDomain,
            @ApiParam(name = "type", value = "Type Code")
            @RequestParam(required = false) @Validated(Create.class) final String typeCode,
            @ApiParam(name = "concept", value = "Concept (Entity) Element")
            @RequestParam(required = false) @Validated(Create.class) final String concept,
            @ApiParam(name = "reference", value = "Reference (Instance) Element")
            @RequestParam(required = false) @Validated(Create.class) final String reference
    ) {
        List<CanonicalURI> cus = this.proxy.getAllByElements(domain, subDomain, typeCode, concept, reference);
        for (CanonicalURI cu:cus) {
            if (cu != null)
                this.proxy.delete(cu);
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
        protected static final String BASE = "/canonical-uri";
    }
}
