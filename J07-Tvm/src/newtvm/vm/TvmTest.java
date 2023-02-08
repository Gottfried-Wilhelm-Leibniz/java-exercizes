//package tvmprog.vm;
//import org.junit.jupiter.api.Test;
//import tvmprog.opcode.ByteBufferOpCode;
//import tvmprog.printer.SoutPrinter;
//import tvmprog.stack.ArrayListStack;
//import java.io.IOException;
//import java.nio.ByteBuffer;
//import java.nio.file.Files;
//import java.nio.file.Path;
//
//class TvmTest {
//    @Test
//    void firstTest() throws IOException {
//        var file = Path.of("kol");
//        Files.deleteIfExists(file);
//        Files.createFile(file);
//        var buff = ByteBuffer.allocate(4 * 13);
//        buff.putInt(5);
//        buff.putInt(12);
//        buff.putInt(5);
//        buff.putInt(8);
//        buff.putInt(6);
//        buff.putInt(12);
//        buff.putInt(12);
//        buff.putInt(2);
//        buff.putInt(1);
//        buff.putInt(6);
//        buff.putInt(8);
//        buff.putInt(9);
//        buff.putInt(11);
//        Files.write(file, buff.array());
////        var tvm = new Tvm(new ArrayListStack(900), new ArrayListStack(100), new ByteBufferOpCode(), new SoutPrinter());
////        tvm.run(file);
//    }
//
//    @Test
//    void secondTest() throws IOException {
//        var file = Path.of("kol");
//        Files.deleteIfExists(file);
//        Files.createFile(file);
//        var buff = ByteBuffer.allocate(4 * 12);
//        buff.putInt(5);
//        buff.putInt(64);
//        buff.putInt(12);
//        buff.putInt(6);
//        buff.putInt(9);
//        buff.putInt(6);
//        buff.putInt(5);
//        buff.putInt(70);
//        buff.putInt(1);
//        buff.putInt(16);
//        buff.putInt(2);
//        buff.putInt(11);
//        Files.write(file, buff.array());
//        var tvm = new Tvm(new ArrayListStack(capacity), new ArrayListStack(capacity), new ByteBufferOpCode(), new SoutPrinter());
//        tvm.run(file);
//    }
//
//    @Test
//    void thirdTest() throws IOException {
//        var file = Path.of("kol");
//        Files.deleteIfExists(file);
//        Files.createFile(file);
//        var buff = ByteBuffer.allocate(4 * 14);
//        buff.putInt(5);
//        buff.putInt(64);
//        buff.putInt(17);
//        buff.putInt(9);
//        buff.putInt(10);
//        buff.putInt(17);
//        buff.putInt(9);
//        buff.putInt(10);
//        buff.putInt(11);
//        buff.putInt(10);
//        buff.putInt(12);
//        buff.putInt(6);
//        buff.putInt(9);
//        buff.putInt(18);
//        Files.write(file, buff.array());
//        var tvm = new Tvm(new ArrayListStack(capacity), new ArrayListStack(capacity), new ByteBufferOpCode(), new SoutPrinter());
//        tvm.run(file);
//    }
//}