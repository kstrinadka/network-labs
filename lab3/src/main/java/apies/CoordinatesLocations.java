package apies;


import client.ParameterStringBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;


//идея - возвращать из класса CompletableFuture и добавлять его в пул потоков. Ну или просто в новый поток его херачить

/**
 * "https://graphhopper.com/api/1/geocode?q=berlin&locale=de&key=api_key"
 * q - textual description of the address you are looking for.
 * locale - страна
 * key - ac4c90ae-905c-44e3-8061-f29a2982284c
 */
public class CoordinatesLocations {

    private static final int CONNECTION_TIMEOUT = 2000;
    final String location;
    final String locale;    // можно оставить пустым

    final String key = "ac4c90ae-905c-44e3-8061-f29a2982284c";

    public CoordinatesLocations(String location, String locale) {
        this.location = location;
        //this.locale = locale;
        this.locale = "";
    }

    public CoordinatesLocations(String location) {
        this.location = location;
        this.locale = "";
    }


    public CompletableFuture<String> getCoordinatesHttpClientAsync() throws IOException, URISyntaxException, InterruptedException {

        String uriString = "https://graphhopper.com/api/1/geocode?q=berlin&locale=de&key=ac4c90ae-905c-44e3-8061-f29a2982284c";
        URI uri = new URI(uriString);

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .build();

        /**
         * Метод sendAsync() возвращает объект CompletableFuture,
         * который внутри себя содержит HttpResponse, который внутри себя содержит строку, которую вернет сервер.
         */
        CompletableFuture<String> responseCompletableFuture = client
                .sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body);

        return responseCompletableFuture;
    }




    public String getCoordinatesHttpClientSync() throws IOException, URISyntaxException, InterruptedException {

        //URL без параметров
        String urlString = "https://graphhopper.com/api/1/geocode?";
        URI uri = getURIWithParametrs(urlString);

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .header("accept", "application/json")
                .build();


        //В зависимости от того, какой тип BodyHandlers ты передал в метод send(), такой тип результата он и вернет.
        HttpResponse<String> response = client.send(request,
                    HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

        if (response.statusCode() == HttpURLConnection.HTTP_OK) {
            System.out.println(response.statusCode());
            System.out.println(response.body());
            return response.body();
        } else {
            // Fail
        }

        return "";
    }

    public String getCoordinates() throws IOException, URISyntaxException {

        //Для отправки запроса, что GET, что POST, необходимо создать объект URL и открыть на его основе соединение:
        final URL url = new URL("https://graphhopper.com/api/1/geocode?q=berlin&locale=de&key=ac4c90ae-905c-44e3-8061-f29a2982284c");


        final HttpURLConnection con = (HttpURLConnection) url.openConnection();

        //Далее необходимо сдобрить соединение всеми параметрами:
        con.setRequestMethod("GET");
        con.setRequestProperty("Content-Type", "application/json");
        con.setConnectTimeout(CONNECTION_TIMEOUT);
        con.setReadTimeout(CONNECTION_TIMEOUT);

        con.connect();


        //И получить InputStream, откуда уже прочитать все полученные данные.
        try (final BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
            String inputLine;
            final StringBuilder content = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            return content.toString();
        } catch (final Exception ex) {
            ex.printStackTrace();
            return "";
        }

    }


    /**
     * @param uriString - начало адреса без параметров
     * Метод
     */
    public URI getURIWithParametrs(String uriString) throws UnsupportedEncodingException, MalformedURLException, URISyntaxException {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("q", location);
        parameters.put("locale", "");
        parameters.put("key", this.key);
        String uriWithParametrsString = ParameterStringBuilder.getParamsString(parameters, uriString);
        URI uri = new URI(uriWithParametrsString);

        System.out.println("такой URL получился: " + uri.toString());
        return uri;
    }



}
