package spaceui;
import loader.FileLoader;
import loader.Loader;
import parser.Parser;
import spaceui.generalactions.UniverseCosmicAction;
import spaceui.input.Input;
import spaceui.input.UserInput;
import spaceui.output.Printer;
import spaceui.output.SoutPrinter;
import station.SpaceStation;
import station.Station;
import station.fleet.RobotsFleet;
import station.robot.Robot;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;

public class Application {
    public static void main(String[] args) throws IOException {
        //practise
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
        // end of practise

        var robotsFleet = new RobotsFleet();
        Station<Robot> spaceStation = new SpaceStation(robotsFleet);
        Printer printer = new SoutPrinter();
        Input input = new UserInput();

        if(args.length > 0) {
            loadFromFile(args[0], printer, spaceStation);
        }
        else {
            loadFromUser(printer, input, spaceStation);
        }
        new Thread(new UniverseCosmicAction(robotsFleet)).start();
        var stationUi = new StationUi(spaceStation, printer, input);
        stationUi.go();
    }

    private static void loadFromUser(Printer printer, Input input, Station<Robot> spaceStation) {

    }

    private static void loadFromFile(String arg, Printer printer, Station<Robot> spaceStation) {
        Loader loader = new FileLoader();
        var bytes = loader.load(Path.of(arg));
        var parser = new Parser();
        var str = parser.bytesToString(bytes, Charset.defaultCharset());
        var robotsStrings = parser.stringSeparator(str, "\n");
        for(var r : robotsStrings) {
            System.out.println(r);
            var robotsDetails = parser.stringSeparator(r, ":");
            printer.print(spaceStation.createNew(robotsDetails[0], robotsDetails[1], robotsDetails[2]).reason());
        }
    }
}
