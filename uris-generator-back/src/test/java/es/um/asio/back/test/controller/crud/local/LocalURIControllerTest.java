package es.um.asio.back.test.controller.crud.local;

import com.fasterxml.jackson.databind.ObjectMapper;
import es.um.asio.back.controller.crud.language.LanguageController;
import es.um.asio.back.controller.crud.local.LocalURIController;
import es.um.asio.back.controller.crud.type.TypeController;
import es.um.asio.service.model.*;
import es.um.asio.service.proxy.*;
import es.um.asio.service.util.Utils;
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
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(LocalURI.class)
@ActiveProfiles("dev")
public class LocalURIControllerTest {

    /**
     * MVC test support
     */
    @Autowired
    private MockMvc mvc;


    /**
     * LocalURIProxy proxy
     */
    @MockBean
    private LocalURIProxy localURIProxy;

    /**
     * CanonicalURILanguageProxy proxy
     */
    @MockBean
    private CanonicalURILanguageProxy canonicalURILanguageProxy;

    /**
     * CanonicalURIProxy proxy
     */
    @MockBean
    private CanonicalURIProxy canonicalURIProxy;

    /**
     * CanonicalURIProxy proxy
     */
    @MockBean
    private StorageTypeProxy storageTypeProxy;

    /**
     * CanonicalURIProxy proxy
     */
    @MockBean
    private TypeProxy typeProxy;

    /**
     * CanonicalURIProxy proxy
     */
    @MockBean
    private LanguageProxy languageProxy;

    /**
     * CanonicalURIProxy proxy
     */
    @MockBean
    private LanguageTypeProxy ltProxy;


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
    static class localUriProxyTestConfiguration {
        @Bean
        public LocalURIController typeController() {
            return new LocalURIController();
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
        Map<String,CanonicalURI> canonicalURIMap = new HashMap<>();
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


        // Storage Type Mocks
        Mockito.when(this.storageTypeProxy.findByName(anyString())).thenAnswer(inv -> {
            for (StorageType st : storageTypes) {
                if (st.getName().equals(inv.getArgument(0)))
                    return st;
            }
            return null;
        });



        // CanonicalURILanguage Mock
        Mockito.when(this.canonicalURILanguageProxy.getAllByFullURI(anyString())).thenAnswer(inv -> {
            for (CanonicalURILanguage cul : canonicalURILanguages) {
                if (cul.getFullURI().equals(inv.getArgument(0)))
                    return cul;
            }
            return null;
        });

        // CanonicalURI Mock
        Mockito.when(this.canonicalURIProxy.getAllByEntityNameAndPropertyName(any(),any())).thenAnswer(inv -> {
            List<CanonicalURI> canonicalURIS = new ArrayList<>();
            for (CanonicalURILanguage cul : canonicalURILanguages) {
                CanonicalURI cu = cul.getCanonicalURI();
                if (cu.getIsEntity() && cu.getEntityName().equals(inv.getArgument(0)))
                    canonicalURIS.add(cu);
            }
            return canonicalURIS;
        });

        Mockito.when(this.canonicalURIProxy.getAllByEntityNameAndReference(anyString(),anyString())).thenAnswer(inv -> {
            List<CanonicalURI> canonicalURIS = new ArrayList<>();
            for (CanonicalURILanguage cul : canonicalURILanguages) {
                CanonicalURI cu = cul.getCanonicalURI();
                if (cu.getIsInstance() && cu.getEntityName().equals(inv.getArgument(0)) && cu.getReference().equals(inv.getArgument(1)) )
                    canonicalURIS.add(cu);
            }
            return canonicalURIS;
        });

        Mockito.when(this.canonicalURIProxy.getAllByPropertyFromProperties(anyString())).thenAnswer(inv -> {
            Set<CanonicalURI> canonicalURIS = new HashSet<>();
            for (CanonicalURILanguage cul : canonicalURILanguages) {
                CanonicalURI cu = cul.getCanonicalURI();
                if (cu.getIsProperty() && cu.getPropertyName().equals(inv.getArgument(0)) )
                    canonicalURIS.add(cu);
            }
            return Arrays.asList(canonicalURIS.toArray());
        });

        // Local URIs Mock
        Mockito.when(this.localURIProxy.save((LocalURI) any())).thenAnswer(inv -> {
            return inv.getArgument(0);
        });

        Mockito.when(this.localURIProxy.findAll()).thenAnswer(inv -> {
            return localURIS;
        });

        Mockito.when(this.localURIProxy.getAllByLocalURIStr(anyString())).thenAnswer(inv -> {
            List<LocalURI> response = new ArrayList<>();
            for (LocalURI lu : localURIS) {
                if (lu.getLocalUri().equals(inv.getArgument(0)))
                    response.add(lu);
            }
            return response;
        });

        Mockito.when(this.localURIProxy.getAllByCanonicalURILanguageStrAndStorageTypeStr(anyString(),anyString())).thenAnswer(inv -> {
            List<LocalURI> response = new ArrayList<>();
            for (LocalURI lu : localURIS) {
                if (lu.getCanonicalURILanguageStr().equals(inv.getArgument(0)) && lu.getStorageTypeStr().equals(inv.getArgument(1)))
                    response.add(lu);
            }
            return response;
        });


    }

    @Test
    public void whenNothing_thenNoError() throws Exception {

    }

    @Test
    public void whenCreateLocalURI_thenNoError() throws Exception {

        for (LocalURI lu : localURIS) {
            System.out.println("LU:" +lu.toString());
            this.mvc.perform(post("/local-uri")
                    .accept(MediaType.APPLICATION_JSON)
                    .param("canonicalURILanguage",lu.getCanonicalURILanguageStr())
                    .param("localURI",lu.getLocalUri())
                    .param("storageType",lu.getStorageTypeStr())
            )
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.localUri", is(lu.getLocalUri())))
                    .andExpect(jsonPath("$.storageTypeStr", is(lu.getStorageTypeStr())))
                    .andExpect(jsonPath("$.canonicalURILanguageStr", is(lu.getCanonicalURILanguageStr())));

        }

    }


    @Test
    public void whenDeleteLocalURIByLocalURI_thenNoError() throws Exception {
        for (LocalURI lu : localURIS) {
            this.mvc.perform(delete("/local-uri/uri")
                    .accept(MediaType.APPLICATION_JSON)
                    .param("localURI",lu.getLocalUri())
            )
                    .andDo(print())
                    .andExpect(status().isOk());

        }
    }

    @Test
    public void whenGetAllLocalURIs_thenNoError() throws Exception {
        for (LocalURI lu : localURIS) {
            this.mvc.perform(get("/local-uri/all")
                    .accept(MediaType.APPLICATION_JSON)
            )
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(localURIS.size())));
        }

    }

    @Test
    public void whenCreateLocalURIByCanonicalParameters_thenNoError() throws Exception {
        for (LocalURI lu : localURIS) {
            CanonicalURI cu = lu.getCanonicalURILanguage().getCanonicalURI();
            this.mvc.perform(post("/local-uri/canonical")
                    .accept(MediaType.APPLICATION_JSON)
                    .param("canonicalEntity",(Utils.isValidString(cu.getEntityName()) && !cu.getIsProperty())?cu.getEntityName():"")
                    .param("typeCode",(Utils.isValidString(cu.getTypeIdCode()))?cu.getTypeIdCode():"")
                    .param("canonicalProperty",(Utils.isValidString(cu.getPropertyName()) && cu.getIsProperty())?cu.getPropertyName():"")
                    .param("canonicalReference",(Utils.isValidString(cu.getReference()) && cu.getIsInstance())?cu.getReference():"")
                    .param("language",lu.getCanonicalURILanguage().getLanguageID())
                    .param("storageType",lu.getStorageTypeStr())
                    .param("localURI",lu.getLocalUri())
            )
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.canonicalURILanguageStr", is(lu.getCanonicalURILanguageStr())))
                    .andExpect(jsonPath("$.storageTypeStr", is(lu.getStorageTypeStr())))
                    .andExpect(jsonPath("$.localUri", is(lu.getLocalUri())));
        }
    }


    @Test
    public void whenGetAllByCanonicalLanguageURI_thenNoError() throws Exception {
        for (LocalURI lu : localURIS) {
            this.mvc.perform(get("/local-uri/uri/canonical")
                    .param("canonicalLanguageURI",lu.getCanonicalURILanguageStr())
                    .param("storageType",lu.getStorageTypeStr())
                    .accept(MediaType.APPLICATION_JSON)
            )
                    .andDo(print())
                    .andExpect(status().isOk());
        }

    }


    @Test
    public void whenGetAllByLocalURI_thenNoError() throws Exception {
        for (LocalURI lu : localURIS) {
            this.mvc.perform(get("/local-uri/uri/local")
                    .param("uriLocal",lu.getLocalUri())
                    .accept(MediaType.APPLICATION_JSON)
            )
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.[0].canonicalURILanguageStr", is(lu.getCanonicalURILanguageStr())))
                    .andExpect(jsonPath("$.[0].storageTypeStr", is(lu.getStorageTypeStr())))
                    .andExpect(jsonPath("$.[0].localUri", is(lu.getLocalUri())));
        }

    }

}