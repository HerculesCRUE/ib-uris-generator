package es.um.asio.back.controller.uri;

import es.um.asio.back.controller.error.CustomNotFoundException;
import es.um.asio.service.model.*;
import es.um.asio.service.proxy.*;
import es.um.asio.service.util.Utils;
import es.um.asio.service.validation.group.Create;
import io.swagger.annotations.ApiParam;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(LocalURIController.Mappings.BASE)
public class LocalURIController {

    /**
     * Proxy service implementation for {@link CanonicalURI}.
     */
    @Autowired
    private LocalURIProxy proxy;

    /**
     * Proxy service implementation for {@link CanonicalURILanguage}.
     */
    @Autowired
    private CanonicalURILanguageProxy culProxy;

    /**
     * Proxy service implementation for {@link CanonicalURI}.
     */
    @Autowired
    private CanonicalURIProxy cuProxy;

    /**
     * Proxy service implementation for {@link StorageType}.
     */
    @Autowired
    private StorageTypeProxy stProxy;


    /**
     * Proxy service implementation for {@link Type}.
     */
    @Autowired
    private TypeProxy tProxy;

    /**
     * Proxy service implementation for {@link Language}.
     */
    @Autowired
    private LanguageProxy lProxy;

    /**
     * Proxy service implementation for {@link LanguageType}.
     */
    @Autowired
    private LanguageTypeProxy ltProxy;

    /**
     * Save.
     *
     * @param entity
     *            the CanonicalURI in JSON format
     * @return the saved CanonicalURI
     */
    @PostMapping("/json")
    public LocalURI save(@RequestBody @Validated(Create.class) final LocalURI entity) {
        return this.proxy.save(entity);
    }

    /**
     * Save.
     *
     * @param localURI
     *            the localURI for insert
     * @param storageType
     *            the storageType for insert
     * @param canonicalURILanguage
     *            the canonicalURILanguage for insert
     * @return the saved CanonicalURI
     */
    @PostMapping
    public LocalURI save(
            @ApiParam( name = "localURI", value = "Local URI")
            @RequestParam(required = true) @Validated(Create.class) final String localURI,
            @ApiParam( name = "storageType", value = "Storage type" ,allowableValues = "trellis, wikibase")
            @RequestParam(required = true ) @Validated(Create.class) final String storageType,
            @ApiParam( name = "canonicalURILanguage", value = "Canonical URI Language")
            @RequestParam(required = true) @Validated(Create.class) final String canonicalURILanguage
    ) {
        StorageType st = stProxy.findByName(storageType);
        CanonicalURILanguage cul = culProxy.getAllByFullURI(canonicalURILanguage);
        if (st == null || cul == null) {
            throw new CustomNotFoundException();
        }
        LocalURI entity = new LocalURI(localURI,cul,st);
        return this.proxy.save(entity);
    }

    /**
     * Save.
     *
     * @param localURI
     *            the localURI for insert
     * @param storageType
     *            the storageType for insert
     * @param canonicalURILanguage
     *            the canonicalURILanguage for insert
     * @return the saved CanonicalURI
     */
    @PostMapping("canonical")
    public LocalURI save(
            @ApiParam( name = "localURI", value = "Local URI")
            @RequestParam(required = true) @Validated(Create.class) final String localURI,
            @ApiParam( name = "storageType", value = "Storage type" ,allowableValues = "trellis, wikibase")
            @RequestParam(required = true ) @Validated(Create.class) final String storageType,
            @ApiParam( name = "language", value = "Canonical URI Language")
            @RequestParam(required = true) @Validated(Create.class) final String language,
            @ApiParam(name = "typeCode", value = "Type Code", defaultValue = "res")
            @RequestParam(required = true) @Validated(Create.class) final String typeCode,
            @ApiParam( name = "canonicalEntity", value = "Canonical Entity")
            @RequestParam(required = true) @Validated(Create.class) final String canonicalEntity,
            @ApiParam( name = "canonicalProperty", value = "Canonical Property")
            @RequestParam(required = true) @Validated(Create.class) final String canonicalProperty,
            @ApiParam( name = "canonicalReference", value = "Canonical Property")
            @RequestParam(required = true) @Validated(Create.class) final String canonicalReference
    ) {

        Type t = tProxy.findOrCreate(typeCode);
        Language l = lProxy.findOrCreate(language);
        /*TODO: Get or Create*/
        List<LanguageType> lts = ltProxy.getByLanguageAndType(l.getISO().trim(),t.getCode().trim());
        LanguageType lt = null;

        boolean isEntity = false;
        boolean isProperty = false;
        boolean isInstance = false;

        if (canonicalProperty!=null && canonicalReference != null)
            throw new CustomNotFoundException();

        if (Utils.isValidString(canonicalProperty)) {
            isProperty = true;
        } else  if (Utils.isValidString(canonicalReference)) {
            isInstance = true;
        } else {
            isEntity = true;
        }

        if (lts.size()>0) {
            lt = lts.get(lts.size()-1);
        }
        List<CanonicalURI> canonicalURIs = new ArrayList<>();
        if (isEntity) {
            List<CanonicalURI> cus = cuProxy.getAllByEntityNameAndPropertyName(canonicalEntity, null);
            for (CanonicalURI cu:cus) {
                if (cu.getIsEntity() && cu.getReference()==null) {
                    canonicalURIs.add(cu);
                }
            }
        } else if (isInstance) {
            List<CanonicalURI> cus = cuProxy.getAllByEntityNameAndReference(canonicalEntity,canonicalReference);
            for (CanonicalURI cu:cus) {
                if (cu.getIsInstance() && cu.getReference().trim().equals(canonicalReference)) {
                    canonicalURIs.add(cu);
                }
            }
        } else  {
            List<CanonicalURI> cus = cuProxy.getAllByEntityNameAndPropertyName(canonicalEntity,canonicalProperty);
            for (CanonicalURI cu:cus) {
                if (cu.getIsProperty() && cu.getReference().trim().equals(canonicalProperty.trim())) {
                    canonicalURIs.add(cu);
                }
            }
        }

        CanonicalURILanguage canonicalURILanguage = null;
        if (canonicalURIs.size()!=1)
            throw new CustomNotFoundException();
        for (CanonicalURILanguage cul: canonicalURIs.get(0).getCanonicalURILanguages()) {
            if (cul.getLanguage().getISO().trim().equals(language)) {
                canonicalURILanguage = cul;
                break;
            }
        }

        StorageType st = stProxy.findByName(storageType);
        if (canonicalURILanguage == null || st == null) {
            throw new CustomNotFoundException();
        }
        LocalURI entity = new LocalURI(localURI,canonicalURILanguage,st);
        return this.proxy.save(entity);
    }

    @GetMapping("all")
    public List<LocalURI> getAll() {
        return this.proxy.findAll();
    }


    @GetMapping("uri")
    public LocalURI getFullURI(
            @RequestParam(required = true) @Validated(Create.class) final String fullURI
    ) {
        return this.proxy.getAllByLocalURIStr(fullURI);
    }


    @DeleteMapping("/uri")
    public void deleteURI(
            @RequestParam(required = true) @Validated(Create.class) final String fullURI
    ) {
        LocalURI lu = this.proxy.getAllByLocalURIStr(fullURI);
        if (lu != null)
            this.proxy.delete(lu);
    }


    /**
     * Mappgins.
     */
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    static final class Mappings {
        /**
         * Controller request mapping.
         */
        protected static final String BASE = "/local-uri";
    }
}
