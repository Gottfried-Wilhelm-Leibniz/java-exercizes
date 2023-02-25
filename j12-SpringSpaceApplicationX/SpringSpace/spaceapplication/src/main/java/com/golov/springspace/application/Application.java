package com.golov.springspace.application;
import com.golov.springspace.application.generalactions.GeneralActions;
import com.golov.springspace.application.generalactions.LoadService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import com.golov.springspace.ui.StationUi;

public class Application {
    public static void main(String[] args) {
        var ctx = new AnnotationConfigApplicationContext(AppConfiguration.class);
        if(args.length > 0) {
            ctx.getBean(LoadService.class, args[0]).run();
        }
        ctx.getBean("createNew", "hal9000", "aaa", "a"); // todo for prac
        ctx.getBean("createNew", "hal9000", "aaa", "b"); // todo for prac
        ctx.getBean("createNew", "hal9000", "aaa", "c"); // todo for prac
        ctx.getBean(GeneralActions.class).run();
        ctx.getBean("stationUi", StationUi.class).go();
    }
}


//practise
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
// end of practise