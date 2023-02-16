package station;
import com.google.gson.Gson;
import station.fleet.Fleet;
import station.robot.Robot;

import java.util.Map;

public class ParseCollectionsToStrMenu {
    public<T> String iterableToJson(Iterable<T> iterable) {
        var sb = new StringBuilder();
        var gson = new Gson();
        var i = 1;
        for(var element : iterable) {
            sb.append(i + ") ");
            sb.append(gson.toJson(element));
            sb.append("\n");
            i++;
        }
        return sb.toString();
    }

    public <T,S> String keys(Map<T, S> map) {
        var sb = new StringBuilder();
        var i = 1;
        for(var key : map.keySet()) {
            sb.append(i + ") ");
            sb.append(key);
            sb.append("\n");
            i++;
        }
        return sb.toString();
    }
}
