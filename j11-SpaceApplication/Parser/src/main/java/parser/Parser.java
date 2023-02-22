package parser;
import com.google.gson.Gson;
//import station.robot.Robot;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
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

    public String bytesToString(byte[] bytes, Charset charset) {
        var byteBuff = ByteBuffer.wrap(bytes);
        return charset.decode(byteBuff).toString();
    }

    public String[] stringSeparator(String s, String sep) {
        return s.split(sep);
    }

    public String strArrToStrList(String... strings) {
        var sb = new StringBuilder();
        var i = 1;
        for(var s : strings) {
            sb.append(i++).append(") ").append(s).append("\n");
        }
        return sb.toString();
    }
}