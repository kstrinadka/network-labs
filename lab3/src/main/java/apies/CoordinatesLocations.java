package apies;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * "https://graphhopper.com/api/1/geocode?q=berlin&locale=de&key=api_key"
 * q - город
 * locale - страна
 * key - ac4c90ae-905c-44e3-8061-f29a2982284c
 */
public class CoordinatesLocations {

    private static final int CONNECTION_TIMEOUT = 2000;
    final String city;
    final String locale;

    final String key = "ac4c90ae-905c-44e3-8061-f29a2982284c";

    public CoordinatesLocations(String city, String locale) {
        this.city = city;
        this.locale = locale;
    }

    public String getCoordinates() throws IOException {

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
}
