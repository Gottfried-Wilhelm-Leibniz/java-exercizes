package PackageQ2;

public class NoUniqueObjectException extends RuntimeException {
    public NoUniqueObjectException(String noSuchUniqueObject) {
        super(noSuchUniqueObject);
    }
}
