package es.um.asio.back.test.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import es.um.asio.back.controller.URISController;
import es.um.asio.back.controller.crud.canonical_language.CanonicalURILanguageController;
import es.um.asio.back.controller.crud.language.LanguageController;
import es.um.asio.back.controller.crud.local.LocalURIController;
import es.um.asio.back.controller.crud.storage_type.StorageTypeController;
import es.um.asio.back.controller.crud.type.TypeController;
import es.um.asio.service.model.*;
import es.um.asio.service.proxy.*;
import es.um.asio.service.service.CanonicalURILanguageService;
import es.um.asio.service.service.CanonicalURIService;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(URISController.class)
@ActiveProfiles("dev")
public class URISControllerTest {

    /**
     * MVC test support
     */
    @Autowired
    private MockMvc mvc;


    /**
     * CanonicalURILanguageController controller
     */
    @Autowired
    private CanonicalURILanguageController canonicalURILanguageController;

    /**
     * CanonicalURILanguageController controller
     */
    @Autowired
    private LocalURIController localURIController;

    /**
     * CanonicalURILanguageController controller
     */
    @Autowired
    private StorageTypeController storageTypeController;

    /**
     * CanonicalURILanguageService controller
     */
    @MockBean
    private CanonicalURILanguageService canonicalURILanguageService;

    /**
     * CanonicalURILanguageService controller
     */
    @MockBean
    private CanonicalURIService canonicalURIService;


    /**
     * LocalURIProxy Proxy
     */
    @MockBean
    private CanonicalURILanguageProxy canonicalURILanguageProxy;

    /**
     * LocalURIProxy Proxy
     */
    @MockBean
    private CanonicalURIProxy canonicalURIProxy;

    /**
     * LocalURIProxy Proxy
     */
    @MockBean
    private LocalURIProxy localURIProxy;

    /**
     * LanguageProxy proxy
     */
    @MockBean
    private LanguageProxy languageProxy;

    /**
     * LanguageTypeProxy proxy
     */
    @MockBean
    private LanguageTypeProxy languageTypeProxy;

    /**
     * TypeProxy proxy
     */
    @MockBean
    private TypeProxy typeProxy;

    /**
     * TypeProxy proxy
     */
    @MockBean
    private StorageTypeProxy storageTypeProxy;

    /**
     * TypeProxy proxy
     */
    @MockBean
    private SchemaService schemaService;

    /**
     * JSON Object mapper
     */
    @Autowired
    private ObjectMapper objectMapper;

    final List<Language> languages = new ArrayList<>();
    final List<Type> types = new ArrayList<>();
    final List<LanguageType> languageTypes = new ArrayList<>();
    final List<CanonicalURILanguage> canonicalURILanguages = new ArrayList<>();
    final List<StorageType> storageTypes = new ArrayList<>();
    final List<LocalURI> localURIS = new ArrayList<>();



    @TestConfiguration
    static class urisControllerProxyTestConfiguration {
        @Bean
        public URISController urisController() {
            return new URISController();
        }

        @Bean
        public CanonicalURILanguageController canonicalUriLanguageController() {
            return new CanonicalURILanguageController();
        }

        @Bean
        public LocalURIController localURIController() {
            return new LocalURIController();
        }

        @Bean
        public StorageTypeController storageTypeController() {
            return new StorageTypeController();
        }
    }

    @Before
    public void beforeTest() {


        // Languages:
        Language l1 = new Language("en-EN","English","domain","sub-domain", "type","concept","reference",false);
        Language l2 = new Language("es-ES","Español","dominio","sub-dominio", "tipo","concepto","referencia",true);
        languages.add(l1);
        languages.add(l2);

        // Types
        Type t1 = new Type("def","definitions");
        Type t2 = new Type("kos","skos");
        Type t3 = new Type("res","resources");
        Type t4 = new Type("cat","catalog");
        types.add(t1);
        types.add(t2);
        types.add(t3);
        types.add(t4);

        // Language Type


        LanguageType lt1 = new LanguageType(l2,t3,"rec","recurso");
        LanguageType lt2 = new LanguageType(l1,t3,"res","resource");
        languageTypes.add(lt1);
        languageTypes.add(lt2);


        String canonicalLanguageSchema = "http://$domain$/$sub-domain$/$language$/$type$/$concept$/$reference$";
        String canonicalSchema = "http://$domain$/$sub-domain$/$type$/$concept$/$reference$";

        int counterLT = 0;
        Map<String, CanonicalURI> canonicalURIMap = new HashMap<>();
        for (LanguageType lt: languageTypes) {
            counterLT++;
            // Add Entity                                                                   , String concept, String reference, String propertyName, String shemaParam
            CanonicalURILanguage entity = new CanonicalURILanguage("hercules.org", "um",lt, String.format("entity-%d",counterLT), null, null, canonicalLanguageSchema );

            CanonicalURI cu1 = new CanonicalURI(entity.getDomain(),entity.getSubDomain(),entity.getType(),entity.getConcept(),entity.getReference(),entity.getPropertyName(),canonicalSchema);
            if (canonicalURIMap.get(cu1.getFullURI()) == null) {
                cu1.setCanonicalURILanguages(new HashSet<>());
                canonicalURIMap.put(cu1.getFullURI(),cu1);
            }
            cu1 = canonicalURIMap.get(cu1.getFullURI());
            cu1.getCanonicalURILanguages().add(entity);

            entity.setCanonicalURI(cu1);
            canonicalURILanguages.add(entity); // Entity

            CanonicalURILanguage property = new CanonicalURILanguage("hercules.org", "um", lt, null, null, String.format("property-%d",counterLT), canonicalLanguageSchema );

            CanonicalURI cu2 = new CanonicalURI(property.getDomain(),property.getSubDomain(),property.getType(),property.getConcept(),property.getReference(),property.getPropertyName(),canonicalSchema);
            if (canonicalURIMap.get(cu2.getFullURI()) == null) {
                cu2.setCanonicalURILanguages(new HashSet<>());
                canonicalURIMap.put(cu2.getFullURI(),cu2);
            }
            cu2 = canonicalURIMap.get(cu2.getFullURI());
            cu2.getCanonicalURILanguages().add(property);

            property.setCanonicalURI(cu2);
            canonicalURILanguages.add(property); // Property

            // Add Instance
            CanonicalURILanguage instance = new CanonicalURILanguage("hercules.org", "um", lt, String.format("entity-%d",counterLT+1), String.format("instance-%d",counterLT), null, canonicalLanguageSchema );

            CanonicalURI cu3 = new CanonicalURI(instance.getDomain(),instance.getSubDomain(),instance.getType(),instance.getConcept(),instance.getReference(),instance.getPropertyName(),canonicalSchema);
            if (canonicalURIMap.get(cu3.getFullURI()) == null) {
                cu3.setCanonicalURILanguages(new HashSet<>());
                canonicalURIMap.put(cu3.getFullURI(),cu3);
            }
            cu3 = canonicalURIMap.get(cu3.getFullURI());
            cu3.getCanonicalURILanguages().add(instance);

            instance.setCanonicalURI(cu3);
            canonicalURILanguages.add(instance); // Instance
        }

        // Storage Types
        StorageType st1 = new StorageType("trellis");
        StorageType st2 = new StorageType("wikibase");
        storageTypes.add(st1);
        storageTypes.add(st2);;

        // Local URIS
        for (CanonicalURILanguage cul : canonicalURILanguages) {
            for (StorageType st : storageTypes) {
                String uri = "http://" +st.getName()+"/"+((cul.getIsInstance())?("instance/"+cul.getReference()):(cul.getIsProperty())?("property/"+cul.getPropertyName()):("concept/"+cul.getConcept()));
                localURIS.add(new LocalURI(uri,cul,st));
            }
        }


        // Mock proxy Type
        Mockito.when(this.typeProxy.findOrCreate(anyString())).thenAnswer(invocation -> {
            for (Type t : types) {
                if (invocation.getArgument(0).equals(t.getCode()))
                    return t;
            }
            return null;
        });

        // Mock proxy Type
        Mockito.when(this.typeProxy.find(anyString())).thenAnswer(invocation -> {
            for (Type t : types) {
                if (invocation.getArgument(0).equals(t.getCode()))
                    return Optional.of(t);
            }
            return Optional.empty();
        });

        // Mock proxy Language
        Mockito.when(this.languageProxy.findOrCreate(anyString())).thenAnswer(invocation -> {
            for (Language l : languages) {
                if (invocation.getArgument(0).equals(l.getIso()))
                    return l;
            }
            return null;
        });

        // Mock proxy LanguageType
        Mockito.when(this.languageTypeProxy.getByLanguageAndType(anyString(),anyString())).thenAnswer(invocation -> {
            List<LanguageType> results = new ArrayList<>();
            for (LanguageType lt : languageTypes) {
                if (invocation.getArgument(0).equals(lt.getLanguageId()) && invocation.getArgument(1).equals(lt.getTypeId()))
                    results.add(lt);
            }
            return results;
        });

        // Mock proxy Canonical
        Mockito.when(this.canonicalURIProxy.save((CanonicalURI) any())).thenAnswer(invocation -> {
            return invocation.getArgument(0);
            /*
            Set<CanonicalURI> canonicalURIS = new HashSet<>();
            for (CanonicalURILanguage cul : canonicalURILanguages) {
                CanonicalURI cu = cul.getCanonicalURI();
                if (cu.getIsProperty() && cu.getPropertyName().equals(inv.getArgument(0)) )
                    canonicalURIS.add(cu);
            }
            return Arrays.asList(canonicalURIS.toArray());
            */
        });

        // Mock proxy CanonicalLanguage
        Mockito.when(this.canonicalURILanguageProxy.save((CanonicalURILanguage) any())).thenAnswer(invocation -> {
            return (CanonicalURILanguage) invocation.getArgument(0);
        });

        Mockito.when(this.canonicalURILanguageProxy.getAllByFullURI(anyString())).thenAnswer(invocation -> {
            for (CanonicalURILanguage cul : canonicalURILanguages) {
                if (invocation.getArgument(0).equals(cul.getFullURI()))
                    return cul;
            }
            return null;
        });

        // Mock proxy CanonicalLanguage
        Mockito.when(this.canonicalURILanguageProxy.save((CanonicalURILanguage) any())).thenAnswer(invocation -> {
            return (CanonicalURILanguage) invocation.getArgument(0);
        });

        Mockito.when(this.canonicalURILanguageProxy.getAllByFullURI(anyString())).thenAnswer(invocation -> {
            for (CanonicalURILanguage cul : canonicalURILanguages) {
                if (invocation.getArgument(0).equals(cul.getFullURI()))
                    return cul;
            }
            return null;
        });

        // Mock proxy LocalURIProxy
        Mockito.when(this.localURIProxy.getAllByLocalURIStr(anyString())).thenAnswer(invocation -> {
            List<LocalURI> response = new ArrayList<>();
            for (LocalURI lu : localURIS) {
                if (lu.getLocalUri().equals(invocation.getArgument(0)))
                    response.add(lu);
            }
            return response;
        });

        // Mock proxy LocalURIProxy
        Mockito.when(this.localURIProxy.getAllByLocalURI((LocalURI) any())).thenAnswer(invocation -> {
            List<LocalURI> response = new ArrayList<>();
            for (LocalURI lu : localURIS) {
                if (
                    ((LocalURI)invocation.getArgument(0)).getCanonicalURILanguageStr().equals(lu.getCanonicalURILanguageStr()) &&
                    ((LocalURI)invocation.getArgument(0)).getLocalUri().equals(lu.getLocalUri()) &&
                    ((LocalURI)invocation.getArgument(0)).getStorageTypeStr().equals(lu.getStorageTypeStr())
                ) {
                    response.add(lu);
                }
            }
            return response;
        });

        Mockito.when(this.localURIProxy.getAllByCanonicalURILanguageStrAndStorageTypeStr(anyString(),anyString())).thenAnswer(invocation -> {
            List<LocalURI> response = new ArrayList<>();
            for (LocalURI lu : localURIS) {
                if (
                        lu.getCanonicalURILanguageStr().equals(invocation.getArgument(0)) &&
                        lu.getStorageTypeStr().equals(invocation.getArgument(1))
                ) {
                    response.add(lu);
                }
            }
            return response;
        });

        // Mock StorageType proxy
        Mockito.when(this.storageTypeProxy.findByName(anyString())).thenAnswer(invocation -> {
            for (StorageType st : storageTypes) {
                if (invocation.getArgument(0).equals(st.getName()))
                    return st;
            }
            return null;
        });

        // Mock service SchemaService
        Mockito.when(this.schemaService.getCanonicalLanguageSchema()).thenAnswer(invocation -> {
            return canonicalLanguageSchema;
        });

        // Mock service SchemaService
        Mockito.when(this.schemaService.getCanonicalSchema()).thenAnswer(invocation -> {
            return canonicalSchema;
        });

        // Mock service CanonicalURILanguageService
        Mockito.when(this.canonicalURILanguageService.getAllByFullURI(anyString())).thenAnswer(invocation -> {
            for (CanonicalURILanguage cul : canonicalURILanguages) {
                if (invocation.getArgument(0).equals(cul.getFullURI()))
                    return cul;
            }
            return null;
        });

        Mockito.when(this.canonicalURILanguageService.getAllByPropertyNameAndIsProperty(anyString())).thenAnswer(invocation -> {
            List<CanonicalURILanguage> response = new ArrayList<>();
            for (CanonicalURILanguage cul : canonicalURILanguages) {
                if (cul.getIsProperty() && invocation.getArgument(0).equals(cul.getPropertyName()))
                    response.add(cul);
            }
            return response;
        });

        // Mock service CanonicalURIService
        Mockito.when(this.canonicalURIService.getAllByFullURI(anyString())).thenAnswer(invocation -> {
            Set<CanonicalURI> canonicalURISSet = new HashSet<>();
            for (CanonicalURILanguage cul : canonicalURILanguages) {
                CanonicalURI cu = cul.getCanonicalURI();
                if (invocation.getArgument(0).equals(cu.getFullURI()))
                    canonicalURISSet.add(cu);
            }
            return Arrays.asList(canonicalURISSet.toArray());
        });


    }

    @Test
    public void whenCreateAResource_thenNoError() throws Exception {
        for (CanonicalURILanguage cu : canonicalURILanguages) {
            if (cu.getIsInstance()) {
                LinkedHashMap<String, String> payload = new LinkedHashMap<>();
                payload.put("@class", cu.getEntityName());
                payload.put("entityId", cu.getReference());
                payload.put("name", cu.getReference());
                payload.put("parentEntity", cu.getEntityName());
                this.mvc.perform(post("/uri-factory/canonical/resource")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("domain", cu.getDomain())
                        .param("subDomain", cu.getSubDomain())
                        .param("lang", cu.getLanguageID())
                        .content(asJsonString(payload))
                )
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.canonicalURI", is(cu.getFullParentURI())))
                        .andExpect(jsonPath("$.canonicalLanguageURI", is(cu.getFullURI())))
                        .andExpect(jsonPath("$.language", is(cu.getLanguageID())));
            }

        }
    }

    @Test
    public void whenCreateAProperty_thenNoError() throws Exception {
        for (CanonicalURILanguage cu : canonicalURILanguages) {
            if (cu.getIsProperty()) {
                LinkedHashMap<String, String> payload = new LinkedHashMap<>();
                payload.put("property", cu.getPropertyName());
                payload.put("canonicalProperty", cu.getPropertyName());
                this.mvc.perform(post("/uri-factory/canonical/property")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("domain", cu.getDomain())
                        .param("subDomain", cu.getSubDomain())
                        .param("lang", cu.getLanguageID())
                        .content(asJsonString(payload))
                )
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.canonicalURI", is(cu.getFullParentURI())))
                        .andExpect(jsonPath("$.canonicalLanguageURI", is(cu.getFullURI())))
                        .andExpect(jsonPath("$.language", is(cu.getLanguageID())));
            }

        }
    }

    @Test
    public void whenCreateAEntity_thenNoError() throws Exception {
        for (CanonicalURILanguage cu : canonicalURILanguages) {
            if (cu.getIsEntity()) {
                LinkedHashMap<String, String> payload = new LinkedHashMap<>();
                payload.put("@class", cu.getEntityName());
                this.mvc.perform(post("/uri-factory/canonical/entity")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("domain", cu.getDomain())
                        .param("subDomain", cu.getSubDomain())
                        .param("lang", cu.getLanguageID())
                        .content(asJsonString(payload))
                )
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.canonicalURI", is(cu.getFullParentURI())))
                        .andExpect(jsonPath("$.canonicalLanguageURI", is(cu.getFullURI())))
                        .andExpect(jsonPath("$.language", is(cu.getLanguageID())));
            }

        }
    }

    /*@Test
    public void whenLinkACanonicalURIWithLocalURI_thenNoError() throws Exception {
        for (LocalURI lu : localURIS) {
            this.mvc.perform(post("/uri-factory/local")
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .param("canonicalLanguageURI", lu.getCanonicalURILanguageStr())
                    .param("localURI", lu.getLocalUri())
                    .param("storageName", lu.getStorageTypeStr())
            )
                    .andDo(print())
                    .andExpect(status().isOk());
        }

    }



    @Test
    public void whenLinkAInstanceWithLocalURIByParams_thenNoError() throws Exception {
        for (LocalURI lu : localURIS) {
            if (lu.getCanonicalURILanguage().getIsInstance()) {
                CanonicalURILanguage cul = lu.getCanonicalURILanguage();
                this.mvc.perform(post("/uri-factory/local/resource")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("domain", cul.getDomain())
                        .param("subDomain", cul.getSubDomain())
                        .param("languageCode", cul.getLanguageID())
                        .param("typeCode", cul.getTypeCode())
                        .param("entity", cul.getEntityName())
                        .param("reference", cul.getReference())
                        .param("localURI", lu.getLocalUri())
                        .param("storageName", lu.getStorageTypeStr())
                )
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.canonicalURILanguageStr", is(lu.getCanonicalURILanguageStr())))
                        .andExpect(jsonPath("$.storageTypeStr", is(lu.getStorageTypeStr())))
                        .andExpect(jsonPath("$.localUri", is(lu.getLocalUri())));
            }

        }
    }

    @Test
    public void whenLinkAPropertyWithLocalURIByParams_thenNoError() throws Exception {
        for (LocalURI lu : localURIS) {
            if (lu.getCanonicalURILanguage().getIsProperty()) {
                CanonicalURILanguage cul = lu.getCanonicalURILanguage();
                this.mvc.perform(post("/uri-factory/local/property")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("domain", cul.getDomain())
                        .param("subDomain", cul.getSubDomain())
                        .param("languageCode", cul.getLanguageID())
                        .param("typeCode", cul.getTypeCode())
                        .param("property", cul.getPropertyName())
                        .param("localURI", lu.getLocalUri())
                        .param("storageName", lu.getStorageTypeStr())
                )
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.canonicalURILanguageStr", is(lu.getCanonicalURILanguageStr())))
                        .andExpect(jsonPath("$.storageTypeStr", is(lu.getStorageTypeStr())))
                        .andExpect(jsonPath("$.localUri", is(lu.getLocalUri())));
            }

        }
    }*/

/*    @Test
    public void whenUnlinkAEntityWithLocalURIByParams_thenNoError() throws Exception {
        for (LocalURI lu : localURIS) {
            if (lu.getCanonicalURILanguage().getIsEntity()) {
                CanonicalURILanguage cul = lu.getCanonicalURILanguage();
                this.mvc.perform(delete("/uri-factory/local")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("canonicalLanguageURI", lu.getCanonicalURILanguageStr())
                        .param("localURI", lu.getLocalUri())
                        .param("storageName", lu.getStorageTypeStr())
                )
                        .andDo(print())
                        .andExpect(status().isOk());
            }

        }
    }*/

/*    @Test
    public void whenDeleteEntityWithLocalURIByParams_thenNoError() throws Exception {
        for (LocalURI lu : localURIS) {
            CanonicalURILanguage cul = lu.getCanonicalURILanguage();
            if (cul.getIsEntity()) {
                this.mvc.perform(delete("/uri-factory/local/entity")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("domain", cul.getDomain())
                        .param("subDomain", cul.getSubDomain())
                        .param("languageCode", cul.getLanguageID())
                        .param("typeCode", cul.getTypeCode())
                        .param("entity", cul.getEntityName())
                        .param("localURI", lu.getLocalUri())
                        .param("storageName", lu.getStorageTypeStr())
                )
                        .andDo(print())
                        .andExpect(status().isOk());
            }

        }
    }

    @Test
    public void whenDeleteInstanceWithLocalURIByParams_thenNoError() throws Exception {
        for (LocalURI lu : localURIS) {
            CanonicalURILanguage cul = lu.getCanonicalURILanguage();
            if (cul.getIsInstance()) {
                this.mvc.perform(delete("/uri-factory/local/resource")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("domain", cul.getDomain())
                        .param("subDomain", cul.getSubDomain())
                        .param("languageCode", cul.getLanguageID())
                        .param("typeCode", cul.getTypeCode())
                        .param("entity", cul.getEntityName())
                        .param("reference", cul.getReference())
                        .param("localURI", lu.getLocalUri())
                        .param("storageName", lu.getStorageTypeStr())
                )
                        .andDo(print())
                        .andExpect(status().isOk());
            }

        }
    }

    public void whenDeletePropertyWithLocalURIByParams_thenNoError() throws Exception {
        for (LocalURI lu : localURIS) {
            CanonicalURILanguage cul = lu.getCanonicalURILanguage();
            if (cul.getIsInstance()) {
                this.mvc.perform(delete("/uri-factory/local/resource")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("domain", cul.getDomain())
                        .param("subDomain", cul.getSubDomain())
                        .param("languageCode", cul.getLanguageID())
                        .param("typeCode", cul.getTypeCode())
                        .param("entity", cul.getEntityName())
                        .param("reference", cul.getReference())
                        .param("localURI", lu.getLocalUri())
                        .param("storageName", lu.getStorageTypeStr())
                )
                        .andDo(print())
                        .andExpect(status().isOk());
            }

        }
    }*/

    @Test
    public void whenGetLocalURIFromCanonicalURI_thenNoError() throws Exception {
        for (LocalURI lu : localURIS) {
            CanonicalURILanguage cul = lu.getCanonicalURILanguage();
            if (cul.getIsProperty()) {
                this.mvc.perform(get("/uri-factory/local/canonical")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("canonicalUri", cul.getFullParentURI())
                        .param("storageName", lu.getStorageTypeStr())
                        .param("languageCode", cul.getLanguageID())
                )
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.[0].canonicalURILanguageStr", is(lu.getCanonicalURILanguageStr())))
                        .andExpect(jsonPath("$.[0].storageTypeStr", is(lu.getStorageTypeStr())))
                        .andExpect(jsonPath("$.[0].localUri", is(lu.getLocalUri())));;
            }

        }
    }

    @Test
    public void whenGetLocalURIFromCanonicalURILanguage_thenNoError() throws Exception {
        for (LocalURI lu : localURIS) {
            CanonicalURILanguage cul = lu.getCanonicalURILanguage();
            if (cul.getIsProperty()) {
                this.mvc.perform(get("/uri-factory/local/canonical/language")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("canonicalLanguageUri", cul.getFullURI())
                        .param("storageName", lu.getStorageTypeStr())
                        .param("languageCode", cul.getLanguageID())
                )
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.[0].canonicalURILanguageStr", is(lu.getCanonicalURILanguageStr())))
                        .andExpect(jsonPath("$.[0].storageTypeStr", is(lu.getStorageTypeStr())))
                        .andExpect(jsonPath("$.[0].localUri", is(lu.getLocalUri())));;
            }

        }
    }

    @Test
    public void whenCanonicalURILanguagesFromLocalURI_thenNoError() throws Exception {
        for (LocalURI lu : localURIS) {
            CanonicalURILanguage cul = lu.getCanonicalURILanguage();
            if (cul.getIsProperty()) {
                this.mvc.perform(get("/uri-factory/local")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("localURI", lu.getLocalUri())
                )
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.[0].fullURI", is(cul.getFullURI())));
            }

        }
    }


    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
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