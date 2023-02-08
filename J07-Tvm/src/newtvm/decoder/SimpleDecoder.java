package newtvm.decoder;

import newtvm.codeword.CodeWord;
import newtvm.codeword.DataWord;
import newtvm.codeword.Instruction;
import newtvm.decoder.AbstractDecoder;

import java.util.Map;

public class SimpleDecoder extends AbstractDecoder {
    private final Map<Integer, Instruction> map;
    public SimpleDecoder(Map<Integer, Instruction> map) {
        this.map = map;
    }

    protected CodeWord translate(int word) {
        CodeWord codeWord = map.get(word);
        if (codeWord == null) {
            codeWord = new DataWord(word);
        }
        return codeWord;
    }
}
