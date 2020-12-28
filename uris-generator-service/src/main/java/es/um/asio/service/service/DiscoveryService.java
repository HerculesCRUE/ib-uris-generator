package es.um.asio.service.service;

import io.cucumber.messages.internal.com.google.gson.internal.LinkedTreeMap;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public interface DiscoveryService {

    LinkedTreeMap<String, Object> findSimilarEntity(String node, String tripleStore, String className, String entityId, HashMap<String,Object> attrs);

}
