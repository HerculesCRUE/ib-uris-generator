package es.um.asio.back.test.controller.crud.language;

import com.fasterxml.jackson.databind.ObjectMapper;
import es.um.asio.back.controller.crud.canonical_language.CanonicalURILanguageController;
import es.um.asio.back.controller.crud.language.LanguageController;
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
@WebMvcTest(LanguageController.class)
public class LanguageControllerTest {

    /**
     * MVC test support
     */
    @Autowired
    private MockMvc mvc;

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
     * JSON Object mapper
     */
    @Autowired
    private ObjectMapper objectMapper;

    final List<Language> languages = new ArrayList<>();



    @TestConfiguration
    static class languageProxyTestConfiguration {
        @Bean
        public LanguageController languageController() {
            return new LanguageController();
        }
    }

    @Before
    public void beforeTest() {


        // Mock data
        // Languages:
        languages.add(new Language("en-EN","English","domain","sub-domain","type","concept","reference",true));
        languages.add(new Language("es-ES","Español","dominio","sub-dominio","tipo","concepto","referencia",false));
        languages.add(new Language("pt-PT","Português","domínio","sub-domínio","tipo","conceito", "referência",false));


        // Mock findElement, Property, Reference
        Mockito.when(this.languageProxy.find(anyString())).thenAnswer(invocation -> {

            Optional<Language> oLanguage = Optional.empty();
            for (Language l: languages) {
                if (l.getIso().equals(invocation.getArgument(0))) {
                    return Optional.of(l);
                }
            }
            return Optional.empty();
        });

        Mockito.when(this.languageProxy.findAll()).thenAnswer(invocation -> {
            return languages;
        });

        Mockito.when(this.languageProxy.save((Language) any())).thenAnswer(invocation -> {
            return invocation.getArgument(0);
        });


    }


    @Test
    public void whenGetLanguage_thenNoError() throws Exception {
        for (Language l : languages) {
            this.mvc.perform(get("/language")
                    .accept(MediaType.APPLICATION_JSON)
                    .param("ISO",l.getIso())
            )
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.iso", is(l.getIso())))
                    .andExpect(jsonPath("$.languageStr", is(l.getLanguageStr())))
                    .andExpect(jsonPath("$.region", is(l.getRegion())))
                    .andExpect(jsonPath("$.name", is(l.getName())))
                    .andExpect(jsonPath("$.domain", is(l.getDomain())))
                    .andExpect(jsonPath("$.subDomain", is(l.getSubDomain())))
                    .andExpect(jsonPath("$.type", is(l.getType())))
                    .andExpect(jsonPath("$.concept", is(l.getConcept())))
                    .andExpect(jsonPath("$.reference", is(l.getReference())))
                    .andExpect(jsonPath("$.isDefault", is(l.getIsDefault())));

        }

    }

    @Test
    public void whenGetAllLanguages_thenNoError() throws Exception {
            this.mvc.perform(get("/language/all")
                    .accept(MediaType.APPLICATION_JSON)
            )
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(languages.size())))
                    .andExpect(jsonPath("$.[0].iso", is(languages.get(0).getIso())))
                    .andExpect(jsonPath("$.[1].iso", is(languages.get(1).getIso())))
                    .andExpect(jsonPath("$.[2].iso", is(languages.get(2).getIso())));
    }


    @Test
    public void whenInsertNewLanguage_thenNoError() throws Exception {
        this.mvc.perform(post("/language")
                .param("ISO","zh-CN")
                .param("domain","yù")
                .param("subDomain","zi-yù")
                .param("type","lèixíng")
                .param("concept","gàiniàn")
                .param("reference","cānkǎo")
                .param("isDefault","false")
                .accept(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.iso", is("zh-CN")))
                .andExpect(jsonPath("$.domain", is("yù")))
                .andExpect(jsonPath("$.subDomain", is("zi-yù")))
                .andExpect(jsonPath("$.type", is("lèixíng")))
                .andExpect(jsonPath("$.concept", is("gàiniàn")))
                .andExpect(jsonPath("$.reference", is("cānkǎo")))
                .andExpect(jsonPath("$.isDefault", is(false)));
    }

    @Test
    public void whenDeleteLanguage_thenNoError() throws Exception {
        this.mvc.perform(post("/language")
                .param("ISO","es-ES")
                .accept(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isOk());
    }


}