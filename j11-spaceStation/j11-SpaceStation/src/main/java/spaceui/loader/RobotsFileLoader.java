package spaceui.loader;
import station.robot.Robot;
import station.robot.robotfactory.RobotFactory;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class RobotsFileLoader implements Loader<Robot> {
    private final Path filePath;
    private static final Charset charset = Charset.defaultCharset();
    public RobotsFileLoader(Path filePath) {
        this.filePath = filePath;
    }

    @Override
    public List<Robot> load() {
        byte[] bytes;
        try {
            bytes = Files.readAllBytes(filePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        var byteBuff = ByteBuffer.wrap(bytes);
        var fileStr = charset.decode(byteBuff).toString();
        var strs = fileStr.split("\n");
        List<Robot> robots = new ArrayList<>();
        var factory = new RobotFactory();
        for(var robotstr : strs) {
            var parts = robotstr.split(":");
            robots.add(factory.create(parts[0], parts[1], parts[2]));
        }
        return robots;
    }
}


//        GsonBuilder gsonBuilder = new GsonBuilder();
//        gsonBuilder.registerTypeAdapter(Robot.class, new ListRobotsCreator());
//        Gson gson = gsonBuilder.create();
//        Type listType = new TypeToken<List<T>>(){}.getType();
//        List<StandardRobot> listis = new Gson().fromJson(jsonList, StandardRobot.class);
