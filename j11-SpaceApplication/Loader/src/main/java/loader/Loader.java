package loader;
import java.nio.file.Path;

public interface Loader {
    byte[] load(Path path);
}
