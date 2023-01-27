package filesystem;
@FunctionalInterface
public interface OpenFile {
    File open(String name);
}
