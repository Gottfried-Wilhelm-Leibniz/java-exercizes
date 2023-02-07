package tvmprog.tvm.opcode;
public interface OpCode {
    Word getNextInstructions();
    boolean hasNext();
    void setStartIdx(int idx);
}
