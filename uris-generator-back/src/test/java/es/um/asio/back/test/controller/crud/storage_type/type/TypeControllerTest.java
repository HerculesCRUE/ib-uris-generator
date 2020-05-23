package es.um.asio.back.test.controller.crud.storage_type.type;

import com.fasterxml.jackson.databind.ObjectMapper;
import es.um.asio.back.controller.crud.language.LanguageController;
import es.um.asio.back.controller.crud.storage_type.StorageTypeController;
import es.um.asio.back.controller.crud.type.TypeController;
import es.um.asio.service.model.StorageType;
import es.um.asio.service.model.Type;
import es.um.asio.service.proxy.StorageTypeProxy;
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
    private StorageTypeProxy storageTypeProxy;


    /**
     * JSON Object mapper
     */
    @Autowired
    private ObjectMapper objectMapper;

    final List<StorageType> storageTypes = new ArrayList<>();



    @TestConfiguration
    static class storageTypeProxyTestConfiguration {
        @Bean
        public StorageTypeController storageTypeController() {
            return new StorageTypeController();
        }
    }

    @Before
    public void beforeTest() {


        // Storage Types
        StorageType st1 = new StorageType("trellis");
        StorageType st2 = new StorageType("wikibase");
        StorageType st3 = new StorageType("weso-wikibase");
        storageTypes.add(st1);
        storageTypes.add(st2);
        storageTypes.add(st3);


        Mockito.when(this.storageTypeProxy.findByName(anyString())).thenAnswer(invocation -> {
            for (StorageType st : storageTypes) {
                if (st.getName().equals(invocation.getArgument(0)))
                    return st;;
            }
            return null;
        });

        Mockito.when(this.storageTypeProxy.findAll()).thenAnswer(invocation -> {
            return storageTypes;
        });

        Mockito.when(this.storageTypeProxy.save((StorageType) any())).thenAnswer(invocation -> {
            return invocation.getArgument(0);
        });





    }


    @Test
    public void whenGetStorageType_thenNoError() throws Exception {
        for (StorageType st : storageTypes) {
            this.mvc.perform(get("/storage-type")
                    .accept(MediaType.APPLICATION_JSON)
                    .param("name",st.getName())
            )
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.name", is(st.getName())))
                    .andExpect(jsonPath("$.apiURL", is(st.getApiURL())))
                    .andExpect(jsonPath("$.endPointURL", is(st.getEndPointURL())))
                    .andExpect(jsonPath("$.schemaURI", is(st.getSchemaURI())));

        }

    }

    @Test
    public void whenGetAllStorageTypes_thenNoError() throws Exception {
            this.mvc.perform(get("/storage-type/all")
                    .accept(MediaType.APPLICATION_JSON)
            )
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(storageTypes.size())))
                    .andExpect(jsonPath("$.[0].name", is(storageTypes.get(0).getName())))
                    .andExpect(jsonPath("$.[0].apiURL", is(storageTypes.get(0).getApiURL())))
                    .andExpect(jsonPath("$.[0].endPointURL", is(storageTypes.get(0).getEndPointURL())))
                    .andExpect(jsonPath("$.[0].schemaURI", is(storageTypes.get(0).getSchemaURI())))
                    .andExpect(jsonPath("$.[1].name", is(storageTypes.get(1).getName())))
                    .andExpect(jsonPath("$.[1].apiURL", is(storageTypes.get(1).getApiURL())))
                    .andExpect(jsonPath("$.[1].endPointURL", is(storageTypes.get(1).getEndPointURL())))
                    .andExpect(jsonPath("$.[1].schemaURI", is(storageTypes.get(1).getSchemaURI())))
                    .andExpect(jsonPath("$.[2].name", is(storageTypes.get(2).getName())))
                    .andExpect(jsonPath("$.[2].apiURL", is(storageTypes.get(2).getApiURL())))
                    .andExpect(jsonPath("$.[2].endPointURL", is(storageTypes.get(2).getEndPointURL())))
                    .andExpect(jsonPath("$.[2].schemaURI", is(storageTypes.get(2).getSchemaURI())));

    }


    @Test
    public void whenInsertNewStorageType_thenNoError() throws Exception {
        for (StorageType st : storageTypes) {
            this.mvc.perform(post("/storage-type")
                    .param("name", st.getName())
                    .param("apiURL", st.getApiURL())
                    .param("endPointURL", st.getEndPointURL())
                    .param("schemaURI", st.getSchemaURI())
                    .accept(MediaType.APPLICATION_JSON)
            )
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.name", is(st.getName())))
                    .andExpect(jsonPath("$.apiURL", is(st.getApiURL())))
                    .andExpect(jsonPath("$.endPointURL", is(st.getEndPointURL())))
                    .andExpect(jsonPath("$.schemaURI", is(st.getSchemaURI())));
        }
    }


    @Test
    public void whenDeleteStorageType_thenNoError() throws Exception {
        this.mvc.perform(delete("/storage-type")
                .param("name","def")
                .accept(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isOk());
    }



}