package jsonHandlers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import pojos.placesFromName.PojoOnePlaceFromName;
import pojos.placesFromName.PojoPlacesFromName;

import java.util.List;

public class CoordinatesLocationsJSONHandler {

    public static List<PojoOnePlaceFromName> getListOfPlaces(String jsonListOfPlaces) {

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        PojoPlacesFromName placesFromName = null;

        try {
            placesFromName = objectMapper.readValue(jsonListOfPlaces, PojoPlacesFromName.class);
        } catch (JsonProcessingException e) {
            System.out.println("can't parse json to get coordinates from place");
            throw new RuntimeException(e);
        }

        List<PojoOnePlaceFromName> listOfPlaces = placesFromName.getHits();

        return listOfPlaces;
    }

}
