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
public class PojoDescription {

    @JsonProperty("address")
    private PojoAdress address;

    @JsonProperty("kinds")
    private String kinds;

    @Override
    public String toString() {
        return "PojoDescription{" + "\n" +
                "address=" + address.toString() + "\n" +
                ", kinds='" + kinds + '\'' + "\n" +
                '}';
    }
}
