package newtvm.loader;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileLoader implements Loader {
    private final Path path;

    public FileLoader(Path path) {
        this.path = path;
    }

    @Override
    public IntBuffer readAll() {
        byte[] bytes;
        try {
            bytes = Files.readAllBytes(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return ByteBuffer.wrap(bytes).asIntBuffer(); // todo match the capasity of stack
    }
}
