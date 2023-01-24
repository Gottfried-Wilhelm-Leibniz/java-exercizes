package exam01Q1;

public class NosuchUniqueIntException extends RuntimeException {
    public NosuchUniqueIntException(String noSuchUniqueObject) {
        super(noSuchUniqueObject);
    }
}
