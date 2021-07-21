package es.um.asio.back.runners.stepdefs;


import com.fasterxml.jackson.databind.ObjectMapper;
import es.um.asio.back.controller.URISController;
import es.um.asio.back.controller.crud.canonical_language.CanonicalURILanguageController;
import es.um.asio.back.controller.crud.local.LocalURIController;
import es.um.asio.back.controller.crud.storage_type.StorageTypeController;
import es.um.asio.service.model.*;
import es.um.asio.service.proxy.*;
import es.um.asio.service.service.CanonicalURILanguageService;
import es.um.asio.service.service.CanonicalURIService;
import es.um.asio.service.service.DiscoveryService;
import es.um.asio.service.service.SchemaService;
import es.um.asio.service.util.Utils;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.messages.internal.com.google.gson.Gson;
import io.cucumber.spring.CucumberContextConfiguration;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.web.server.LocalManagementPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.Assert;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;


@CucumberContextConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = { URIControllerTest.CucumberTestConfiguration.class })
public class URIControllerTest extends AbstractStepDefinitionConsumerTest {


    @Autowired
    Environment environment;

    @LocalServerPort
    int randomServerPort;

    @LocalManagementPort
    int randomManagementPort;

    String port;

    static MvcResult result;
    static String reference;
    static Map<String,String> queryParams = new HashMap();
    static Map<String,String> pathParams = new HashMap();
    static Map<String,Object> bodyRequest = new HashMap();

    /**
     * MVC test support
     */
    @Autowired
    private WebApplicationContext webApplicationContext;
    protected MockMvc mockMvc;


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
    @Autowired
    @MockBean
    private CanonicalURILanguageService canonicalURILanguageService;

    /**
     * CanonicalURILanguageService controller
     */
    @Autowired
    @MockBean
    private CanonicalURIService canonicalURIService;


    /**
     * LocalURIProxy Proxy
     */
    @Autowired
    @MockBean
    private CanonicalURILanguageProxy canonicalURILanguageProxy;

    /**
     * LocalURIProxy Proxy
     */
    @Autowired
    @MockBean
    private CanonicalURIProxy canonicalURIProxy;

    /**
     * LocalURIProxy Proxy
     */
    @Autowired
    @MockBean
    private LocalURIProxy localURIProxy;

    /**
     * LanguageProxy proxy
     */
    @Autowired
    @MockBean
    private LanguageProxy languageProxy;

    /**
     * LanguageTypeProxy proxy
     */
    @Autowired
    @MockBean
    private LanguageTypeProxy languageTypeProxy;

    /**
     * TypeProxy proxy
     */
    @Autowired
    @MockBean
    private TypeProxy typeProxy;

    /**
     * TypeProxy proxy
     */
    @Autowired
    @MockBean
    private StorageTypeProxy storageTypeProxy;

    /**
     * TypeProxy proxy
     */
    @Autowired
    @MockBean
    private SchemaService schemaService;

    /**
     * TypeProxy proxy
     */
    @Autowired
    @MockBean
    private DiscoveryService discoveryService;

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
    static class CucumberTestConfiguration {
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
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
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
            CanonicalURILanguage instance = new CanonicalURILanguage("hercules.org", "um", lt, String.format("entity-%d",counterLT+1), Utils.getUUIDFromString(String.format("instance-%d",counterLT)), null, canonicalLanguageSchema );

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

        /*
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

         */

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

        // Mock service CanonicalURILanguageService AQUI ESTA
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

        // Mock Discovery Service
        Mockito.when(this.discoveryService.findSimilarEntity(anyString(),anyString(),anyString(),anyString(),any())).thenAnswer(invocation -> {
            return null;
        });

    }


/**
     * First step is to retrieve the base uri
     * @param uri base uri*/


    @Given("^baseUri is (.*)$")
    public void baseUri(String uri) {
        Assert.notNull(uri);
        Assert.isTrue(!uri.isEmpty());
        baseUri = uri+":"+port;
    }
     /** Feature: GetEntityCanonicalURI*/



     /** Scenario: The client invokes the end point to get the canonical URI*/


    @When("When post entity the client calls endpoint {string} with domain {string} ,subDomain {string}, language {string} with JSON body")
    public void the_client_post_entity(String endPoint,String domain,String subDomain, String lang,String body) throws Throwable {
        initRequestParams();
        queryParams.put("domain",domain);
        queryParams.put("subDomain",subDomain);
        queryParams.put("lang",lang);
        bodyRequest = new Gson().fromJson(body,Map.class);
        assertTrue(bodyRequest.get("@class")!=null);
        result = mockMvc.perform(post(endPoint)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .param("domain", domain)
                .param("subDomain", subDomain)
                .param("lang", lang)
                .content(body)
        )
                .andReturn();

    }

    @Then("^after post entity the client receives status code of (\\d+)$")
    public void the_client_post_entity_receives_status_code_of(int statusCode) throws Throwable {
        assertEquals(result.getResponse().getStatus(), statusCode);
    }

    @And("^after post entity the client receives server valid response URI from Post Entity Request$")
    public void the_client_post_entity_receives_server_version_body() throws Throwable {
        Map<Object,Object> res = new Gson().fromJson(result.getResponse().getContentAsString(),Map.class);
        String schema = schemaService.getCanonicalLanguageSchema();
        String clURI = generateCanonicalLanguageURIFromSchema(queryParams.get("domain"), queryParams.get("subDomain"), queryParams.get("lang"), "res", String.valueOf(bodyRequest.get("@class")),null,null,schemaService.getCanonicalLanguageSchema());
        String cURI = generateCanonicalURIFromSchema(queryParams.get("domain"), queryParams.get("subDomain"),  "res", String.valueOf(bodyRequest.get("canonicalClassName")!=null?bodyRequest.get("canonicalClassName"):bodyRequest.get("@class")),null,null,schemaService.getCanonicalSchema());
        // assertEquals(clURI,res.get("canonicalLanguageURI"));
        // assertEquals(cURI,res.get("canonicalURI"));
        // assertEquals(queryParams.get("lang"),res.get("language"));
    }

/*     * Feature: GetEntityCanonicalURI


     * Scenario: The client invokes the end point to get the canonical URI*/


    /*
    @When("When post property the client calls endpoint {string} with domain {string} ,subDomain {string}, language {string} with JSON body")
    public void the_client_post_property_entity(String endPoint,String domain,String subDomain, String lang,String body) throws Throwable {
        initRequestParams();
        queryParams.put("domain",domain);
        queryParams.put("subDomain",subDomain);
        queryParams.put("lang",lang);
        bodyRequest = new Gson().fromJson(body,Map.class);
        assertTrue(bodyRequest.get("property")!=null);
        result = mockMvc.perform(post(endPoint)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .param("domain", domain)
                .param("subDomain", subDomain)
                .param("lang", lang)
                .content(body)
        )
                .andReturn();

    }

    @Then("^after post property the client receives status code of (\\d+)$")
    public void the_client_post_property_receives_status_code_of(int statusCode) throws Throwable {
        assertEquals(result.getResponse().getStatus(), statusCode);
    }

    @And("^after post property the client receives server valid response URI from Post Property Request$")
    public void the_client_post_property_receives_server_version_body() throws Throwable {
        Map<Object,Object> res = new Gson().fromJson(result.getResponse().getContentAsString(),Map.class);
        String schema = schemaService.getCanonicalLanguageSchema();
        String clURI = generateCanonicalLanguageURIFromSchema(queryParams.get("domain"), queryParams.get("subDomain"), queryParams.get("lang"), "res", null,null,String.valueOf(bodyRequest.get("property")),schemaService.getCanonicalLanguageSchema());
        String cURI = generateCanonicalURIFromSchema(queryParams.get("domain"), queryParams.get("subDomain"),  "res", null,null,String.valueOf(bodyRequest.get("canonicalProperty")!=null?bodyRequest.get("canonicalProperty"):bodyRequest.get("property")),schemaService.getCanonicalSchema());
        assertEquals(clURI,res.get("canonicalLanguageURI"));
        assertEquals(cURI,res.get("canonicalURI"));
        assertEquals(queryParams.get("lang"),res.get("language"));
    }

     */

/*     * Feature: GetCanonicalLanguageFromCanonicalURI


     * Scenario: The client invokes the end point to get the canonical URI*/


    @When("When post instance the client calls endpoint {string} with domain {string} ,subDomain {string}, language {string} with JSON body")
    public void the_client_post_resource(String endPoint,String domain,String subDomain, String lang,String body) throws Throwable {
        initRequestParams();
        queryParams.put("domain",domain);
        queryParams.put("subDomain",subDomain);
        queryParams.put("lang",lang);
        bodyRequest = new Gson().fromJson(body,Map.class);
        assertTrue(bodyRequest.get("@class")!=null);
        result = mockMvc.perform(post(endPoint)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .param("domain", domain)
                .param("subDomain", subDomain)
                .param("lang", lang)
                .content(body)
        )
                .andReturn();

    }

    @Then("^after post instance the client receives status code of (\\d+)$")
    public void the_client_post_resource_receives_status_code_of(int statusCode) throws Throwable {
        assertEquals(result.getResponse().getStatus(), statusCode);
    }

    @And("^after post instance the client receives server valid response URI from Post Property Request$")
    public void the_client_post_resource_receives_server_version_body() throws Throwable {
        Map<Object,Object> res = new Gson().fromJson(result.getResponse().getContentAsString(),Map.class);
        String [] uriParts = String.valueOf(res.get("canonicalURI")).split("/");
        String ref = uriParts[uriParts.length-1];
        String clURI = generateCanonicalLanguageURIFromSchema(queryParams.get("domain"), queryParams.get("subDomain"), queryParams.get("lang"), "res", String.valueOf(bodyRequest.get("@class")),ref,null,schemaService.getCanonicalLanguageSchema());
        String cURI = generateCanonicalURIFromSchema(queryParams.get("domain"), queryParams.get("subDomain"),  "res", String.valueOf(bodyRequest.get("canonicalClassName")!=null?bodyRequest.get("canonicalClassName"):bodyRequest.get("@class")),ref,null,schemaService.getCanonicalSchema());
        assertEquals(clURI,res.get("canonicalLanguageURI"));
        assertEquals(cURI,res.get("canonicalURI"));
        assertEquals(queryParams.get("lang"),res.get("language"));
    }

/*     * Feature: GetEntityCanonicalURI


     * Scenario: The client invokes the end point to get the canonical URI*/


    @When("When the client calls endpoint GET {string} with canonicalURI {string} and language {string}")
    public void the_client_get_canonical_uri_language_from_canonical_uri(String endPoint, String canonicalUri,String language) throws Throwable {
        initRequestParams();
        queryParams.put("canonicalURI",canonicalUri);
        queryParams.put("language",language);
        assertTrue(canonicalUri != null && language != null);
        result = mockMvc.perform(get(endPoint)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .param("canonicalURI", canonicalUri)
                .param("language", language)
        )
                .andReturn();

    }

    @Then("after get request GET \\\\/uri-factory\\\\/canonical\\\\/languages the client receives status code of {int}")
    public void the_client_get_canonical_uri_language_from_canonical_uri_status_code_of(int statusCode) throws Throwable {
        assertEquals(result.getResponse().getStatus(), statusCode);
    }

    @And("after get request GET \\\\/uri-factory\\\\/canonical\\\\/languages the client receives a valid response URI from Post Property Request")
    public void the_client_get_canonical_uri_language_from_canonical_uri_body() throws Throwable {
        Map<Object,Object> res = new Gson().fromJson(result.getResponse().getContentAsString(),Map.class);
        ArrayList<Map<Object,Object>> values = (ArrayList<Map<Object,Object>>) res.get(queryParams.get("canonicalURI"));
        assertEquals(values.size(),1);
        Map<Object,Object> val = values.get(0);
        assertEquals(val.get("languageIso"),queryParams.get("language"));
        Set<String> cul = new HashSet<>(Arrays.asList(val.get("canonicalURILanguage").toString().split("/")));
        for (String s :queryParams.get("canonicalURI").split("/")) {
            cul.remove(s);
        }
        assertTrue(cul.size()==2);
        assertTrue(cul.contains(queryParams.get("language")));
        assertTrue(cul.contains("rec") || cul.contains("res"));
    }

/*     * Feature: LocalURI


     * Scenario: POST Local URI. The client invokes POST to endpoint /uri-factory/local to get response of API*/


    @When("POST Local URI to endpoint {string} to link Canonical URI Language {string} to localUri {string} in storageName {string}")
    public void the_client_post_local_uri_from_canonical_uri(String endPoint, String canonicalUriLanguage,String localURI,String storageName) throws Throwable {
        initRequestParams();
        queryParams.put("canonicalLanguageURI",canonicalUriLanguage);
        queryParams.put("localURI",localURI);
        queryParams.put("storageName",storageName);
        assertTrue(canonicalUriLanguage != null && localURI != null && storageName != null);
        result = mockMvc.perform(post(endPoint)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .param("canonicalLanguageURI", canonicalUriLanguage)
                .param("localURI", localURI)
                .param("storageName", storageName)
        )
                .andReturn();

    }

    @Then("after POST Local URI to endpoint \\/uri-factory\\/local the client receives health status code of {int}")
    public void the_client_post_local_uri_from_canonical_uri_status_code_ok(int statusCode) throws Throwable {
        assertEquals(result.getResponse().getStatus(), statusCode);
    }

    @And("after POST Local URI to endpoint \\/uri-factory\\/local  the client receives server health valid response")
    public void the_client_post_local_uri_from_canonical_uri_check_body() throws Throwable {
        List<Map<Object,Object>> responses = new Gson().fromJson(result.getResponse().getContentAsString(),List.class);
        assertTrue(responses.size() == 1);
        assertTrue(responses.get(0).get("canonicalURILanguageStr").equals(queryParams.get("canonicalLanguageURI")));
        assertTrue(responses.get(0).get("storageTypeStr").equals(queryParams.get("storageName")));
        assertTrue(responses.get(0).get("localUri").equals(queryParams.get("localURI")));
    }
     /** Scenario: GET Local URI. The client invokes GET to endpoint /uri-factory/local to get response of API*/


    @When("GET Local URI to endpoint {string} with localUri {string}")
    public void the_client_get_local_uri_from_canonical_uri(String endPoint, String localURI) throws Throwable {
        initRequestParams();
        queryParams.put("localURI",localURI);
        assertTrue(localURI != null);
        result = mockMvc.perform(get(endPoint)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .param("localURI", localURI)
        )
                .andReturn();

    }

    @Then("after GET Local URI to endpoint \\/uri-factory\\/local the client receives health status code of {int}")
    public void the_client_get_local_uri_from_canonical_uri_status_code_ok(int statusCode) throws Throwable {
        assertEquals(result.getResponse().getStatus(), statusCode);
    }

    @And("after GET Local URI to endpoint \\/uri-factory\\/local  the client receives server health valid response")
    public void the_client_get_local_uri_from_canonical_uri_check_body() throws Throwable {
        List<Map<Object,Object>> responses = new Gson().fromJson(result.getResponse().getContentAsString(),List.class);
        assertTrue(responses.size() == 1);
    }
     /** Scenario: GET Local URI. The client invokes GET to endpoint /uri-factory/local to get response of API*/


    @When("DELETE Local URI to endpoint {string} to unlink Canonical URI Language {string} to localUri {string} in storageName {string}")
    public void the_client_delete_local_uri_from_canonical_uri(String endPoint, String canonicalLanguageURI,String localURI, String storageName) throws Throwable {
        initRequestParams();
        queryParams.put("canonicalLanguageURI",canonicalLanguageURI);
        queryParams.put("localURI",localURI);
        queryParams.put("storageName",storageName);
        assertTrue(localURI != null);
        result = mockMvc.perform(delete(endPoint)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .param("canonicalLanguageURI", canonicalLanguageURI)
                .param("localURI", localURI)
                .param("storageName", storageName)
        )
                .andReturn();

    }

    @Then("after DELETE Local URI to endpoint \\/uri-factory\\/local the client receives health status code of {int}")
    public void the_client_delete_local_uri_from_canonical_uri_status_code_ok(int statusCode) throws Throwable {
        assertEquals(result.getResponse().getStatus(), statusCode);
    }

     /** GET Local URI from Canonical URI. The client invokes GET to endpoint /uri-factory/local/canonical to get response of API*/


    @When("GET Local URI from Canonical URI to endpoint {string} with canonicalUri {string} and languageCode {string} and storageName {string}")
    public void the_client_get_local_uri_from_canonical_uri2(String endPoint, String canonicalURI, String languageCode, String storageName) throws Throwable {
        initRequestParams();
        queryParams.put("canonicalUri",canonicalURI);
        queryParams.put("languageCode",languageCode);
        queryParams.put("storageName",storageName);
        assertTrue(canonicalURI != null && languageCode != null && storageName != null);
        result = mockMvc.perform(get(endPoint)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .param("canonicalUri", canonicalURI)
                .param("languageCode", languageCode)
                .param("storageName", storageName)
        )
                .andReturn();

    }

    @Then("after GET Local URI from Canonical URI to endpoint \\/uri-factory\\/local\\/canonical the client receives health status code of {int}")
    public void the_client_get_local_uri_from_canonical_uri_status_code_ok2(int statusCode) throws Throwable {
        assertEquals(result.getResponse().getStatus(), statusCode);
    }

    @And("after GET Local URI from Canonical URI to endpoint \\/uri-factory\\/local\\/canonical the client receives server health valid response")
    public void the_client_get_local_uri_from_canonical_uri_check_body2() throws Throwable {
        List<Map<Object,Object>> responses = new Gson().fromJson(result.getResponse().getContentAsString(),List.class);
        assertTrue(responses.size() == 1);
        assertTrue(responses.get(0).get("storageTypeStr").equals(queryParams.get("storageName")));
    }

     /** GET Local URI from Canonical Language URI. The client invokes GET to endpoint /uri-factory/local/canonical/language to get response of API*/


    @When("GET Local URI from Canonical Language URI to endpoint {string} with canonicalLanguageUri {string} and languageCode {string} and storageName {string}")
    public void the_client_get_local_uri_from_canonical_language_uri(String endPoint, String canonicalURI, String languageCode, String storageName) throws Throwable {
        initRequestParams();
        queryParams.put("canonicalLanguageUri",canonicalURI);
        queryParams.put("languageCode",languageCode);
        queryParams.put("storageName",storageName);
        assertTrue(canonicalURI != null && languageCode != null && storageName != null);
        result = mockMvc.perform(get(endPoint)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .param("canonicalLanguageUri", canonicalURI)
                .param("languageCode", languageCode)
                .param("storageName", storageName)
        )
                .andReturn();

    }

    @Then("after GET Local URI from Canonical Language URI to endpoint \\/uri-factory\\/local\\/canonical\\/language the client receives health status code of {int}")
    public void the_client_get_local_uri_from_canonical_language_uri_status_code_ok(int statusCode) throws Throwable {
        assertEquals(result.getResponse().getStatus(), statusCode);
    }

    @And("after GET Local URI from Canonical Language URI to endpoint \\/uri-factory\\/local\\/canonical\\/language the client receives server health valid response")
    public void the_client_get_local_uri_from_canonical_language_uri_check_body() throws Throwable {
        List<Map<Object,Object>> responses = new Gson().fromJson(result.getResponse().getContentAsString(),List.class);
        assertTrue(responses.size() == 1);
        assertTrue(responses.get(0).get("storageTypeStr").equals(queryParams.get("storageName")));
        assertTrue(responses.get(0).get("canonicalURILanguageStr").equals(queryParams.get("canonicalLanguageUri")));
    }

     /** POST Local URI Entity. The client invokes GET to endpoint /uri-factory/local/entity to get response of API*/


    @When("POST Local URI Entity to endpoint {string} with domain {string} and subDomain {string} and languageCode {string} and typeCode {string} and entity {string} and localUri {string} and storageName {string}")
    public void the_client_post_local_uri_entity(String endPoint, String domain, String subDomain, String languageCode, String typeCode, String entity, String localUri, String storageName) throws Throwable {
        initRequestParams();
        queryParams.put("domain",domain);
        queryParams.put("subDomain",subDomain);
        queryParams.put("languageCode",languageCode);
        queryParams.put("typeCode",typeCode);
        queryParams.put("entity",entity);
        queryParams.put("localUri",localUri);
        queryParams.put("storageName",storageName);
        assertTrue(domain != null && subDomain != null && languageCode != null && typeCode != null && entity != null && localUri != null && storageName != null);
        result = mockMvc.perform(post(endPoint)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .param("domain", domain)
                .param("subDomain", subDomain)
                .param("languageCode", languageCode)
                .param("typeCode", typeCode)
                .param("entity", entity)
                .param("localURI", localUri)
                .param("storageName", storageName)
        )
                .andReturn();

    }

    @Then("after POST Local URI Entity to endpoint \\/uri-factory\\/local\\/entity the client receives health status code of {int}")
    public void the_client_post_local_uri_entity_status_code_ok(int statusCode) throws Throwable {
        assertEquals(result.getResponse().getStatus(), statusCode);
    }

    @And("after POST Local URI Entity to endpoint \\/uri-factory\\/local\\/entity  the client receives server health valid response")
    public void the_client_post_local_uri_entity_check_body() throws Throwable {
        Map<Object,Object> response = new Gson().fromJson(result.getResponse().getContentAsString(),Map.class);
        assertTrue(response.get("storageTypeStr").equals(queryParams.get("storageName")));
        assertTrue(response.get("localUri").equals(queryParams.get("localUri")));
    }

     /** DELETE Local URI Entity. The client invokes DELETE to endpoint /uri-factory/local/entity to get response of API*/


    @When("DELETE Local URI Entity to endpoint {string} with domain {string} and subDomain {string} and languageCode {string} and typeCode {string} and entity {string} and localUri {string} and storageName {string}")
    public void the_client_delete_local_uri_entity(String endPoint, String domain, String subDomain, String languageCode, String typeCode, String entity, String localUri, String storageName) throws Throwable {
        initRequestParams();
        queryParams.put("domain",domain);
        queryParams.put("subDomain",subDomain);
        queryParams.put("languageCode",languageCode);
        queryParams.put("typeCode",typeCode);
        queryParams.put("entity",entity);
        queryParams.put("localUri",localUri);
        queryParams.put("storageName",storageName);
        assertTrue(domain != null && subDomain != null && languageCode != null && typeCode != null && entity != null && localUri != null && storageName != null);
        result = mockMvc.perform(delete(endPoint)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .param("domain", domain)
                .param("subDomain", subDomain)
                .param("languageCode", languageCode)
                .param("typeCode", typeCode)
                .param("entity", entity)
                .param("localURI", localUri)
                .param("storageName", storageName)
        )
                .andReturn();

    }

    @Then("after DELETE Local URI Entity to endpoint \\/uri-factory\\/local\\/entity the client receives health status code of {int}")
    public void the_client_delete_local_uri_entity_status_code_ok(int statusCode) throws Throwable {
        assertEquals(result.getResponse().getStatus(), statusCode);
    }






     /** POST Local URI Property. The client invokes GET to endpoint /uri-factory/local/entity to get response of API*/


    @When("POST Local URI Property to endpoint {string} with domain {string} and subDomain {string} and languageCode {string} and typeCode {string} and property {string} and localUri {string} and storageName {string}")
    public void the_client_post_local_uri_property(String endPoint, String domain, String subDomain, String languageCode, String typeCode, String property, String localUri, String storageName) throws Throwable {
        initRequestParams();
        queryParams.put("domain",domain);
        queryParams.put("subDomain",subDomain);
        queryParams.put("languageCode",languageCode);
        queryParams.put("typeCode",typeCode);
        queryParams.put("property",property);
        queryParams.put("localUri",localUri);
        queryParams.put("storageName",storageName);
        assertTrue(domain != null && subDomain != null && languageCode != null && typeCode != null && property != null && localUri != null && storageName != null);
        result = mockMvc.perform(post(endPoint)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .param("domain", domain)
                .param("subDomain", subDomain)
                .param("languageCode", languageCode)
                .param("typeCode", typeCode)
                .param("property", property)
                .param("localURI", localUri)
                .param("storageName", storageName)
        )
                .andReturn();

    }

    @Then("after POST Local URI Property to endpoint \\/uri-factory\\/local\\/property the client receives health status code of {int}")
    public void the_client_post_local_uri_property_status_code_ok(int statusCode) throws Throwable {
        assertEquals(result.getResponse().getStatus(), statusCode);
    }

    @And("after POST Local URI Property to endpoint \\/uri-factory\\/local\\/property  the client receives server health valid response")
    public void the_client_post_local_uri_property_check_body() throws Throwable {
        Map<Object,Object> response = new Gson().fromJson(result.getResponse().getContentAsString(),Map.class);
        assertTrue(response.get("storageTypeStr").equals(queryParams.get("storageName")));
        assertTrue(response.get("localUri").equals(queryParams.get("localUri")));
    }

     /** DELETE Local URI Property. The client invokes DELETE to endpoint /uri-factory/local/entity to get response of API*/


    @When("DELETE Local URI Property to endpoint {string} with domain {string} and subDomain {string} and languageCode {string} and typeCode {string} and entity {string} and localUri {string} and storageName {string}")
    public void the_client_delete_local_uri_property(String endPoint, String domain, String subDomain, String languageCode, String typeCode, String property, String localUri, String storageName) throws Throwable {
        initRequestParams();
        queryParams.put("domain",domain);
        queryParams.put("subDomain",subDomain);
        queryParams.put("languageCode",languageCode);
        queryParams.put("typeCode",typeCode);
        queryParams.put("property",property);
        queryParams.put("localUri",localUri);
        queryParams.put("storageName",storageName);
        assertTrue(domain != null && subDomain != null && languageCode != null && typeCode != null && property != null && localUri != null && storageName != null);
        result = mockMvc.perform(delete(endPoint)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .param("domain", domain)
                .param("subDomain", subDomain)
                .param("languageCode", languageCode)
                .param("typeCode", typeCode)
                .param("property", property)
                .param("localURI", localUri)
                .param("storageName", storageName)
        )
                .andReturn();

    }

    @Then("after DELETE Local URI Property to endpoint \\/uri-factory\\/local\\/property the client receives health status code of {int}")
    public void the_client_delete_local_uri_property_status_code_ok(int statusCode) throws Throwable {
        assertEquals(result.getResponse().getStatus(), statusCode);
    }




     /** POST Local URI Instance. The client invokes POST to endpoint /uri-factory/local/resource to get response of API*/


    @When("POST Local URI Instance to endpoint {string} with domain {string} and subDomain {string} and languageCode {string} and typeCode {string} and entity {string} and reference {string} and localUri {string} and storageName {string}")
    public void the_client_post_local_uri_instance(String endPoint, String domain, String subDomain, String languageCode, String typeCode, String entity, String reference, String localUri, String storageName) throws Throwable {
        initRequestParams();
        queryParams.put("domain",domain);
        queryParams.put("subDomain",subDomain);
        queryParams.put("languageCode",languageCode);
        queryParams.put("typeCode",typeCode);
        queryParams.put("entity",entity);
        queryParams.put("reference",reference);
        queryParams.put("localUri",localUri);
        queryParams.put("storageName",storageName);
        assertTrue(domain != null && subDomain != null && languageCode != null && typeCode != null && entity != null && reference != null && localUri != null && storageName != null);
        result = mockMvc.perform(post(endPoint)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .param("domain", domain)
                .param("subDomain", subDomain)
                .param("languageCode", languageCode)
                .param("typeCode", typeCode)
                .param("entity", entity)
                .param("reference", reference)
                .param("localURI", localUri)
                .param("storageName", storageName)
        )
                .andReturn();

    }

    @Then("after POST Local URI Instance to endpoint \\/uri-factory\\/local\\/resource the client receives health status code of {int}")
    public void the_client_post_local_uri_instance_status_code_ok(int statusCode) throws Throwable {
        MockHttpServletResponse r = result.getResponse();
        assertEquals(result.getResponse().getStatus(), statusCode);
    }

    @And("after POST Local URI Instance to endpoint \\/uri-factory\\/local\\/resource  the client receives server health valid response")
    public void the_client_post_local_uri_instance_check_body() throws Throwable {
        Map<Object,Object> response = new Gson().fromJson(result.getResponse().getContentAsString(),Map.class);
        assertTrue(response.get("storageTypeStr").equals(queryParams.get("storageName")));
        assertTrue(response.get("localUri").equals(queryParams.get("localUri")));
    }

     /** DELETE Local URI Property. The client invokes DELETE to endpoint /uri-factory/local/entity to get response of API*/


    @When("DELETE Local URI Instance to endpoint {string} with domain {string} and subDomain {string} and languageCode {string} and typeCode {string} and entity {string} and reference {string} and localUri {string} and storageName {string}")
    public void the_client_delete_local_uri_instance(String endPoint, String domain, String subDomain, String languageCode, String typeCode, String entity, String reference, String localUri, String storageName) throws Throwable {
        initRequestParams();
        queryParams.put("domain",domain);
        queryParams.put("subDomain",subDomain);
        queryParams.put("languageCode",languageCode);
        queryParams.put("typeCode",typeCode);
        queryParams.put("entity",entity);
        queryParams.put("reference",reference);
        queryParams.put("localUri",localUri);
        queryParams.put("storageName",storageName);
        assertTrue(domain != null && subDomain != null && languageCode != null && typeCode != null && entity != null && reference != null && localUri != null && storageName != null);
        result = mockMvc.perform(delete(endPoint)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .param("domain", domain)
                .param("subDomain", subDomain)
                .param("languageCode", languageCode)
                .param("typeCode", typeCode)
                .param("entity", entity)
                .param("reference", reference)
                .param("localURI", localUri)
                .param("storageName", storageName)
        )
                .andReturn();

    }

    @Then("after DELETE Local URI Instance to endpoint \\/uri-factory\\/local\\/resource the client receives health status code of {int}")
    public void the_client_delete_local_uri_instance_status_code_ok(int statusCode) throws Throwable {
        // assertEquals(result.getResponse().getStatus(), statusCode);
    }















     /** Private functions*/



    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public  String generateCanonicalLanguageURIFromSchema(String domain, String subDomain,String l,String t, String concept, String reference, String property, String schema) {
        List<LanguageType> lts = languageTypeProxy.getByLanguageAndType(l,t);
        CanonicalURILanguage cul = new CanonicalURILanguage(domain, subDomain, lts.get(0), concept, reference, property, schema);
        return cul.getFullURI();
    }

    public  String generateCanonicalURIFromSchema(String domain, String subDomain,String t, String concept, String reference, String property, String schema) {
        Type type = typeProxy.findOrCreate("res");
        CanonicalURI cu = new CanonicalURI(domain, subDomain, type, concept, reference, property, schema);
        return cu.getFullURI();
    }

    public void initRequestParams(){
        queryParams = new HashMap();
        pathParams = new HashMap();
        bodyRequest = new HashMap();
    }


}
