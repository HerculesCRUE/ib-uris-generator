package es.um.asio.service.service.impl;

import es.um.asio.service.config.properties.URISChemaProperties;
import es.um.asio.service.service.SchemaService;
import es.um.asio.service.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class SchemaServiceImpl implements SchemaService {

    @Autowired
    private URISChemaProperties properties;


    @Override
    public String getCanonicalSchema() {
        return properties.getCanonicalURISchema();
    }

    @Override
    public String getCanonicalLanguageSchema() {
        return properties.getCanonicalURILanguageSchema();
    }

    @Override
    public String buildCanonical(String domain, String subDomain, String type, String entity, String reference) {
        String schema = properties.getCanonicalURISchema();
        Pattern pattern = Pattern.compile("(https?://)([^:^/]*)(:\\d*)?(.*)?");
        Matcher matcher = pattern.matcher(schema);
        matcher.find();
        String protocol = matcher.group(1);
        String url = schema.replace(protocol,"");
        url = replaceURIChunk(url,"$domain$",domain);
        url = replaceURIChunk(url,"$sub-domain$",subDomain);
        url = replaceURIChunk(url,"$type$",type);
        url = replaceURIChunk(url,"$concept$",entity);
        url = replaceURIChunk(url,"$reference$",reference);
        url = url.replace("//","/");
        return protocol + url;
    }

    @Override
    public String buildCanonicalLanguage(String domain, String subDomain, String language, String type, String entity, String reference) {
        String schema = properties.getCanonicalURILanguageSchema();
        Pattern pattern = Pattern.compile("(https?://)([^:^/]*)(:\\d*)?(.*)?");
        Matcher matcher = pattern.matcher(schema);
        matcher.find();
        String protocol = matcher.group(1);
        String url = schema.replace(protocol,"");
        url = replaceURIChunk(url,"$domain$",domain);
        url = replaceURIChunk(url,"$sub-domain$",subDomain);
        url = replaceURIChunk(url,"$language$",language);
        url = replaceURIChunk(url,"$type$",type);
        url = replaceURIChunk(url,"$concept$",entity);
        url = replaceURIChunk(url,"$reference$",reference);
        url = url.replace("//","/");
        return protocol + url;
    }

    private String replaceURIChunk(String url, String regex, String value) {
        return url.replace(regex,Utils.isValidString(value)?value:"");
    }
}
