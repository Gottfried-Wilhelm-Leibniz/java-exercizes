package newtvm.program;

import newtvm.codeword.CodeWord;

import java.util.Iterator;

public interface Program extends Iterator<CodeWord> {
    void add(CodeWord codeWord);

    void setIndex(int idx);

    int getIndex();

}
