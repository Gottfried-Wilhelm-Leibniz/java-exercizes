package ui.loader;
import java.util.List;

public interface Loader<T> {
    List<T> load();
}