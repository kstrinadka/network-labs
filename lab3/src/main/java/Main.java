import apies.CoordinatesLocations;
import apies.deprecated_httpurlconnection.DescriptionPlaceById;
import apies.InterestingPlacesByCoordinates;
import apies.WeatherFromCoordinate;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.net.URISyntaxException;

public class Main {

    public static void main(String[] args) throws URISyntaxException, InterruptedException, IOException, ParseException {


        String response = WeatherFromCoordinate.getWeather();
        System.out.println(response);


       /* try {
            //callCoordinatesLocations();

            //callWeatherFromCoordinate();

            //callInterestingPlacesByCoordinates();

            //callDescriptionPlaceById();

        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }*/



    }


    public static void callDescriptionPlaceById() {

        DescriptionPlaceById descriptionPlaceById = new DescriptionPlaceById();

        try {
            System.out.println(descriptionPlaceById.getDescription());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static void callInterestingPlacesByCoordinates() {
        InterestingPlacesByCoordinates interestingPlacesByCoordinates = new InterestingPlacesByCoordinates();

        try {
            System.out.println(interestingPlacesByCoordinates.getPlaces());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public static void callCoordinatesLocations() throws URISyntaxException, InterruptedException {
        CoordinatesLocations coordinatesLocations = new CoordinatesLocations("Berlin", "de");

        try {
            System.out.println(coordinatesLocations.getCoordinatesHttpClientSync());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public static void callWeatherFromCoordinate() {
        WeatherFromCoordinate weatherFromCoordinate = new WeatherFromCoordinate();

        try {
            System.out.println(weatherFromCoordinate.getWeather());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
