package es.um.asio.back;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
    public static void main(String[] args) {
        Logger logger = LoggerFactory.getLogger(Main.class);
        String uri = ("http:///ConceptoGrupo/es-ES/rec/um/hercules.org");
        uri = uri.replaceFirst("///","//");
        logger.info(uri);
    }
}
