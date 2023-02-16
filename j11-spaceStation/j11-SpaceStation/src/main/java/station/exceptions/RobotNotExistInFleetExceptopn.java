package station.exceptions;
public class RobotNotExistInFleetExceptopn extends RuntimeException {
    private final String reason;
    public RobotNotExistInFleetExceptopn(String s) {
        reason = s;
    }
    @Override
    public String getMessage() {
        return reason;
    }
}
