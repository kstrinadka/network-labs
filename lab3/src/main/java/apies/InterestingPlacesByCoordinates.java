package apies;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * получение списка интересных мест по координатам:
 * key - 5ae2e3f221c38a28845f05b66530af0af94bfb85c3e628b04f9abbf8
 *
 * https://api.opentripmap.com/0.1/en/places/radius?radius=5000&lon=82.95159894278376&lat=54.96781445&apikey=5ae2e3f221c38a28845f05b66530af0af94bfb85c3e628b04f9abbf8
 */
public class InterestingPlacesByCoordinates {
    private static final int CONNECTION_TIMEOUT = 2000;
     String lon = "82.95159894278376";
     String lat = "54.96781445";
    final String key = "5ae2e3f221c38a28845f05b66530af0af94bfb85c3e628b04f9abbf8";

    public InterestingPlacesByCoordinates(String lon, String lat) {
        this.lon = lon;
        this.lat = lat;
    }

    public InterestingPlacesByCoordinates() {
    }

    public String getPlaces() throws IOException {

        //Для отправки запроса, что GET, что POST, необходимо создать объект URL и открыть на его основе соединение:
        final URL url = new URL("https://api.opentripmap.com/0.1/en/places/radius?radius=5000&lon=82.95159894278376&lat=54.96781445&apikey=5ae2e3f221c38a28845f05b66530af0af94bfb85c3e628b04f9abbf8");
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
