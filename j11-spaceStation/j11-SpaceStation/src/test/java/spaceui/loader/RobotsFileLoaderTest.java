package spaceui.loader;
import station.robot.Robot;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;

class RobotsFileLoaderTest {
    @org.junit.jupiter.api.Test
    void load() throws IOException {
        Path path = Path.of("try");
        Files.deleteIfExists(path);
        Files.createFile(path);
        var strFile = """
               Hal9000:haly:h23
               Maschinemensch:machi:m15
               Johnny5:jhonny:j34
               Tachikomas:taki:tt3"
                """;
        var charset = Charset.defaultCharset();
        var byteBuff = ByteBuffer.wrap(strFile.getBytes(charset));
        Files.write(path, byteBuff.array());
        Loader<Robot> loader;
        loader = new RobotsFileLoader(path);
        var loadedList = loader.load();
        Files.deleteIfExists(path);
    }
}