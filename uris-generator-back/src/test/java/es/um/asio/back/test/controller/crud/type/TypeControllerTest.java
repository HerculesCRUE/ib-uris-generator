package es.um.asio.back.test.controller.crud.type;

import com.fasterxml.jackson.databind.ObjectMapper;
import es.um.asio.back.controller.crud.language.LanguageController;
import es.um.asio.back.controller.crud.language_type.LanguageTypeController;
import es.um.asio.back.controller.crud.type.TypeController;
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
public class TypeControllerTest {

    /**
     * MVC test support
     */
    @Autowired
    private MockMvc mvc;


    /**
     * Language proxy
     */
    @MockBean
    private TypeProxy typeProxy;


    /**
     * JSON Object mapper
     */
    @Autowired
    private ObjectMapper objectMapper;

    final List<Type> types = new ArrayList<>();



    @TestConfiguration
    static class typeProxyTestConfiguration {
        @Bean
        public TypeController typeController() {
            return new TypeController();
        }
    }

    @Before
    public void beforeTest() {


        // Types
        Type t1 = new Type("def","definitions");
        Type t2 = new Type("kos","skos");
        Type t3 = new Type("res","resources");
        Type t4 = new Type("cat","catalog");
        types.add(t1);
        types.add(t2);
        types.add(t3);
        types.add(t4);


        Mockito.when(this.typeProxy.find(anyString())).thenAnswer(invocation -> {
            for (Type t : types) {
                if (t.getCode().equals(invocation.getArgument(0)))
                    return Optional.of(t);;
            }
            return Optional.empty();
        });

        Mockito.when(this.typeProxy.findAll()).thenAnswer(invocation -> {
            return types;
        });

        Mockito.when(this.typeProxy.findOrCreate(anyString())).thenAnswer(invocation -> {
            for (Type t : types) {
                if (t.getCode().equals(invocation.getArgument(0)))
                    return t;
            }
            return null;
        });

        Mockito.when(this.typeProxy.save((Type) any())).thenAnswer(invocation -> {
            return invocation.getArgument(0);
        });



    }


    @Test
    public void whenGetType_thenNoError() throws Exception {
        for (Type t : types) {
            this.mvc.perform(get("/type")
                    .accept(MediaType.APPLICATION_JSON)
                    .param("code",t.getCode())
            )
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code", is(t.getCode())))
                    .andExpect(jsonPath("$.name", is(t.getName())));

        }

    }

    @Test
    public void whenGetAllTypes_thenNoError() throws Exception {
            this.mvc.perform(get("/type/all")
                    .accept(MediaType.APPLICATION_JSON)
            )
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(types.size())))
                    .andExpect(jsonPath("$.[0].code", is(types.get(0).getCode())))
                    .andExpect(jsonPath("$.[0].name", is(types.get(0).getName())))
                    .andExpect(jsonPath("$.[1].code", is(types.get(1).getCode())))
                    .andExpect(jsonPath("$.[1].name", is(types.get(1).getName())))
                    .andExpect(jsonPath("$.[2].code", is(types.get(2).getCode())))
                    .andExpect(jsonPath("$.[2].name", is(types.get(2).getName())))
                    .andExpect(jsonPath("$.[3].code", is(types.get(3).getCode())))
                    .andExpect(jsonPath("$.[3].name", is(types.get(3).getName())));

    }


    @Test
    public void whenInsertNewType_thenNoError() throws Exception {
        for (Type t : types) {
            this.mvc.perform(post("/type")
                    .param("code", t.getCode())
                    .param("name", t.getName())

                    .accept(MediaType.APPLICATION_JSON)
            )
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code", is(t.getCode())))
                    .andExpect(jsonPath("$.name", is(t.getName())));
        }
    }


    @Test
    public void whenDeleteLanguage_thenNoError() throws Exception {
        this.mvc.perform(delete("/type")
                .param("code","def")
                .accept(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isOk());
    }



}