package es.um.asio.service;

import es.um.asio.service.mapper.MapperConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

@SpringBootApplication
@EnableAutoConfiguration
@Import(MapperConfig.class)
//@WebMvcTest(URISController.class)
@ActiveProfiles("dev")
public class TestApplication {
    /**@SpringBootApplication
     @EnableAutoConfiguration
     @Import({ ServiceConfig.class })
     @ComponentScan
     * Main method for embedded deployment.
     *
     * @param args
     *            the arguments
     */
    public static void main(final String[] args) {
        SpringApplication.run(TestApplication.class, args);
    }

    /**
     * Creates the password encoder BCrypt.
     *
     * @return The password encoder.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
