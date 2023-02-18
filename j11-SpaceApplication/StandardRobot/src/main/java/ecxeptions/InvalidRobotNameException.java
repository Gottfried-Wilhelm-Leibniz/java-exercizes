package ecxeptions;
public class InvalidRobotNameException extends RuntimeException {
    public InvalidRobotNameException(String s) {
        super(s);
    }
}
