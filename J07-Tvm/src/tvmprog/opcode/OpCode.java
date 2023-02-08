package tvmprog.opcode;

import java.nio.file.Path;

public interface OpCode {
    int getNextInstructions();
    boolean hasNext();
    void setIndex(int idx);
    void load(Path path);

    int getIndex();
}
