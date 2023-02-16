package ui;
import station.SpaceStation;
import station.Station;
import station.fleet.Fleet;
import station.fleet.RobotsFleet;
import station.robot.Robot;
import ui.generalactions.UniverseCosmicAction;
import ui.loader.RobotsFileLoader;
import ui.loader.InputLoader;
import ui.loader.Loader;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;

public class Application {
    public static void main(String[] args) throws IOException {
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
        if(args.length == 0) {
            loader = new RobotsFileLoader(path);
//            loader = new InputLoader<>();
        }
        else {
            loader = new RobotsFileLoader(Path.of(args[0]));
        }
        var initialList = loader.load();
        Fleet<Robot> robotsFleet = new RobotsFleet(initialList);
        Station<Robot> spaceStation = new SpaceStation(robotsFleet);
        new Thread(new UniverseCosmicAction(robotsFleet)).start();
        var stationUi = new StationUi(spaceStation);
        stationUi.go();
    }
}