package tvmprog.tvm.opcode;
import java.util.List;

public class WordsListOpCode implements OpCode{
    private final List<Word> list;
    private int idx;

    public WordsListOpCode(List<Word> list, int startIdx) {
        this.list = list;
    }

//    public WordsListOpCode(BitSet bitSet, int startIdx) {
//
//        }
//    }

    @Override
    public Word getNextInstructions() {
        return list.get(idx++);
    }

    @Override
    public boolean hasNext() {
        return idx < list.size();
    }

    @Override
    public void setStartIdx(int idx) {
        this.idx = idx;
    }
}
