package com.golov.springspace.application;
import com.golov.springspace.application.generalactions.LoadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.golov.springspace.ui.StationUi;
@SpringBootApplication
public class Application implements CommandLineRunner {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
    @Autowired
    private  LoadService loadService;
    @Autowired
    private StationUi stationUi;
    @Override
    public void run(String... args) throws Exception {
        if(args.length > 0) {
            loadService.load(args[0]);
            loadService.run();
        }
//            stationUi.go();
        }
}


//for initial practise
//Path path = Path.of("try");
//        try {
//                Files.deleteIfExists(path);
//                } catch (IOException e) {
//                throw new RuntimeException(e);
//                }
//                try {
//                Files.createFile(path);
//                } catch (IOException e) {
//                throw new RuntimeException(e);
//                }
//                var strFile = """
//               Hal9000:haly:h23
//               Maschinemensch:machi:m15
//               Johnny5:jhonny:j34
//               Tachikomas:taki:tt3"
//                """;
//                var charset = Charset.defaultCharset();
//                var byteBuff = ByteBuffer.wrap(strFile.getBytes(charset));
//                try {
//                Files.write(path, byteBuff.array());
//                } catch (IOException e) {
//                throw new RuntimeException(e);
//                }

//                ctx.getBean("createNew", "hal9000", "aaa", "a");
//                ctx.getBean("createNew", "hal9000", "aaa", "b");
//                ctx.getBean("createNew", "hal9000", "aaa", "c");
// end of practise


//        var ctx = new AnnotationConfigApplicationContext(AppConfiguration.class);
//        if(args.length > 0) {
//            ctx.getBean(LoadService.class).load(args[0]);
//        }
//        ctx.getBean(StationUi.class).go();