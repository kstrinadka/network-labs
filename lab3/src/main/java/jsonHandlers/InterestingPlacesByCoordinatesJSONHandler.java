package jsonHandlers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import pojos.interestingPlace.PojoFeaturesList;
import pojos.interestingPlace.PojoProperties;

import java.util.List;

public class InterestingPlacesByCoordinatesJSONHandler {

    public static List<PojoProperties> getListOfInterestingPlaces(String jsonResponse) {

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        PojoFeaturesList futuresList  = null;
        try {
            futuresList = objectMapper.readValue(jsonResponse, PojoFeaturesList.class);
        } catch (JsonProcessingException e) {
            System.out.println("can't parse json to get list of interesting places");
            throw new RuntimeException(e);
        }

        List<PojoProperties> listOfPlaces = futuresList.getFeatures();

        return listOfPlaces;
    }
}
