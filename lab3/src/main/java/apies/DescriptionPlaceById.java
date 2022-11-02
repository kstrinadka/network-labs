package apies;


import java.io.IOException;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;


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
    final String key = "5ae2e3f221c38a28845f05b66530af0af94bfb85c3e628b04f9abbf8";


    public static String getDescription(String xid) throws IOException, InterruptedException {

        //URL без параметров
        String urlString = "https://api.opentripmap.com/0.1/en/places/xid/" + xid + "?apikey=5ae2e3f221c38a28845f05b66530af0af94bfb85c3e628b04f9abbf8";

        System.out.println(urlString);

        URI uri = URI.create(urlString);

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
            //System.out.println(response.body());
            return response.body();
        } else {
            // Fail
        }

        return "";

    }

}
