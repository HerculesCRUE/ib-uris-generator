package es.um.asio.back.test.controller.crud.language_type.language;

import com.fasterxml.jackson.databind.ObjectMapper;
import es.um.asio.back.controller.crud.language.LanguageController;
import es.um.asio.back.controller.crud.language_type.LanguageTypeController;
import es.um.asio.service.model.Language;
import es.um.asio.service.model.LanguageType;
import es.um.asio.service.model.Type;
import es.um.asio.service.proxy.LanguageProxy;
import es.um.asio.service.proxy.LanguageTypeProxy;
import es.um.asio.service.proxy.TypeProxy;
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
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(LanguageController.class)
public class LanguageTypeControllerTest {

    /**
     * MVC test support
     */
    @Autowired
    private MockMvc mvc;

    /**
     * Language proxy
     */
    @MockBean
    private LanguageProxy languageProxy;

    /**
     * Language proxy
     */
    @MockBean
    private TypeProxy typeProxy;

    /**
     * Language Type proxy
     */
    @MockBean
    private LanguageTypeProxy languageTypeProxy;


    /**
     * JSON Object mapper
     */
    @Autowired
    private ObjectMapper objectMapper;

    final List<Language> languages = new ArrayList<>();
    final List<Type> types = new ArrayList<>();
    final List<LanguageType> languageTypes = new ArrayList<>();



    @TestConfiguration
    static class languageTypeProxyTestConfiguration {
        @Bean
        public LanguageTypeController languageTypeController() {
            return new LanguageTypeController();
        }
    }

    @Before
    public void beforeTest() {


        // Mock data
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

        LanguageType lt1 = new LanguageType(l2,t1,"def","definiciones");
        LanguageType lt2 = new LanguageType(l2,t2,"kos","skos");
        LanguageType lt3 = new LanguageType(l2,t3,"rec","recurso");
        LanguageType lt4 = new LanguageType(l2,t4,"cat","catalogo");
        LanguageType lt5 = new LanguageType(l1,t1,"def","definitions");
        LanguageType lt6 = new LanguageType(l1,t2,"kos","skos");
        LanguageType lt7 = new LanguageType(l1,t3,"res","resource");
        LanguageType lt8 = new LanguageType(l1,t4,"cat","catalog");
        languageTypes.add(lt1);
        languageTypes.add(lt2);
        languageTypes.add(lt3);
        languageTypes.add(lt4);
        languageTypes.add(lt5);
        languageTypes.add(lt6);
        languageTypes.add(lt7);
        languageTypes.add(lt8);


        // Mock findElement, Property, Reference
        Mockito.when(this.languageTypeProxy.getByLanguageAndType(anyString(),anyString())).thenAnswer(invocation -> {

            List<LanguageType> response = new ArrayList<>();
            for (LanguageType lt: languageTypes) {
                if (lt.getLanguage().getIso().equals(invocation.getArgument(0)) && lt.getType().getCode().equals(invocation.getArgument(1)) ) {
                    response.add(lt);
                }
            }
            return response;
        });

        Mockito.when(this.languageTypeProxy.findAll()).thenAnswer(invocation -> {
            return languageTypes;
        });

        Mockito.when(this.languageTypeProxy.save((LanguageType) any())).thenAnswer(invocation -> {
            return invocation.getArgument(0);
        });

        Mockito.when(this.languageProxy.findOrCreate(anyString())).thenAnswer(invocation -> {
            for (Language l : languages) {
                if (l.getIso().equals(invocation.getArgument(0)))
                    return l;
            }
            return null;
        });

        Mockito.when(this.typeProxy.findOrCreate(anyString())).thenAnswer(invocation -> {
            for (Type t : types) {
                if (t.getCode().equals(invocation.getArgument(0)))
                    return t;
            }
            return null;
        });


    }


    @Test
    public void whenGetLanguageType_thenNoError() throws Exception {
        for (LanguageType lt : languageTypes) {
            this.mvc.perform(get("/language-type")
                    .accept(MediaType.APPLICATION_JSON)
                    .param("ISO",lt.getLanguage().getIso())
                    .param("type",lt.getType().getCode())
            )
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(1)))
                    .andExpect(jsonPath("$.[0].languageId", is(lt.getLanguageId())))
                    .andExpect(jsonPath("$.[0].typeId", is(lt.getTypeId())))
                    .andExpect(jsonPath("$.[0].typeLangCode", is(lt.getTypeLangCode())))
                    .andExpect(jsonPath("$.[0].description", is(lt.getDescription())));

        }

    }

    @Test
    public void whenGetAllLanguagesType_thenNoError() throws Exception {
            this.mvc.perform(get("/language-type/all")
                    .accept(MediaType.APPLICATION_JSON)
            )
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(languageTypes.size())))
                    .andExpect(jsonPath("$.[0].languageId", is(languageTypes.get(0).getLanguageId())))
                    .andExpect(jsonPath("$.[0].typeId", is(languageTypes.get(0).getTypeId())))
                    .andExpect(jsonPath("$.[0].typeLangCode", is(languageTypes.get(0).getTypeLangCode())))
                    .andExpect(jsonPath("$.[0].description", is(languageTypes.get(0).getDescription())))
                    .andExpect(jsonPath("$.[1].languageId", is(languageTypes.get(1).getLanguageId())))
                    .andExpect(jsonPath("$.[1].typeId", is(languageTypes.get(1).getTypeId())))
                    .andExpect(jsonPath("$.[1].typeLangCode", is(languageTypes.get(1).getTypeLangCode())))
                    .andExpect(jsonPath("$.[1].description", is(languageTypes.get(1).getDescription())))
                    .andExpect(jsonPath("$.[2].languageId", is(languageTypes.get(2).getLanguageId())))
                    .andExpect(jsonPath("$.[2].typeId", is(languageTypes.get(2).getTypeId())))
                    .andExpect(jsonPath("$.[2].typeLangCode", is(languageTypes.get(2).getTypeLangCode())))
                    .andExpect(jsonPath("$.[2].description", is(languageTypes.get(2).getDescription())))
                    .andExpect(jsonPath("$.[3].languageId", is(languageTypes.get(3).getLanguageId())))
                    .andExpect(jsonPath("$.[3].typeId", is(languageTypes.get(3).getTypeId())))
                    .andExpect(jsonPath("$.[3].typeLangCode", is(languageTypes.get(3).getTypeLangCode())))
                    .andExpect(jsonPath("$.[3].description", is(languageTypes.get(3).getDescription())))
                    .andExpect(jsonPath("$.[4].languageId", is(languageTypes.get(4).getLanguageId())))
                    .andExpect(jsonPath("$.[4].typeId", is(languageTypes.get(4).getTypeId())))
                    .andExpect(jsonPath("$.[4].typeLangCode", is(languageTypes.get(4).getTypeLangCode())))
                    .andExpect(jsonPath("$.[4].description", is(languageTypes.get(4).getDescription())))
                    .andExpect(jsonPath("$.[5].languageId", is(languageTypes.get(5).getLanguageId())))
                    .andExpect(jsonPath("$.[5].typeId", is(languageTypes.get(5).getTypeId())))
                    .andExpect(jsonPath("$.[5].typeLangCode", is(languageTypes.get(5).getTypeLangCode())))
                    .andExpect(jsonPath("$.[5].description", is(languageTypes.get(5).getDescription())))
                    .andExpect(jsonPath("$.[6].languageId", is(languageTypes.get(6).getLanguageId())))
                    .andExpect(jsonPath("$.[6].typeId", is(languageTypes.get(6).getTypeId())))
                    .andExpect(jsonPath("$.[6].typeLangCode", is(languageTypes.get(6).getTypeLangCode())))
                    .andExpect(jsonPath("$.[6].description", is(languageTypes.get(6).getDescription())))
                    .andExpect(jsonPath("$.[7].languageId", is(languageTypes.get(7).getLanguageId())))
                    .andExpect(jsonPath("$.[7].typeId", is(languageTypes.get(7).getTypeId())))
                    .andExpect(jsonPath("$.[7].typeLangCode", is(languageTypes.get(7).getTypeLangCode())))
                    .andExpect(jsonPath("$.[7].description", is(languageTypes.get(7).getDescription())));

    }


    @Test
    public void whenInsertNewLanguageType_thenNoError() throws Exception {
        for (LanguageType lt : languageTypes) {
            this.mvc.perform(post("/language-type")
                    .param("ISOCode", lt.getLanguage().getIso())
                    .param("typeCode", lt.getType().getCode())
                    .param("languageTypeCode", lt.getTypeLangCode())
                    .param("description", lt.getDescription())

                    .accept(MediaType.APPLICATION_JSON)
            )
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.languageId", is(lt.getLanguageId())))
                    .andExpect(jsonPath("$.typeId", is(lt.getTypeId())))
                    .andExpect(jsonPath("$.typeLangCode", is(lt.getTypeLangCode())))
                    .andExpect(jsonPath("$.description", is(lt.getDescription())));
        }
    }

    @Test
    public void whenDeleteLanguage_thenNoError() throws Exception {
        this.mvc.perform(delete("/language-type")
                .param("ISO","es-ES")
                .param("type","res")
                .accept(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isOk());
    }


}