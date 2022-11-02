package client;

import apies.CoordinatesLocations;
import apies.DescriptionPlaceById;
import apies.InterestingPlacesByCoordinates;
import apies.WeatherFromCoordinate;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import jsonHandlers.CoordinatesLocationsJSONHandler;
import jsonHandlers.DescriptionPlaceByIdJSONHandler;
import jsonHandlers.InterestingPlacesByCoordinatesJSONHandler;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import pojos.description.PojoDescription;
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
        List<PojoOnePlaceFromName> listOfPlaces = CoordinatesLocationsJSONHandler.getListOfPlaces(jsonListOfPlaces);

        return listOfPlaces;
    }

    public String callCoordinatesLocations(String location) throws URISyntaxException, InterruptedException {
        String jsonResponse;

        try {
            jsonResponse = CoordinatesLocations.getCoordinatesHttpClientSync(location);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return jsonResponse;
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

        for (PojoOnePlaceFromName place: listOfPlaces) {
            System.out.println(place.getName() + " - " + place.getCountry() + " - " + place.getPoint());
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

        System.out.println("temperature at this place in Celsius: " + currentWeather);

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

        if (pojoInterestingPlaceList == null) {
            System.out.println("we can't get list of interesting places. Sorry...");
            return;
        }

        for (PojoProperties place: pojoInterestingPlaceList) {
            System.out.println(place.getProperties().getName()+ " - " + place.getProperties().getXid());
        }

        System.out.println("You can choose one of this places to open description: ");
        String interestingLocationName = sc.nextLine();

        PojoInterestingPlace interestingPlace = this.chooseInterestingPlaceForDescription(interestingLocationName,
                pojoInterestingPlaceList);

        if (interestingPlace == null){
            System.out.println("Sorry. Smth went wrong... We can't get coordinates for this place");
            return;
        }

        System.out.println("you choosed : " + interestingPlace.getName());

        String xidOfPlace = interestingPlace.getXid();
        PojoDescription description;

        try {
            description = getDescriptionByXid(xidOfPlace);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        if (description == null){
            System.out.println("Sorry. Smth went wrong... We can't get description for this place");
            return;
        }

        
        System.out.println("description: " + description);

    }

    private PojoDescription getDescriptionByXid(String xidOfPlace) throws IOException, InterruptedException, ParseException {

        String descriptionJSON = DescriptionPlaceById.getDescription(xidOfPlace);
        PojoDescription pojoDescription  = DescriptionPlaceByIdJSONHandler.getDescriptionFromJSON(descriptionJSON);

        return pojoDescription;
    }

    private PojoInterestingPlace chooseInterestingPlaceForDescription(String interestingLocationName,
                                                                      List<PojoProperties> pojoInterestingPlaceList) {
        for (PojoProperties place: pojoInterestingPlaceList) {

            if (place.getProperties().getName().equalsIgnoreCase(interestingLocationName)) {
                return place.getProperties();
            }
        }
        return null;
    }

    private List<PojoProperties> getInterestingPlacesByCoordinates(PojoPoint coordinates) throws URISyntaxException, InterruptedException, ParseException, JsonProcessingException {

        String jsonListOfPlaces;
        try {
            jsonListOfPlaces = InterestingPlacesByCoordinates.getPlaces(coordinates);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        List<PojoProperties> listOfPlaces = InterestingPlacesByCoordinatesJSONHandler
                .getListOfInterestingPlaces(jsonListOfPlaces);


        return listOfPlaces;
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
