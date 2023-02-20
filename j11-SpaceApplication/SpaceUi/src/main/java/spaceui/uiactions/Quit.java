package spaceui.uiactions;
import spaceui.UiEnum;
import spaceui.exceptions.ErrorWhenExitingProgramException;

///////////// deprecated ///////////////////////

public class Quit implements UiAction {

    @Override
    public UiEnum act() {
        throw new ErrorWhenExitingProgramException("the program failled to exit");
    }
}
