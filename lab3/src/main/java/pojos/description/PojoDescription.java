package pojos.description;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.json.simple.JSONObject;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown=true)
public class PojoDescription {



    @JsonProperty("address")
    private PojoAdress address;

    @JsonProperty("kinds")
    private String kinds;

    @JsonProperty("info")
    private JSONObject info;

    @JsonProperty("name")
    private String name;

    @Override
    public String toString() {
        return "PojoDescription{" + "\n" +
                "info =" + (String) info.get("descr") + "\n" +
                ", kinds='" + kinds + '\'' + "\n" +
                '}';
    }
}
