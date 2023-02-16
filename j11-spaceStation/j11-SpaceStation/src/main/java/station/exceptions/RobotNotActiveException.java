package station.exceptions;
public class RobotNotActiveException extends RuntimeException {
    private final String reason;
    public RobotNotActiveException(String robotNotActive) {
        reason = robotNotActive;
    }
    @Override
    public String getMessage() {
        return reason;
    }
}
