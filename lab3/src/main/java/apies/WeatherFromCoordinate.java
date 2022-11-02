package apies;


import client.ParameterStringBuilder;
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

/**
 * https://api.openweathermap.org/data/2.5/weather?lat={lat}&lon={lon}&appid={API key}
 * lat - первая координата
 * lon - вторая
 * appid - 69640eaad50baf8c0c1690037c53d281
 */
public class WeatherFromCoordinate {

    private static final int CONNECTION_TIMEOUT = 2000;
    final double lat;
    final double lon;

     final static String key = "69640eaad50baf8c0c1690037c53d281";

    public WeatherFromCoordinate(double lat, double lon) {
        this.lat = lat;
        this.lon = lon;
    }

    public WeatherFromCoordinate() {
        this.lat = 44.9521;
        this.lon = 34.1024;
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

        // Считываем json
        Object obj = new JSONParser().parse(responseJSON); // Object obj = new JSONParser().parse(new FileReader("JSONExample.json"));
        // Кастим obj в JSONObject
        JSONObject jo = (JSONObject) obj;


        JSONObject main = (JSONObject) jo.get("main");
        double temperatureInCelsiusDouble = (double) main.get("temp");
        String temperatureInCelsius = Double.toString(temperatureInCelsiusDouble);

        System.out.println("temperature at this place in Celsius: " + temperatureInCelsius);

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


    public static String getWeather() throws IOException {

        //Для отправки запроса, что GET, что POST, необходимо создать объект URL и открыть на его основе соединение:
        final URL url = new URL("http://api.openweathermap.org/data/2.5/weather?lat=44.9521&lon=34.1024&appid=69640eaad50baf8c0c1690037c53d281");
        final URL urp = new URL("http://api.openweathermap.org/data/2.5/weather?lon=82.9515&lat=54.9673&key=69640eaad50baf8c0c1690037c53d281");
        final HttpURLConnection con = (HttpURLConnection) url.openConnection();

        //Далее необходимо сдобрить соединение всеми параметрами:
        con.setRequestMethod("GET");
        //con.setRequestProperty("Content-Type", "application/json");
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
}
