package notlab.habr.test;



import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Map;

public class Tester {

    public static void main(String[] args) throws IOException {

        URL url = new URL("https://graphhopper.com/api/1/");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        // For a GET request
        connection.setRequestMethod("GET");


        // Adding Request Parameters (имя параметра + его значение)
        Map<String, String> parameters = new HashMap<>();
        parameters.put("param1", "val");

        connection.setDoOutput(true);
        DataOutputStream out = new DataOutputStream(connection.getOutputStream());
        out.writeBytes(ParameterStringBuilder.getParamsString(parameters));
        out.flush();
        out.close();

    }

    public void geoCoding() throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://graphhopper.com/api/1/geocode?q=berlin&locale=de&key=api_key")
                .get()
                .build();

        Response response = client.newCall(request).execute();
    }

/*

    public void get(){

        // For a POST request
        connection.setRequestMethod("POST");

        // For a HEAD request
        connection.setRequestMethod("HEAD");

        // For a OPTIONS request
        connection.setRequestMethod("OPTIONS");

        // For a PUT request
        connection.setRequestMethod("PUT");

        // For a DELETE request
        connection.setRequestMethod("DELETE");

        // For a TRACE request
        connection.setRequestMethod("TRACE");


        // To store our response
        StringBuilder content;

        // Get the input stream of the connection
        try (BufferedReader input = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            String line;
            content = new StringBuilder();
            while ((line = input.readLine()) != null) {
                // Append each line of the response and separate them
                content.append(line);
                content.append(System.lineSeparator());
            }
        } finally {
            connection.disconnect();
        }

        // Returns the value of the content-type header field
        connection.getContentType();

        // Returns an unmodifiable Map of the header fields
        connection.getHeaderFields();

        // Gets the status code from an HTTP response message
        connection.getResponseCode();

        // Gets the HTTP response message returned along with the response code from a server
        connection.getResponseMessage();

        // Returns the error stream if the connection failed but the server sent useful data nonetheless
        connection.getErrorStream();
        // ...etc

        // Output the content to the console
        System.out.println(content.toString());
    }
*/


    public class ParameterStringBuilder {
        public static String getParamsString(Map<String, String> params)
                throws UnsupportedEncodingException {
            StringBuilder result = new StringBuilder();

            for (Map.Entry<String, String> entry : params.entrySet()) {
                result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
                result.append("=");
                result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
                result.append("&");
            }

            String resultString = result.toString();
            return resultString.length() > 0
                    ? resultString.substring(0, resultString.length() - 1)
                    : resultString;
        }
    }

    /*public static void main(String[] args) throws IOException {


        URL url = new URL("https://www.youtube.com");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("POST");
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("v", "dQw4w9WgXcQ");

        StringBuilder postData = new StringBuilder();
        for (var param : params.entrySet()) {
            if (postData.length() != 0) {
                postData.append('&');
            }
            postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
            postData.append('=');
            postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
        }

        byte[] postDataBytes = postData.toString().getBytes("UTF-8");
        connection.setDoOutput(true);

        try (DataOutputStream writer = new DataOutputStream(connection.getOutputStream())) {
            writer.write(postDataBytes);
            writer.flush();
            writer.close();

            StringBuilder content;

            try (BufferedReader in = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()))) {
                String line;
                content = new StringBuilder();
                while ((line = in.readLine()) != null) {
                    content.append(line);
                    content.append(System.lineSeparator());
                }
            }
            System.out.println(content.toString());
        } finally {
            connection.disconnect();
        }
    }*/

}
