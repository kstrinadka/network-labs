import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.Socket;

public class ClientHandler implements Runnable{



    private final Socket socket;


    public ClientHandler(Socket socket) {
        this.socket = socket;
    }



    /**
     * Клиенту передаётся в параметрах относительный или абсолютный путь к файлу,
     * который нужно отправить. Длина имени файла не превышает 4096 байт в кодировке UTF-8.
     * Размер файла не более 1 терабайта.
     * @param fileName
     */
    private void checkFileName(String fileName) {

    }

    private FileContext getFileContextFromClient() throws IOException, ClassNotFoundException {

        FileContext fileContext = null;

        // get the input stream from the connected socket
        InputStream inputStream = socket.getInputStream();
        // create a DataInputStream so we can read data from it.
        ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);

        fileContext = (FileContext) objectInputStream.readObject();
        System.out.println("fileName = " + fileContext.getFileName());
        System.out.println("fileSize = " + fileContext.getFileSizeInBytes() + " bytes");

        System.out.println("Closing sockets.");
        //socket.close();

        return fileContext;
    }


    @Override
    public void run() {


        try {
            FileContext fileContext = getFileContextFromClient();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }


    }
}
