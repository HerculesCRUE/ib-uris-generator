package es.um.asio.back.controller.uri;

import es.um.asio.service.mapper.UserMapper;
import es.um.asio.service.model.URIMap;
import es.um.asio.service.model.User;
import es.um.asio.service.proxy.URIMapProxy;
import es.um.asio.service.validation.group.Create;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * User controller.
 */
@RestController
@RequestMapping(UrisFactoryController.Mappings.BASE)
public class UrisFactoryController {

    /**
     * Proxy service implementation for {@link User}.
     */
    @Autowired
    private URIMapProxy proxy;
    /**
     * DTO to entity mapper.
     */
    @Autowired
    private UserMapper mapper;

    /**
     * Gets the current user details.
     *
     * @param authentication
     *            the authentication
     * @return the current user details
     */
    @GetMapping
    public Object getUri() {
        return "HOLA POLLO2";
    }

    /**
     * Save.
     *
     * @param userDto
     *            the user dto
     * @return the application user dto
     */
    @PostMapping
    public URIMap save(@RequestBody @Validated(Create.class) final URIMap uriMap) {
        return this.proxy.save(uriMap);
    }

    /**
     * Mappgins.
     */
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    static final class Mappings {
        /**
         * Controller request mapping.
         */
        protected static final String BASE = "/uris-factory";
    }
}
