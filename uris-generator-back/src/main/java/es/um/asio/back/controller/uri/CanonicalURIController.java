package es.um.asio.back.controller.uri;

import es.um.asio.service.model.CanonicalURI;
import es.um.asio.service.model.URIMap;
import es.um.asio.service.model.User;
import es.um.asio.service.proxy.CanonicalURIProxy;
import es.um.asio.service.proxy.URIMapProxy;
import es.um.asio.service.validation.group.Create;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(CanonicalURIController.Mappings.BASE)
public class CanonicalURIController {

    /**
     * Proxy service implementation for {@link User}.
     */
    @Autowired
    private CanonicalURIProxy proxy;

    /**
     * Save.
     *
     * @param entity
     *            the CanonicalURI in JSON format
     * @return the saved CanonicalURI
     */
    @PostMapping
    public CanonicalURI save(@RequestBody @Validated(Create.class) final CanonicalURI entity) {
        return this.proxy.save(entity);
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
