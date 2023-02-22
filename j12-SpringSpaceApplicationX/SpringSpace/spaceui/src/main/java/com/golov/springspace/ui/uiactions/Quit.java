package com.golov.springspace.ui.uiactions;
import com.golov.springspace.ui.exceptions.ErrorWhenExitingProgramException;
import com.golov.springspace.ui.UiEnum;

///////////// deprecated ///////////////////////

public class Quit implements UiAction {

    @Override
    public UiEnum act() {
        throw new ErrorWhenExitingProgramException("the program failled to exit");
    }
}
