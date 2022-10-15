import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class MainClient {

    private static final int DEFAULT_SERVER_PORT = 6060;


    public static void main(String[] args) {

        InetAddress address = null;
        try {
            address = InetAddress.getByName("localhost");
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }

        String fileName = "Client/largefile.iso";
        Path filePath = Paths.get(fileName);
        int port = DEFAULT_SERVER_PORT;


        Client client = null;
        try {
            client = new Client(filePath, address, port);
        } catch (IOException e) {
            System.out.println("error in connecting to server");
            throw new RuntimeException(e);
        }

        client.run();
    }

}
