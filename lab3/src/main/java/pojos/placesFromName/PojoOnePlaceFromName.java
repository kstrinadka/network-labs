package pojos.placesFromName;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown=true)
public class PojoOnePlaceFromName {

    @JsonProperty("point")
    private PojoPoint point;

    @JsonProperty("name")
    private String name;

    @JsonProperty("country")
    private String country;
}
