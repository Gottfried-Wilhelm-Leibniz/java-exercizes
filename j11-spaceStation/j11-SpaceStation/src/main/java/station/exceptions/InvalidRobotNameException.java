package station.exceptions;
public class InvalidRobotNameException extends RuntimeException {
    private final String reason;
    public InvalidRobotNameException(String s) {
        reason = s;
    }
    @Override
    public String getMessage() {
        return reason;
    }
}
