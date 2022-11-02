package json_testing;

import java.net.URISyntaxException;
import java.util.Scanner;
import client.Client;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.json.simple.parser.ParseException;


public class TestParser {



    public static void main(String[] args) throws URISyntaxException, InterruptedException, JsonProcessingException, ParseException {


        Client client = new Client();
        client.run();


    }



}
