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

    //Executor executor = Executors.newFixedThreadPool(10);

    //List<CompletableFuture<String>> requests = new ArrayList<>();


    @Override
    public void run() {

        Scanner sc = new Scanner(System.in);
        System.out.print("Enter a location: ");
        String location = sc.nextLine();

        List<PojoOnePlaceFromName> listOfPlaces = this.getListOfPlaces(location);


        if (listOfPlaces.isEmpty() || listOfPlaces == null){
            System.out.println("we can't find any locations sorry");
            return;
        }

        for (PojoOnePlaceFromName place: listOfPlaces) {
            System.out.println(place.getName() + " - " + place.getCountry() + " - " + place.getPoint());
        }

        System.out.println("Choose location from list: ");
        String locationName = sc.nextLine();

        // 1 вызов API (получение координат и списка мест)
        PojoPoint coordinates = this.chooseLocationByName(locationName, listOfPlaces);

        if (coordinates == null){
            System.out.println("Sorry. Smth went wrong... We can't get coordinates for this place");
            return;
        }

        System.out.println("you choosed place with coordinates: " + coordinates.getLat() + ", " + coordinates.getLng());

        // 2 вызов API (получение погоды по координатам)
        String currentWeather = this.getWeatherFromCoordinates(coordinates);

        System.out.println("temperature at this place in Celsius: " + currentWeather);

        // 3 вызов API (получение списка интересных мест по координатам)
        List<PojoProperties> pojoInterestingPlaceList = getInterestingPlacesByCoordinates(coordinates);

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

        // 4 вызов API (получение описания интересного места)
        PojoDescription description = getDescriptionByXid(xidOfPlace);


        if (description == null){
            System.out.println("Sorry. Smth went wrong... We can't get description for this place");
            return;
        }


        System.out.println("description: " + description);

    }



    public List<PojoOnePlaceFromName> getListOfPlaces(String location) {

        String jsonResponse;
        try {
            jsonResponse = CoordinatesLocations.getCoordinatesHttpClientSync(location);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        List<PojoOnePlaceFromName> listOfPlaces = CoordinatesLocationsJSONHandler.getListOfPlaces(jsonResponse);

        return listOfPlaces;
    }



    private PojoDescription getDescriptionByXid(String xidOfPlace) {

        String descriptionJSON = null;
        try {
            descriptionJSON = DescriptionPlaceById.getDescription(xidOfPlace);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
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

    private List<PojoProperties> getInterestingPlacesByCoordinates(PojoPoint coordinates) {

        String jsonListOfPlaces;
        try {
            jsonListOfPlaces = InterestingPlacesByCoordinates.getPlaces(coordinates);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        List<PojoProperties> listOfPlaces = InterestingPlacesByCoordinatesJSONHandler
                .getListOfInterestingPlaces(jsonListOfPlaces);


        return listOfPlaces;
    }





    private String getWeatherFromCoordinates(PojoPoint coordinates) {
        String lat = coordinates.getLat();
        String lon = coordinates.getLng();

        String response = null;
        try {
            response = WeatherFromCoordinate.getWeather(lat, lon);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
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
