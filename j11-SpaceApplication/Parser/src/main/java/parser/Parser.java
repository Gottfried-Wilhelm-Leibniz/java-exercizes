package parser;
import com.google.gson.Gson;
//import station.robot.Robot;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

public class Parser {
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

    public<T> String objectToJson(T t) {
        return new Gson().toJson(t);
    }

    public <T,S> String keys(Map<T, S> map) {
        var sb = new StringBuilder();
        var i = 1;
        for(var key : map.keySet()) {
            sb.append(i).append(") ");
            sb.append(key);
            sb.append("\n");
            i++;
        }
        return sb.toString();
    }

    public <T> String listGetters(Iterable<T> iterable, List<Method> mList) throws InvocationTargetException, IllegalAccessException {
        var sb = new StringBuilder();
        var i = 1;
        for(var r : iterable) {
            sb.append(i++).append(") ");
            for(var m : mList) {
                sb.append(m.getName()).append(": ").append(m.invoke(r)).append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}