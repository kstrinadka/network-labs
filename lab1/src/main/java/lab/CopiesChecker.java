package lab;


import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;

/**
 * Проверяет копии программ в сети. Если давно не было сообщений от одного из хостов, то удаляет его
 */
public class CopiesChecker implements Runnable{


    Map<String, Boolean> hostsMap;
    private InetAddress address;
    private int port;
    boolean isAlive = true;


    public CopiesChecker(Map<String, Boolean> hostsMap) throws UnknownHostException {
        this.hostsMap = hostsMap;
        address = InetAddress.getByName("225.0.0.0");
        port = 6060;
    }

    public CopiesChecker(Map<String, Boolean> hostsMap, InetAddress address, int port) {
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
     * 1) Пометить всех хостов как мертвых
     *
     * 2) Подождать какое-то время за которое от хостов поприходят сообщения. Если пришло сообщение,
     * то пометить хоста как живого
     *
     * 3) Удалить всех мертвых хостов
     */
    public void deleteDeadHosts() throws InterruptedException {

        while (isAlive) {

            synchronized(hostsMap){
                //System.out.println("List of alive hosts: ");
                for(String host : hostsMap.keySet()){
                    hostsMap.put(host, false);
                }
            }

            Thread.sleep(3000);

            synchronized(hostsMap){
                for(String host : hostsMap.keySet()){
                    if (!hostsMap.get(host)) {
                        System.out.println("host: " + host + " was deleted");
                        hostsMap.remove(host);
                        showAliveHosts();
                        break;
                    }
                }
            }

        }

    }


    @Override
    public void run() {
        try {
            deleteDeadHosts();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void stop () {
        this.isAlive = false;
    }
}
