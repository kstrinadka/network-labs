package lab;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.Scanner;

public class CopiesCounter implements Runnable{


    Map<String, Boolean> hostsMap;
    private InetAddress address;
    private int port;

    public CopiesCounter(Map<String, Boolean> hostsMap) throws UnknownHostException {
        this.hostsMap = hostsMap;
        address = InetAddress.getByName("225.0.0.0");
        port = 6060;
    }

    public CopiesCounter(Map<String, Boolean> hostsMap, InetAddress address, int port) {
        this.hostsMap = hostsMap;
        this.address = address;
        this.port = port;
    }

    @Override
    public void run() {

        CopiesChecker copiesChecker = new CopiesChecker(hostsMap, address, port);
        MulticastPublisher publisher = new MulticastPublisher(hostsMap, address, port);
        MulticastReceiver receiver = new MulticastReceiver(hostsMap, address, port);


        Thread receiverThred = new Thread(receiver);
        Thread publisherThred = new Thread(publisher);
        Thread copiesCheckerThred = new Thread(copiesChecker);

        publisherThred.start();
        receiverThred.start();
        copiesCheckerThred.start();

        while (true) {
            Scanner in = new Scanner(System.in);

            if(in.nextLine().equals("off")) {
                break;
            }
        }

        publisher.stop();
        receiver.stop();
        copiesChecker.stop();


        try {
            publisherThred.join();
            receiverThred.join();
            copiesCheckerThred.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
