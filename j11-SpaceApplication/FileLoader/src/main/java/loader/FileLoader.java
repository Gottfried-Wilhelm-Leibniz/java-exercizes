package loader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileLoader implements Loader{
    @Override
    public byte[] load(Path path) {
        byte[] bytes;
        try {
            bytes = Files.readAllBytes(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return bytes;
    }
}
