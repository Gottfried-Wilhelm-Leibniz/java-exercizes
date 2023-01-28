package filesystem;

import java.nio.ByteBuffer;

@FunctionalInterface
public interface RenameFile {
    void renameIt (String oldName, String newName);
}
