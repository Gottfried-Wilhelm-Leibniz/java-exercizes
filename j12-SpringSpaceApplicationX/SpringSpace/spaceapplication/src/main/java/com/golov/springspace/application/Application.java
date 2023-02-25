package com.golov.springspace.application;
import com.golov.springspace.application.generalactions.GeneralActions;
import com.golov.springspace.application.generalactions.UniverseCosmicAction;
import loader.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import output.Printer;
import parser.Parser;
import java.nio.charset.Charset;
import java.nio.file.Path;
import com.golov.springspace.station.*;
import com.golov.springspace.ui.StationUi;

public class Application {
    public static void main(String[] args) {
        var ctx = new AnnotationConfigApplicationContext(AppConfiguration.class);
        if(args.length > 0) {
            loadFromFile(args[0], ctx);
        }
        ctx.getBean("createNew", "hal9000", "aaa", "a"); // todo erase
        ctx.getBean(GeneralActions.class).run();
        ctx.getBean("stationUi", StationUi.class).go();
    }

    private static void loadFromFile(String arg, AnnotationConfigApplicationContext ctx) { //todo class of runnable
        Loader loader = ctx.getBean("fileLoader", FileLoader.class);
        var bytes = loader.load(Path.of(arg));
        var parser =ctx.getBean("parser", Parser.class);
        var str = parser.bytesToString(bytes, Charset.defaultCharset());
        var robotsStrings = parser.stringSeparator(str, "\n");
        var printer = ctx.getBean("printer", Printer.class);
        var spaceStation = ctx.getBean("station", SpaceStation.class);
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