import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server implements Runnable{

    private final int port;

    private final int clientsQueueSize;

    private static final Logger logger = Logger.getLogger(Server.class.getName());

    //private final String fileName;

    //private final Path filePath;


    public Server(int port) {
        this.port = port;
        this.clientsQueueSize = 5;
    }

    /**
     * Клиенту передаётся в параметрах относительный или абсолютный путь к файлу,
     * который нужно отправить. Длина имени файла не превышает 4096 байт в кодировке UTF-8.
     * Размер файла не более 1 терабайта.
     * @param fileName
     */
    private void checkFileName(String fileName) {

    }


    @Override
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(this.port, this.clientsQueueSize)){
            logger.info("Server start on port - " + this.port);
            while (true){
                System.out.println("ServerSocket awaiting connections...");
                Socket socket = serverSocket.accept();
                logger.info("Client connected - " + socket.getInetAddress() + ":" + socket.getPort());

                Thread clientHandler = new Thread(new ClientHandler(socket));
                clientHandler.start();
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Cant open server socket on port: " + this.port);
        }
    }
}
