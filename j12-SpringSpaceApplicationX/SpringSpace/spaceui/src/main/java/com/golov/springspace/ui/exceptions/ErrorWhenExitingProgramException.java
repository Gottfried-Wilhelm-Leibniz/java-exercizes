package com.golov.springspace.ui.exceptions;
public class ErrorWhenExitingProgramException extends RuntimeException {
    public ErrorWhenExitingProgramException(String theProgramFailledToExit) {
        super(theProgramFailledToExit);
    }
}
