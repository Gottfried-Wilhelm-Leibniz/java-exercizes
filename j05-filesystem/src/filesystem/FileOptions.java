package filesystem;

import java.nio.ByteBuffer;

public class FileOptions {
    private final SaveFile m_save;

    public FileOptions(SaveFile save) {
        m_save = save;
    }

    public void saveToDisc(ByteBuffer fileBuffer, String fileName, int size) {
        m_save.saveIt(fileBuffer, fileName, size);
    }
}
