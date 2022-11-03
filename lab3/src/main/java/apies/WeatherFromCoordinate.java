package apies;


import client.ParameterStringBuilder;
import jsonHandlers.WeatherFromCoordinateJSONHandler;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

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

/**
 * https://api.openweathermap.org/data/2.5/weather?lat={lat}&lon={lon}&appid={API key}
 * lat - первая координата
 * lon - вторая
 * appid - 69640eaad50baf8c0c1690037c53d281
 */
public class WeatherFromCoordinate {

    private static final int CONNECTION_TIMEOUT = 2000;

     final static String key = "69640eaad50baf8c0c1690037c53d281";



    public static CompletableFuture<String> getWeatherAsync(String lat, String lon) {

        String urlString = "http://api.openweathermap.org/data/2.5/weather?";
        URI uri = null;
        try {
            uri = getURIWithParametrs(urlString, lat, lon);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .header("accept", "application/json")
                .build();


        CompletableFuture<String> responseCompletableFuture = client
                .sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body);

        return responseCompletableFuture;
    }


    public static String getWeather(String lat, String lon) throws IOException, URISyntaxException, InterruptedException, ParseException {

        String urlString = "http://api.openweathermap.org/data/2.5/weather?";
        URI uri = getURIWithParametrs(urlString, lat, lon);

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .header("accept", "application/json")
                .build();

        //В зависимости от того, какой тип BodyHandlers ты передал в метод send(), такой тип результата он и вернет.
        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

        String responseJSON;

        if (response.statusCode() == HttpURLConnection.HTTP_OK) {
            System.out.println(response.statusCode());
            System.out.println(response.body());
            responseJSON = response.body();
        } else {
            System.out.println("bad response from weather");
            return "";
            // Fail
        }

        String temperatureInCelsius = WeatherFromCoordinateJSONHandler.getWeather(responseJSON);

        return temperatureInCelsius;
    }

    /**
     * @param uriString - начало адреса без параметров
     * Метод
     */
    public static URI getURIWithParametrs(String uriString, String lat, String lon) throws UnsupportedEncodingException, MalformedURLException, URISyntaxException {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("lat", lat);
        parameters.put("lon", lon);
        parameters.put("units", "metric");
        parameters.put("appid", WeatherFromCoordinate.key);
        String uriWithParametrsString = ParameterStringBuilder.getParamsString(parameters, uriString);
        URI uri = new URI(uriWithParametrsString);

        System.out.println("такой URL получился: " + uri.toString());
        return uri;
    }


}
