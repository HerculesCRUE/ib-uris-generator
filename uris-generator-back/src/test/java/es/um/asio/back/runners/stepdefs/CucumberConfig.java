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
import es.um.asio.service.service.SchemaService;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.ArgumentMatchers.anyString;

@CucumberContextConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = { CucumberConfig.CucumberTestConfiguration.class })
public class CucumberConfig {


    /**
     * MVC test support
     */
    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;


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

        // Storage Types
        StorageType st1 = new StorageType("trellis");
        StorageType st2 = new StorageType("wikibase");
        storageTypes.add(st1);
        storageTypes.add(st2);

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

    }


    @When("^the client calls endpoint with JSON body$")
    public void the_client_issues_GET_version(String body) throws Throwable {

    }

    @Then("^the client receives status code of (\\d+)$")
    public void the_client_receives_status_code_of(int statusCode) throws Throwable {

    }

    @And("^the client receives server version (.+)$")
    public void the_client_receives_server_version_body(String version) throws Throwable {

    }

}
