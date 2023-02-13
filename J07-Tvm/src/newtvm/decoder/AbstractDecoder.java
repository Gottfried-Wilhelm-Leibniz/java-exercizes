package newtvm.decoder;

import newtvm.codeword.CodeWord;
import newtvm.program.Program;
import newtvm.program.TinyProgram;

import java.nio.IntBuffer;

public abstract class AbstractDecoder implements Decoder {
    @Override
    public Program decode(IntBuffer buffer) {
        buffer.rewind();
        Program prog = new TinyProgram();
        while (buffer.hasRemaining()) {
            var word = buffer.get();
            var codeWord = translate(word);
            prog.add(codeWord);
        }
        return prog;
    }

    protected abstract CodeWord translate(int word);
}
