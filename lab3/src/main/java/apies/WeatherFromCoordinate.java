package apies;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

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

    final String key = "69640eaad50baf8c0c1690037c53d281";

    public WeatherFromCoordinate(double lat, double lon) {
        this.lat = lat;
        this.lon = lon;
    }

    public WeatherFromCoordinate() {
        this.lat = 44.9521;
        this.lon = 34.1024;
    }


    public String getWeather() throws IOException {

        //Для отправки запроса, что GET, что POST, необходимо создать объект URL и открыть на его основе соединение:
        final URL url = new URL("http://api.openweathermap.org/data/2.5/weather?lat=44.9521&lon=34.1024&" +
                "appid=69640eaad50baf8c0c1690037c53d281");
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
