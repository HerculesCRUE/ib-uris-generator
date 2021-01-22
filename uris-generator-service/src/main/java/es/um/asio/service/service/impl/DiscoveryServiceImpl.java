package es.um.asio.service.service.impl;

import es.um.asio.service.config.DiscoveryConfig;
import es.um.asio.service.service.DiscoveryService;
import io.cucumber.messages.internal.com.google.gson.Gson;
import io.cucumber.messages.internal.com.google.gson.JsonArray;
import io.cucumber.messages.internal.com.google.gson.JsonObject;
import io.cucumber.messages.internal.com.google.gson.internal.LinkedTreeMap;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.boot.json.JsonParser;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class DiscoveryServiceImpl implements DiscoveryService {

    @Autowired
    DiscoveryConfig discoveryConfig;

    @Override
    public LinkedTreeMap<String, Object> findSimilarEntity(String node, String tripleStore, String className, String entityId, HashMap<String, Object> attrs) {
        try {
            JsonObject jResult = doRequest(node,tripleStore,className,entityId,attrs);
            if (jResult!=null && jResult.has("automatics")) {
                JsonArray jAutomaticsArray = jResult.get("automatics").getAsJsonArray();
                if (jAutomaticsArray != null && jAutomaticsArray.size() > 0) {
                    JsonObject jAuto = jAutomaticsArray.get(0).getAsJsonObject();
                    return new Gson().fromJson(jAuto.toString(),LinkedTreeMap.class);
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private JsonObject doRequest(String node, String tripleStore, String className, String entityId, HashMap<String, Object> attrs) throws IOException {

        JSONObject jObject = new JSONObject(attrs);
        StringBuffer queryParams = new StringBuffer();
        queryParams.append(String.format("userId=%s", URLEncoder.encode("uris_factory", StandardCharsets.UTF_8.toString())));
        queryParams.append(String.format("&requestCode=%s", URLEncoder.encode(RandomStringUtils.randomAlphabetic(10), StandardCharsets.UTF_8.toString())));
        queryParams.append(String.format("&node=%s", URLEncoder.encode(node, StandardCharsets.UTF_8.toString())));
        queryParams.append(String.format("&tripleStore=%s", URLEncoder.encode(tripleStore, StandardCharsets.UTF_8.toString())));
        queryParams.append(String.format("&className=%s", URLEncoder.encode(className, StandardCharsets.UTF_8.toString())));
        queryParams.append(String.format("&entityId=%s", URLEncoder.encode(entityId+"-temp", StandardCharsets.UTF_8.toString())));
        queryParams.append(String.format("&doSynchronous=%s", "true"));
        queryParams.append(String.format("&propague_in_kafka=%s", "false"));
        queryParams.append(String.format("&linkEntities=%s", "false"));
        URL url = new URL(String.format("%s:%s%s?%s",discoveryConfig.getHost(),discoveryConfig.getPort(),discoveryConfig.getEndPoint(),queryParams.toString()));
        HttpURLConnection con = (HttpURLConnection)url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json; utf-8");
        con.setRequestProperty("Accept", "application/json");
        con.setDoOutput(true);
        try(OutputStream os = con.getOutputStream()) {
            byte[] input = jObject.toString().getBytes("utf-8");
            os.write(input, 0, input.length);
        }
        StringBuilder response = new StringBuilder();
        try(BufferedReader br = new BufferedReader(
                new InputStreamReader(con.getInputStream(), "utf-8"))) {
            response = new StringBuilder();
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
        }

        JsonObject jResponse = new Gson().fromJson(response.toString(), JsonObject.class);

        if (jResponse!=null && jResponse.has("response")) {
            JsonObject jRes = jResponse.get("response").getAsJsonObject();
            if (jRes!=null && jRes.has("results")) {
                JsonArray jResults = jRes.get("results").getAsJsonArray();
                if (jResults.size() > 0) {
                    return jResults.get(0).getAsJsonObject();
                }
            }
        }
        return null;
    }
}
