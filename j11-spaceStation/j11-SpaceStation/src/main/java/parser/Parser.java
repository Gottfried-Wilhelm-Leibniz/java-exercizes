package parser;
import com.google.gson.Gson;
import station.robot.Robot;

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

    public String listSignState(List<Robot> list) {
        var sb = new StringBuilder();
        var i = 1;
        for(var r : list) {
            sb.append(i).append(") ");
            sb.append("Call sign: ").append(r.getSign());
            sb.append(" State: ").append(r.getState());
            sb.append("\n");
            i++;
        }
        return sb.toString();
    }

    public <T> String listMethods(List<T> list, List<Method> mList) {
        var sb = new StringBuilder();
        var i = 1;
        for(var r : list) {
            sb.append(i++).append(") ");
            for(var m : mList) {
                try {
                    sb.append(m.getName()).append(": ").append(m.invoke(r)).append(" ");
                } catch (InvocationTargetException | IllegalAccessException e) {
                    sb.append("Procedure is not possible at the moment");
                    break;
                }
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
