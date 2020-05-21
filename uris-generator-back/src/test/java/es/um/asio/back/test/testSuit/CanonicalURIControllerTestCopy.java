package es.um.asio.back.test.testSuit;

import com.fasterxml.jackson.databind.ObjectMapper;
import es.um.asio.back.controller.uri.CanonicalURIController;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Order;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(CanonicalURIController.class)
public class CanonicalURIControllerTestCopy {

    @Autowired
    private WebApplicationContext context;

    /**
     * MVC test support
     */
    @Autowired
    private MockMvc mockMvc;

    /**
     * User service
     */
    @MockBean
    private CanonicalURIController controller;

    /**
     * JSON Object mapper
     */
    @Autowired
    private ObjectMapper objectMapper;

    @Before
    public void beforeTest() {
        //mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    public void test_1_doPostEntity() throws Exception {
        MvcResult result = this.mockMvc.perform(
                post("/canonical-uri")
                    .param("domain","hercules.org")
                    .param("subDomain","um")
                    .param("typeCode","res")
                    .param("concept","entity1")
                    .accept(MediaType.APPLICATION_JSON)
        )

                .andExpect(status().isOk())
                .andReturn();
        String content = result.getResponse().getContentAsString();
        MockHttpServletResponse r = result.getResponse();
        System.out.println("Response:" + content);
    }


    @Test
    public void test_2_getAll() throws Exception {

        /*
        this.mockMvc.perform(get("/canonical-uri/all").accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(0)));
        */
        // Given
        // Real application context

        // When
        final ResultActions result = mockMvc.perform(
                get("/canonical-uri/all")
                        .accept(MimeTypeUtils.APPLICATION_JSON_VALUE));

        // Then
        //final int expectedSize = LANGUAGES.size();
        result.andDo(print()).andExpect(status().isOk());
    }
}
