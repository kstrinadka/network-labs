package lab;


import java.io.IOException;
import java.net.*;
import java.util.Map;


/**
 * В следующем примере показано, как опубликовать на многоадресном IP-адресе:
 */
public class MulticastPublisher implements Runnable{


    Map<String, Boolean> hostsMap;
    private InetAddress address;
    private int port;

    boolean isAlive = true;

    private DatagramSocket datagramSocket;
    private InetAddress group;
    private byte[] buf;

    public MulticastPublisher(Map<String, Boolean> hostsMap) throws UnknownHostException {
        this.hostsMap = hostsMap;
        address = InetAddress.getByName("225.0.0.0");
        port = 6060;
    }

    public MulticastPublisher(Map<String, Boolean> hostsMap, InetAddress address, int port) {
        this.hostsMap = hostsMap;
        this.address = address;
        this.port = port;
    }


    @Override
    public void run() {

        String multicastMessage = "hello";
        try {

            datagramSocket = new DatagramSocket();
            group = address;
            buf = multicastMessage.getBytes();

            while (isAlive) {
                DatagramPacket packet
                        = new DatagramPacket(buf, buf.length, group, port);
                datagramSocket.send(packet);

                Thread.sleep(100);
            }

        } catch (SocketException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


    public void stop () {
        this.isAlive = false;
    }
}
