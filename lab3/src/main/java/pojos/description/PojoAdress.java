package pojos.description;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown=true)
public class PojoAdress {

    @JsonProperty("city")
    private String city;

    @JsonProperty("road")
    private String road;

    @JsonProperty("state")
    private String state;

    @JsonProperty("county")
    private String county;

    @JsonProperty("suburb")
    private String suburb;

    @JsonProperty("country")
    private String country;

    @JsonProperty("postcode")
    private String postcode;

    @JsonProperty("country_code")
    private String country_code;

    @JsonProperty("city_district")
    private String city_district;

    @JsonProperty("neighbourhood")
    private String neighbourhood;

    @Override
    public String toString() {
        return "PojoAdress{" + "\n" +
                "city='" + city + '\'' + "\n" +
                ", road='" + road + '\'' + "\n" +
                ", state='" + state + '\'' + "\n" +
                ", county='" + county + '\'' + "\n" +
                ", suburb='" + suburb + '\'' + "\n" +
                ", country='" + country + '\'' + "\n" +
                ", postcode='" + postcode + '\'' + "\n" +
                ", country_code='" + country_code + '\'' + "\n" +
                ", city_district='" + city_district + '\'' + "\n" +
                ", neighbourhood='" + neighbourhood + '\'' + "\n" +
                '}';
    }
}

