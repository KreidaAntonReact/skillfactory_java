package ru.skillfactory.weather;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Client {
    private final ObjectMapper mapper = new ObjectMapper();
    private final HttpClient client = HttpClient.newHttpClient();

    public JsonNode getRequest(String uri, String[] headers) {
        HttpRequest request = this.buildGetRequest(uri, headers);
        String responseBody = null;

        try {
            responseBody = sendRequest(request);
        } catch (InterruptedException | IOException error) {
            logError(error);
        }

        return this.getJson(responseBody);
    }

    private HttpRequest buildGetRequest(String uri, String[] headers) {
        return HttpRequest.newBuilder().uri(URI.create(uri)).headers(headers).GET().build();
    }

    private String sendRequest (HttpRequest request) throws InterruptedException, IOException {
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        return response.body();
    }

    private JsonNode getJson(String data) {
        JsonNode node = null;

        try {
            node = mapper.readTree(data);
        } catch(JsonProcessingException error) {
            logError(error);
        }

        return node;
    }

    private void logError(Exception error) {
        System.err.println(error.getMessage());
    }
}
