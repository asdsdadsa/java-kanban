package clients;

import utility.JsonCreater;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

public class KVTaskClient {
    public static final int PORT = 8078;
    private final String url;
    private String apiToken;

    public KVTaskClient() {
        url = "http://localhost:" + PORT;
        apiToken = registerAPIToken(url);
        JsonCreater.createGson();
    }

    private String registerAPIToken(String url) {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create(url + "/register");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            apiToken = response.body();
        } catch (InterruptedException | IOException e) {
            System.out.println("Регистрация apiToken неудачна. " + e.getMessage());
        }
        return apiToken;
    }

    public void save(String key, String value) {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create(url + "/save/" + key + "?API_TOKEN=" + apiToken);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(value, StandardCharsets.UTF_8);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .POST(body)
                .build();
        try {
            client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (InterruptedException | IOException e) {
            System.out.println("Сохранение неудачно. " + e.getMessage());
        }
    }

    public String load(String key) {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create(url + "/load/" + key + "?API_TOKEN=" + apiToken);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();
        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (InterruptedException | IOException e) {
            System.out.println("Загрузка неудачна. " + e.getMessage());
        }
        if (response != null) {
            return response.body();
        } else {
            return "Метод load";
        }
    }
}
