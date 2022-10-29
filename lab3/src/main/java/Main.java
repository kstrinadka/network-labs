import apies.CoordinatesLocations;
import apies.DescriptionPlaceById;
import apies.InterestingPlacesByCoordinates;
import apies.WeatherFromCoordinate;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.HashMap;
import java.util.Map;

public class Main {

    public static void main(String[] args) {

        //callCoordinatesLocations();
        //callWeatherFromCoordinate();

        //callInterestingPlacesByCoordinates();

        callDescriptionPlaceById();


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


    public static void callCoordinatesLocations() {
        CoordinatesLocations coordinatesLocations = new CoordinatesLocations("Berlin", "de");

        try {
            System.out.println(coordinatesLocations.getCoordinates());
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
