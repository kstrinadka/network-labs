package pojos.placesFromName;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PojoPoint {

    @JsonProperty("lat")
    private String lat;

    @JsonProperty("lng")
    private String lng;
}
