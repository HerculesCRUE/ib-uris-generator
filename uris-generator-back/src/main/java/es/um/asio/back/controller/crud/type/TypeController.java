package es.um.asio.back.controller.crud.type;

import es.um.asio.service.model.Type;
import es.um.asio.service.model.User;
import es.um.asio.service.proxy.TypeProxy;
import es.um.asio.service.validation.group.Create;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(TypeController.Mappings.BASE)
public class TypeController {

    /**
     * Proxy service implementation for {@link User}.
     */
    @Autowired
    private TypeProxy proxy;

    /**
     * Save.
     *
     * @param entity
     *            the Language in JSON format
     * @return the saved Language
     */

    @PostMapping("/json")
    public Type save(@RequestBody @Validated(Create.class) final Type entity) {
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
    public Type save(
            @RequestParam(required = true) @Validated(Create.class) final String code,
            @RequestParam(required = false) @Validated(Create.class) final String name
            ) {
        Type entity = new Type(code, name);
        return this.proxy.save(entity);
    }

    @GetMapping("all")
    public List<Type> getAll() {
        return this.proxy.findAll();
    }

    @GetMapping()
    public Type get(@RequestParam(required = true) @Validated(Create.class) final String ISO) {
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
        protected static final String BASE = "/type";
    }
}
