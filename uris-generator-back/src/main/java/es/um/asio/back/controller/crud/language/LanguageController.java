package es.um.asio.back.controller.crud.language;

import es.um.asio.service.model.Language;
import es.um.asio.service.model.User;
import es.um.asio.service.proxy.LanguageProxy;
import es.um.asio.service.validation.group.Create;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(LanguageController.Mappings.BASE)
public class LanguageController {

    /**
     * Proxy service implementation for {@link User}.
     */
    @Autowired
    private LanguageProxy proxy;

    /**
     * Save.
     *
     * @param entity
     *            the Language in JSON format
     * @return the saved Language
     */

    @PostMapping("/json")
    public Language save(@RequestBody @Validated(Create.class) final Language entity) {
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
    public Language save(
            @RequestParam(required = true) @Validated(Create.class) final String ISO,
            @RequestParam(required = false) @Validated(Create.class) final String name,
            @RequestParam(required = false) @Validated(Create.class) final String domain,
            @RequestParam(required = false) @Validated(Create.class) final String subDomain,
            @RequestParam(required = false) @Validated(Create.class) final String type,
            @RequestParam(required = false) @Validated(Create.class) final String concept,
            @RequestParam(required = false) @Validated(Create.class) final String reference,
            @RequestParam(required = false) @Validated(Create.class) final boolean isDefault
            ) {
        Language entity = new Language(ISO,name,domain,subDomain,type,concept,reference,isDefault);
        return this.proxy.save(entity);
    }

    @GetMapping("all")
    public List<Language> getLenguages() {
        return this.proxy.findAll();
    }

    @GetMapping()
    public Language getLenguage(@RequestParam(required = true) @Validated(Create.class) final String ISO) {
        return this.proxy.find(ISO).orElse(null);
    }

    @DeleteMapping
    public void delete(@RequestParam(required = true) @Validated(Create.class) final String ISO) {
        this.proxy.delete(ISO);
    }

    /**
     * Mappgins.
     */
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    static final class Mappings {
        /**
         * Controller request mapping.
         */
        protected static final String BASE = "/language";
    }
}
