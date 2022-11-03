package jsonHandlers;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class WeatherFromCoordinateJSONHandler {

    public static String getWeather(String jsonString) {

        Object obj = null;
        try {
            obj = new JSONParser().parse(jsonString);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        JSONObject jo = (JSONObject) obj;
        JSONObject main = (JSONObject) jo.get("main");

        String temperatureInCelsius;

        if (main.get("temp").getClass() == Long.class) {
            Long temperatureInCelsiusLong = (Long) main.get("temp");
            temperatureInCelsius = Long.toString(temperatureInCelsiusLong);
        }
        else {
            double temperatureInCelsiusDouble = (double) main.get("temp");
            temperatureInCelsius = Double.toString(temperatureInCelsiusDouble);
        }

        return temperatureInCelsius;
    }

}
