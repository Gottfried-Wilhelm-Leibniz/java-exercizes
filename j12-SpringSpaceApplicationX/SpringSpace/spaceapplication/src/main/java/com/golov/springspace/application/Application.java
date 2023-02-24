package com.golov.springspace.application;
import com.golov.springspace.startkit.robotsmodels.Hal9000;
import com.golov.springspace.startkit.robotsmodels.anotations.Hal9000Tool;
import com.golov.springspace.startkit.toolmodels.LaserCutter;
import loader.*;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import output.Printer;
import parser.Parser;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

import com.golov.springspace.station.*;
import com.golov.springspace.ui.StationUi;

public class Application {
    public static void main(String[] args) throws InvocationTargetException, IllegalAccessException {
        var ctx = new AnnotationConfigApplicationContext(AppConfiguration.class);
        if(args.length > 0) {
            loadFromFile(args[0], ctx);
        }
//        new Thread(new UniverseCosmicAction()).start();
//        ctx.getBean("rununi", UniverseCosmicAction.class).run();
//        System.out.println(ctx.getBean(Station.class).createNew("hal9000", "aaa", "bbbb"));

        ctx.getBean("stationUi", StationUi.class).go();
    }

    private static void loadFromFile(String arg, AnnotationConfigApplicationContext ctx) {
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