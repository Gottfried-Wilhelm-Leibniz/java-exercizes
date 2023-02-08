package tvmprog.opcode;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;

public class ByteBufferOpCode implements OpCode{
    private final int INTSIZE = 4;
    private ByteBuffer buff;
    @Override
    public int getNextInstructions() {
        return buff.getInt();
    }

    @Override
    public boolean hasNext() {
        return buff.position() + INTSIZE <= buff.limit();
    }

    @Override
    public void setIndex(int idx) {
        buff.position(idx * INTSIZE);
    }

    @Override
    public void load(Path path) {
        byte[] bytes;
        try {
            bytes = Files.readAllBytes(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        buff = ByteBuffer.wrap(bytes);
    }

    @Override
    public int getIndex() {
        return buff.position() / INTSIZE;
    }

}
