package ui.context;
import station.Reply;

@FunctionalInterface
public interface CreateNew {
    Reply create(String model, String name, String sign);
}
