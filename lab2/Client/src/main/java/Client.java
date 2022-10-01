import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;

public class Client implements Runnable{

    private final String fileName;

    private final Path filePath;

    private final InetAddress serverAddress;

    private final int serverPort;




    /**
     * @param fileName - Клиенту передаётся в параметрах относительный или абсолютный путь к файлу,
     *                 который нужно отправить. Длина имени файла не превышает 4096 байт в кодировке UTF-8.
     *                 Размер файла не более 1 терабайта.
     * @param serverAddress - Клиенту также передаётся в параметрах DNS-имя (или IP-адрес) и номер порта сервера.
     * @param serverPort - номер порта сервера
     */
    public Client(String fileName, InetAddress serverAddress, int serverPort) {
        this.fileName = fileName;
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
        this.filePath = Path.of(fileName);
    }


    /**
     * Типо шаблон для чтения файла и узнавания его размера
     */
    public void readWholeFile() {



        String content = null;
        try {
            content = Files.readString(filePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {

            // size of a file (in bytes)
            long bytes = Files.size(filePath);
            System.out.println(String.format("%,d bytes", bytes));
            System.out.println(String.format("%,d kilobytes", bytes / 1024));

        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(content);
    }


    public void sendFileContextToServer (FileContext fileContext) throws IOException {
        // need host and port, we want to connect to the ServerSocket at port 7777
        Socket socket = new Socket("localhost", serverPort);
        System.out.println("Connected!");

        // get the output stream from the socket.
        OutputStream outputStream = socket.getOutputStream();
        // create an object output stream from the output stream so we can send an object through it
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);

        System.out.println("Sending messages to the ServerSocket");
        objectOutputStream.writeObject(fileContext);

        // не надо закрывать сокет пока файл не передался, но это тестовая хуйня
        //socket.close();
    }


    private Long getFileSizeInBytes () {

        Long fileSize = null;

        try {
            fileSize = Files.size(filePath);
            System.out.println(String.format("%,d bytes", fileSize));
            System.out.println(String.format("%,d kilobytes", fileSize / 1024));

        } catch (IOException e) {
            e.printStackTrace();
        }

        return fileSize;
    }

    @Override
    public void run() {

        Long fileSize = getFileSizeInBytes();
        FileContext fileContext = new FileContext(fileName, fileSize);


        try {
            sendFileContextToServer(fileContext);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }



    }
}
