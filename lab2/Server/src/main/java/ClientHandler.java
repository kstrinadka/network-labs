import java.io.*;
import java.net.Socket;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ClientHandler implements Runnable{


    private final Socket socket;

    private FileContext fileContext;


    public ClientHandler(Socket socket) {
        this.socket = socket;
    }


    private FileContext getFileContextFromClient(InputStream inputStream) throws IOException, ClassNotFoundException {

        ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
        FileContext fileContext = (FileContext) objectInputStream.readObject();

        System.out.println("fileName = " + fileContext.getFileName());
        System.out.println("fileSize = " + fileContext.getFileSizeInBytes() + " bytes");

        return fileContext;
    }


    private void receiveFile(String fileName, Long fileSizeInBytes, InputStream inputStream) throws IOException {

        Path filePath = Paths.get(fileName);
        DataInputStream dataInputStream = new DataInputStream(inputStream);
        int bytes = 0;
        long size = fileSizeInBytes;

        File file = filePath.toFile();
        file.getParentFile().mkdirs();
        file.createNewFile();
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        byte[] buffer = new byte[4*1024];

        while (size > 0 && (bytes = dataInputStream.read(buffer, 0,
                                       (int)Math.min(buffer.length, size))) != -1) {
            fileOutputStream.write(buffer,0,bytes);
            size -= bytes;      // read upto file size
        }

        fileOutputStream.close();
    }


    @Override
    public void run() {


        try {
            InputStream inputStream = socket.getInputStream();
            this.fileContext = getFileContextFromClient(inputStream);

            receiveFile("./Server/uploads/" + fileContext.getFileName(), fileContext.getFileSizeInBytes(), inputStream);

        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }


    }
}
