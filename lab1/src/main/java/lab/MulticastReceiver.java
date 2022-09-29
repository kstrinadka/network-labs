package lab;


import java.io.IOException;
import java.net.*;
import java.util.Map;

public class MulticastReceiver implements Runnable {

    protected MulticastSocket multicastSocket = null;
    protected byte[] buf = new byte[256];

    Map<String, Boolean> hostsMap;
    private InetAddress address;
    private int port;

    boolean isAlive = true;


    public MulticastReceiver(Map<String, Boolean> hostsMap) throws UnknownHostException {
        this.hostsMap = hostsMap;
        address = InetAddress.getByName("225.0.0.0");
        port = 6060;
    }

    public MulticastReceiver(Map<String, Boolean> hostsMap, InetAddress address, int port) {
        this.hostsMap = hostsMap;
        this.address = address;
        this.port = port;
    }

    public void showAliveHosts() {
        for(String host : hostsMap.keySet()){
            System.out.println(host + " - is alive host");
        }
    }

    /**
     * Сокету не нужно быть в мультикаст группе для отправки туда датаграммы.
     *
     */
    public void run()  {
        try {
            multicastSocket = new MulticastSocket(port);
            InetAddress mcastaddr = address;
            InetSocketAddress group = new InetSocketAddress(mcastaddr, port);

            NetworkInterface netIf = NetworkInterface.getByInetAddress(mcastaddr);

            multicastSocket.joinGroup(group, netIf);

            while (isAlive) {
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                multicastSocket.receive(packet);
                String received = new String(
                        packet.getData(), 0, packet.getLength());

                String hostName = packet.getAddress().getHostAddress();

                if (!hostsMap.containsKey(hostName)) {
                    System.out.println("added new host: " + hostName);
                    showAliveHosts();
                }
                hostsMap.put(hostName, true);



                if ("end".equals(received)) {
                    break;
                }
            }

            multicastSocket.leaveGroup(group, netIf);
            multicastSocket.close();
        }

        catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void stop () {
        this.isAlive = false;
    }

}
