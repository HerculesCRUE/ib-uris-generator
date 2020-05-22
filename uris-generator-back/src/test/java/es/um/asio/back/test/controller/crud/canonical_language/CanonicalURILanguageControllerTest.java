package es.um.asio.back.test.controller.crud.canonical_language;

import com.fasterxml.jackson.databind.ObjectMapper;
import es.um.asio.back.controller.crud.canonical.CanonicalURIController;
import es.um.asio.back.controller.crud.canonical_language.CanonicalURILanguageController;
import es.um.asio.service.model.*;
import es.um.asio.service.proxy.*;
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
import static org.hamcrest.Matchers.in;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(CanonicalURILanguageController.class)
public class CanonicalURILanguageControllerTest {

    /**
     * MVC test support
     */
    @Autowired
    private MockMvc mvc;

    /**
     * User service
     */
    @MockBean
    private CanonicalURILanguageProxy canonicalURILanguageProxy;

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
    private LanguageProxy languageProxy;

    /**
     * User service
     */
    @MockBean
    private LanguageTypeProxy languageTypeProxy;

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

    final List<CanonicalURILanguage> canonicalURISSchema1 = new ArrayList<>();


    String canonicalSchema, canonicalLanguageSchema;


    @TestConfiguration
    static class canonicalURILanguageProxyTestConfiguration {
        @Bean
        public CanonicalURILanguageController canonicalURILanguageController() {
            return new CanonicalURILanguageController();
        }
    }

    @Before
    public void beforeTest() {

        canonicalSchema = generateRandomCanonicalSchema();
        canonicalLanguageSchema = generateRandomCanonicalLanguageSchema();

        // Mock data
        // Language:
        final Language l = new Language("en-EN","English","domain","sub-domain","type","concept","reference",true);
        // Type:
        final Type t = new Type("res","resource");
        // LanguageType:
        final LanguageType lt = new LanguageType(l,t,"res","resource");
        // CanonicalURIs Schema 1
        // Entities

        // Mock findAll
        Mockito.when(this.canonicalURILanguageProxy.findAll()).thenAnswer(invocation -> {
            return canonicalURISSchema1;
        });

        for (int i = 0; i<3 ; i++) {
            // Add Entity                                                                   , String concept, String reference, String propertyName, String shemaParam
            CanonicalURILanguage entity = new CanonicalURILanguage("hercules.org", "um",lt, String.format("entity-%d",i+1), null, null, canonicalLanguageSchema );
            canonicalURISSchema1.add(entity); // Entity
            // Add Property
            CanonicalURILanguage property = new CanonicalURILanguage("hercules.org", "um", lt, null, null, String.format("property-%d",i+1), canonicalLanguageSchema );
            canonicalURISSchema1.add(property); // Entity
            // Add Instance
            CanonicalURILanguage instance = new CanonicalURILanguage("hercules.org", "um", lt, String.format("entity-%d",i+1), String.format("instance-%d",i+1), null, canonicalLanguageSchema );
            canonicalURISSchema1.add(instance); // Entity
        }

        // Mock findAll
        Mockito.when(this.canonicalURILanguageProxy.findAll()).thenAnswer(invocation -> {
            return canonicalURISSchema1;
        });
        // Mock findElement, Property, Reference
        Mockito.when(this.canonicalURILanguageProxy.getAllByElements(anyString(),anyString(),anyString(),anyString(),any(),any())).thenAnswer(invocation -> {
            List<CanonicalURILanguage> results = new ArrayList<>();
            for (CanonicalURILanguage cu : canonicalURISSchema1) {
                if (
                        cu.getDomain().equals(invocation.getArgument(0)) &&
                                cu.getSubDomain().equals(invocation.getArgument(1)) &&
                                cu.getLanguageID().equals(invocation.getArgument(2)) &&
                                cu.getType().getCode().equals(invocation.getArgument(3)) &&
                                ( (cu.getConcept() != null && cu.getConcept().equals(invocation.getArgument(4))) || (cu.getPropertyName() != null && cu.getPropertyName().equals(invocation.getArgument(4))) ) &&
                                ((cu.getIsEntity() && cu.getReference() == null && invocation.getArgument(5).equals("")) || (cu.getIsProperty() && cu.getReference().equals(invocation.getArgument(4))) || (cu.getIsInstance() && cu.getReference().equals(invocation.getArgument(5))))
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
        Mockito.when(this.canonicalURILanguageProxy.save(any(CanonicalURILanguage.class))).thenAnswer(invocation -> {
            return invocation.getArgument(0);
        });
        // Mock canonicalURILanguageProxy getAllByEntityNameFromEntities
        Mockito.when(this.canonicalURILanguageProxy.getAllByEntityNameFromEntities(anyString())).thenAnswer(invocation -> {
            List<CanonicalURILanguage> response = new ArrayList<>();
            for (CanonicalURILanguage cu : canonicalURISSchema1) {
                if (cu.getIsEntity() && cu.getConcept().equals(invocation.getArgument(0))) {
                    response.add(cu);
                }
            }
            return response;
        });
        // Mock canonicalURILanguageProxy getAllByPropertyFromProperties
        Mockito.when(this.canonicalURILanguageProxy.getAllByPropertyNameFromProperties(anyString())).thenAnswer(invocation -> {
            List<CanonicalURILanguage> response = new ArrayList<>();
            for (CanonicalURILanguage cu : canonicalURISSchema1) {
                if (cu.getIsProperty() && cu.getPropertyName().equals(invocation.getArgument(0))) {
                    response.add(cu);
                }
            }
            return response;
        });
        // Mock canonicalURILanguageProxy getAllByInstanceByEntity
        Mockito.when(this.canonicalURILanguageProxy.getAllByEntityNameAndReference(anyString(),anyString())).thenAnswer(invocation -> {
            List<CanonicalURILanguage> response = new ArrayList<>();
            for (CanonicalURILanguage cu : canonicalURISSchema1) {
                if (cu.getIsInstance() && cu.getConcept().equals(invocation.getArgument(0)) && cu.getReference().equals(invocation.getArgument(1))) {
                    response.add(cu);
                }
            }
            return response;
        });
        // Mock canonicalURILanguageProxy getAllByEntityNameAndPropertyName
        Mockito.when(this.canonicalURIProxy.getAllByPropertyFromProperties(anyString())).thenAnswer(invocation -> {
            List<CanonicalURI> response = new ArrayList<>();
            for (CanonicalURILanguage cu : canonicalURISSchema1) {
                if (cu.getIsProperty() && cu.getPropertyName().equals(invocation.getArgument(0))) {
                    CanonicalURI canonicalURI = new CanonicalURI(
                            cu.getDomain(),
                            cu.getSubDomain(),
                            cu.getType(),
                            cu.getConcept(),
                            cu.getReference(),
                            cu.getPropertyName(),
                            canonicalSchema);
                    response.add(canonicalURI);
                }
            }
            return response;
        });

        // Mock canonicalURILanguageProxy getAllByFullURI
        Mockito.when(this.canonicalURILanguageProxy.getAllByFullURI(anyString())).thenAnswer(invocation -> {
            CanonicalURILanguage response = null;
            for (CanonicalURILanguage cu : canonicalURISSchema1) {
                if (cu.getFullURI().equals(invocation.getArgument(0))) {
                    response= cu;
                }
            }
            return response;
        });

        // Mock canonicalURILanguageProxy getAllByEntityName
        Mockito.when(this.canonicalURILanguageProxy.getAllByEntityName(anyString())).thenAnswer(invocation -> {
            List<CanonicalURILanguage> response = new ArrayList<>();
            for (CanonicalURILanguage cu : canonicalURISSchema1) {
                if (cu.getIsEntity() && cu.getConcept().equals(invocation.getArgument(0))) {
                    response.add(cu);
                }
            }
            return response;
        });

        // Mock post Entity, Property, Reference
        Mockito.when(this.schemaService.getCanonicalLanguageSchema()).thenAnswer(invocation -> {
            return canonicalLanguageSchema;
        });

        // Mock post Entity, Property, Reference
        Mockito.when(this.schemaService.getCanonicalSchema()).thenAnswer(invocation -> {
            return canonicalSchema;
        });

        // Mock proxy Type
        Mockito.when(this.typeProxy.findOrCreate(anyString())).thenAnswer(invocation -> {
            if (invocation.getArgument(0).equals(t.getCode()))
                return t;
            else
                return null;
        });

        // Mock proxy Language
        Mockito.when(this.languageProxy.findOrCreate(anyString())).thenAnswer(invocation -> {
            if (invocation.getArgument(0).equals(l.getIso()))
                return l;
            else
                return null;
        });

        // Mock proxy Language
        Mockito.when(this.languageTypeProxy.getByLanguageAndType(anyString(),anyString())).thenAnswer(invocation -> {
            if (invocation.getArgument(0).equals(lt.getLanguageId()) && invocation.getArgument(1).equals(lt.getTypeId())) {
                return Arrays.asList(new LanguageType[] {lt});
            } else {
                return null;
            }
        });

        // Mock Canonical URI Proxy
        Mockito.when(this.canonicalURIProxy.save((CanonicalURI) any())).thenAnswer(invocation -> {
            return invocation.getArgument(0);
        });

    }


    @Test
    public void whenGetAllCanonicalLanguageUris_thenNoError() throws Exception {
        this.mvc.perform(get("/canonical-uri-language/all").accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(canonicalURISSchema1.size())));

        // @formatter:on
    }

    @Test
    public void whenGetAllEntities_thenNoError() throws Exception {
        for (CanonicalURILanguage cu : canonicalURISSchema1) {
            if (cu.getIsEntity()) {
                this.mvc.perform(get("/canonical-uri-language")
                        .param("domain", cu.getDomain())
                        .param("subDomain", cu.getSubDomain())
                        .param("language","en-EN")
                        .param("typeCode", "res")
                        .param("concept", cu.getConcept())
                        .param("reference", "")
                        .accept(MediaType.APPLICATION_JSON))
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$", hasSize(1)))
                        .andExpect(jsonPath("$[0].domain", is(cu.getDomain())))
                        .andExpect(jsonPath("$[0].subDomain", is(cu.getSubDomain())))
                        .andExpect(jsonPath("$[0].typeCode", is(cu.getTypeCode())))
                        .andExpect(jsonPath("$[0].languageID", is(cu.getLanguageID())))
                        .andExpect(jsonPath("$[0].concept", is(cu.getConcept())))
                        .andExpect(jsonPath("$[0].fullURI", is(cu.getFullURI())))
                        .andExpect(jsonPath("$[0].fullURI", is(generateURLFromSchema(cu,canonicalLanguageSchema))  ));
            }
        }
    }

    @Test
    public void whenGetAllProperties_thenNoError() throws Exception {
        for (CanonicalURILanguage cu : canonicalURISSchema1) {
            if (cu.getIsProperty()) {
                this.mvc.perform(get("/canonical-uri-language")
                        .param("domain", cu.getDomain())
                        .param("subDomain", cu.getSubDomain())
                        .param("language","en-EN")
                        .param("typeCode", "res")
                        .param("concept", cu.getPropertyName())
                        .param("reference", "")
                        .accept(MediaType.APPLICATION_JSON))
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$", hasSize(1)))
                        .andExpect(jsonPath("$[0].domain", is(cu.getDomain())))
                        .andExpect(jsonPath("$[0].subDomain", is(cu.getSubDomain())))
                        .andExpect(jsonPath("$[0].typeCode", is(cu.getTypeCode())))
                        .andExpect(jsonPath("$[0].languageID", is(cu.getLanguageID())))
                        .andExpect(jsonPath("$[0].propertyName", is(cu.getPropertyName())))
                        .andExpect(jsonPath("$[0].fullURI", is(cu.getFullURI())))
                        .andExpect(jsonPath("$[0].fullURI", is(generateURLFromSchema(cu,canonicalLanguageSchema))  ));
            }
        }
    }

    @Test
    public void whenGetAllInstances_thenNoError() throws Exception {
        for (CanonicalURILanguage cu : canonicalURISSchema1) {
            if (cu.getIsInstance()) {
                this.mvc.perform(get("/canonical-uri-language")
                        .param("domain", cu.getDomain())
                        .param("subDomain", cu.getSubDomain())
                        .param("language","en-EN")
                        .param("typeCode", "res")
                        .param("concept", cu.getConcept())
                        .param("reference", cu.getReference())
                        .accept(MediaType.APPLICATION_JSON))
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$", hasSize(1)))
                        .andExpect(jsonPath("$[0].domain", is(cu.getDomain())))
                        .andExpect(jsonPath("$[0].subDomain", is(cu.getSubDomain())))
                        .andExpect(jsonPath("$[0].typeCode", is(cu.getTypeCode())))
                        .andExpect(jsonPath("$[0].languageID", is(cu.getLanguageID())))
                        .andExpect(jsonPath("$[0].reference", is(cu.getReference())))
                        .andExpect(jsonPath("$[0].fullURI", is(cu.getFullURI())))
                        .andExpect(jsonPath("$[0].fullURI", is(generateURLFromSchema(cu,canonicalLanguageSchema))));
            }
        }
    }

    @Test
    public void whenInsertNewEntity_thenNoError() throws Exception {

        for (CanonicalURILanguage cu : canonicalURISSchema1) {
            if (cu.getIsEntity()) {
                this.mvc.perform(post("/canonical-uri-language")
                        .param("domain", cu.getDomain())
                        .param("subDomain", cu.getSubDomain())
                        .param("language", cu.getLanguageID())
                        .param("typeCode", cu.getTypeCode())
                        .param("concept", cu.getConcept())
                        .param("parentEntity", cu.getConcept())
                        .param("createCanonicalIfNotExist", "true")
                        .accept(MediaType.APPLICATION_JSON))
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.domain", is(cu.getDomain())))
                        .andExpect(jsonPath("$.subDomain", is(cu.getSubDomain())))
                        .andExpect(jsonPath("$.typeCode", is(cu.getTypeCode())))
                        .andExpect(jsonPath("$.concept", is(cu.getConcept())))
                        .andExpect(jsonPath("$.isEntity", is(cu.getIsEntity())))
                        .andExpect(jsonPath("$.fullURI", is(cu.getFullURI())))
                        .andExpect(jsonPath("$.fullURI", is(generateURLFromSchema(cu,canonicalLanguageSchema))));
            }
        }
    }

    @Test
    public void whenInsertNewProperty_thenNoError() throws Exception {

        for (CanonicalURILanguage cu : canonicalURISSchema1) {
            if (cu.getIsProperty()) {
                this.mvc.perform(post("/canonical-uri-language")
                        .param("domain", cu.getDomain())
                        .param("subDomain", cu.getSubDomain())
                        .param("language", cu.getLanguageID())
                        .param("typeCode", cu.getTypeCode())
                        .param("property", cu.getPropertyName())
                        .param("parentProperty", cu.getPropertyName())
                        .param("createCanonicalIfNotExist", "true")
                        .accept(MediaType.APPLICATION_JSON))
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.domain", is(cu.getDomain())))
                        .andExpect(jsonPath("$.subDomain", is(cu.getSubDomain())))
                        .andExpect(jsonPath("$.typeCode", is(cu.getTypeCode())))
                        .andExpect(jsonPath("$.propertyName", is(cu.getPropertyName())))
                        .andExpect(jsonPath("$.isProperty", is(cu.getIsProperty())))
                        .andExpect(jsonPath("$.fullURI", is(cu.getFullURI())))
                        .andExpect(jsonPath("$.fullURI", is(generateURLFromSchema(cu,canonicalLanguageSchema))  ));
            }
        }
    }

    @Test
    public void whenInsertNewInstance_thenNoError() throws Exception {

        for (CanonicalURILanguage cu : canonicalURISSchema1) {
            if (cu.getIsInstance()) {
                this.mvc.perform(post("/canonical-uri-language")
                        .param("domain", cu.getDomain())
                        .param("subDomain", cu.getSubDomain())
                        .param("language", cu.getLanguageID())
                        .param("typeCode", cu.getTypeCode())
                        .param("concept", cu.getConcept())
                        .param("parentEntity", cu.getConcept())
                        .param("reference", cu.getReference())
                        .param("createCanonicalIfNotExist", "true")
                        .accept(MediaType.APPLICATION_JSON))
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.domain", is(cu.getDomain())))
                        .andExpect(jsonPath("$.subDomain", is(cu.getSubDomain())))
                        .andExpect(jsonPath("$.typeCode", is(cu.getTypeCode())))
                        .andExpect(jsonPath("$.concept", is(cu.getConcept())))
                        .andExpect(jsonPath("$.reference", is(cu.getReference())))
                        .andExpect(jsonPath("$.isInstance", is(cu.getIsInstance())))
                        .andExpect(jsonPath("$.fullURI", is(cu.getFullURI())))
                        .andExpect(jsonPath("$.fullURI", is(generateURLFromSchema(cu,canonicalLanguageSchema))  ));
            }
        }
    }

    @Test
    public void whenGetASingleEntity_thenNoError() throws Exception {

        for (CanonicalURILanguage cu : canonicalURISSchema1) {

            if (cu.getIsEntity()) {

                this.mvc.perform(get(String.format("/canonical-uri-language/entity/%s", cu.getConcept()))
                        .accept(MediaType.APPLICATION_JSON))
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$[0].domain", is(cu.getDomain())))
                        .andExpect(jsonPath("$[0].subDomain", is(cu.getSubDomain())))
                        .andExpect(jsonPath("$[0].typeCode", is(cu.getTypeCode())))
                        .andExpect(jsonPath("$[0].concept", is(cu.getConcept())))
                        .andExpect(jsonPath("$[0].isEntity", is(cu.getIsEntity())))
                        .andExpect(jsonPath("$[0].fullURI", is(cu.getFullURI())))
                        .andExpect(jsonPath("$[0].fullURI", is(generateURLFromSchema(cu, canonicalLanguageSchema))));

            }

        }
    }

    @Test
    public void whenGetASingleProperty_thenNoError() throws Exception {
        for (CanonicalURILanguage cu : canonicalURISSchema1) {

            if (cu.getIsProperty()) {

                this.mvc.perform(get(String.format("/canonical-uri-language/property/%s", cu.getPropertyName()))
                        .accept(MediaType.APPLICATION_JSON))
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$[0].domain", is(cu.getDomain())))
                        .andExpect(jsonPath("$[0].subDomain", is(cu.getSubDomain())))
                        .andExpect(jsonPath("$[0].typeCode", is(cu.getTypeCode())))
                        .andExpect(jsonPath("$[0].propertyName", is(cu.getPropertyName())))
                        .andExpect(jsonPath("$[0].isProperty", is(cu.getIsProperty())))
                        .andExpect(jsonPath("$[0].fullURI", is(cu.getFullURI())))
                        .andExpect(jsonPath("$[0].fullURI", is(generateURLFromSchema(cu, canonicalLanguageSchema))));
            }
        }
    }

    @Test
    public void whenGetASingleInstance_thenNoError() throws Exception {
        for (CanonicalURILanguage cu : canonicalURISSchema1) {
            if (cu.getIsInstance()) {
                this.mvc.perform(get(String.format("/canonical-uri-language/entity/%s/reference/%s", cu.getConcept(),cu.getReference()))
                        .accept(MediaType.APPLICATION_JSON))
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$[0].domain", is(cu.getDomain())))
                        .andExpect(jsonPath("$[0].subDomain", is(cu.getSubDomain())))
                        .andExpect(jsonPath("$[0].typeCode", is(cu.getTypeCode())))
                        .andExpect(jsonPath("$[0].concept", is(cu.getConcept())))
                        .andExpect(jsonPath("$[0].reference", is(cu.getReference())))
                        .andExpect(jsonPath("$[0].isInstance", is(cu.getIsInstance())))
                        .andExpect(jsonPath("$[0].fullURI", is(cu.getFullURI())))
                        .andExpect(jsonPath("$[0].fullURI", is(generateURLFromSchema(cu, canonicalLanguageSchema))));
            }
        }
    }

    @Test
    public void whenGetByCanonicalURI_thenNoError() throws Exception {
        for (CanonicalURILanguage cu : canonicalURISSchema1) {
            if (cu.getIsInstance()) {
                this.mvc.perform(get("/canonical-uri-language/uri")
                        .param("fullURI", cu.getFullURI())
                        .accept(MediaType.APPLICATION_JSON))
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.domain", is(cu.getDomain())))
                        .andExpect(jsonPath("$.subDomain", is(cu.getSubDomain())))
                        .andExpect(jsonPath("$.typeCode", is(cu.getTypeCode())))
                        .andExpect(jsonPath("$.concept", is(cu.getConcept())))
                        .andExpect(jsonPath("$.reference", is(cu.getReference())))
                        .andExpect(jsonPath("$.propertyName", is(cu.getPropertyName())))
                        .andExpect(jsonPath("$.isEntity", is(cu.getIsEntity())))
                        .andExpect(jsonPath("$.isProperty", is(cu.getIsProperty())))
                        .andExpect(jsonPath("$.isInstance", is(cu.getIsInstance())))
                        .andExpect(jsonPath("$.fullURI", is(cu.getFullURI())))
                        .andExpect(jsonPath("$.fullURI", is(generateURLFromSchema(cu, canonicalLanguageSchema))));
            }
        }
    }

    @Test
    public void whenDeleteEntity_thenNoError() throws Exception {

        for (CanonicalURILanguage cu : canonicalURISSchema1) {
            if (cu.getIsEntity()) {
                this.mvc.perform(delete(String.format("/canonical-uri-language/entity/%s", cu.getConcept()))
                    .accept(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk());
            }
        }
    }

    @Test
    public void whenDeleteProperty_thenNoError() throws Exception {

        for (CanonicalURILanguage cu : canonicalURISSchema1) {
            if (cu.getIsEntity()) {
                this.mvc.perform(delete(String.format("/canonical-uri-language/property/%s", cu.getPropertyName()))
                        .accept(MediaType.APPLICATION_JSON))
                        .andDo(print())
                        .andExpect(status().isOk());
            }
        }
    }

    @Test
    public void whenDeleteInstance_thenNoError() throws Exception {

        for (CanonicalURILanguage cu : canonicalURISSchema1) {
            if (cu.getIsEntity()) {
                this.mvc.perform(delete(String.format("/canonical-uri-language/entity/%s/reference/%s", cu.getPropertyName(),cu.getReference()))
                        .accept(MediaType.APPLICATION_JSON))
                        .andDo(print())
                        .andExpect(status().isOk());
            }
        }
    }

    public String generateURLFromSchema(CanonicalURILanguage entity, String schema) {
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