package com.golov.springspace.application;
import com.golov.springspace.application.generalactions.UniverseCosmicAction;
import input.Input;
import input.UserInput;
import loader.*;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import output.Printer;
import output.SoutPrinter;
import parser.Parser;
import com.golov.springspace.infra.Robot;
import java.nio.charset.Charset;
import java.nio.file.Path;
import com.golov.springspace.station.*;
import com.golov.springspace.station.fleet.*;
import com.golov.springspace.ui.StationUi;

public class Application {
    public static void main(String[] args) {
        var ctx = new AnnotationConfigApplicationContext(AppConfiguration.class);
        Station<Robot> spaceStation = new SpaceStation(ctx);
        Printer printer = ctx.getBean("printer", Printer.class);
        Input input = new UserInput();

        if(args.length > 0) {
            loadFromFile(args[0], printer, spaceStation);
        }
        new Thread(new UniverseCosmicAction(ctx.getBean("robotsFleet", RobotsFleet.class), printer)).start();
//        ctx.getBean("rununi", UniverseCosmicAction.class).run();
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
            printer.print(spaceStation.createNew(robotsDetails[0]).reason());
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