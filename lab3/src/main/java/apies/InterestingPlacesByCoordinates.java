package apies;


import client.ParameterStringBuilder;
import pojos.placesFromName.PojoPoint;

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
import java.util.List;
import java.util.Map;

/**
 * получение списка интересных мест по координатам:
 * key - 5ae2e3f221c38a28845f05b66530af0af94bfb85c3e628b04f9abbf8
 *
 * https://api.opentripmap.com/0.1/en/places/radius?radius=5000&lon=82.95159894278376&lat=54.96781445&apikey=5ae2e3f221c38a28845f05b66530af0af94bfb85c3e628b04f9abbf8
 */
public class InterestingPlacesByCoordinates {
    private static final int CONNECTION_TIMEOUT = 2000;
    final static String key = "5ae2e3f221c38a28845f05b66530af0af94bfb85c3e628b04f9abbf8";


    public static String getPlaces(PojoPoint coordinates) throws IOException, URISyntaxException, InterruptedException {

        //URL без параметров
        String urlString = "https://api.opentripmap.com/0.1/en/places/radius?";
        URI uri = getURIWithParametrs(urlString, coordinates.getLng(), coordinates.getLat());

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


    /**
     * @param uriString - начало адреса без параметров
     * Метод
     */
    public static URI getURIWithParametrs(String uriString, String lon, String lat) throws UnsupportedEncodingException, MalformedURLException, URISyntaxException {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("radius", "5000");
        parameters.put("lon", lon);
        parameters.put("lat", lat);
        parameters.put("apikey", InterestingPlacesByCoordinates.key);
        String uriWithParametrsString = ParameterStringBuilder.getParamsString(parameters, uriString);
        URI uri = new URI(uriWithParametrsString);

        System.out.println("такой URL получился: " + uri.toString());
        return uri;
    }
}
