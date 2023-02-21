import input.*;
import loader.*;
import output.*;
import parser.Parser;
import spaceui.StationUi;
import station.SpaceStation;
import robot.Robot;
import java.nio.charset.Charset;
import java.nio.file.Path;
import generalactions.UniverseCosmicAction;
import station.*;
import station.fleet.Fleet;
import station.fleet.RobotsFleet;

public class Application {
    public static void main(String[] args) {
        Fleet<Robot> robotsFleet = new RobotsFleet();
        Station<Robot> spaceStation = new SpaceStation(robotsFleet);
        Printer printer = new SoutPrinter();
        Input input = new UserInput();

        if(args.length > 0) {
            loadFromFile(args[0], printer, spaceStation);
        }
        new Thread(new UniverseCosmicAction(robotsFleet, printer)).start();
        var stationUi = new StationUi(spaceStation, printer, input);
        stationUi.go();
    }

    private static void loadFromFile(String arg, Printer printer, Station<Robot> spaceStation) {
        Loader loader = new FileLoader();
        var bytes = loader.load(Path.of(arg));
        var parser = new Parser();
        var str = parser.bytesToString(bytes, Charset.defaultCharset());
        var robotsStrings = parser.stringSeparator(str, "\n");
        for(var r : robotsStrings) {
            var robotsDetails = parser.stringSeparator(r, ":");
            printer.print(spaceStation.createNew(robotsDetails[0], robotsDetails[1], robotsDetails[2]).reason());
        }
    }
}


//practise
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
// end of practise