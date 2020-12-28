package es.um.asio.service.service.impl;

import es.um.asio.service.TestApplication;
import es.um.asio.service.proxy.LanguageProxy;
import es.um.asio.service.service.SchemaService;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;


@RunWith(SpringRunner.class )
@ContextConfiguration(classes = TestApplication.class, loader = AnnotationConfigContextLoader.class)
@SpringBootTest
class SchemaServiceImplTest {

    /**
     * MVC test support
     */
    @Autowired
    private SchemaService schemaService;

    @TestConfiguration
    static class schemaServiceImplTestConfiguration {
        @Bean
        public SchemaService schemaService() {
            return new SchemaServiceImpl();
        }

    }

    @Before
    public void beforeTest() {

    }

    @Test
    void getCanonicalSchema() {
    }

    @Test
    void getCanonicalLanguageSchema() {
    }

    @Test
    void buildCanonical() {

        System.out.println();
    }
}