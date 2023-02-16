package ui.loader;
import com.google.gson.InstanceCreator;
import station.robot.Robot;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ListRobotsCreator implements InstanceCreator<List<Robot>> {
    @Override
    public List<Robot> createInstance(Type type) {
        return new ArrayList<>();
    }
}
