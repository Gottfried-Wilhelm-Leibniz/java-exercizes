package newtvm.decoder;

import newtvm.program.Program;

import java.nio.IntBuffer;

public interface Decoder {
    Program decode(IntBuffer buffer);
}
