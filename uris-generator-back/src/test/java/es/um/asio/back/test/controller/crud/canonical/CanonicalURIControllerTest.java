package es.um.asio.back.test.controller.crud.canonical;

import com.fasterxml.jackson.databind.ObjectMapper;
import es.um.asio.back.controller.crud.canonical.CanonicalURIController;
import es.um.asio.service.model.CanonicalURI;
import es.um.asio.service.model.Type;
import es.um.asio.service.proxy.CanonicalURIProxy;
import es.um.asio.service.proxy.TypeProxy;
import es.um.asio.service.service.SchemaService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(CanonicalURIController.class)
public class CanonicalURIControllerTest {


    /**
     * MVC test support
     */
    @Autowired
    private MockMvc mvc;

    /**
     * User service
     */
    @MockBean
    private CanonicalURIProxy canonicalURIProxy;

    /**
     * User service
     */
    @MockBean
    private TypeProxy typeProxy;

    /**
     * User service
     */
    @MockBean
    private SchemaService schemaService;

    /**
     * JSON Object mapper
     */
    @Autowired
    private ObjectMapper objectMapper;

    final List<CanonicalURI> canonicalURISSchema1 = new ArrayList<>();


    // Schema Canonical 1
    //final String canonicalSchema = "http://$domain$/$sub-domain$/$type$/$concept$/$reference$";
    //final String SCHEMA_CANONICAL_LANGUAGE_1 = "http://$domain$/$sub-domain$/$language$/$type$/$concept$/$reference$";

    String canonicalSchema, canonicalLanguageSchema;


    @TestConfiguration
    static class CanonicalURIProxyTestConfiguration {
        @Bean
        public CanonicalURIController canonicalURIController() {
            return new CanonicalURIController();
        }
    }

    @Before
    public void beforeTest() {

        canonicalSchema = generateRandomCanonicalSchema();
        canonicalLanguageSchema = generateRandomCanonicalLanguageSchema();

        // Mock data
        // Types:
        final Type type = new Type("res","resource");
        // CanonicalURIs Schema 1
        // Entities
        for (int i = 0; i<3 ; i++) {
            // Add Entity                                                                   , String concept, String reference, String propertyName, String shemaParam
            CanonicalURI entity = new CanonicalURI("hercules.org", "um", type, String.format("entity-%d",i+1), null, null, canonicalSchema );
            canonicalURISSchema1.add(entity); // Entity
            // Add Property
            CanonicalURI property = new CanonicalURI("hercules.org", "um", type, null, null, String.format("property-%d",i+1), canonicalSchema );
            canonicalURISSchema1.add(property); // Entity
            // Add Instance
            CanonicalURI instance = new CanonicalURI("hercules.org", "um", type, String.format("entity-%d",i+1), String.format("instance-%d",i+1), null, canonicalSchema );
            canonicalURISSchema1.add(instance); // Entity
        }
        // Mock findAll
        Mockito.when(this.canonicalURIProxy.findAll()).thenAnswer(invocation -> {
            return canonicalURISSchema1;
        });
        // Mock findElement, Property, Reference
        Mockito.when(this.canonicalURIProxy.getAllByElements(anyString(),anyString(),anyString(),anyString(),anyString())).thenAnswer(invocation -> {
            List<CanonicalURI> results = new ArrayList<>();
            for (CanonicalURI cu : canonicalURISSchema1) {
                if (
                        cu.getDomain().equals(invocation.getArgument(0)) &&
                                cu.getSubDomain().equals(invocation.getArgument(1)) &&
                                cu.getType().getCode().equals(invocation.getArgument(2)) &&
                                ( (cu.getConcept() != null && cu.getConcept().equals(invocation.getArgument(3))) || (cu.getPropertyName() != null && cu.getPropertyName().equals(invocation.getArgument(3))) ) &&
                                ((cu.getIsEntity() && cu.getReference() == null && invocation.getArgument(4).equals("")) || (cu.getIsProperty() && cu.getReference().equals(invocation.getArgument(3))) || (cu.getIsInstance() && cu.getReference().equals(invocation.getArgument(4))))
                ) {
                    results.add(cu);
                }
            }
            return results;
        });
        // Mock TypeProxy
        Mockito.when(this.typeProxy.findOrCreate(anyString())).thenAnswer(invocation -> {
            return new Type(invocation.getArgument(0),invocation.getArgument(0));
        });
        // Mock post Entity, Property, Reference
        Mockito.when(this.canonicalURIProxy.save(any(CanonicalURI.class))).thenAnswer(invocation -> {
            return invocation.getArgument(0);
        });
        // Mock canonicalURIProxy getAllByEntityNameFromEntities
        Mockito.when(this.canonicalURIProxy.getAllByEntityNameFromEntities(anyString())).thenAnswer(invocation -> {
            List<CanonicalURI> response = new ArrayList<>();
            for (CanonicalURI cu : canonicalURISSchema1) {
                if (cu.getIsEntity() && cu.getConcept().equals(invocation.getArgument(0))) {
                    response.add(cu);
                }
            }
            return response;
        });
        // Mock canonicalURIProxy getAllByPropertyFromProperties
        Mockito.when(this.canonicalURIProxy.getAllByPropertyFromProperties(anyString())).thenAnswer(invocation -> {
            List<CanonicalURI> response = new ArrayList<>();
            for (CanonicalURI cu : canonicalURISSchema1) {
                if (cu.getIsProperty() && cu.getPropertyName().equals(invocation.getArgument(0))) {
                    response.add(cu);
                }
            }
            return response;
        });
        // Mock canonicalURIProxy getAllByInstanceByEntity
        Mockito.when(this.canonicalURIProxy.getAllByEntityNameAndReference(anyString(),anyString())).thenAnswer(invocation -> {
            List<CanonicalURI> response = new ArrayList<>();
            for (CanonicalURI cu : canonicalURISSchema1) {
                if (cu.getIsInstance() && cu.getConcept().equals(invocation.getArgument(0)) && cu.getReference().equals(invocation.getArgument(1))) {
                    response.add(cu);
                }
            }
            return response;
        });

        // Mock canonicalURIProxy getAllByFullURI
        Mockito.when(this.canonicalURIProxy.getAllByFullURI(anyString())).thenAnswer(invocation -> {
            List<CanonicalURI> response = new ArrayList<>();
            for (CanonicalURI cu : canonicalURISSchema1) {
                if (cu.getFullURI().equals(invocation.getArgument(0))) {
                    response.add(cu);
                }
            }
            return response;
        });

        // Mock post Entity, Property, Reference
        Mockito.when(this.schemaService.getCanonicalSchema()).thenAnswer(invocation -> {
            return canonicalSchema;
        });

    }


    @Test
    public void whenGetAllCanonicalUris_thenNoError() throws Exception {
        this.mvc.perform(get("/canonical-uri/all").accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(canonicalURISSchema1.size())));

        // @formatter:on
    }

    @Test
    public void whenGetAllEntities_thenNoError() throws Exception {
        for (CanonicalURI cu : canonicalURISSchema1) {
            if (cu.getIsEntity()) {
                this.mvc.perform(get("/canonical-uri")
                        .param("domain", cu.getDomain())
                        .param("subDomain", cu.getSubDomain())
                        .param("typeCode", cu.getTypeIdCode())
                        .param("concept", cu.getConcept())
                        .param("reference", "")
                        .accept(MediaType.APPLICATION_JSON))
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$", hasSize(1)))
                        .andExpect(jsonPath("$[0].domain", is(cu.getDomain())))
                        .andExpect(jsonPath("$[0].subDomain", is(cu.getSubDomain())))
                        .andExpect(jsonPath("$[0].typeIdCode", is(cu.getTypeIdCode())))
                        .andExpect(jsonPath("$[0].concept", is(cu.getConcept())))
                        .andExpect(jsonPath("$[0].fullURI", is(cu.getFullURI())))
                        .andExpect(jsonPath("$[0].fullURI", is(generateURLFromSchema(cu,canonicalSchema))  ));
            }
        }
    }

    @Test
    public void whenGetAllProperties_thenNoError() throws Exception {
        for (CanonicalURI cu : canonicalURISSchema1) {
            if (cu.getIsProperty()) {
                this.mvc.perform(get("/canonical-uri")
                        .param("domain", cu.getDomain())
                        .param("subDomain", cu.getSubDomain())
                        .param("typeCode", cu.getTypeIdCode())
                        .param("concept", cu.getPropertyName())
                        .param("reference", "")
                        .accept(MediaType.APPLICATION_JSON))
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$", hasSize(1)))
                        .andExpect(jsonPath("$[0].domain", is(cu.getDomain())))
                        .andExpect(jsonPath("$[0].subDomain", is(cu.getSubDomain())))
                        .andExpect(jsonPath("$[0].typeIdCode", is(cu.getTypeIdCode())))
                        .andExpect(jsonPath("$[0].propertyName", is(cu.getPropertyName())))
                        .andExpect(jsonPath("$[0].fullURI", is(cu.getFullURI())))
                        .andExpect(jsonPath("$[0].fullURI", is(generateURLFromSchema(cu,canonicalSchema))  ));
            }
        }
    }

    @Test
    public void whenGetAllInstances_thenNoError() throws Exception {
        for (CanonicalURI cu : canonicalURISSchema1) {
            if (cu.getIsInstance()) {
                this.mvc.perform(get("/canonical-uri")
                        .param("domain", cu.getDomain())
                        .param("subDomain", cu.getSubDomain())
                        .param("typeCode", cu.getTypeIdCode())
                        .param("concept", cu.getConcept())
                        .param("reference", cu.getReference())
                        .accept(MediaType.APPLICATION_JSON))
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$", hasSize(1)))
                        .andExpect(jsonPath("$[0].domain", is(cu.getDomain())))
                        .andExpect(jsonPath("$[0].subDomain", is(cu.getSubDomain())))
                        .andExpect(jsonPath("$[0].typeIdCode", is(cu.getTypeIdCode())))
                        .andExpect(jsonPath("$[0].reference", is(cu.getReference())))
                        .andExpect(jsonPath("$[0].fullURI", is(cu.getFullURI())))
                        .andExpect(jsonPath("$[0].fullURI", is(generateURLFromSchema(cu,canonicalSchema))));
            }
        }
    }

    @Test
    public void whenInsertNewEntity_thenNoError() throws Exception {

        for (CanonicalURI cu : canonicalURISSchema1) {
            if (cu.getIsEntity()) {
                this.mvc.perform(post("/canonical-uri")
                        .param("domain", cu.getDomain())
                        .param("subDomain", cu.getSubDomain())
                        .param("typeCode", cu.getTypeIdCode())
                        .param("concept", cu.getConcept())
                        .accept(MediaType.APPLICATION_JSON))
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.domain", is(cu.getDomain())))
                        .andExpect(jsonPath("$.subDomain", is(cu.getSubDomain())))
                        .andExpect(jsonPath("$.typeIdCode", is(cu.getTypeIdCode())))
                        .andExpect(jsonPath("$.concept", is(cu.getConcept())))
                        .andExpect(jsonPath("$.isEntity", is(cu.getIsEntity())))
                        .andExpect(jsonPath("$.fullURI", is(cu.getFullURI())))
                        .andExpect(jsonPath("$.fullURI", is(generateURLFromSchema(cu,canonicalSchema))));
            }
        }
    }

    @Test
    public void whenInsertNewProperty_thenNoError() throws Exception {

        for (CanonicalURI cu : canonicalURISSchema1) {
            if (cu.getIsProperty()) {
                this.mvc.perform(post("/canonical-uri")
                        .param("domain", cu.getDomain())
                        .param("subDomain", cu.getSubDomain())
                        .param("typeCode", cu.getTypeIdCode())
                        .param("property", cu.getPropertyName())
                        .accept(MediaType.APPLICATION_JSON))
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.domain", is(cu.getDomain())))
                        .andExpect(jsonPath("$.subDomain", is(cu.getSubDomain())))
                        .andExpect(jsonPath("$.typeIdCode", is(cu.getTypeIdCode())))
                        .andExpect(jsonPath("$.propertyName", is(cu.getPropertyName())))
                        .andExpect(jsonPath("$.isProperty", is(cu.getIsProperty())))
                        .andExpect(jsonPath("$.fullURI", is(cu.getFullURI())))
                        .andExpect(jsonPath("$.fullURI", is(generateURLFromSchema(cu,canonicalSchema))  ));
            }
        }
    }

    @Test
    public void whenInsertNewInstance_thenNoError() throws Exception {

        for (CanonicalURI cu : canonicalURISSchema1) {
            if (cu.getIsInstance()) {
                this.mvc.perform(post("/canonical-uri")
                        .param("domain", cu.getDomain())
                        .param("subDomain", cu.getSubDomain())
                        .param("typeCode", cu.getTypeIdCode())
                        .param("concept", cu.getConcept())
                        .param("reference", cu.getReference())
                        .accept(MediaType.APPLICATION_JSON))
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.domain", is(cu.getDomain())))
                        .andExpect(jsonPath("$.subDomain", is(cu.getSubDomain())))
                        .andExpect(jsonPath("$.typeIdCode", is(cu.getTypeIdCode())))
                        .andExpect(jsonPath("$.concept", is(cu.getConcept())))
                        .andExpect(jsonPath("$.reference", is(cu.getReference())))
                        .andExpect(jsonPath("$.isInstance", is(cu.getIsInstance())))
                        .andExpect(jsonPath("$.fullURI", is(cu.getFullURI())))
                        .andExpect(jsonPath("$.fullURI", is(generateURLFromSchema(cu,canonicalSchema))  ));
            }
        }
    }

    @Test
    public void whenGetASingleEntity_thenNoError() throws Exception {

        for (CanonicalURI cu : canonicalURISSchema1) {

            if (cu.getIsEntity()) {

                this.mvc.perform(get(String.format("/canonical-uri/entity/%s", cu.getConcept()))
                        .accept(MediaType.APPLICATION_JSON))
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$[0].domain", is(cu.getDomain())))
                        .andExpect(jsonPath("$[0].subDomain", is(cu.getSubDomain())))
                        .andExpect(jsonPath("$[0]typeIdCode", is(cu.getTypeIdCode())))
                        .andExpect(jsonPath("$[0].concept", is(cu.getConcept())))
                        .andExpect(jsonPath("$[0].isEntity", is(cu.getIsEntity())))
                        .andExpect(jsonPath("$[0].fullURI", is(cu.getFullURI())))
                        .andExpect(jsonPath("$[0].fullURI", is(generateURLFromSchema(cu, canonicalSchema))));

            }

        }
    }

    @Test
    public void whenGetASingleProperty_thenNoError() throws Exception {
        for (CanonicalURI cu : canonicalURISSchema1) {

            if (cu.getIsProperty()) {

                this.mvc.perform(get(String.format("/canonical-uri/property/%s", cu.getPropertyName()))
                        .accept(MediaType.APPLICATION_JSON))
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$[0].domain", is(cu.getDomain())))
                        .andExpect(jsonPath("$[0].subDomain", is(cu.getSubDomain())))
                        .andExpect(jsonPath("$[0]typeIdCode", is(cu.getTypeIdCode())))
                        .andExpect(jsonPath("$[0].propertyName", is(cu.getPropertyName())))
                        .andExpect(jsonPath("$[0].isProperty", is(cu.getIsProperty())))
                        .andExpect(jsonPath("$[0].fullURI", is(cu.getFullURI())))
                        .andExpect(jsonPath("$[0].fullURI", is(generateURLFromSchema(cu, canonicalSchema))));
            }
        }
    }

    @Test
    public void whenGetASingleInstance_thenNoError() throws Exception {
        for (CanonicalURI cu : canonicalURISSchema1) {
            if (cu.getIsInstance()) {
                this.mvc.perform(get(String.format("/canonical-uri/entity/%s/reference/%s", cu.getConcept(),cu.getReference()))
                        .accept(MediaType.APPLICATION_JSON))
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$[0].domain", is(cu.getDomain())))
                        .andExpect(jsonPath("$[0].subDomain", is(cu.getSubDomain())))
                        .andExpect(jsonPath("$[0]typeIdCode", is(cu.getTypeIdCode())))
                        .andExpect(jsonPath("$[0].concept", is(cu.getConcept())))
                        .andExpect(jsonPath("$[0].reference", is(cu.getReference())))
                        .andExpect(jsonPath("$[0].isInstance", is(cu.getIsInstance())))
                        .andExpect(jsonPath("$[0].fullURI", is(cu.getFullURI())))
                        .andExpect(jsonPath("$[0].fullURI", is(generateURLFromSchema(cu, canonicalSchema))));
            }
        }
    }

    @Test
    public void whenGetByCanonicalURI_thenNoError() throws Exception {
        for (CanonicalURI cu : canonicalURISSchema1) {
            if (cu.getIsInstance()) {
                this.mvc.perform(get("/canonical-uri/uri")
                        .param("fullURI", cu.getFullURI())
                        .accept(MediaType.APPLICATION_JSON))
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$[0].domain", is(cu.getDomain())))
                        .andExpect(jsonPath("$[0].subDomain", is(cu.getSubDomain())))
                        .andExpect(jsonPath("$[0]typeIdCode", is(cu.getTypeIdCode())))
                        .andExpect(jsonPath("$[0].concept", is(cu.getConcept())))
                        .andExpect(jsonPath("$[0].reference", is(cu.getReference())))
                        .andExpect(jsonPath("$[0].propertyName", is(cu.getPropertyName())))
                        .andExpect(jsonPath("$[0].isEntity", is(cu.getIsEntity())))
                        .andExpect(jsonPath("$[0].isProperty", is(cu.getIsProperty())))
                        .andExpect(jsonPath("$[0].isInstance", is(cu.getIsInstance())))
                        .andExpect(jsonPath("$[0].fullURI", is(cu.getFullURI())))
                        .andExpect(jsonPath("$[0].fullURI", is(generateURLFromSchema(cu, canonicalSchema))));
            }
        }
    }

    @Test
    public void whenDeleteEntity_thenNoError() throws Exception {

        for (CanonicalURI cu : canonicalURISSchema1) {
            if (cu.getIsEntity()) {
                this.mvc.perform(delete(String.format("/canonical-uri/entity/%s", cu.getConcept()))
                    .accept(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk());
            }
        }
    }

    @Test
    public void whenDeleteProperty_thenNoError() throws Exception {

        for (CanonicalURI cu : canonicalURISSchema1) {
            if (cu.getIsEntity()) {
                this.mvc.perform(delete(String.format("/canonical-uri/property/%s", cu.getPropertyName()))
                        .accept(MediaType.APPLICATION_JSON))
                        .andDo(print())
                        .andExpect(status().isOk());
            }
        }
    }

    @Test
    public void whenDeleteInstance_thenNoError() throws Exception {

        for (CanonicalURI cu : canonicalURISSchema1) {
            if (cu.getIsEntity()) {
                this.mvc.perform(delete(String.format("/canonical-uri/entity/%s/reference/%s", cu.getPropertyName(),cu.getReference()))
                        .accept(MediaType.APPLICATION_JSON))
                        .andDo(print())
                        .andExpect(status().isOk());
            }
        }
    }

    public String generateURLFromSchema(CanonicalURI entity, String schema) {
        entity.generateFullURL(schema);
        return entity.getFullURI();
    }

    public String generateRandomCanonicalSchema() {
        List<String> elements = Arrays.asList(new String[] {"$domain$", "$sub-domain$", "$type$", "$concept$", "$reference$" });
        Collections.shuffle(elements);
        return String.join("/",String.join("/", elements));
    }

    public String generateRandomCanonicalLanguageSchema() {
        List<String> elements = Arrays.asList(new String[] {"$domain$", "$sub-domain$", "$language$","$type$", "$concept$", "$reference$" });
        Collections.shuffle(elements);
        return String.join("/",String.join("/", elements));
    }
}