import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;

public class Client implements Runnable{

    private final String fileName;

    private final Path filePath;

    private final InetAddress serverAddress;

    private final int serverPort;

    private final Socket clientSocket;




    /**
     * @param filePath - Клиенту передаётся в параметрах относительный или абсолютный путь к файлу,
     *                 который нужно отправить. Длина имени файла не превышает 4096 байт в кодировке UTF-8.
     *                 Размер файла не более 1 терабайта.
     * @param serverAddress - Клиенту также передаётся в параметрах DNS-имя (или IP-адрес) и номер порта сервера.
     * @param serverPort - номер порта сервера
     */
    public Client(Path filePath, InetAddress serverAddress, int serverPort) throws IOException {
        this.filePath = filePath;
        this.fileName = filePath.getFileName().toString();
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
        //this.filePath = Path.of(fileName);
        this.clientSocket = new Socket("localhost", serverPort);
    }

    private static void sendFile(Path path, OutputStream outputStream) throws IOException {

        DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
        int bytes = 0;
        File file = path.toFile();
        FileInputStream fileInputStream = new FileInputStream(file);

        /*// send file size
        dataOutputStream.writeLong(file.length());*/

        // break file into chunks
        byte[] buffer = new byte[4*1024];
        while ((bytes=fileInputStream.read(buffer))!=-1){
            dataOutputStream.write(buffer,0,bytes);
            dataOutputStream.flush();
        }
        fileInputStream.close();
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


    public void sendFileContextToServer (FileContext fileContext, OutputStream outputStream) throws IOException {
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        System.out.println("Sending fileContext to the ServerSocket");
        objectOutputStream.writeObject(fileContext);
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

        System.out.println("Connected to Server!");

        Long fileSize = getFileSizeInBytes();
        FileContext fileContext = new FileContext(fileName, fileSize);

        try {
            OutputStream outputStream = clientSocket.getOutputStream();
            sendFileContextToServer(fileContext, outputStream);

            sendFile(filePath, outputStream);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }
}
