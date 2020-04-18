package es.um.asio.back.controller.uri;

import es.um.asio.service.model.Language;
import es.um.asio.service.model.LanguageType;
import es.um.asio.service.model.Type;
import es.um.asio.service.model.User;
import es.um.asio.service.proxy.LanguageProxy;
import es.um.asio.service.proxy.LanguageTypeProxy;
import es.um.asio.service.proxy.TypeProxy;
import es.um.asio.service.validation.group.Create;
import io.swagger.annotations.ApiParam;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(LanguageTypeController.Mappings.BASE)
public class LanguageTypeController {

    /**
     * Proxy service implementation for {@link User}.
     */
    @Autowired
    private LanguageTypeProxy proxy;

    /**
     * Proxy service implementation for {@link User}.
     */
    @Autowired
    private LanguageProxy languageProxy;

    /**
     * Proxy service implementation for {@link User}.
     */
    @Autowired
    private TypeProxy typeProxy;

    /**
     * Save.
     *
     * @param entity
     *            the Language in JSON format
     * @return the saved Language
     */

    @PostMapping("/json")
    public LanguageType save(@RequestBody @Validated(Create.class) final LanguageType entity) {
        return this.proxy.save(entity);
    }

    /**
     * Save.
     *
     * @param entity
     *            the Language in JSON format
     * @return the saved Language
     */
    @PostMapping
    public LanguageType save(
            @ApiParam( name = "ISOCode", value = "1 ISO Language Code", defaultValue = "")
            @RequestParam(required = true) @Validated(Create.class) final String ISOCode,
            @ApiParam(name = "typeCode", value = "2 Type Code", defaultValue = "")
            @RequestParam(required = true) @Validated(Create.class) final String typeCode,
            @ApiParam(name = "languageTypeCode", value = "3 Type Language Code", defaultValue = "")
            @RequestParam(required = true) @Validated(Create.class) final String languageTypeCode,
            @ApiParam(name = "description", value = "4 Description  Type Language Code", defaultValue = "")
            @RequestParam(required = false) @Validated(Create.class) final String description
            ) {
        Language l = languageProxy.findOrCreate(ISOCode);
        Type t = typeProxy.findOrCreate(typeCode);
        LanguageType entity = new LanguageType(l,t,languageTypeCode,description);
        return this.proxy.save(entity);
    }

    @GetMapping("all")
    public List<LanguageType> getAll() {
        return this.proxy.findAll();
    }

    @GetMapping()
    public List<LanguageType> get(
            @RequestParam(required = false) @Validated(Create.class) final String ISO,
            @RequestParam(required = false) @Validated(Create.class) final String type
    ) {
        return this.proxy.getByLanguageAndType(ISO,type);
    }

    @DeleteMapping
    public void delete(
            @RequestParam(required = true) @Validated(Create.class) final String ISO,
            @RequestParam(required = true) @Validated(Create.class) final String type
    ) {
        List<LanguageType> lts = this.proxy.getByLanguageAndType(ISO,type);
        for (LanguageType lt:lts) {
            if (lt != null)
                this.proxy.delete(lt);
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
        protected static final String BASE = "/language-type";
    }


}
