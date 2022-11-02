package apies.deprecated_httpurlconnection;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * получение описания места по его id
 * Returns detailed information about the object. Objects can contain different amount of information.
 *
 * https://api.opentripmap.com/0.1/en/places/xid/228?apikey=5ae2e3f221c38a28845f05b66530af0af94bfb85c3e628b04f9abbf8
 *
 * key - 5ae2e3f221c38a28845f05b66530af0af94bfb85c3e628b04f9abbf8
 */
public class DescriptionPlaceById {

    private static final int CONNECTION_TIMEOUT = 2000;
    String id = "228";
    final String key = "5ae2e3f221c38a28845f05b66530af0af94bfb85c3e628b04f9abbf8";

    public DescriptionPlaceById(String id) {
        this.id = id;
    }

    public DescriptionPlaceById() {
    }

    public String getDescription() throws IOException {

        //Для отправки запроса, что GET, что POST, необходимо создать объект URL и открыть на его основе соединение:
        final URL url = new URL("https://api.opentripmap.com/0.1/en/places/xid/228?apikey=5ae2e3f221c38a28845f05b66530af0af94bfb85c3e628b04f9abbf8");
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
