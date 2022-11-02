package client;

import apies.CoordinatesLocations;
import apies.InterestingPlacesByCoordinates;
import apies.WeatherFromCoordinate;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import pojos.interestingPlace.PojoFeaturesList;
import pojos.interestingPlace.PojoInterestingPlace;
import pojos.interestingPlace.PojoProperties;
import pojos.placesFromName.PojoOnePlaceFromName;
import pojos.placesFromName.PojoPlacesFromName;
import pojos.placesFromName.PojoPoint;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Client implements Runnable {

    Executor executor = Executors.newFixedThreadPool(10);

    List<CompletableFuture<String>> requests = new ArrayList<>();


    public List<PojoOnePlaceFromName> getListOfPlaces(String address) throws URISyntaxException, InterruptedException, JsonProcessingException, ParseException {

        String jsonListOfPlaces = callCoordinatesLocations(address);

        System.out.println(jsonListOfPlaces);

        /*
        // Считываем json
        Object obj = new JSONParser().parse(jsonListOfPlaces); // Object obj = new JSONParser().parse(new FileReader("JSONExample.json"));
        // Кастим obj в JSONObject
        JSONObject jo = (JSONObject) obj;

        List<String> stringList =(List<String>) jo.get("hits");

        System.out.println("list of hits: " + stringList);
        */

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        PojoPlacesFromName placesFromName = objectMapper.readValue(jsonListOfPlaces, PojoPlacesFromName.class);

        List<PojoOnePlaceFromName> listOfPlaces = placesFromName.getHits();

        for (PojoOnePlaceFromName place: listOfPlaces) {
            System.out.println(place.getName() + " - " + place.getCountry() + " - " + place.getPoint());
        }
        //System.out.println(placesFromName.getHits().toString());


        return listOfPlaces;
    }

    public String callCoordinatesLocations(String address) throws URISyntaxException, InterruptedException {
        CoordinatesLocations coordinatesLocations = new CoordinatesLocations(address);
        String jsonResponse;

        try {
            jsonResponse = coordinatesLocations.getCoordinatesHttpClientSync();
            //System.out.println(jsonResponse);
            //requests.add(coordinatesLocations.getCoordinatesHttpClientAsync());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return jsonResponse;
    }


    public void callCoordinatesLocations() throws URISyntaxException, InterruptedException {
        CoordinatesLocations coordinatesLocations = new CoordinatesLocations("Berlin", "de");

        try {
            System.out.println(coordinatesLocations.getCoordinatesHttpClientSync());
            requests.add(coordinatesLocations.getCoordinatesHttpClientAsync());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {

        Scanner sc = new Scanner(System.in);
        System.out.print("Enter a location: ");
        String location = sc.nextLine();
        List<PojoOnePlaceFromName> listOfPlaces;

        try {

            listOfPlaces = this.getListOfPlaces(location);

        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        if (listOfPlaces.isEmpty() || listOfPlaces == null){
            System.out.println("we can't find any locations sorry");
            return;
        }

        System.out.println("Choose location from list: ");
        String locationName = sc.nextLine();

        PojoPoint coordinates = this.chooseLocationByName(locationName, listOfPlaces);

        if (coordinates == null){
            System.out.println("Sorry. Smth went wrong... We can't get coordinates for this place");
            return;
        }

        System.out.println("you choosed place with coordinates: " + coordinates.getLat() + ", " + coordinates.getLng());

        String currentWeather = null;
        try {
            currentWeather = this.getWeatherFromCoordinates(coordinates);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        System.out.println(currentWeather);

        List<PojoProperties> pojoInterestingPlaceList;

        try {
            pojoInterestingPlaceList = getInterestingPlacesByCoordinates(coordinates);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }


    }

    private List<PojoProperties> getInterestingPlacesByCoordinates(PojoPoint coordinates) throws URISyntaxException, InterruptedException, ParseException, JsonProcessingException {

        String jsonListOfPlaces = callIntegestingPlaces(coordinates);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        PojoFeaturesList futuresList  = objectMapper.readValue(jsonListOfPlaces, PojoFeaturesList.class);

        List<PojoProperties> listOfPlaces = futuresList.getFeatures();

        for (PojoProperties place: listOfPlaces) {
            System.out.println(place.getProperties().getName()+ " - " + place.getProperties().getXid());
        }

        return listOfPlaces;
    }

    public String callIntegestingPlaces(PojoPoint coordinates) throws URISyntaxException, InterruptedException {

        String jsonResponse;

        try {
            jsonResponse = InterestingPlacesByCoordinates.getPlaces(coordinates);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return jsonResponse;
    }




    private String getWeatherFromCoordinates(PojoPoint coordinates) throws IOException, URISyntaxException, InterruptedException, ParseException {
        String lat = coordinates.getLat();
        String lon = coordinates.getLng();

        String response = WeatherFromCoordinate.getWeather(lat, lon);
        return response;
    }

    private PojoPoint chooseLocationByName(String locationName, List<PojoOnePlaceFromName> listOfPlaces) {
        for (PojoOnePlaceFromName place: listOfPlaces) {
            System.out.println(place.getName() + " - " + place.getCountry() + " - " + place.getPoint());
            if (place.getName().equalsIgnoreCase(locationName)) {
                return place.getPoint();
            }
        }
        return null;
    }
}
