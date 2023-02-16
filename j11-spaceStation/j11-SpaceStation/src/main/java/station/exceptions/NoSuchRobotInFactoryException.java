package station.exceptions;
public class NoSuchRobotInFactoryException extends RuntimeException {
    private final String reason;
    public NoSuchRobotInFactoryException(String noSuch) {
        reason = noSuch;
    }
    @Override
    public String getMessage() {
        return reason;
    }
}
