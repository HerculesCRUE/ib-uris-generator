package es.um.asio.back.runners;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import es.um.asio.service.mapper.MapperConfig;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import io.cucumber.spring.CucumberContextConfiguration;

@CucumberContextConfiguration
@SpringBootApplication
@EnableAutoConfiguration
@Import(MapperConfig.class)
@RunWith(Cucumber.class)
@CucumberOptions(
        plugin = {"pretty", "html:target/cucumber/cucumber.html", "summary", "json:target/cucumber/cucumber.json", "junit:target/cucumber/cucumber-junit.xml"},
        features = {"src/test/features"},
        glue = {"es.um.asio.back.runners.stepdefs"}
)

public class UrisGeneratorRunnerTest {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
