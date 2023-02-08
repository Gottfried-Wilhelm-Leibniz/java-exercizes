package newtvm.program;

import newtvm.codeword.CodeWord;
import newtvm.program.Program;

import java.util.ArrayList;
import java.util.List;

public class TinyProgram implements Program {
    private final List<CodeWord> progList = new ArrayList<>();
    private int current = 0;

    @Override
    public void add(CodeWord codeWord) {
        progList.add(codeWord);
    }

    @Override
    public void setIndex(int idx) {
        current = idx;
    }

    @Override
    public int getIndex() {
        return current;
    }

    @Override
    public boolean hasNext() {
        return current < progList.size();
    }

    @Override
    public CodeWord next() {
        return progList.get(current++);
    }
}
