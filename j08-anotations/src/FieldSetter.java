import java.lang.reflect.Field;

@FunctionalInterface
public interface FieldSetter {
    void set(Object o, Field f, Randomize r);
}
