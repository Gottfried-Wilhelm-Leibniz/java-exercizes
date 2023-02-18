package spaceui;
import loader.FileLoader;
import loader.Loader;
import station.SpaceStation;
import station.Station;
import station.fleet.Fleet;
import station.fleet.RobotsFleet;
import station.robot.Robot;
import spaceui.generalactions.UniverseCosmicAction;
//import spaceui.loader.RobotsFileLoader;
//import spaceui.loader.Loader;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;

public class Application {
    public static void main(String[] args) throws IOException {
        Loader loader;
//        Path path = Path.of("try");
//        Files.deleteIfExists(path);
//        Files.createFile(path);
//        var strFile = """
//               Hal9000:haly:h23
//               Maschinemensch:machi:m15
//               Johnny5:jhonny:j34
//               Tachikomas:taki:tt3"
//                """;
//        var charset = Charset.defaultCharset();
//        var byteBuff = ByteBuffer.wrap(strFile.getBytes(charset));
//        Files.write(path, byteBuff.array());
//        Loader<Robot> loader;
        if(args.length == 0) {
            System.exit(0);
//            loader = new RobotsFileLoader(path);
//            loader = new InputLoader<>();
        }
        else {
            loader = new FileLoader();
            loader.load(Path.of(args[0]));

//            loader = new RobotsFileLoader(Path.of(args[0]));
        }
        var initialList = loader.load();
        Fleet<Robot> robotsFleet = new RobotsFleet(initialList);
        new Thread(new UniverseCosmicAction(robotsFleet)).start();
        Station<Robot> spaceStation = new SpaceStation(robotsFleet);
        var stationUi = new StationUi(spaceStation);
        stationUi.go();
    }
}
