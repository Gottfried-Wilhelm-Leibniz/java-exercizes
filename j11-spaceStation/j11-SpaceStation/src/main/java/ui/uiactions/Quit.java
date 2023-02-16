package ui.uiactions;
public class Quit implements UiAction {

    @Override
    public void act() {
        System.exit(0);
    }
}
