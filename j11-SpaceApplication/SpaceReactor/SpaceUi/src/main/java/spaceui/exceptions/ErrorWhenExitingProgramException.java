package spaceui.exceptions;
public class ErrorWhenExitingProgramException extends RuntimeException {
    public ErrorWhenExitingProgramException(String theProgramFailledToExit) {
        super(theProgramFailledToExit);
    }
}
