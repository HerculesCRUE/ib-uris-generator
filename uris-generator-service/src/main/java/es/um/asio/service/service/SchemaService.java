package es.um.asio.service.service;

public interface SchemaService {
    String getCanonicalSchema();
    String getCanonicalLanguageSchema();
    String buildCanonical(String domain,String subDomain,String type, String entity, String reference);
    String buildCanonicalLanguage(String domain, String subDomain, String language, String type, String entity, String reference);
}
