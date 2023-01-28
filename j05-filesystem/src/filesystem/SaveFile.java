package filesystem;

import java.nio.ByteBuffer;

@FunctionalInterface
public interface SaveFile {
    void saveIt (ByteBuffer b, String name, int size);
}
