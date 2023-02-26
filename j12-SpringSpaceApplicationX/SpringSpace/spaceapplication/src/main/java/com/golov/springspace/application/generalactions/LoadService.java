package com.golov.springspace.application.generalactions;
import com.golov.springspace.station.StationService;
import loader.Loader;
import org.springframework.beans.factory.annotation.Autowired;
import output.Printer;
import parser.Parser;
import java.nio.charset.Charset;
import java.nio.file.Path;

public class LoadService implements Runnable {
    @Autowired
    private Parser parser;
    @Autowired
    private Loader loader;
    @Autowired
    private Printer printer;
    private final String arg;
    @Autowired
    StationService stationService;

    public LoadService(String arg) {
        this.arg = arg;
    }

    @Override
    public void run() {
        byte[] bytes;
        try {
            bytes = loader.load(Path.of(arg));
        } catch (RuntimeException e) {
            printer.print("No such file");
            return;
        }
        var str = parser.bytesToString(bytes, Charset.defaultCharset());
        var robotsStrings = parser.stringSeparator(str, "\n");
        for(var r : robotsStrings) {
            var robotsDetails = parser.stringSeparator(r, ":");
            var reply = stationService.createNew(robotsDetails[0], robotsDetails[1], robotsDetails[2]);
            printer.print(reply.reason());
        }
    }
}