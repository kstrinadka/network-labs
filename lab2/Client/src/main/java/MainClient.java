import java.net.InetAddress;
import java.net.UnknownHostException;

public class MainClient {




    public static void main(String[] args) {

        InetAddress address = null;
        try {
            address = InetAddress.getByName("localhost");
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }

        String fileName = "Client/text";
        int port = 6060;

        Client client = new Client(fileName, address, port);

        client.readWholeFile();

        client.run();
    }

}
