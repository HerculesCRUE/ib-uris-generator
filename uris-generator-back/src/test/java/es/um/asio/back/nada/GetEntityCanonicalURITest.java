package es.um.asio.back.nada;

import es.um.asio.back.runners.stepdefs.URIControllerTest;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.http.MediaType;

import static es.um.asio.back.test.controller.URISControllerTest.asJsonString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class GetEntityCanonicalURITest extends URIControllerTest {

/*    @Autowired
    URISController urisController;*/

    //@When("^the client calls endpoint with JSON body \"(.*?)\"$")
    @When("the client calls endpoint with JSON body")
    public void the_client_issues_GET_version(String body) throws Throwable {
        //urisController.createResourceID("hercules.org","um","es-ES",new Gson().fromJson(body, HashMap.class));
        //System.out.println(body);
        mockMvc.perform(post("/uri-factory/canonical/entity")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .param("domain", "hercules.org")
                .param("subDomain", "um")
                .param("lang", "es-ES")
                .content(asJsonString(body))
        )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Then("the client receives status code of {int}")
    public void the_client_receives_status_code_of(int statusCode) throws Throwable {

    }

    @And("the client receives status code of {int}")
    public void the_client_receives_server_version_body(String version) throws Throwable {

    }
}
