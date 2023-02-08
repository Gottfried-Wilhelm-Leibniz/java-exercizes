package newtvm;
import newtvm.stack.ArrayListStack;
import newtvm.stack.Stack;
import newtvm.vm.*;
import newtvm.codeword.Instruction;
import newtvm.program.Program;
import newtvm.decoder.SimpleDecoder;
import newtvm.loader.FileLoader;
import newtvm.loader.Loader;
import newtvm.printer.Printer;
import newtvm.printer.SoutPrinter;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws IOException {
        var filesList = new ArrayList<Path>(3);
        // first prog
        var file = Path.of("kol");
        Files.deleteIfExists(file);
        Files.createFile(file);
        var buff = ByteBuffer.allocate(4 * 13);
        buff.putInt(105);
        buff.putInt(12);
        buff.putInt(105);
        buff.putInt(8);
        buff.putInt(106);
        buff.putInt(112);
        buff.putInt(112);
        buff.putInt(102);
        buff.putInt(101);
        buff.putInt(106);
        buff.putInt(108);
        buff.putInt(109);
        buff.putInt(111);
        Files.write(file, buff.array());
        filesList.add(file);
        // sec prog
        var secfile = Path.of("kolo");
        Files.deleteIfExists(secfile);
        Files.createFile(secfile);
        buff = ByteBuffer.allocate(4 * 12);
        buff.putInt(105);
        buff.putInt(64);
        buff.putInt(112);
        buff.putInt(106);
        buff.putInt(109);
        buff.putInt(106);
        buff.putInt(105);
        buff.putInt(70);
        buff.putInt(101);
        buff.putInt(116);
        buff.putInt(2);
        buff.putInt(111);
        Files.write(secfile, buff.array());
        filesList.add(secfile);
        // third prog
        var thirdfile = Path.of("kololo");
        Files.deleteIfExists(thirdfile);
        Files.createFile(thirdfile);
        buff = ByteBuffer.allocate(4 * 14);
        buff.putInt(105);
        buff.putInt(64);
        buff.putInt(117);
        buff.putInt(9);
        buff.putInt(110);
        buff.putInt(117);
        buff.putInt(9);
        buff.putInt(110);
        buff.putInt(111);
        buff.putInt(110);
        buff.putInt(112);
        buff.putInt(106);
        buff.putInt(109);
        buff.putInt(118);
        Files.write(thirdfile, buff.array());
        filesList.add(thirdfile);
        // exec
        // for all
        Printer printer = new SoutPrinter();
        Stack stack = new ArrayListStack(1024);
        Stack callStack = new ArrayListStack(128);
        Vm tvm = new Tvm(stack, callStack, printer);
        var decoder = new SimpleDecoder(Instruction.getInstructionMap());

        Loader fileLoader = new FileLoader(file);
        var buffer = fileLoader.readAll();
        Program program = decoder.decode(buffer);
        tvm.run(program);
    }
}
