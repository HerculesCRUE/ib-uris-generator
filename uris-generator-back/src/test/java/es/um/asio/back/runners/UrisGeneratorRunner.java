package es.um.asio.back.runners;

<<<<<<< HEAD
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
public class UrisGeneratorRunner {
=======
import es.um.asio.back.Application;
import es.um.asio.service.mapper.MapperConfig;
import io.cucumber.junit.CucumberOptions;
import io.cucumber.spring.CucumberContextConfiguration;
import org.junit.runner.RunWith;
import io.cucumber.junit.Cucumber;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;

@CucumberContextConfiguration
@SpringBootApplication
@EnableAutoConfiguration
@Import(MapperConfig.class)
@RunWith(Cucumber.class)
@CucumberOptions(
        plugin = {"pretty", "html:target/cucumber", "summary"},
        features = {"src/test/features"},
        glue = {"es.um.asio.back.runners.stepdefs"}
)

public class UrisGeneratorRunner {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
>>>>>>> develop
}
