package client;

import apies.CoordinatesLocations;
import apies.DescriptionPlaceById;
import apies.InterestingPlacesByCoordinates;
import apies.WeatherFromCoordinate;
import jsonHandlers.CoordinatesLocationsJSONHandler;
import jsonHandlers.DescriptionPlaceByIdJSONHandler;
import jsonHandlers.InterestingPlacesByCoordinatesJSONHandler;
import jsonHandlers.WeatherFromCoordinateJSONHandler;
import pojos.description.PojoDescription;
import pojos.interestingPlace.PojoProperties;
import pojos.placesFromName.PojoOnePlaceFromName;
import pojos.placesFromName.PojoPoint;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class ClientAsync implements Runnable {

    @Override
    public void run() {

        Scanner sc = new Scanner(System.in);
        System.out.print("Enter a location: ");
        String location = sc.nextLine();

        // 1 вызов API (получение координат и списка мест)
        CompletableFuture<List<PojoOnePlaceFromName>> listOfPlacesFuture = this.getListOfPlacesFuture(location);
        List<PojoOnePlaceFromName> listOfPlaces = getListOfPlacesFromFuture(listOfPlacesFuture);

        if (listOfPlaces.isEmpty()){
            System.out.println("we can't find any locations sorry");
            return;
        }

        for (PojoOnePlaceFromName place: listOfPlaces) {
            System.out.println(place.getName());
        }

        System.out.println("Choose location from list: ");
        String locationName = sc.nextLine();

        // выбор одного из мест
        PojoPoint coordinates = this.chooseLocationByName(locationName, listOfPlaces);

        if (coordinates == null){
            System.out.println("Sorry. Smth went wrong... We can't get coordinates for this place");
            return;
        }

        System.out.println("you choosed place with coordinates: " + coordinates.getLat() + ", " + coordinates.getLng());

        // 2 вызов API (получение погоды по координатам)
        CompletableFuture<String> currentWeatherFuture = this.getWeatherFromCoordinatesFuture(coordinates);
        String currentWeather = getWeatherFromFuture(currentWeatherFuture);

        System.out.println("temperature at this place in Celsius: " + currentWeather);

        // 3 вызов API (получение списка интересных мест по координатам)
        CompletableFuture<List<PojoProperties>> pojoInterestingPlaceListFuture =
                    getInterestingPlacesByCoordinatesFuture(coordinates);


        System.out.println("Please wait some time. We are handling your request...");


        // 4 вызов API (получение описания интересного места)
        CompletableFuture<List<PojoDescription>> descriptionListFuture =
                    getAllDescriptionsFuture(pojoInterestingPlaceListFuture);


        List<PojoDescription> descriptionList = getDescriptionsFromFuture(descriptionListFuture);


        if (descriptionList == null || descriptionList.isEmpty()){
            System.out.println("Sorry. Smth went wrong... We can't get description for this place");
            return;
        }


        for (PojoDescription description: descriptionList) {
            System.out.println("name: " + description.getName());
            System.out.println("description: " + description.getInfo());
            System.out.println();
        }

    }

    private List<PojoDescription> getDescriptionsFromFuture(CompletableFuture<List<PojoDescription>> descriptionListFuture) {
        try {
            return descriptionListFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }


    private CompletableFuture<List<PojoDescription>> getAllDescriptionsFuture
            (CompletableFuture<List<PojoProperties>> pojoInterestingPlaceListFuture) {

        return pojoInterestingPlaceListFuture
                .thenApply(ClientAsync::convertPojoPropertiesToDescriptions);
    }

    private static List<PojoDescription> convertPojoPropertiesToDescriptions(List<PojoProperties> pojoPropertiesList) {

        List<PojoDescription> descriptionList = new ArrayList<>();

        for (PojoProperties properties: pojoPropertiesList) {
            try {
                String descriptionJSON = DescriptionPlaceById.getDescription(properties.getProperties().getXid());
                PojoDescription description = DescriptionPlaceByIdJSONHandler.getDescriptionFromJSON(descriptionJSON);
                descriptionList.add(description);
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        return descriptionList;
    }


    private CompletableFuture<List<PojoOnePlaceFromName>> getListOfPlacesFuture(String location) {

        CompletableFuture<List<PojoOnePlaceFromName>> listOfPlacesFuture;

        try {
            CompletableFuture<String> jsonResponseFuture = CoordinatesLocations.getCoordinatesHttpClientAsync(location);
            listOfPlacesFuture = jsonResponseFuture.thenApply(CoordinatesLocationsJSONHandler::getListOfPlaces);
        } catch (IOException | URISyntaxException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        return listOfPlacesFuture;
    }


    private List<PojoOnePlaceFromName> getListOfPlacesFromFuture(CompletableFuture<List<PojoOnePlaceFromName>> future) {
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }


    private CompletableFuture<List<PojoProperties>> getInterestingPlacesByCoordinatesFuture(PojoPoint coordinates) {

        CompletableFuture<String> jsonListOfPlacesFuture = InterestingPlacesByCoordinates.getPlacesAsync(coordinates);

        return jsonListOfPlacesFuture
                .thenApply(InterestingPlacesByCoordinatesJSONHandler::getListOfInterestingPlaces);
    }


    private CompletableFuture<String> getWeatherFromCoordinatesFuture(PojoPoint coordinates) {

        String lat = coordinates.getLat();
        String lon = coordinates.getLng();

        return WeatherFromCoordinate.getWeatherAsync(lat, lon).
                thenApply(WeatherFromCoordinateJSONHandler::getWeather);
    }

    private String getWeatherFromFuture(CompletableFuture<String> future) {
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    private PojoPoint chooseLocationByName(String locationName, List<PojoOnePlaceFromName> listOfPlaces) {
        for (PojoOnePlaceFromName place: listOfPlaces) {
            if (place.getName().equalsIgnoreCase(locationName)) {
                return place.getPoint();
            }
        }
        return null;
    }



}
