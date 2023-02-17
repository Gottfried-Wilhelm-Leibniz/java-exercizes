package spaceui.uiactions;
import spaceui.UiEnum;

public class Quit implements UiAction {

    @Override
    public UiEnum act() {
        System.exit(0);
        return null;
    }
}
