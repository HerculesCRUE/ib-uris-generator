package es.um.asio.back.controller.uri;

import es.um.asio.service.model.StorageType;
import es.um.asio.service.model.Type;
import es.um.asio.service.model.User;
import es.um.asio.service.proxy.StorageTypeProxy;
import es.um.asio.service.proxy.TypeProxy;
import es.um.asio.service.validation.group.Create;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(StorageTypeController.Mappings.BASE)
public class StorageTypeController {

    /**
     * Proxy service implementation for {@link User}.
     */
    @Autowired
    private StorageTypeProxy proxy;

    /**
     * Save.
     *
     * @param entity
     *            the Language in JSON format
     * @return the saved Language
     */

    @PostMapping("/json")
    public StorageType save(@RequestBody @Validated(Create.class) final StorageType entity) {
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
    public StorageType save(
            @RequestParam(required = true) @Validated(Create.class) final String name,
            @RequestParam(required = false) @Validated(Create.class) final String apiURL,
            @RequestParam(required = false) @Validated(Create.class) final String endPointURL,
            @RequestParam(required = false) @Validated(Create.class) final String schemaURI
            ) {
        StorageType entity = new StorageType(name.trim().toLowerCase(), apiURL, endPointURL, schemaURI);
        return this.proxy.save(entity);
    }

    @GetMapping("all")
    public List<StorageType> getAll() {
        return this.proxy.findAll();
    }

    @GetMapping()
    public StorageType get(@RequestParam(required = true) @Validated(Create.class) final String name) {
        return this.proxy.findByName(name.trim().toLowerCase());
    }

    @DeleteMapping
    public void delete(@RequestParam(required = true) @Validated(Create.class) final String name) {
        this.proxy.deleteByName(name);
    }

    /**
     * Mappgins.
     */
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    static final class Mappings {
        /**
         * Controller request mapping.
         */
        protected static final String BASE = "/storage-type";
    }
}
