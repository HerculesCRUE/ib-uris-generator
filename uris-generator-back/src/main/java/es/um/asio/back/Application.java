package es.um.asio.back;

import es.um.asio.service.ServiceConfig;
import es.um.asio.service.model.Language;
import es.um.asio.service.model.LanguageType;
import es.um.asio.service.model.StorageType;
import es.um.asio.service.model.Type;
import es.um.asio.service.proxy.LanguageProxy;
import es.um.asio.service.proxy.LanguageTypeProxy;
import es.um.asio.service.proxy.StorageTypeProxy;
import es.um.asio.service.proxy.TypeProxy;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@EnableAutoConfiguration
@Import({ ServiceConfig.class })
@ComponentScan
public class Application {


    @Autowired
    TypeProxy typeProxy;

    @Autowired
    LanguageProxy languageProxy;

    @Autowired
    LanguageTypeProxy languageTypeProxy;

    @Autowired
    StorageTypeProxy storageTypeProxy;
    /**
     * Main method for embedded deployment.
     *
     * @param args
     *            the arguments
     */
    public static void main(final String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    InitializingBean populateInitData() {
        return () -> {

            // Load Initial Data

            // Type Load
            Type t1 = new Type("def","definitions");
            Type t2 = new Type("kos","skos");
            Type t3 = new Type("res","resources");
            Type t4 = new Type("cat","catalog");
            typeProxy.save(t1);
            typeProxy.save(t2);
            typeProxy.save(t3);
            typeProxy.save(t4);

            // Language Load
            Language l1 = new Language("en-EN","English","domain","sub-domain", "type","concept","reference",false);
            Language l2 = new Language("es-ES","Español","dominio","sub-dominio", "tipo","concepto","referencia",true);
            languageProxy.save(l1);
            languageProxy.save(l2);

            // LanguageType Load
            LanguageType lt1 = new LanguageType(l2,t1,"def","definiciones");
            LanguageType lt2 = new LanguageType(l2,t2,"kos","skos");
            LanguageType lt3 = new LanguageType(l2,t3,"rec","recurso");
            LanguageType lt4 = new LanguageType(l2,t4,"cat","catalogo");
            LanguageType lt5 = new LanguageType(l1,t1,"def","definitions");
            LanguageType lt6 = new LanguageType(l1,t2,"kos","skos");
            LanguageType lt7 = new LanguageType(l1,t3,"res","resource");
            LanguageType lt8 = new LanguageType(l1,t4,"cat","catalog");
            languageTypeProxy.save(lt1);
            languageTypeProxy.save(lt2);
            languageTypeProxy.save(lt3);
            languageTypeProxy.save(lt4);
            languageTypeProxy.save(lt5);
            languageTypeProxy.save(lt6);
            languageTypeProxy.save(lt7);
            languageTypeProxy.save(lt8);

            StorageType st1 = new StorageType("trellis");
            StorageType st2 = new StorageType("wikibase");
            StorageType st3 = new StorageType("weso-wikibase");
            storageTypeProxy.save(st1);
            storageTypeProxy.save(st2);
            storageTypeProxy.save(st3);
        };
    }
}
