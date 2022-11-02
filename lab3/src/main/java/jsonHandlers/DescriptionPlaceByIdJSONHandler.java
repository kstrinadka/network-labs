package jsonHandlers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import pojos.description.PojoDescription;

public class DescriptionPlaceByIdJSONHandler {

    public static PojoDescription getDescriptionFromJSON(String jsonString) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        PojoDescription pojoDescription  = null;
        try {
            pojoDescription = objectMapper.readValue(jsonString, PojoDescription.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return pojoDescription;
    }
}
