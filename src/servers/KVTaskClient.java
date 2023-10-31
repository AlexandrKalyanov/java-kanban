package servers;

import exceptions.ManagerSaveException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class KVTaskClient {
    private final String serverUrl;
    private final String apiToken;
    private HttpResponse<String> response;

    public KVTaskClient(String serverUrl) {
        this.serverUrl = serverUrl;
        URI url = URI.create(serverUrl + "/register");
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(url)
                .header("Accept", "application/json")
                .build();
        HttpClient client = HttpClient.newHttpClient();
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            int statusCode = response.statusCode();
            if (statusCode != 200) {
                throw new ManagerSaveException("Don't register client. Status code is: " + statusCode);
            }
        } catch (IOException | InterruptedException e) {
            throw new ManagerSaveException("There is a problem\n" +
                    "Check the address and try again");
        }
        apiToken = response.body();
    }

    public void save(String key, String requestBody) {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create(serverUrl + "/save/" + key + "?API_TOKEN=" + apiToken);
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .uri(uri)
                .header("Accept", "application/json")
                .build();

        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            int statusCode = response.statusCode();
            if (statusCode != 200) {
                throw new ManagerSaveException("Don't register client. Status code is: " + statusCode);
            }
        } catch (IOException | InterruptedException e) {
            throw new ManagerSaveException("Don't register client");
        }
    }

    public String load(String key) {
        URI uri = URI.create(serverUrl + "/load/" + key + "?API_TOKEN=" + apiToken);
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .header("Accept", "application/json")
                .build();
        HttpClient client = HttpClient.newHttpClient();
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            int statusCode = response.statusCode();
            if (statusCode != 200) {
                throw new ManagerSaveException("Something went wrong. Status code is: " + statusCode);
            }
            return response.body();
        } catch (IOException | InterruptedException e) {
            throw new ManagerSaveException("There is a problem\n" +
                    "Check the address and try again");
        }
    }
}