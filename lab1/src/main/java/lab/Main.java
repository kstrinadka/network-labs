package lab;


import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Обнаружение копий себя в локальной сети
 *
 * Разработать приложение, обнаруживающее копии себя в локальной сети с помощью обмена multicast UDP сообщениями.
 * Приложение должно отслеживать моменты появления и исчезновения других копий себя в локальной сети и
 * при изменениях выводить список IP адресов "живых" копий.
 *
 * Адрес multicast-группы необходимо передавать параметром в приложение.
 * Приложение должно поддерживать работу как в IPv4 так и в IPv6 сетях,
 * выбирая протокол автоматически в зависимости от переданного адреса группы.
 */

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    private static final Logger logger = Logger.getLogger(lab.Main.class.getName());

    private static final int DEFAULT_PORT = 1333;
    private static final String DEFAULT_IP = "225.0.0.0";

    public static void main(String[] args) throws IOException {

        if (args.length == 0){
            logger.log(Level.SEVERE, "Wrong arguments amount");
            System.exit(2);
        }

        int port = DEFAULT_PORT;

        try {
            if (args.length >= 2) {
                port = Integer.parseInt(args[1], 10);
            }
        }
        catch (NumberFormatException ex){
            logger.warning("Wrong port format");
            logger.info("Port has default value = " + DEFAULT_PORT);
        }

        String multicastIP = args[0];
        InetAddress address = InetAddress.getByName(multicastIP);

        if (!address.isMulticastAddress()){
            logger.log(Level.SEVERE, "IP = " + multicastIP + " is from multicast range");
            System.exit(1);
        }


        Map<String, Boolean> hostsMap = Collections.synchronizedMap(new HashMap<String, Boolean>());
        CopiesCounter application = new CopiesCounter(hostsMap, InetAddress.getByName(multicastIP), port);

        application.run();

    }

}

