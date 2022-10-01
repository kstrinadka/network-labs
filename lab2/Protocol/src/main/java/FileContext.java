import java.io.Serializable;

public class FileContext implements Serializable {

    private final String fileName;
    private final Long fileSizeInBytes;

    public FileContext(String fileName, Long fileSizeInBytes) {
        this.fileName = fileName;
        this.fileSizeInBytes = fileSizeInBytes;
    }

    public String getFileName() {
        return fileName;
    }

    public Long getFileSizeInBytes() {
        return fileSizeInBytes;
    }
}
