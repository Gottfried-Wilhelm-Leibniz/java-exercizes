package ui.uiactions;
import ui.UiEnum;

public class Quit implements UiAction {

    @Override
    public UiEnum act() {
        System.exit(0);
        return null;
    }
}
